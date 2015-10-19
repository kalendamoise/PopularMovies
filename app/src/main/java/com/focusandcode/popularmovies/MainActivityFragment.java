package com.focusandcode.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getName();
    private GridViewAdapter adapter;
    private List<Movie> movies = new ArrayList<Movie>();
    private Parcelable state;
    private GridView gridview;
    private Context context;
    private View rootView;
    private NetworkChangeReceiver broadcastReceiver = new NetworkChangeReceiver();
    private LinearLayout spinnerView;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        updateUI(inflater, container);

        return rootView;
    }

    private void updateUI(LayoutInflater inflater, ViewGroup container) {
        Log.d(LOG_TAG, "Updating the UI");

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        spinnerView =(LinearLayout) rootView.findViewById(R.id.linlaProgressBar);
        adapter = new GridViewAdapter(context, R.layout.gridview_layout, movies);

        gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        gridview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                fetchDataAppend(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Movie item = (Movie) parent.getItemAtPosition(position);
                Log.d(LOG_TAG, item.toString());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", item);
                startActivity(intent);

            }
        });

        if (state != null) {
            Log.d(LOG_TAG, "trying to restore listview state..");
            gridview.onRestoreInstanceState(state);
        }

        TextView tv = (TextView) rootView.findViewById(R.id.tv_no_data);
        if (Constants.NET_STATUS_NOT_CONNECTED.equals(NetworkUtil.getConnectivityStatusString(context)) && (movies.size() == 0)) {
            tv.setText(getString(R.string.no_data));
        } else {
            tv.setText("");
        }

        fetchData();
    }


    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d(LOG_TAG, "saving listview state @ onPause");
        state = gridview.onSaveInstanceState();
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore previous state (including selected item index and scroll position)
        if (state != null) {
            Log.d(LOG_TAG, "trying to restore listview state..");
            gridview.onRestoreInstanceState(state);
        }
    }

    private void fetchData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortOrder = preferences.getString(this.getString(R.string.pref_title_sort_order_key),
                this.getString(R.string.pref_sort_order_default));

        Log.d(LOG_TAG, "SortOrder: " + sortOrder);

        if (adapter == null) {
            adapter = new GridViewAdapter(getActivity(), R.layout.gridview_layout, movies);
        }
        adapter.getMovies().clear();
        FetchMovieTask task = new FetchMovieTask(adapter, spinnerView);
        task.execute(sortOrder, Constants.API_KEY, String.valueOf(1));
        adapter.notifyDataSetChanged();

    }


    private void fetchDataAppend(int page) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String sortOrder = preferences.getString(this.getString(R.string.pref_title_sort_order_key),
                this.getString(R.string.pref_sort_order_default));

        Log.d(LOG_TAG, "SortOrder: " + sortOrder);
        if (adapter == null) {
            adapter = new GridViewAdapter(getActivity(), R.layout.gridview_layout, movies);
        }
        FetchMovieTask task = new FetchMovieTask(adapter, spinnerView);
        task.execute(sortOrder, Constants.API_KEY, String.valueOf(page));
        adapter.notifyDataSetChanged();

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            String status = NetworkUtil.getConnectivityStatusString(context);

            Intent shareIntent = new Intent(context, MainActivity.class);
            //context.startActivity(shareIntent);
            Log.d(LOG_TAG, "Net work status is: " + status);
            if (!Constants.NET_STATUS_NOT_CONNECTED.equals(NetworkUtil.getConnectivityStatusString(context))) {
                fetchData();
                if (rootView != null) {
                    TextView tv = (TextView) rootView.findViewById(R.id.tv_no_data);
                    tv.setText("");
                }
            }
        }
    }
}
