package edu.ricky.mada2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import edu.ricky.mada2.controller.MainRecyclerViewAdapter;
import edu.ricky.mada2.model.Movie;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovielistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovielistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovielistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    public static MovielistFragment newInstance() {
        MovielistFragment fragment = new MovielistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public MovielistFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_movielist, container, false);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainRecyclerViewAdapter(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        ((MainRecyclerViewAdapter) mAdapter).setOnItemClickListener(rvItemClickListener);
    }

    private MainRecyclerViewAdapter.MyClickListener rvItemClickListener = new MainRecyclerViewAdapter.MyClickListener() {
        @Override
        public void onItemClick(List<Movie> dataset, int position, View v) {
            Intent intent = new Intent(getActivity().getBaseContext(), MovieActivity.class);
            Toast.makeText(getActivity().getApplicationContext(), (String) dataset.get(position).getTitle(),
                    Toast.LENGTH_SHORT).show();
            intent.putExtra("id", dataset.get(position).getImdbId());

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
        ((MainRecyclerViewAdapter) mAdapter).reloadDataset();
    }

    public void releaseResource() {
        ((MainRecyclerViewAdapter) mAdapter).close();
    }

}
