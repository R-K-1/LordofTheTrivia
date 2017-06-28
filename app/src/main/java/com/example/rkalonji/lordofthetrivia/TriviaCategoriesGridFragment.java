package com.example.rkalonji.lordofthetrivia;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by rkalonji on 06/26/2017.
 */

public class TriviaCategoriesGridFragment extends Fragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    // private TriviaCategoriesGridAdapter adapter;
    private TriviaCategoriesCustomCursorRecyclerViewAdapter adapter;
    private ImageView collapsingToolbarImage;
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

        View appBar = inflater.inflate(R.layout.app_bar_main, container, false);
        collapsingToolbarImage = (ImageView) appBar.findViewById(R.id.collapsing_toolbar_image);

        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        File imageFile = utils.getFileFromInternalStorage(getContext(), "LOTTRImgCatPeople.jpg");
        Picasso.with(getContext()).load(imageFile).into(collapsingToolbarImage);
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
