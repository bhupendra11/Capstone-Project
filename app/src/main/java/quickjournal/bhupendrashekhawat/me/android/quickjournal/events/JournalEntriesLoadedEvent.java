package quickjournal.bhupendrashekhawat.me.android.quickjournal.events;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;

/**
 * Created by Bhupendra Shekhawat on 13/11/16.
 */

public class JournalEntriesLoadedEvent {

    public ArrayList<JournalEntryModel> getJournalEntryModelsList() {
        return journalEntryModelsList;
    }

    private ArrayList<JournalEntryModel> journalEntryModelsList = null;

    public JournalEntriesLoadedEvent(ArrayList<JournalEntryModel> list)
    {
        this.journalEntryModelsList = list;
    }
}

