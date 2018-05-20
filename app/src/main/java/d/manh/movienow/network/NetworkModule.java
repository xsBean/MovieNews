package d.manh.movienow.network;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import d.manh.movienow.MainActivity;
import d.manh.movienow.MovieDetailsActivity;
import d.manh.movienow.utils.ListItemClickListener;
import d.manh.movienow.models.Movie;
import d.manh.movienow.utils.RecyclerViewAdapter;
import d.manh.movienow.data.StoreContract;
import d.manh.movienow.R;

public class NetworkModule implements LoaderManager.LoaderCallbacks<List<Movie>>, ListItemClickListener {
    private String TAG = "NetworkModule";
    private RecyclerView recyclerViewMainMovie;
    private List<Movie> listMovie = new ArrayList<>();
    private RecyclerViewAdapter rvAdapter;
    private MainActivity view;
    private Activity activity;
    private Context context;
    private TextView tvErrorMessage;
    private ProgressBar loadingIndicator;
    private URL url;
    private int option;
    private LoaderManager loaderManager;

    // Store a member variable for the listener
    private boolean itShouldLoadMore = true;
    private int currentPage = 1;

    public NetworkModule(MainActivity view, Activity activity,Context context){
        this.view = view;
        this.activity = activity;
        this.context = context;
        loaderManager = activity.getLoaderManager();
        tvErrorMessage = view.findViewById(R.id.tv_error_message);
        loadingIndicator =  view.findViewById(R.id.pb_loading_indicator);
        recyclerViewMainMovie = view.findViewById(R.id.rv_contain_main);
    }
    public void firstLoadData(int option){
        // Create Layout manager and add to RecyclerView
        recyclerViewMainMovie.setLayoutManager(new GridLayoutManager(context,3));
        recyclerViewMainMovie.setHasFixedSize(true);
        rvAdapter = new RecyclerViewAdapter(this, listMovie);
        recyclerViewMainMovie.setAdapter(rvAdapter);
        implementMovieLoader(option);
        ImplementEndlessRecyclerViewScrollListener();
    }
    public void cleanData(){
        //clear data
        if(!listMovie.isEmpty()) {
            listMovie.clear();
            currentPage = 1;
        }
    }
    //Load movie details
    public void implementMovieLoader(int option){
        // Create URL
        this.option = option;
        url = new URLCreate(this.option,currentPage).joinURLPath();

        //  Using loader
        if(checkInternetConnection()){
            loaderManager.initLoader(StoreContract.MOVIE_LOADER_ID,null,this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            showErrorMessage(view.getString(R.string.no_connection));
        }
    }

    private boolean checkInternetConnection(){
        //Check the internet connection
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        loadingIndicator.setVisibility(View.VISIBLE);

        return new MovieLoader(context, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        ProgressBar progressBar = view.findViewById(R.id.pb_loading_indicator);
        assert progressBar != null;
        progressBar.setVisibility(View.GONE);

        if(data !=null && !data.isEmpty()) {
            showMovieDataView();
            listMovie.addAll(data);
            rvAdapter.notifyDataSetChanged(); // Important
            itShouldLoadMore = true;
        }else {
            showErrorMessage(view.getString(R.string.no_data));
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
//            loader = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(activity,MovieDetailsActivity.class);
        Movie movie = listMovie.get(clickedItemIndex);
        intent.putExtra("MoviesObject", movie);
        activity.startActivity(intent);
        Toast toast = Toast.makeText(activity,movie.getTitle(),Toast.LENGTH_LONG);
        toast.show();
    }
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        tvErrorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerViewMainMovie.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(String message) {
        /* First, hide the currently visible data */
        recyclerViewMainMovie.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        tvErrorMessage.setVisibility(View.VISIBLE);
        tvErrorMessage.setText(message);
    }

    private void ImplementEndlessRecyclerViewScrollListener(){
        // Setup Endless RecyclerView ScrollListener
        recyclerViewMainMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    //Recycle view scrolling downwards..
                    // this if statement detects when user reaches the end of RecyclerView, this
                    // is only time we should load more
                    if(!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)){
                        // we now allowed to load more, but we need to be careful, we must check if itShouldLoadMore variable is true(unlocked)
                        if(itShouldLoadMore){
                            loadNextDataFromApi();
                        }
                    }
                }
            }
        });
    }
    // Implement Endless RecyclerView Scroll Listener
    private void loadNextDataFromApi(){
        currentPage ++;
        this.url = new URLCreate(option,currentPage).joinURLPath();
        itShouldLoadMore = false;
        //  Using loader
        LoaderManager loaderManager = activity.getLoaderManager();
        if(checkInternetConnection()){
            if(loaderManager.getLoader(StoreContract.MOVIE_LOADER_ID).isStarted())
                loaderManager.restartLoader(StoreContract.MOVIE_LOADER_ID,null,this);
            else {
                loaderManager.initLoader(StoreContract.MOVIE_LOADER_ID,null,this);
            }
        }else {
            loadingIndicator.setVisibility(View.GONE);
            showErrorMessage(view.getString(R.string.no_connection));
        }
    }

}
