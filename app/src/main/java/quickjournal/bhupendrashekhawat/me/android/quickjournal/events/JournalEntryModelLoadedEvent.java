package quickjournal.bhupendrashekhawat.me.android.quickjournal.events;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;

/**
 * Created by Bhupendra Shekhawat on 14/11/16.
 */

public class JournalEntryModelLoadedEvent {

    private JournalEntryModel journalEntryModel;

    public JournalEntryModelLoadedEvent(JournalEntryModel journalEntryModel){
        this.journalEntryModel = journalEntryModel;
    }

    public JournalEntryModel getJournalEntryModel() {
        return journalEntryModel;
    }

    public void setJournalEntryModel(JournalEntryModel journalEntryModel) {
        this.journalEntryModel = journalEntryModel;
    }


}
