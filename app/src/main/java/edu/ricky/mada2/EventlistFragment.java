package edu.ricky.mada2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import edu.ricky.mada2.controller.EventActivity;
import edu.ricky.mada2.controller.EventRecyclerViewAdapter;
import edu.ricky.mada2.controller.MainRecyclerViewAdapter;
import edu.ricky.mada2.model.Event;
import edu.ricky.mada2.model.Movie;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventlistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventlistFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovielistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventlistFragment newInstance() {
        EventlistFragment fragment = new EventlistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public EventlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_eventlist, container, false);
        initRecyclerView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataset();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EventRecyclerViewAdapter(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        ((EventRecyclerViewAdapter) mAdapter).setOnItemClickListener(rvItemClickListener);
    }

    private EventRecyclerViewAdapter.MyClickListener rvItemClickListener = new EventRecyclerViewAdapter.MyClickListener() {
        @Override
        public void onItemClick(List<Event> dataset, int position, View v) {
            Intent intent = new Intent(getActivity().getBaseContext(), EventActivity.class);
            Toast.makeText(getActivity().getApplicationContext(), (String) dataset.get(position).getName(),
                    Toast.LENGTH_SHORT).show();
            intent.putExtra("eventID", dataset.get(position).getID());
            startActivity(intent);
        }
    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void reloadDataset() {
        ((EventRecyclerViewAdapter) mAdapter).reload();
    }

    public void releaseResource() {
        ((EventRecyclerViewAdapter) mAdapter).close();
    }

}
