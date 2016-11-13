package quickjournal.bhupendrashekhawat.me.android.quickjournal.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;


/**
 * Created by Bhupendra Shekhawat on 13/11/16.
 */

public class SaveJournalEntryService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private Context mContext;

    public static final String JOURNAL_ENTRY ="journal_entry";
    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    public static final String LOG_TAG = SaveJournalEntryService.class.getSimpleName();

    public  SaveJournalEntryService(){
        super("SaveJournalEntryService");
        mContext = this;

    }

    public SaveJournalEntryService(String name) {
        super(name);
    }

    public SaveJournalEntryService(Context context) {
        super("SaveJournalEntryService");
        this.mContext = context;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        JournalEntryModel journalEntryModel = intent.getParcelableExtra(JOURNAL_ENTRY);
        long journalEntryDate = intent.getLongExtra(JOURNAL_ENTRY_DATE,0);
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
}
