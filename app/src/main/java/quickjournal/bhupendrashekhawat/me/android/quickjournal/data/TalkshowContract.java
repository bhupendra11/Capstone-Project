package quickjournal.bhupendrashekhawat.me.android.quickjournal.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bhupendra Singh on 24/3/16.
 */
public class TalkshowContract {


    public static final String CONTENT_AUTHORITY = "radioscope.bhupendrashekhawat.me.android.radioscope";

    public static final Uri BASE_CONTENT_URL =  Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_TALKSHOW = "talkshow";

    public static final class TalkshowEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon().appendPath(PATH_TALKSHOW).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +"/" +CONTENT_AUTHORITY +"/" +PATH_TALKSHOW;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" +CONTENT_AUTHORITY +"/" +PATH_TALKSHOW;

        public static  final String TABLE_NAME = "talkshow";

        public static final String COLUMN_TALKSHOW_ID = "talkshow_id";
        public static final String COLUMN_NAME= "title";
        public static final String COLUMN_GENRE = "poster";
        public static final String COLUMN_IMAGE_URL = "backdrop";

        public static Uri buildTalkshowUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
