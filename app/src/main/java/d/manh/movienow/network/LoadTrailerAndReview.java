package d.manh.movienow.network;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.net.URL;

import d.manh.movienow.models.Movie;
import d.manh.movienow.utils.ListItemClickListener;
import d.manh.movienow.utils.LoadReviewAndTrailerInt;
import d.manh.movienow.data.StoreContract;


public class LoadTrailerAndReview implements LoaderManager.LoaderCallbacks<Movie> ,ListItemClickListener {

    private Activity activity;
    private Context context;
    private URL urlTrailer;
    private URL urlReview;
    private LoaderManager loaderManager;
    private Movie movie;
    private LoadReviewAndTrailerInt loadReviewAndTrailerInt;
//    private RecyclerView rvTrailer;
//    private RecyclerView rvReview;
//    private RecyclerViewTrailerAdapter rvTrailerAdapter;
//    private RecyclerViewReviewAdapter rvReviewAdapter;

    public LoadTrailerAndReview(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        loaderManager = activity.getLoaderManager();
//        rvTrailer = view.findViewById(R.id.rv_trailers);
//        rvReview = view.findViewById(R.id.rv_review);

        loadReviewAndTrailerInt = (LoadReviewAndTrailerInt) activity;
    }

    public void implementTrailerReviewLoader(Movie movie){

        String movieID = ""+movie.getMovieId();
        // Option = 3 -> getTrailers, option = 4 -> getReviews, page = 1
        urlTrailer = new URLCreate(3,1).createGetReviewOrVideoURL(movieID);
        urlReview = new URLCreate(4,1).createGetReviewOrVideoURL(movieID);
        if(checkInternetConnection()){
            this.movie = movie;
            loaderManager.initLoader(StoreContract.REVIEW_TRAILER_LOADER_ID,null,this);
        }
    }
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        return new ReviewAndTrailerLoader(context, movie, loadReviewAndTrailerInt);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

//        loadReviewAndTrailerInt.LoadRvAndTrailer(data);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
    private boolean checkInternetConnection(){
        //Check the internet connection
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
