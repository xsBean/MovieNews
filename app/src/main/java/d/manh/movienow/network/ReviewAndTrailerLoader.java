package d.manh.movienow.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;

import d.manh.movienow.utils.GetReviewAPI;
import d.manh.movienow.utils.GetTrailerAPI;
import d.manh.movienow.models.Movie;
import d.manh.movienow.models.Review;
import d.manh.movienow.models.ReviewWrapper;
import d.manh.movienow.utils.LoadReviewAndTrailerInt;
import d.manh.movienow.data.StoreContract;
import d.manh.movienow.models.Trailer;
import d.manh.movienow.models.TrailerWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewAndTrailerLoader extends AsyncTaskLoader<Movie> {

    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private Movie movie;
    private LoadReviewAndTrailerInt loadReviewAndTrailerInt;

    public ReviewAndTrailerLoader(Context context, Movie movie, LoadReviewAndTrailerInt loadReviewAndTrailerInt) {
        super(context);
        this.movie = movie;
        this.loadReviewAndTrailerInt = loadReviewAndTrailerInt;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public Movie loadInBackground() {
        fetchTrailers();
        fetchReviews();
        return movie;
    }

    private void fetchTrailers() {
        Retrofit retrofitGetTrailer = new Retrofit.Builder()
                .baseUrl(StoreContract.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetTrailerAPI apiTrailer = retrofitGetTrailer.create(GetTrailerAPI.class);
        Call<TrailerWrapper> callTrailerWrapper = apiTrailer.getTrailerWrapper("" + movie.getMovieId());
        callTrailerWrapper.enqueue(new Callback<TrailerWrapper>() {
            @Override
            public void onResponse(@NonNull Call<TrailerWrapper> call, @NonNull Response<TrailerWrapper> response) {
                TrailerWrapper trailerWrapper = response.body();
                assert trailerWrapper != null;
                trailers = trailerWrapper.getResults();
                movie.setTrailers(trailers);
                loadReviewAndTrailerInt.loadTrailer(trailers);
            }
            @Override
            public void onFailure(Call<TrailerWrapper> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchReviews() {
        Retrofit retrofitGetReview = new Retrofit.Builder()
                .baseUrl(StoreContract.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetReviewAPI apiReview = retrofitGetReview.create(GetReviewAPI.class);
        Call<ReviewWrapper> callReview = apiReview.getReviewWrapper(""+movie.getMovieId());
        callReview.enqueue(new Callback<ReviewWrapper>() {
            @Override
            public void onResponse(@NonNull Call<ReviewWrapper> call, @NonNull Response<ReviewWrapper> response) {
                ReviewWrapper reviewWrapper = response.body();
                assert reviewWrapper != null;
                reviews = reviewWrapper.getResults();
                movie.setReviews(reviews);
                loadReviewAndTrailerInt.loadReview(reviews);

            }
            @Override
            public void onFailure(@NonNull Call<ReviewWrapper> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
