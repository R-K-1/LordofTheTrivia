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

import com.example.rkalonji.lordofthetrivia.Utils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.internal.request.StringParcel;

/**
 * Created by Rkalonji on 05/21/2017.
 */

public class TriviaSetsGridFragment extends Fragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private TriviaSetsGridAdapter adapter;
    private Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_sets_grid_fragment, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.trivia_sets_grid);
        adapter = new TriviaSetsGridAdapter(getActivity(), R.layout.trivia_sets_grid_fragment_item);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.adViewTriviaSetsGrid);

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
        /*Toast toast = Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG);
        toast.show();*/
        // Create a new fragment and specify the fragment to show based on nav item clicked
        TriviaSetDetailsFragment triviaSetDetailsFragment = new TriviaSetDetailsFragment();
        triviaSetDetailsFragment.setGlobalTriviaSetFirebaseId(String.valueOf(id));
/*        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = TriviaSetDetailsFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, triviaSetDetailsFragment).commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                TriviasProvider.TRIVIAS_BASE_URI,
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
