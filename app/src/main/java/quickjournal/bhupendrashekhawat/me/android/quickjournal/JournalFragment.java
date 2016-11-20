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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryContract;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.data.JournalEntryModel;
import quickjournal.bhupendrashekhawat.me.android.quickjournal.services.JournalIntentService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = JournalFragment.class.getSimpleName();
    private static final String ACTION_FETCH_ALL_JOURNAL_ENTRIES= "quickjournal.bhupendrashekhawat.me.android.quickjournal.services.action.FETCH_ALL_JOURNAL_ENTRIES";
    public static final String JOURNAL_ENTRY_DATE ="journal_entry_date";

    private static final String[] JOURNAL_COLUMNS = {
            //Array of all the column names in Journal table
            JournalEntryContract.JournalEntry.TABLE_NAME + "." + JournalEntryContract.JournalEntry._ID,
            JournalEntryContract.JournalEntry.COLUMN_DATE,
            JournalEntryContract.JournalEntry.COLUMN_ENTRY
    };

    // These indices are tied to JOURNAL_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_DATE =1;
    public static final int COL_ENTRY = 2;

    public static final String SORT_ORDER = JOURNAL_COLUMNS[1] +" "+"DESC";
    private static final int JOURNAL_LOADER = 0;

    private OnFragmentInteractionListener mListener;
    private JournalCursorAdapter journalAdapter;
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

        //small AdView
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

       //send Intent to fetch all journal entries
        Intent intent = new Intent(getActivity(), JournalIntentService.class);
        intent.setAction(ACTION_FETCH_ALL_JOURNAL_ENTRIES);
        getActivity().startService(intent);

        journalAdapter =  new JournalCursorAdapter(getActivity(),null,0);

        listView = (ListView) rootView.findViewById(R.id.journal_entries_list_view);
        listView.setAdapter(journalAdapter);

        TextView listEmptyTextView = (TextView) rootView.findViewById(R.id.empty_journal_list_textview);
        listView.setEmptyView(listEmptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if(cursor !=  null){

                }

                String date = cursor.getString(COL_DATE);
               // journalEntryModel = new JournalEntryModel(cursor);

                Intent displayIntent = new Intent(getActivity() , JournalEntryDisplayActivity.class);
                displayIntent.putExtra(JOURNAL_ENTRY_DATE, date);
                startActivity(displayIntent);


            }
        } );

        return  rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(JOURNAL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri = JournalEntryContract.JournalEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),uri, JOURNAL_COLUMNS,null,null,SORT_ORDER);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        journalAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        journalAdapter.swapCursor(null);
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


}
