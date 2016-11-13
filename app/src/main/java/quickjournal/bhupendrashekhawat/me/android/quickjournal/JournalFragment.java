package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.events.JournalEntriesLoadedEvent;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.services.JournalIntentService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment {

    public static final String LOG_TAG = JournalFragment.class.getSimpleName();
    private static final String ACTION_FETCH_ALL_JOURNAL_ENTRIES= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_ALL_JOURNAL_ENTRIES";


    private static final String[] Journal_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_DATE =1;
    public static final int COL_ENTRY = 2;


    private OnFragmentInteractionListener mListener;
    private JournalAdapter journalAdapter;
    private JournalEntryModel journalEntryModel;
    private ArrayList<JournalEntryModel> journalEntryModelList = new ArrayList<>();
    private ListView listView;

    public JournalFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static JournalFragment newInstance() {
        JournalFragment fragment = new JournalFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


       //send Intent to fetch all journal entries
        Intent intent = new Intent(getActivity(), JournalIntentService.class);
        intent.setAction(ACTION_FETCH_ALL_JOURNAL_ENTRIES);
        getActivity().startService(intent);



       journalAdapter = new JournalAdapter(getActivity() , this.journalEntryModelList);

        listView = (ListView) rootView.findViewById(R.id.journal_entries_list_view);
        listView.setAdapter(journalAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                journalEntryModel = journalAdapter.getItem(position);

              /*  ((Callback) getActivity()).onItemSelected(movie);*/

                //do something


            }
        } );

        return  rootView;

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
    public void updateJournalEntriesList(JournalEntriesLoadedEvent journalEntriesLoadedEvent){

        ArrayList<JournalEntryModel> newData = journalEntriesLoadedEvent.getJournalEntryModelsList();

        Log.d(LOG_TAG , "updateJournalEntriesList called , ListSize = " +journalEntryModelList.size());

        journalEntryModelList.clear();
        journalEntryModelList.addAll(newData);
        journalAdapter.notifyDataSetChanged();

       /* journalAdapter =  new JournalAdapter(getActivity() , journalEntryModelList);
        listView.setAdapter(journalAdapter);*/

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }


}
