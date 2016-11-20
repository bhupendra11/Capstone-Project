package quickjournal.bhupendrashekhawat.me.android.quickjournal.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.events.JournalEntryEditUpdateOnDateChangeEvent;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;


/**
 * Created by Bhupendra Shekhawat on 13/11/16.
 */

public class JournalIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private Context mContext;

    public static final String JOURNAL_ENTRY ="journal_entry";
    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    public static final String LOG_TAG = JournalIntentService.class.getSimpleName();

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SAVE_JOURNAL_ENTRY= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.SAVE_JOURNAL_ENTRY";
    private static final String ACTION_UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE = "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE ";

    private ArrayList<JournalEntryModel> mJournalEntriesist = new ArrayList<JournalEntryModel>();
    Gson gson = new Gson();

    private static final String[] JOURNAL_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };

    // These indices are tied to JOURNAL_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_DATE = 1;
    public static final int COL_ENTRY = 2;

    public static final String SORT_ORDER = JOURNAL_COLUMNS[1] +" "+"DESC";

    public JournalIntentService(){
        super("JournalIntentService");
        mContext = this;

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_JOURNAL_ENTRY.equals(action)) {
                JournalEntryModel journalEntryModel = intent.getParcelableExtra(JOURNAL_ENTRY);
                long journalEntryDate = intent.getLongExtra(JOURNAL_ENTRY_DATE,0);
                handleActionSaveJournalEntry(journalEntryModel,journalEntryDate);
            }
            else if(ACTION_UPDATE_EDIT_JOURNAL_ENTRY_ON_DATE_CHANGE.equals(action)){
                long journalEntryDate = intent.getLongExtra(JOURNAL_ENTRY_DATE,0);
                handleActionUpdateEditJournalEntryOnDateChange(journalEntryDate);
            }

        }
    }



    private void handleActionSaveJournalEntry(JournalEntryModel journalEntryModel , long journalEntryDate) {

        int numRows =0;

        //handle the saving in DB here
        //serialize json and store as string

        String journalEntryJson = gson.toJson(journalEntryModel);

        Log.d(LOG_TAG , "Journal Entry to be made on Date " + DateHelper.getDisplayDate(journalEntryDate) +"\n Json: \n"+journalEntryJson );

        String whereNotNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + "= ?";
        String whereNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + " IS NULL";
        String[] whereArgs = new String[]{Long.toString(journalEntryDate)};
        Cursor cursor;

        if(whereArgs == null){
            cursor = mContext.getContentResolver().query(
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                    whereNull,
                    null,      // selectionArgs : gets the rows with this movieID
                    null             // Sort order

            );
        }
        else{
            cursor = mContext.getContentResolver().query(
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                    whereNotNull,
                    whereArgs,      // selectionArgs : gets the rows with this movieID
                    null           // Sort order

            );
        }


        if (cursor != null) {
            numRows = cursor.getCount();
            cursor.close();
        }


        if (numRows == 1) {    // Inside db so delete


            int delete = mContext.getContentResolver().delete(
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    JournalEntryContract.JournalEntry.COLUMN_DATE + " = ?",
                    new String[]{Long.toString(journalEntryDate)}
            );

        }

        //Insert new entry

        ContentValues values = new ContentValues();
        values.put(JournalEntryContract.JournalEntry.COLUMN_DATE, journalEntryDate);
        values.put(JournalEntryContract.JournalEntry.COLUMN_ENTRY, journalEntryJson);

        Uri insertedUri = mContext.getContentResolver().insert(JournalEntryContract.JournalEntry.CONTENT_URI,values);

        Log.d(LOG_TAG , "Diary Entry saved in db at URI : "+insertedUri);


    }




    public void  handleActionUpdateEditJournalEntryOnDateChange(long journalEntryDate){

        JournalEntryModel journalEntryModel = null;

        Log.d(LOG_TAG , "Journal Entry to be updated is for date " +DateHelper.getDisplayDate(journalEntryDate)+" Epoch = "+journalEntryDate );
        int numRows =0;

        String whereNotNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + "= ?";
        String whereNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + " IS NULL";
        String[] whereArgs = new String[]{Long.toString(journalEntryDate)};

        Cursor cursor;
        if(whereArgs == null) {
            cursor = mContext.getContentResolver().query(
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                    whereNull,
                    null,      // selectionArgs : gets the rows with this movieID
                    null             // Sort order

            );
        }
        else{
             cursor = mContext.getContentResolver().query(
                    JournalEntryContract.JournalEntry.CONTENT_URI,
                    null,   //projection
                    whereNotNull,
                    whereArgs,      // selectionArgs : gets the rows with this movieID
                    null             // Sort order

            );
        }



        if (cursor != null) {
            numRows = cursor.getCount();
        }

        if(cursor != null) {
            while (cursor.moveToNext()) {
                journalEntryModel = new JournalEntryModel(cursor);
            }
        }

        if(journalEntryModel != null) {
            Log.d(LOG_TAG, "JournalEntry model fetched \n " + journalEntryModel.toString());
        }

        EventBus.getDefault().post(new JournalEntryEditUpdateOnDateChangeEvent(journalEntryModel));
        cursor.close();



    }





}
