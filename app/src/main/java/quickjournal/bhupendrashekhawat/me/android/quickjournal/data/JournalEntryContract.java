package quickjournal.bhupendrashekhawat.me.android.quickjournal.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bhupendra Singh on 24/3/16.
 */
public class JournalEntryContract {


    public static final String CONTENT_AUTHORITY = "quickjournal.bhupendrashekhawat.me.android.quickjournal";
    public static final Uri BASE_CONTENT_URL =  Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_JOURNAL = "journal";

    public static final class JournalEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon().appendPath(PATH_JOURNAL).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +"/" +CONTENT_AUTHORITY +"/" +PATH_JOURNAL;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" +CONTENT_AUTHORITY +"/" +PATH_JOURNAL;
        public static  final String TABLE_NAME = "journal";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ENTRY = "entry";
       // public static final String COLUMN_IMAGE_URL = "backdrop";

        public static Uri buildJournalEntryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



    }


}
