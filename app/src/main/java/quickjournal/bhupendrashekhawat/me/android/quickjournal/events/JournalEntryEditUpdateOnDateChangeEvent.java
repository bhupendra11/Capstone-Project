package quickjournal.bhupendrashekhawat.me.android.quickjournal.events;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;

/**
 * Created by Bhupendra Shekhawat on 15/11/16.
 */

public class JournalEntryEditUpdateOnDateChangeEvent{

    private JournalEntryModel journalEntryModel;

    public JournalEntryEditUpdateOnDateChangeEvent(JournalEntryModel journalEntryModel){
        this.journalEntryModel = journalEntryModel;
    }

    public JournalEntryModel getJournalEntryModel() {
        return journalEntryModel;
    }

    public void setJournalEntryModel(JournalEntryModel journalEntryModel) {
        this.journalEntryModel = journalEntryModel;
    }


}
