package d.manh.movienow;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private final String STATE_POSTER_ARRAY = "POSTER_ARRAY";
    private final String CURRENT_OPTION = "CURRENT_OPTION";
    private byte[] bitmapPosterByteArray;
    private int option;



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
        outState.putByteArray(STATE_POSTER_ARRAY, bitmapPosterByteArray);
        outState.putParcelable(STATE_MOVIE, movie);
        outState.putInt(CURRENT_OPTION, option);
    }

    // Save move object when rotating
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.movie = savedInstanceState.getParcelable(STATE_MOVIE);
        this.option = savedInstanceState.getInt(CURRENT_OPTION);
        assert movie != null;
        trailersList = movie.getTrailers();
        reviewsList = movie.getReviews();
        bitmapPosterByteArray = savedInstanceState.getByteArray(STATE_POSTER_ARRAY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get Intent
        if(movie == null){
            Intent i = getIntent();
            this.movie = i.getParcelableExtra("MoviesObject");
            this.option = i.getIntExtra("identify",0);
            bitmapPosterByteArray = i.getByteArrayExtra("poster");

        }
        // Update views
        UpdateDetailActivity(movie);
    }

    private void UpdateDetailActivity(Movie movie){
        if(option != 3){

            // Load trailers and reviews from API
            LoadTrailerAndReview loadTrailerAndReview = new LoadTrailerAndReview(this,this);
            loadTrailerAndReview.implementTrailerReviewLoader(movie);

            // Load image from URL
            String url = StoreContract.URL_IMAGE + StoreContract.URL_PATH_BACKGROUND_IMAGE_SIZE + movie.getBackgroundPath();
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

            // Setup trailers and reviews adapters
            rvTrailerAdapter = new RecyclerViewTrailerAdapter(this, trailersList);
            rvTrailer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            rvTrailer.setHasFixedSize(true);
            rvTrailer.setAdapter(rvTrailerAdapter);

            rvReviewAdapter = new RecyclerViewReviewAdapter(reviewsList);
            rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvReview.setHasFixedSize(true);
            rvReview.setAdapter(rvReviewAdapter);

        }else {
            // Load background image from Cursor
            String uri = movie.getBackgroundPath();
            Drawable drawable = Drawable.createFromPath(uri);
            collapsingToolbar.setBackground(drawable);
        }

        tvTitleDetail.setText(movie.getTitle());
        String releaseDate = getString(R.string.release_date) + movie.getReleaseDate();
        tvReleaseDateDetail.setText(releaseDate);
        String rating = getString (R.string.rating) + movie.getRating().toString();
        tvRatingDetail.setText(rating);
        tvSummaryDetail.setText(movie.getDescription());

        if(!trailersList.isEmpty()){
            trailerLabel.setVisibility(View.VISIBLE);
            rvTrailer.setVisibility(View.VISIBLE);
        }
        if(!reviewsList.isEmpty()){
            reviewLabel.setVisibility(View.VISIBLE);
            rvReview.setVisibility(View.VISIBLE);
        }
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
        Trailer trailer = trailersList.get(clickedItemIndex);
        String key = trailer.getKey();
        String url = "https://www.youtube.com/watch?v="+key;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    public void loadTrailer(ArrayList<Trailer> trailers) {
        if(trailers != null){
            trailersList.clear();
//            Collections.addAll(trailersList,trailers);
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
//            Collections.addAll(reviewsList,reviews);
            reviewsList.addAll(reviews);
            reviewLabel.setVisibility(View.VISIBLE);
            rvReview.setVisibility(View.VISIBLE);
            rvReviewAdapter.notifyDataSetChanged();
        }else{
            reviewLabel.setVisibility(View.GONE);
            rvReview.setVisibility(View.GONE);
        }
    }

    // Catch the onclick event from floating button
    public void fbOnClick(View view){

        //Step 1: Check whether the movie in the database
        String selection = StoreContract.COLUMN_MOVIE_TITLES + " = ?";
        String[] selectionArgs = new String[]{movie.getTitle()};
        String sortOrder = StoreContract.COLUMN_MOVIE_TITLES + " DESC";

        Uri queryUri = StoreContract.CONTENT_URI;
        Cursor cursor = getContentResolver().query(queryUri,null,selection,selectionArgs,sortOrder);

        if(cursor != null && cursor.moveToFirst() ) {

            // Build appropriate uri with String row id appended
            int idIndex = cursor.getInt(0);
            String stringId = Integer.toString(idIndex);
            Uri uri = StoreContract.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            String posterPath = cursor.getString(4);
            String backgroundPath = cursor.getString(5);
            int row = getContentResolver().delete(uri, null, null);
            if (row > 0) {
                deleteImageFile(posterPath);
                deleteImageFile(backgroundPath);
                Toast.makeText(this, "Removed the movie from the favorite lists", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Cannot remove the movie from the favorite lists", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        else {
//        Step 2: If not - insert to database
            Bitmap bitmapPoster = BitmapFactory.decodeByteArray(bitmapPosterByteArray,0,bitmapPosterByteArray.length);
            String posterPath = SaveImagesToExternalStorage(bitmapPoster, movie.getTitle());

            BitmapDrawable bitmapDrawable = (BitmapDrawable) collapsingToolbar.getBackground();
            Bitmap bitmapBackground = bitmapDrawable.getBitmap();
            String backgroundPath = SaveImagesToExternalStorage(bitmapBackground,movie.getTitle()+"_bg");

            ContentValues values = new ContentValues();
            values.put(StoreContract.COLUMN_MOVIE_ID,movie.getMovieId());
            values.put(StoreContract.COLUMN_MOVIE_TITLES, movie.getTitle());
            values.put(StoreContract.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
            values.put(StoreContract.COLUMN_MOVIE_RATING,movie.getRating());
            values.put(StoreContract.COLUMN_MOVIE_POSTER_PATH, posterPath);
            values.put(StoreContract.COLUMN_MOVIE_BACKGROUND_IMAGE_PATH,backgroundPath);
            values.put(StoreContract.COLUMN_MOVIE_SUMMARY,movie.getDescription());

            Uri newUri = getContentResolver().insert(StoreContract.CONTENT_URI,values);
            if(newUri == null)
                Toast.makeText(this,"Insert Movie failed",Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this,"Insert Movie successful to favorite list" ,Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String SaveImagesToExternalStorage(Bitmap bitmap, String nameFile) {
        File file;
        // Get the external storage directory path
        PackageManager m = getPackageManager();
        String s = getPackageName();
        String path = null;
        try{
            PackageInfo info = m.getPackageInfo(s, 0);
            path = info.applicationInfo.dataDir;
            File directory = new File(path+File.separator + "MovieImages");
            boolean result = false;
            if(! directory.exists() && !directory.isDirectory()){
                result = directory.mkdir();
            }
            path = path+File.separator + "MovieImages";
            Log.v(TAG+" Package Dir: ", path+"\n Create Status: "+result);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.v("Package Dir:" , "Error Package name not found", e);
        }

        // Create a file to save the image
        file = new File(path, nameFile+".jpg");
        try{
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void deleteImageFile(String imagePath) {
        File file = new File(imagePath);
        boolean result = false;
        if (file.exists() && file.isFile()) {
            result = file.delete();
        }
        Log.v(TAG+": Delete: " +imagePath, "Result: "+result);
    }

}
