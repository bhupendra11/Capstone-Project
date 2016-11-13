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
import quickjournal.bhupendrashekhawat.me.android.quickjournal.events.JournalEntriesLoadedEvent;


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
    private static final String ACTION_FETCH_ALL_JOURNAL_ENTRIES= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_ALL_JOURNAL_ENTRIES";

    private ArrayList<JournalEntryModel> mJournalEntriesist = new ArrayList<JournalEntryModel>();

    private static final String[] Journal_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };

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
            } else if (ACTION_FETCH_ALL_JOURNAL_ENTRIES.equals(action)) {
                //do something

                handleActionFetchAllJournalEntries();
            }
        }



    }



    private void handleActionSaveJournalEntry(JournalEntryModel journalEntryModel , long journalEntryDate) {

        int numRows =0;

        //handle the saving in DB here


        //serialize json and store as string
        Gson gson = new Gson();
        String journalEntryJson = gson.toJson(journalEntryModel);

        Log.d(LOG_TAG , "Journal Entry to be made on Date " +journalEntryDate +"\n Json: \n"+journalEntryJson );

        Cursor cursor = mContext.getContentResolver().query(
                JournalEntryContract.JournalEntry.CONTENT_URI,
                null,   //projection
                JournalEntryContract.JournalEntry.COLUMN_DATE + " =?",
                new String[]{Long.toString(journalEntryDate)},      // selectionArgs : gets the rows with this movieID
                null             // Sort order

        );

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

        //Toast.makeText(mContext,"Diary Entry saved in db ",Toast.LENGTH_SHORT);
    }


    private void handleActionFetchAllJournalEntries() {

        Cursor cursor = mContext.getContentResolver().query(JournalEntryContract.JournalEntry.CONTENT_URI, Journal_COLUMNS, null,null,null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                JournalEntryModel journalEntryModel = new JournalEntryModel(cursor);
                mJournalEntriesist.add(journalEntryModel);
            }
        }

        Log.d(LOG_TAG , "EventBus called with list size = " +mJournalEntriesist.size());

        for (JournalEntryModel journalEntryModel :mJournalEntriesist){
            Log.d(LOG_TAG , journalEntryModel.toString());
        }

        EventBus.getDefault().post(new JournalEntriesLoadedEvent(mJournalEntriesist));

        cursor.close();



    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of the changes in journal entries
     */
    /*public interface Callback {
        void updateJournalEntryList(ArrayList<JournalEntryModel> journalEntryModelList);
    }*/

}
