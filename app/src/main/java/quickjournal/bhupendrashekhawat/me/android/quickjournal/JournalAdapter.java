package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;

/**
 * Created by Bhupendra Shekhawat on 13/11/16.
 */



public class JournalAdapter extends ArrayAdapter<JournalEntryModel>
{
    Context mContext;
    List<JournalEntryModel> journalEntryModelList = null;
    public static final String ACTION_DATA_UPDATED = "quickjournal.bhupendrashekhawat.me.android.quickjournal.ACTION_DATA_UPDATED";

    public JournalAdapter(Activity context, List<JournalEntryModel> journalEntryModelList){

        super(context,0, journalEntryModelList);
        this.journalEntryModelList = journalEntryModelList;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JournalEntryModel journalEntryModel = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.journal_entry_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.date_textview);
            viewHolder.grateFulOneTextView = (TextView) convertView.findViewById(R.id.gratefulone_textview);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String displayDate = DateHelper.getDisplayDate(journalEntryModel.getTimestamp());
        viewHolder.dateTextView.setText(displayDate);

        String titleDisplay = journalEntryModel.getGratefulForList().size() != 0 ?  journalEntryModel.getGratefulForList().get(0) : "Add gratitude";

        viewHolder.grateFulOneTextView.setText(titleDisplay);



        return convertView;
    }

    @Override
    public int getCount() {
        return journalEntryModelList.size();
    }

    @Override
    public JournalEntryModel getItem(int position) {
        return journalEntryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Implement viewHolder inorder to avoid expensive findViewVyId calls by every list item

    static class ViewHolder{
        TextView dateTextView;
        TextView grateFulOneTextView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED).setPackage(mContext.getPackageName());
        mContext.sendBroadcast(dataUpdatedIntent);

    }
}