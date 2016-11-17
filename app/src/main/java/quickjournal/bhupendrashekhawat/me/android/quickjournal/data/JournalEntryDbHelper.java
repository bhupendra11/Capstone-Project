package quickjournal.bhupendrashekhawat.me.android.quickjournal.data;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bhupendra Shekhawat on 24/3/16.
 */
public class JournalEntryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME ="journal.db";

    public JournalEntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create Journal Table
        final String SQL_CREATE_JOURNAL_TABLE ="CREATE TABLE "+ JournalEntryContract.JournalEntry.TABLE_NAME+ " ("+
                    JournalEntryContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    JournalEntryContract.JournalEntry.COLUMN_DATE+" INTEGER UNIQUE, "+
                    JournalEntryContract.JournalEntry.COLUMN_ENTRY +" TEXT);" ;

        db.execSQL(SQL_CREATE_JOURNAL_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + JournalEntryContract.JournalEntry.TABLE_NAME);
       // db.execSQL("DROP TABLE IF EXISTS " + JournalEntryContract.JournalImageEntry.TABLE_NAME);
        onCreate(db);
    }




}

