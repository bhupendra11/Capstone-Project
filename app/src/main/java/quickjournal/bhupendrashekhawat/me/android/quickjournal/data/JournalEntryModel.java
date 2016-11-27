package quickjournal.bhupendrashekhawat.me.android.quickjournal.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.JournalFragment;

/**
 * Created by Bhupendra Shekhawat on 4/11/16.
 */

public class JournalEntryModel implements Parcelable {

    private String quote;
    private long timestamp;
    private ArrayList<String> gratefulForList;
    private ArrayList<String> makesTodayGreatList;
    private String dailyAffirmations;
    private ArrayList<String> amazingThingsHappenedList;
    private String howCouldIHaveMadeTodayBetter;

    public JournalEntryModel(){

    }

    public ArrayList<String> getGratefulForList() {
        return gratefulForList;
    }

    public void setGratefulForList(ArrayList<String> gratefulForList) {
        this.gratefulForList = gratefulForList;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long epoch) {
        this.timestamp = epoch;
    }

    public ArrayList<String> getMakesTodayGreatList() {
        return makesTodayGreatList;
    }

    public void setMakesTodayGreatList(ArrayList<String> makesTodayGreatList) {
        this.makesTodayGreatList = makesTodayGreatList;
    }

    public String getDailyAffirmations() {
        return dailyAffirmations;
    }

    public void setDailyAffirmations(String dailyAffirmations) {
        this.dailyAffirmations = dailyAffirmations;
    }

    public ArrayList<String> getAmazingThingsHappenedList() {
        return amazingThingsHappenedList;
    }

    public void setAmazingThingsHappenedList(ArrayList<String> amazingThingsHappenedList) {
        this.amazingThingsHappenedList = amazingThingsHappenedList;
    }

    public String getHowCouldIHaveMadeTodayBetter() {
        return howCouldIHaveMadeTodayBetter;
    }

    public void setHowCouldIHaveMadeTodayBetter(String howCouldIHaveMadeTodayBetterList) {
        this.howCouldIHaveMadeTodayBetter = howCouldIHaveMadeTodayBetterList;
    }

    @Override
    public String toString() {
        String output = "Quote = "+quote+" dailyAffirmations:  "+dailyAffirmations+"  \n howCouldIHaveMadeTodayBetterList "+howCouldIHaveMadeTodayBetter
                + "\n gratefulList: " +getGratefulForList().get(0)
                +"\n makeTodayGreatList  "+ getMakesTodayGreatList().get(0)
                +"\n amazingThingsHappened "+ getAmazingThingsHappenedList().get(0);
        return output;
    }


    // get JournalEntryModel object from Cursor
    public JournalEntryModel(Cursor cursor) {
        long date= cursor.getLong(JournalFragment.COL_DATE);
        String  journalEntryJson = cursor.getString(JournalFragment.COL_ENTRY);
        Gson gson = new Gson();
        JournalEntryModel journalEntryObj = gson.fromJson(journalEntryJson, JournalEntryModel.class);

        this.quote = journalEntryObj.getQuote();
        this.timestamp = journalEntryObj.getTimestamp();
        this.gratefulForList = journalEntryObj.getGratefulForList();
        this.makesTodayGreatList = journalEntryObj.getMakesTodayGreatList();
        this.dailyAffirmations = journalEntryObj.getDailyAffirmations();
        this.amazingThingsHappenedList = journalEntryObj.getAmazingThingsHappenedList();
        this.howCouldIHaveMadeTodayBetter = journalEntryObj.getHowCouldIHaveMadeTodayBetter();
    }


    protected JournalEntryModel(Parcel in) {
        quote = in.readString();
        timestamp = in.readLong();
        if (in.readByte() == 0x01) {
            gratefulForList = new ArrayList<String>();
            in.readList(gratefulForList, String.class.getClassLoader());
        } else {
            gratefulForList = null;
        }
        if (in.readByte() == 0x01) {
            makesTodayGreatList = new ArrayList<String>();
            in.readList(makesTodayGreatList, String.class.getClassLoader());
        } else {
            makesTodayGreatList = null;
        }
        dailyAffirmations = in.readString();
        if (in.readByte() == 0x01) {
            amazingThingsHappenedList = new ArrayList<String>();
            in.readList(amazingThingsHappenedList, String.class.getClassLoader());
        } else {
            amazingThingsHappenedList = null;
        }
        howCouldIHaveMadeTodayBetter = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quote);
        dest.writeLong(timestamp);
        if (gratefulForList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gratefulForList);
        }
        if (makesTodayGreatList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(makesTodayGreatList);
        }
        dest.writeString(dailyAffirmations);
        if (amazingThingsHappenedList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(amazingThingsHappenedList);
        }
        dest.writeString(howCouldIHaveMadeTodayBetter);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<JournalEntryModel> CREATOR = new Parcelable.Creator<JournalEntryModel>() {
        @Override
        public JournalEntryModel createFromParcel(Parcel in) {
            return new JournalEntryModel(in);
        }

        @Override
        public JournalEntryModel[] newArray(int size) {
            return new JournalEntryModel[size];
        }
    };
}