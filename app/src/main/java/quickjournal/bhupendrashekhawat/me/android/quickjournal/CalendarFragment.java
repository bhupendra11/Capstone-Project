/*
package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.events.FetchJounalEntryForDateEvent;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.services.JournalIntentService;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;


*/
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 *//*

public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private ArrayList<JournalEntryModel> journalEntryModelList = new ArrayList<>();
    private JournalCursorAdapter journalAdapter;
    private JournalEntryModel journalEntryModel;
    private ListView listView;
    private long epochDate;

    TextView dateTextView;
    TextView grateFulOneTextView;
    CardView journalCardView;

    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    public static final String JOURNAL_ENTRY_MODEL ="joural_entry_model";
    private static final String ACTION_FETCH_JOURNAL_ENTRY_FOR_DATE= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_JOURNAL_ENTRY_FOR_DATE";
    public static final String LOG_TAG = CalendarFragment.class.getSimpleName();

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView v = (CalendarView) rootView.findViewById(R.id.calendarView);
        v.setOnDateChangeListener(this);

        epochDate = System.currentTimeMillis()/1000;

        Log.d(LOG_TAG , "EpochDate = " +epochDate+"  Current date is "+DateHelper.getDisplayDate(epochDate));

        dateTextView = (TextView) rootView.findViewById(R.id.date_textview);
        grateFulOneTextView = (TextView) rootView.findViewById(R.id.gratefulone_textview);
        journalCardView  = (CardView) rootView.findViewById(R.id.calendar_fragment_journal_entry_card);

        //for cardview onClick

        journalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = epochDate+"";
                // journalEntryModel = new JournalEntryModel(cursor);

                Log.d(LOG_TAG ,"Date sent with intent = "+ date);
                Intent displayIntent = new Intent(getActivity() , JournalEntryDisplayActivity.class);
                displayIntent.putExtra(JOURNAL_ENTRY_DATE, date.trim());
                startActivity(displayIntent);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

        epochDate = DateHelper.convertDateToEpoch(year,month,dayOfMonth);

        Intent intent = new Intent(getActivity(), JournalIntentService.class);
        intent.setAction(ACTION_FETCH_JOURNAL_ENTRY_FOR_DATE);
        intent.putExtra( JOURNAL_ENTRY_DATE , epochDate);
        getActivity().startService(intent);

    }

    */
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCalendarFragmentListView(FetchJounalEntryForDateEvent fetchJounalEntryForDateEvent){

        JournalEntryModel journalEntryModel = fetchJounalEntryForDateEvent.getJournalEntryModel();
        if(journalEntryModel != null){
            Log.d(LOG_TAG , "updateCalendarFragmentListView called with journalEntryModel =  " + journalEntryModel.toString() );

           */
/* journalEntryModelList.clear();
            journalEntryModelList.add(journalEntryModel);
            journalAdapter.notifyDataSetChanged();*//*


            String displayDate = DateHelper.getDisplayDate(journalEntryModel.getTimestamp());
            dateTextView.setText(displayDate);

            String titleDisplay = journalEntryModel.getGratefulForList().size() != 0 ?  journalEntryModel.getGratefulForList().get(0) : "Add gratitude";

            grateFulOneTextView.setText(titleDisplay);


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        //fetch calendar entry for todays date

        Intent intent = new Intent(getActivity(), JournalIntentService.class);
        intent.setAction(ACTION_FETCH_JOURNAL_ENTRY_FOR_DATE);
        intent.putExtra( JOURNAL_ENTRY_DATE , epochDate);
        getActivity().startService(intent);


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}





*/
























































package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.services.JournalIntentService;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.util.DateHelper;




public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener ,  LoaderManager.LoaderCallbacks<Cursor>  {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private ArrayList<JournalEntryModel> journalEntryModelList = new ArrayList<>();
    private JournalCursorAdapter journalAdapter;
    private JournalEntryModel journalEntryModel;
    private ListView listView;
    private long epochDate;
    TextView dateTextView;
    TextView grateFulOneTextView;
    CardView journalCardView;
    TextView emptytextView;

    public static final String JOURNAL_ENTRY_DATE = "journal_entry_date";
    public static final String JOURNAL_ENTRY_MODEL ="joural_entry_model";
    private static final String ACTION_FETCH_JOURNAL_ENTRY_FOR_DATE= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_JOURNAL_ENTRY_FOR_DATE";
    public static final String LOG_TAG = CalendarFragment.class.getSimpleName();

    private static final String[] JOURNAL_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };
    public static final String SORT_ORDER = JOURNAL_COLUMNS[1] +" "+"DESC";

    private static final int CALENDAR_FRAGMENT_LOADER = 2;

    // These indices are tied to JOURNAL_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_DATE =1;
    public static final int COL_ENTRY = 2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView v = (CalendarView) rootView.findViewById(R.id.calendarView);
        v.setOnDateChangeListener(this);

        epochDate = System.currentTimeMillis()/1000;

        Bundle bundle = new Bundle();
        bundle.putLong(JOURNAL_ENTRY_DATE , epochDate);

        dateTextView = (TextView) rootView.findViewById(R.id.date_textview);
        grateFulOneTextView = (TextView) rootView.findViewById(R.id.gratefulone_textview);
        journalCardView  = (CardView) rootView.findViewById(R.id.calendar_fragment_journal_entry_card);
        emptytextView = (TextView) rootView.findViewById(R.id.empty_card_textview);

        journalCardView.setVisibility(View.GONE);

        getLoaderManager().initLoader(CALENDAR_FRAGMENT_LOADER, bundle, this);

        Log.d(LOG_TAG , "EpochDate = " +epochDate+"  Current date is "+DateHelper.getDisplayDate(epochDate));



        //For the listView below the calendar view to show journal entries for the selected day


 /*journalAdapter =  new JournalCursorAdapter(getActivity(),null,0);

        listView = (ListView) rootView.findViewById(R.id.calendar_fragment_journal_entries_list_view);
        listView.setAdapter(journalAdapter);
        TextView listEmptyTextView = (TextView) rootView.findViewById(R.id.empty_list_textview);
        listView.setEmptyView(listEmptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if(cursor !=  null){
                    String date = cursor.getString(COL_DATE);
                    // journalEntryModel = new JournalEntryModel(cursor);

                    Intent displayIntent = new Intent(getActivity() , JournalEntryDisplayActivity.class);
                    displayIntent.putExtra(JOURNAL_ENTRY_DATE, date);
                    startActivity(displayIntent);
                }

            }
        } );
*/


        //for cardview onClick

        journalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = epochDate+"";
                // journalEntryModel = new JournalEntryModel(cursor);

                Log.d(LOG_TAG ,"Date sent with intent = "+ date);
                Intent displayIntent = new Intent(getActivity() , JournalEntryDisplayActivity.class);
                displayIntent.putExtra(JOURNAL_ENTRY_DATE, date.trim());
                startActivity(displayIntent);
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

        epochDate = DateHelper.convertDateToEpoch(year,month,dayOfMonth);

        /*Intent intent = new Intent(getActivity(), JournalIntentService.class);
        intent.setAction(ACTION_FETCH_JOURNAL_ENTRY_FOR_DATE);
        intent.putExtra( JOURNAL_ENTRY_DATE , epochDate);
        getActivity().startService(intent);*/


        Log.d(LOG_TAG , "Date selected is "+ epochDate+"   "+DateHelper.getDisplayDate(epochDate));
        Bundle bundle = new Bundle();
        bundle.putLong(JOURNAL_ENTRY_DATE , epochDate);

        getLoaderManager().restartLoader(CALENDAR_FRAGMENT_LOADER, bundle, this);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG , "Date selected is "+ epochDate+"   "+DateHelper.getDisplayDate(epochDate));
        Bundle bundle2 = new Bundle();
        bundle2.putLong(JOURNAL_ENTRY_DATE , epochDate);

        getLoaderManager().restartLoader(CALENDAR_FRAGMENT_LOADER, bundle2, this);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String whereNotNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + "= ?";
        String whereNull = JournalEntryContract.JournalEntry.COLUMN_DATE  + " IS NULL";
        String[] whereArgs ;
        if(id == CALENDAR_FRAGMENT_LOADER){
            long journalEntryDate = args.getLong(JOURNAL_ENTRY_DATE);
            whereArgs =   new String[]{Long.toString(journalEntryDate)};

            Log.d(LOG_TAG , "Journal Entry to be to fetch for date in calendar fragment " +DateHelper.getDisplayDate(journalEntryDate) );

            if(whereArgs == null) {
                return new CursorLoader(getActivity(),
                        JournalEntryContract.JournalEntry.CONTENT_URI,
                        null,   //projection
                        whereNull,
                        whereArgs,      // selectionArgs : gets the rows with this movieID
                        null             // Sort order

                );
            }
            else{
                return new CursorLoader(getActivity(),
                        JournalEntryContract.JournalEntry.CONTENT_URI,
                        null,   //projection
                        whereNotNull,
                        whereArgs,      // selectionArgs : gets the rows with this movieID
                        null             // Sort order

                );
            }
        }

        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

       // journalAdapter.swapCursor(cursor);

        Log.d(LOG_TAG, "Cursorcount = "+cursor.getCount()) ;
        if(cursor != null) {
            while (cursor.moveToNext()) {
                journalEntryModel = new JournalEntryModel(cursor);
            }
            if(journalEntryModel != null) {
                Log.d(LOG_TAG, "JournalEntry model fetched \n " + journalEntryModel.toString());
                String displayDate = DateHelper.getDisplayDate(journalEntryModel.getTimestamp());
                dateTextView.setText(displayDate);

                String titleDisplay = journalEntryModel.getGratefulForList().size() != 0 ?  journalEntryModel.getGratefulForList().get(0) : "Add gratitude";

                grateFulOneTextView.setText(titleDisplay);
                journalCardView.setVisibility(View.VISIBLE);
                emptytextView.setVisibility(View.INVISIBLE);

            }
        }
        else{
            journalCardView.setVisibility(View.INVISIBLE);
            emptytextView.setText(R.string.string_calendar_fragment_empty_list);
            emptytextView.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // do nothing
    }

}
