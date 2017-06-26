package com.example.rkalonji.lordofthetrivia;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import static android.R.attr.data;

/**
 * Created by rkalonji on 06/26/2017.
 */

public class ScoresGridFragment extends Fragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ScoresGridAdapter adapter;
    private Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.scores_grid_fragment, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.scores_grid);
        adapter = new ScoresGridAdapter(getActivity(), R.layout.scores_grid_fragment_item);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.adViewScoresGrid);

        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        ScoresGridFragment scoresGridFragment = new ScoresGridFragment();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.firebaseId.flContent, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, scoresGridFragment).commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                TriviasProvider.SCORES_BASE_URI,
                null,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
