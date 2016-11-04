package quickjournal.bhupendrashekhawat.me.android.quickjournal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bhupendra Singh on 24/3/16.
 */
public class TalkshowDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME ="talkshow.db";

    public TalkshowDbHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TALKSHOW_TABLE ="CREATE TABLE "+TalkshowContract.TalkshowEntry.TABLE_NAME+ " ("+
                    TalkshowContract.TalkshowEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TalkshowContract.TalkshowEntry.COLUMN_TALKSHOW_ID +" INTEGER NOT NULL, " +
                    TalkshowContract.TalkshowEntry.COLUMN_NAME +" TEXT NOT NULL, " +
                    TalkshowContract.TalkshowEntry.COLUMN_GENRE+" TEXT, " +
                    TalkshowContract.TalkshowEntry.COLUMN_IMAGE_URL +" TEXT);" ;

        db.execSQL(SQL_CREATE_TALKSHOW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +TalkshowContract.TalkshowEntry.TABLE_NAME);
        onCreate(db);
    }
}
