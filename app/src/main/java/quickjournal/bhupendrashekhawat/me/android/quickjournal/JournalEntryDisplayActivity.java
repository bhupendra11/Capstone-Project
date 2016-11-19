package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;



public class JournalEntryDisplayActivity extends AppCompatActivity  implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = JournalEntryDisplayActivity.class.getSimpleName();
    public static final String JOURNAL_ENTRY_MODEL ="joural_entry_model";
    public static final String JOURNAL_ENTRY_DATE ="journal_entry_date";

    private JournalEntryModel journalEntryModel;
    private Toolbar toolbar;
    private static final String ACTION_FETCH_ALL_JOURNAL_ENTRIES= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_ALL_JOURNAL_ENTRIES";
    private static final String ACTION_UPDATE_JOURNAL_ENTRY = "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.UPDATE_JOURNAL_ENTRY";


    public static final int JOURNAL_ENTRY_LOADER =0;

    private static final String[] JOURNAL_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };

    // These indices are tied to JOURNAL_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_DATE =1;
    public static final int COL_ENTRY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportLoaderManager().initLoader(JOURNAL_ENTRY_LOADER,null,this);

        setContentView(R.layout.activity_journal_entry_display);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set custom fonts
        setFonts();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get data from intent
        /*Intent intent = getIntent();
        journalEntryModel = intent.getParcelableExtra(JOURNAL_ENTRY_MODEL);*/


       // displayEntry(journalEntryModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_entry_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_edit){

            //do something
            Intent intent = new Intent(this, JournalEntryActivity.class);
            intent.setAction(ACTION_UPDATE_JOURNAL_ENTRY);
            intent.putExtra(JOURNAL_ENTRY_MODEL, journalEntryModel);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    public void displayEntry(JournalEntryModel journalEntryModel){

        GridLayout grateful_gridlayout = (GridLayout) findViewById(R.id.grateful_tv);
        ArrayList<String> gratefulList = journalEntryModel.getGratefulForList();

        TextView gratefulLine1 = (TextView) grateful_gridlayout.findViewById(R.id.line1_tv);
        gratefulLine1.setText(gratefulList.get(0));

        TextView gratefulLine2 = (TextView) grateful_gridlayout.findViewById(R.id.line2_tv);
        gratefulLine2.setText(gratefulList.get(1));

        TextView gratefulLine3 = (TextView) grateful_gridlayout.findViewById(R.id.line3_tv);
        gratefulLine3.setText(gratefulList.get(2));




        GridLayout today_great_gridlayout = (GridLayout) findViewById(R.id.today_great_tv);
        ArrayList<String> todayGreatList = journalEntryModel.getMakesTodayGreatList();

        TextView todayGreatLine1 = (TextView) today_great_gridlayout.findViewById(R.id.line1_tv);
        todayGreatLine1.setText(todayGreatList.get(0));

        TextView todayGreatLine2 = (TextView) today_great_gridlayout.findViewById(R.id.line2_tv);
        todayGreatLine2.setText(todayGreatList.get(1));

        TextView todayGreatLine3 = (TextView) today_great_gridlayout.findViewById(R.id.line3_tv);
        todayGreatLine3.setText(todayGreatList.get(2));





        TextView myAffirmations = (TextView) findViewById(R.id.affirmations_tv);
        myAffirmations.setText(journalEntryModel.getDailyAffirmations());


        GridLayout amazing_things_gridlayout = (GridLayout) findViewById(R.id.amazing_tv);
        ArrayList<String> amazingThingsTodayList = journalEntryModel.getAmazingThingsHappenedList();

        TextView amazingThingsTodayLine1 = (TextView) amazing_things_gridlayout.findViewById(R.id.line1_tv);
        amazingThingsTodayLine1.setText(amazingThingsTodayList.get(0));

        TextView amazingThingsTodayLine2 = (TextView) amazing_things_gridlayout.findViewById(R.id.line2_tv);
        amazingThingsTodayLine2.setText(amazingThingsTodayList.get(1));

        TextView amazingThingsTodayLine3 = (TextView) amazing_things_gridlayout.findViewById(R.id.line3_tv);
        amazingThingsTodayLine3.setText(amazingThingsTodayList.get(2));


        TextView howMadeTodayEvenBetter = (TextView) findViewById(R.id.made_today_better_tv);
        howMadeTodayEvenBetter.setText(journalEntryModel.getHowCouldIHaveMadeTodayBetter());


    }



    public void setFonts(){
        //custom font for quotes textview
        TextView text = (TextView) findViewById(R.id.quote_tv);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GreatVibes-Regular.ttf");
        text.setTypeface(tf);

        TextView text1 = (TextView) findViewById(R.id.grateful_qtv);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text1.setTypeface(tf1);

        TextView text2 = (TextView) findViewById(R.id.make_today_great_qtv);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text2.setTypeface(tf2);

        TextView text3 = (TextView) findViewById(R.id.my_affirmations_qtv);
        Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text3.setTypeface(tf3);

        TextView text4 = (TextView) findViewById(R.id.amazing_things_happened_qtv);
        Typeface tf4 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text4.setTypeface(tf4);

        TextView text5 = (TextView) findViewById(R.id.made_today_better_qtv);
        Typeface tf5 = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        text5.setTypeface(tf5);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getIntent();
        if(intent == null){
            return null;
        }

        String journalEntryDate = intent.getStringExtra(JOURNAL_ENTRY_DATE);
        if(journalEntryDate == null){
            return null;
        }

        Log.d(LOG_TAG, "Journal date = "+journalEntryDate);

        //String selectionArgs[] = {journalEntryDate};

        String whereNotNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + "= ?";
        String whereNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + " IS NULL";
        String[] whereArgs = new String[]{journalEntryDate};
        CursorLoader cursorLoader = null;

        if(whereArgs == null){
            cursorLoader =  new CursorLoader(this,
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                    whereNull,  // selectionArgs : gets the rows with this movieID
                    null ,
                    null             // Sort order

            );
        }
        else{
            cursorLoader =  new CursorLoader(this,
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                   whereNotNull,
                    whereArgs,      // selectionArgs : gets the rows with this movieID
                    null             // Sort order

            );
        }



        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int size = cursor.getColumnCount();

        Log.d(LOG_TAG , "COlumn count of cursor = "+size + "cursor position = "+ +cursor.getPosition() );

        if(cursor != null) {
            while (cursor.moveToNext()) {
                journalEntryModel = new JournalEntryModel(cursor);
            }
        }

        if(journalEntryModel != null) {
            Log.d(LOG_TAG, "JournalEntry model fetched \n " + journalEntryModel.toString());
        }

        displayEntry(journalEntryModel);
        getSupportActionBar().setTitle(DateHelper.getDisplayDate(journalEntryModel.getTimestamp()));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //do nothing
    }
}
