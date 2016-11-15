package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private ArrayList<JournalEntryModel> journalEntryModelList = new ArrayList<>();
    private JournalAdapter journalAdapter;
    private JournalEntryModel journalEntryModel;
    private ListView listView;
    private long epochDate;

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

        /*
        For the listView below the calendar view to show journal entries for the selected day
         */
        journalAdapter = new JournalAdapter(getActivity() , this.journalEntryModelList);

        listView = (ListView) rootView.findViewById(R.id.calendar_fragment_journal_entries_list_view);
        listView.setAdapter(journalAdapter);
        TextView listEmptyTextView = (TextView) rootView.findViewById(R.id.empty_list_textview);
        listView.setEmptyView(listEmptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                journalEntryModel = journalAdapter.getItem(position);

                Intent displayIntent = new Intent(getActivity() , JournalEntryDisplayActivity.class);
                displayIntent.putExtra(JOURNAL_ENTRY_MODEL , journalEntryModel);
                startActivity(displayIntent);

            }
        } );

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCalendarFragmentListView(FetchJounalEntryForDateEvent fetchJounalEntryForDateEvent){

        JournalEntryModel journalEntryModel = fetchJounalEntryForDateEvent.getJournalEntryModel();
        if(journalEntryModel != null){
            Log.d(LOG_TAG , "updateCalendarFragmentListView called with journalEntryModel =  " + journalEntryModel.toString() );

            journalEntryModelList.clear();
            journalEntryModelList.add(journalEntryModel);
            journalAdapter.notifyDataSetChanged();


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
