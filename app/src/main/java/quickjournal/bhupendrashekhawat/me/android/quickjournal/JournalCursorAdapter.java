package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;


/**
 * Created by Bhupendra Shekhawat on 18/11/16.
 */

public class JournalCursorAdapter extends CursorAdapter {


    /**
     * Cache of the children views for a forecast list item.
     */

    public static final String ACTION_DATA_UPDATED = "quickjournal.bhupendrashekhawat.me.android.quickjournal.ACTION_DATA_UPDATED";

    public static class ViewHolder {
        TextView dateTextView;
        TextView grateFulOneTextView;


        public ViewHolder(View view) {
           dateTextView = (TextView) view.findViewById(R.id.date_textview);
           grateFulOneTextView = (TextView) view.findViewById(R.id.gratefulone_textview);
        }
    }

    public JournalCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type

        View view = LayoutInflater.from(context).inflate(R.layout.journal_entry_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        JournalEntryModel journalEntryModel = new JournalEntryModel(cursor);

        String displayDate = DateHelper.getDisplayDate(journalEntryModel.getTimestamp());
        viewHolder.dateTextView.setText(displayDate);

        String titleDisplay = journalEntryModel.getGratefulForList().size() != 0 ?  journalEntryModel.getGratefulForList().get(0) : "Add gratitude";

        viewHolder.grateFulOneTextView.setText(titleDisplay);


    }


}