package quickjournal.bhupendrashekhawat.me.android.quickjournal.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.R;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;

/**
 * Created by Bhupendra Shekhawat on 17/11/16.
 */
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor mCursor = null;
    private Context mContext;
    private Intent mIntent;

    private static final String[] JOURNAL_COLUMNS = new String[] {
            JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY

    };

    // these indices must match the projection
    private static final int INDEX_COLUMN_ID=0;
    private static final int INDEX_COLUMN_DATE =1;
    private static final int INDEX_COLUMN_ENTRY= 2;

    private Gson gson = new Gson();

    public WidgetFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission

        mCursor = mContext.getContentResolver().query(JournalEntryContract.JournalEntry.CONTENT_URI,
                JOURNAL_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.journal_widget_list_item);

        if(mCursor.moveToPosition(position)){

            String journeyModelJson = mCursor.getString(INDEX_COLUMN_ENTRY);
            JournalEntryModel journalEntryModel = gson.fromJson(journeyModelJson, JournalEntryModel.class);

            String displayDate = DateHelper.getDisplayDate(journalEntryModel.getTimestamp());
            String titleDisplay = journalEntryModel.getGratefulForList().size() != 0 ?  journalEntryModel.getGratefulForList().get(0) : "No Title";


            remoteViews.setTextViewText(R.id.journal_entry_date_textview, displayDate );
            remoteViews.setTextViewText(R.id.journal_entry_title_textview, titleDisplay);
           // remoteViews.setImageViewBitmap(R.id.journal_entry_title_textview, buildTextWithFone(titleDisplay));

            Intent newIntent = new Intent();
            newIntent.putExtra("date", journalEntryModel.getTimestamp());
            // In setOnClickFillIntent method, the ID to be passed is of the Rootview
            // of the layout passed in the remote view - above, i.e. rootview of the list_item_quote.
            remoteViews.setOnClickFillInIntent(R.id.widget, newIntent);

        }
        return  remoteViews;
    }

/*

    public Bitmap buildTextWithFone(String time)
    {
        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(mContext.getAssets(), "fonts/Courgette-Regular.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(time, 80, 60, paint);
        return myBitmap;
    }
*/

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor.moveToPosition(position))
            return mCursor.getLong(INDEX_COLUMN_ID);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



}
