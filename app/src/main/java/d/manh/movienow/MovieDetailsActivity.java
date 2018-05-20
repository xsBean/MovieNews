package d.manh.movienow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import d.manh.movienow.models.Movie;
import d.manh.movienow.models.Review;
import d.manh.movienow.models.Trailer;
import d.manh.movienow.network.LoadTrailerAndReview;
import d.manh.movienow.utils.ListItemClickListener;
import d.manh.movienow.utils.LoadReviewAndTrailerInt;
import d.manh.movienow.utils.RecyclerViewReviewAdapter;
import d.manh.movienow.utils.RecyclerViewTrailerAdapter;
import d.manh.movienow.data.StoreContract;

public class MovieDetailsActivity extends AppCompatActivity implements ListItemClickListener,LoadReviewAndTrailerInt {

    private String TAG = MovieDetailsActivity.class.getSimpleName();
    private TextView tvTitleDetail;
    private TextView tvReleaseDateDetail;
    private TextView tvRatingDetail;
    private TextView tvSummaryDetail;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView rvTrailer;
    private RecyclerView rvReview;
    private RecyclerViewTrailerAdapter rvTrailerAdapter;
    private RecyclerViewReviewAdapter rvReviewAdapter;
    private Movie movie;
    private TextView trailerLabel;
    private TextView reviewLabel;
    private ProgressBar progressBarTrailer;
    private ArrayList<Review> reviewsList = new ArrayList<>();
    private ArrayList<Trailer> trailersList = new ArrayList<>();
    private final String STATE_MOVIE = "STATE_MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        tvTitleDetail = findViewById(R.id.tv_title_detail);
        tvReleaseDateDetail = findViewById(R.id.tv_release_date_detail);
        tvRatingDetail = findViewById(R.id.tv_top_rate_detail);
        tvSummaryDetail = findViewById(R.id.tv_summary_detail);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        rvTrailer = findViewById(R.id.rv_trailers);
        rvReview = findViewById(R.id.rv_review);
        trailerLabel = findViewById(R.id.trailers_label);
        reviewLabel = findViewById(R.id.reviews_label);
        progressBarTrailer = findViewById(R.id.pb_trailers);
        CustomAppBar();

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.movie.setTrailers(trailersList);
        this.movie.setReviews(reviewsList);
        outState.putParcelable(STATE_MOVIE, movie);
    }

    // Save move object when rotating
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.movie = (Movie) savedInstanceState.getParcelable(STATE_MOVIE);
        trailersList = movie.getTrailers();
        reviewsList = movie.getReviews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get Intent
        if(movie == null){
            Intent i = getIntent();
            this.movie = i.getParcelableExtra("MoviesObject");

            // Load trailers and reviews from API
            LoadTrailerAndReview loadTrailerAndReview = new LoadTrailerAndReview(this,this);
            loadTrailerAndReview.implementTrailerReviewLoader(movie);
        }

        // Update views
        UpdateDetailActivity(movie);

    }

    @SuppressLint("SetTextI18n")
    private void UpdateDetailActivity(Movie movie){
        String url = StoreContract.URL_IMAGE + StoreContract.URL_PATH_BACKGROUND_IMAGE_SIZE + movie.getBackgroundPath();
//        Log.v("Detail: ", url);
        Picasso.with(this)
                .load(url)
                .error(R.drawable.error)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable d = new BitmapDrawable(getResources(),bitmap);
                        collapsingToolbar.setBackground(d);
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
        tvTitleDetail.setText(movie.getTitle());
        tvReleaseDateDetail.setText(getString(R.string.release_date) + movie.getReleaseDate());
        tvRatingDetail.setText(getString (R.string.rating) + movie.getRating().toString());
        tvSummaryDetail.setText(movie.getDescription());

        // Set invisible all views and visible progress bar
        if(trailersList.isEmpty()){
            progressBarTrailer.setVisibility(View.VISIBLE);
            trailerLabel.setVisibility(View.GONE);
            rvTrailer.setVisibility(View.GONE);
            reviewLabel.setVisibility(View.GONE);
            rvReview.setVisibility(View.GONE);
        }else{
            trailerLabel.setVisibility(View.VISIBLE);
            rvTrailer.setVisibility(View.VISIBLE);
            reviewLabel.setVisibility(View.VISIBLE);
            rvReview.setVisibility(View.VISIBLE);
        }

        // Setup trailers and reviews adapters
        rvTrailerAdapter = new RecyclerViewTrailerAdapter(this, trailersList);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rvTrailer.setHasFixedSize(true);
        rvTrailer.setAdapter(rvTrailerAdapter);

        rvReviewAdapter = new RecyclerViewReviewAdapter(reviewsList);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReview.setHasFixedSize(true);
        rvReview.setAdapter(rvReviewAdapter);
    }
    private void CustomAppBar(){
        collapsingToolbar.setTitle("");
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.CollapsedToolbar);

        Toolbar toolbar = findViewById(R.id.toolbar_id);
        if(toolbar != null){
            this.setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1)
                    scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0){
                    collapsingToolbar.setTitle(getString(R.string.movie_detail));
                    isShow = true;
                }else if(isShow){
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
    @Override
    public void loadTrailer(ArrayList<Trailer> trailers) {
        if(trailers != null){
            trailersList.clear();
            trailersList.addAll(trailers);
            progressBarTrailer.setVisibility(View.GONE);
            trailerLabel.setVisibility(View.VISIBLE);
            rvTrailer.setVisibility(View.VISIBLE);
            rvTrailerAdapter.notifyDataSetChanged();
        }else {
            progressBarTrailer.setVisibility(View.GONE);
            trailerLabel.setVisibility(View.GONE);
            rvTrailer.setVisibility(View.GONE);
        }
    }
    @Override
    public void loadReview(ArrayList<Review> reviews) {
        if(reviews != null){
            reviewsList.clear();
            reviewsList.addAll(reviews);
            reviewLabel.setVisibility(View.VISIBLE);
            rvReview.setVisibility(View.VISIBLE);
            rvReviewAdapter.notifyDataSetChanged();
        }else{
            reviewLabel.setVisibility(View.GONE);
            rvReview.setVisibility(View.GONE);
        }
    }


}
