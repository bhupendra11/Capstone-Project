package quickjournal.bhupendrashekhawat.me.android.quickjournal.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

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


        //handle the saving in DB here


    }
}
