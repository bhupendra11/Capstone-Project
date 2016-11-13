package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;

import static java.security.AccessController.getContext;

public class JournalEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final String LOG_TAG = JournalEntryActivity.class.getSimpleName();
    public static final String JOURNAL_ENTRY ="journal_entry";
    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    private Calendar now;
    private DatePickerDialog dpd;
    private long epochDate =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("JournalEntry");
        setSupportActionBar(toolbar);

        //custom font for quotes textview
        TextView text = (TextView) findViewById(R.id.quote_textview);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text.setTypeface(tf);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

            /*SaveJournalEntryTask saveJournalEntryTask = new SaveJournalEntryTask(getContext(), rootView);
            saveJournalEntryTask.execute(savedEntry);*/



           // System.out.println(savedEntry);
        }
        if(id == R.id.action_choose_date){
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public JournalEntryModel saveEntry(){



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


        //show Toast saying journal entry saved
        Toast.makeText(this, "Journal entry saved !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(JOURNAL_ENTRY , journalEntryModel);
        startService(intent);

        return journalEntryModel;
    }


    @Override
    protected void onStop() {
        JournalEntryModel savedEntry =  saveEntry();
        Log.d(LOG_TAG, savedEntry.toString());

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        JournalEntryModel savedEntry =  saveEntry();
        Log.d(LOG_TAG, savedEntry.toString());

        super.onDestroy();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        epochDate = convertDateToEpoch(year,monthOfYear,dayOfMonth);
        System.out.println(epochDate); // prints 1404461110000

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }



    public long convertDateToEpoch(int year, int monthOfYear, int dayOfMonth){

        long epoch = 0;
        String str = year+"-"+monthOfYear+"-"+dayOfMonth;   // UTC

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date datenew = null;
        try {
            datenew = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        epoch= datenew.getTime()/1000;   //to seconds

        return epoch;
    }
}
