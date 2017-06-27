package com.example.rkalonji.lordofthetrivia;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by rkalonji on 06/26/2017.
 */

public class TriviaCategoriesGridFragment extends Fragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    // private TriviaCategoriesGridAdapter adapter;
    private TriviaCategoriesCustomCursorRecyclerViewAdapter adapter;
    private Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_categories_grid_fragment, container, false);

        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new TriviaCategoriesCustomCursorRecyclerViewAdapter(getContext(), null);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.trivia_categories_recycler_view);
        rv.setLayoutManager(sglm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.trivia_categories_ad_view);

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

    // Note that the following method does work with RecyclerView  but is required for Loaders
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        TriviaSetsGridFragment triviaSetsGridFragment = new TriviaSetsGridFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.firebaseId.flContent, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, triviaSetsGridFragment).commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                TriviasProvider.CATEGORIES_BASE_URI,
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
