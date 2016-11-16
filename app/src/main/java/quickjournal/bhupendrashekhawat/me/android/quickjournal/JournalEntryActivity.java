package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.events.JournalEntryEditUpdateOnDateChangeEvent;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.services.JournalIntentService;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.ImageHelper;

import static quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper.convertDateToEpoch;
import static quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper.getDisplayDate;

public class JournalEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private final String LOG_TAG = JournalEntryActivity.class.getSimpleName();
    public static final String JOURNAL_ENTRY ="journal_entry";
    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    private Calendar now;
    private DatePickerDialog dpd;
    private long epochDate =0;
    private static String toolbarDate = "JournalEntry";
    private Toolbar toolbar;
    private ArrayList<byte []> journalImageItems = new ArrayList<>();

    public static final String JOURNAL_ENTRY_MODEL ="joural_entry_model";
    private static final String ACTION_SAVE_JOURNAL_ENTRY= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.SAVE_JOURNAL_ENTRY";
    private static final String ACTION_FETCH_ALL_JOURNAL_ENTRIES= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_ALL_JOURNAL_ENTRIES";
    private static final String ACTION_UPDATE_JOURNAL_ENTRY = "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.UPDATE_JOURNAL_ENTRY";
    private static final String ACTION_UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE = "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE ";

    public static final String EMPTY_STRING="";

    public static final int SELECT_PICTURE = 100;

    public ImageView sampleImageView ;
    private HorizontalScrollView mJournalEntryImagesScrollView;
    private ViewGroup mImagesView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("JournalEntry");
        setSupportActionBar(toolbar);

        mContext = this;
        //custom font for quotes textview
        TextView text = (TextView) findViewById(R.id.quote_textview);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text.setTypeface(tf);

        sampleImageView = (ImageView) findViewById(R.id.sampleImageView);
        mJournalEntryImagesScrollView = (HorizontalScrollView) findViewById(R.id.journal_entry_images_container);
        mImagesView = (ViewGroup) findViewById(R.id.images_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sdintent = new Intent(Intent.ACTION_GET_CONTENT);
                sdintent.setType("image/*");
                startActivityForResult(Intent.createChooser(sdintent, "Select Picture"), SELECT_PICTURE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setFonts();

        //for calendar to pick date
        now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                JournalEntryActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        epochDate = convertDateToEpoch(           //set current day
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        toolbarDate = getDisplayDate(now.get(Calendar.YEAR),
                now.get(Calendar.MONTH ) +1,
                now.get(Calendar.DAY_OF_MONTH));

        Log.d(LOG_TAG, "Toolbar date = "+toolbarDate);

        toolbar.setTitle(toolbarDate);


        //check if intent is supplied and if prsesent handle it
        Intent intent = getIntent();
        if(intent != null){
            handleIntentHere(intent);
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG , "onActivityResult called");
        if (requestCode == SELECT_PICTURE ) {
            Uri selectedImageUri = data.getData();
            byte[] inputData = null;

            if(selectedImageUri != null){
                InputStream iStream = null;
                try {
                    iStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    inputData = ImageHelper.getBytes(iStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.d(LOG_TAG , "Inside onActivityResult , bitmap is "+inputData +"Image list size : "+journalImageItems.size() );
            Bitmap fetchedImage = ImageHelper.getImage(inputData);
            sampleImageView.setImageBitmap(fetchedImage);
            if(inputData != null)
                journalImageItems.add(inputData);

            addTrailers(journalImageItems);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_save){

           JournalEntryModel savedEntry =  saveEntry();
            Log.d(LOG_TAG, savedEntry.toString());

        }
        if(id == R.id.action_choose_date){
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JournalEntryModel savedEntry =  saveEntry();
        Log.d(LOG_TAG, savedEntry.toString());
    }

    public JournalEntryModel saveEntry(){

        Log.d(LOG_TAG, "saveEntry called");

        JournalEntryModel curJournalEntryModel = getCurrentJournalEntryModelObj();
        //show Toast saying journal entry saved
        Toast.makeText(this, "Journal entry saved !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, JournalIntentService.class);
        intent.setAction(ACTION_SAVE_JOURNAL_ENTRY);
        intent.putExtra(JOURNAL_ENTRY , curJournalEntryModel);
        intent.putExtra(JOURNAL_ENTRY_DATE, epochDate);
        startService(intent);

        return curJournalEntryModel;
    }

    public JournalEntryModel updateEntry(){
        JournalEntryModel curJournalEntryModel = getCurrentJournalEntryModelObj();

        //show Toast saying journal entry saved
        Toast.makeText(this, "Journal entry updated !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, JournalIntentService.class);
        intent.setAction(ACTION_UPDATE_JOURNAL_ENTRY);
        intent.putExtra(JOURNAL_ENTRY , curJournalEntryModel);
        //intent.putExtra(JOURNAL_ENTRY_DATE, epochDate);
        startService(intent);

        return curJournalEntryModel;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop Called");
        EventBus.getDefault().unregister(this);
        /*JournalEntryModel savedEntry =  saveEntry();
        Log.d(LOG_TAG, savedEntry.toString());*/



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy Called");
        JournalEntryModel savedEntry =  saveEntry();
        Log.d(LOG_TAG, savedEntry.toString());


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        epochDate = convertDateToEpoch(year,monthOfYear,dayOfMonth);
        System.out.println(epochDate);

        toolbarDate = getDisplayDate(year,monthOfYear,dayOfMonth);

        toolbar.setTitle(toolbarDate);

        populateEditEntryOnDateChange();

        //toolbar.setTitle(toolbarDate);

        Log.d(LOG_TAG, "Epoch Time is " +epochDate);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    public void populateEditEntryOnDateChange(){

        Intent intent = new Intent(this, JournalIntentService.class);
        intent.setAction(ACTION_UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE );
        intent.putExtra(JOURNAL_ENTRY_DATE, epochDate);
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateEditEntryOnDateChange(JournalEntryEditUpdateOnDateChangeEvent journalEntryEditUpdateOnDateChangeEvent){



        JournalEntryModel journalEntryModel = journalEntryEditUpdateOnDateChangeEvent.getJournalEntryModel();
        Log.d(LOG_TAG, "   updateEditEntryOnDateChange is called with  journalEntryModel :\n " +journalEntryModel);


            populateJournalEntry(journalEntryModel);


    }


    public void handleIntentHere(Intent intent){
        final String action = intent.getAction();
        if(ACTION_UPDATE_JOURNAL_ENTRY.equals(action)){

            //populate the journalEntry with already available data

            JournalEntryModel journalEntryModel = intent.getParcelableExtra(JOURNAL_ENTRY_MODEL);
            epochDate = journalEntryModel.getTimestamp();
            populateJournalEntry(journalEntryModel);
            updateEntry();
        }
    }

    public void populateJournalEntry(JournalEntryModel journalEntryModel){

        GridLayout grateful_gridlayout = (GridLayout) findViewById(R.id.grateful_editext);
        TextView gratefulLine1 = (TextView) grateful_gridlayout.findViewById(R.id.line1);
        TextView gratefulLine2 = (TextView) grateful_gridlayout.findViewById(R.id.line2);
        TextView gratefulLine3 = (TextView) grateful_gridlayout.findViewById(R.id.line3);
        GridLayout today_great_gridlayout = (GridLayout) findViewById(R.id.today_great_editext);
        TextView todayGreatLine1 = (TextView) today_great_gridlayout.findViewById(R.id.line1);
        TextView todayGreatLine2 = (TextView) today_great_gridlayout.findViewById(R.id.line2);
        TextView todayGreatLine3 = (TextView) today_great_gridlayout.findViewById(R.id.line3);
        TextView myAffirmations = (TextView) findViewById(R.id.affirmations_edittext);
        GridLayout amazing_things_gridlayout = (GridLayout) findViewById(R.id.amazing_editext);
        TextView amazingThingsTodayLine1 = (TextView) amazing_things_gridlayout.findViewById(R.id.line1);
        TextView amazingThingsTodayLine2 = (TextView) amazing_things_gridlayout.findViewById(R.id.line2);
        TextView amazingThingsTodayLine3 = (TextView) amazing_things_gridlayout.findViewById(R.id.line3);
        TextView howMadeTodayEvenBetter = (TextView) findViewById(R.id.made_today_better_edittext);

        if(journalEntryModel == null){
            getSupportActionBar().setTitle(DateHelper.getDisplayDate(epochDate));

            gratefulLine1.setText(EMPTY_STRING);
            gratefulLine2.setText(EMPTY_STRING);
            gratefulLine3.setText(EMPTY_STRING);

            todayGreatLine1.setText(EMPTY_STRING);
            todayGreatLine2.setText(EMPTY_STRING);
            todayGreatLine3.setText(EMPTY_STRING);

            myAffirmations.setText(EMPTY_STRING);

            amazingThingsTodayLine1.setText(EMPTY_STRING);
            amazingThingsTodayLine2.setText(EMPTY_STRING);
            amazingThingsTodayLine3.setText(EMPTY_STRING);

            howMadeTodayEvenBetter.setText(EMPTY_STRING);

        }
        else{
            ArrayList<String> gratefulList = journalEntryModel.getGratefulForList();
            gratefulLine1.setText(gratefulList.get(0));
            gratefulLine2.setText(gratefulList.get(1));
            gratefulLine3.setText(gratefulList.get(2));

            ArrayList<String> todayGreatList = journalEntryModel.getMakesTodayGreatList();
            todayGreatLine1.setText(todayGreatList.get(0));
            todayGreatLine2.setText(todayGreatList.get(1));
            todayGreatLine3.setText(todayGreatList.get(2));

            myAffirmations.setText(journalEntryModel.getDailyAffirmations());

            ArrayList<String> amazingThingsTodayList = journalEntryModel.getAmazingThingsHappenedList();
            amazingThingsTodayLine1.setText(amazingThingsTodayList.get(0));
            amazingThingsTodayLine2.setText(amazingThingsTodayList.get(1));
            amazingThingsTodayLine3.setText(amazingThingsTodayList.get(2));

            howMadeTodayEvenBetter.setText(journalEntryModel.getHowCouldIHaveMadeTodayBetter());

            getSupportActionBar().setTitle(DateHelper.getDisplayDate(journalEntryModel.getTimestamp()));
        }




    }



    public JournalEntryModel getCurrentJournalEntryModelObj(){
        JournalEntryModel journalEntryModel = new JournalEntryModel();

        GridLayout grateful_gridlayout = (GridLayout) findViewById(R.id.grateful_editext);
        ArrayList<String> gratefulList = new ArrayList<>();

        EditText gratefulLine1 = (EditText) grateful_gridlayout.findViewById(R.id.line1);
        gratefulList.add(gratefulLine1.getText().toString());

        EditText gratefulLine2 = (EditText) grateful_gridlayout.findViewById(R.id.line2);
        gratefulList.add(gratefulLine2.getText().toString());

        EditText gratefulLine3 = (EditText) grateful_gridlayout.findViewById(R.id.line3);
        gratefulList.add(gratefulLine3.getText().toString());

        journalEntryModel.setGratefulForList(gratefulList);

        GridLayout today_great_gridlayout = (GridLayout) findViewById(R.id.today_great_editext);
        ArrayList<String> todayGreatList = new ArrayList<>();

        EditText todayGreatLine1 = (EditText) today_great_gridlayout.findViewById(R.id.line1);
        todayGreatList.add(todayGreatLine1 .getText().toString());

        EditText todayGreatLine2 = (EditText) today_great_gridlayout.findViewById(R.id.line2);
        todayGreatList.add(todayGreatLine2.getText().toString());

        EditText todayGreatLine3 = (EditText) today_great_gridlayout.findViewById(R.id.line3);
        todayGreatList.add(todayGreatLine3.getText().toString());

        journalEntryModel.setMakesTodayGreatList(todayGreatList);

        EditText myAffirmations = (EditText) findViewById(R.id.affirmations_edittext);
        journalEntryModel.setDailyAffirmations(myAffirmations.getText().toString());


        GridLayout amazing_things_gridlayout = (GridLayout) findViewById(R.id.amazing_editext);
        ArrayList<String> amazingThingsTodayList = new ArrayList<>();

        EditText amazingThingsTodayLine1 = (EditText) amazing_things_gridlayout.findViewById(R.id.line1);
        amazingThingsTodayList.add(amazingThingsTodayLine1.getText().toString());

        EditText amazingThingsTodayLine2 = (EditText) today_great_gridlayout.findViewById(R.id.line2);
        amazingThingsTodayList.add(amazingThingsTodayLine2.getText().toString());

        EditText amazingThingsTodayLine3 = (EditText) today_great_gridlayout.findViewById(R.id.line3);
        amazingThingsTodayList.add(amazingThingsTodayLine3.getText().toString());

        journalEntryModel.setAmazingThingsHappenedList(amazingThingsTodayList);

        EditText howMadeTodayEvenBetter = (EditText) findViewById(R.id.made_today_better_edittext);
        journalEntryModel.setHowCouldIHaveMadeTodayBetter(howMadeTodayEvenBetter.getText().toString());

        journalEntryModel.setTimestamp(epochDate);


        boolean hasImages = !journalImageItems.isEmpty();
        mJournalEntryImagesScrollView .setVisibility(hasImages ? View.VISIBLE : View.GONE);
        if (hasImages) {
            addTrailers(journalImageItems);
        }

        journalEntryModel.setImagesList(journalImageItems);


        return journalEntryModel;
    }


    private void addTrailers(List<byte []> journalImages) {
        mImagesView.removeAllViews();


        //  LayoutInflater inflater = getActivity().getLayoutInflater();

        LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // Picasso picasso = Picasso.with(getActivity());
        for (byte[] journalImageByteArray : journalImages) {
            ViewGroup imagesContainer = (ViewGroup) inflater.inflate(R.layout.journal_entry_image_item, mImagesView, false);

            ImageView journalImage = (ImageView) imagesContainer.findViewById(R.id.journal_image);
            journalImage.setOnClickListener(this);

            Bitmap imageBitmap = ImageHelper.getImage(journalImageByteArray);
            journalImage.setImageBitmap(imageBitmap);

            mImagesView.addView(imagesContainer);

        }
    }


    public void setFonts(){
        //custom font for quotes textview
        TextView text = (TextView) findViewById(R.id.quote_textview);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GreatVibes-Regular.ttf");
        text.setTypeface(tf);

        TextView text1 = (TextView) findViewById(R.id.grateful_textview);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text1.setTypeface(tf1);

        TextView text2 = (TextView) findViewById(R.id.make_today_great_textview);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text2.setTypeface(tf2);

        TextView text3 = (TextView) findViewById(R.id.my_affirmations_textview);
        Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text3.setTypeface(tf3);

        TextView text4 = (TextView) findViewById(R.id.amazing_things_happened_textview);
        Typeface tf4 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text4.setTypeface(tf4);

        TextView text5 = (TextView) findViewById(R.id.made_today_better_textview);
        Typeface tf5 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text5.setTypeface(tf5);


    }


    @Override
    public void onClick(View v) {

    }
}
