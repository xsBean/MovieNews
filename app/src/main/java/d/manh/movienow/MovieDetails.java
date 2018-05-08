package d.manh.movienow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import d.manh.movienow.utils.Movie;
import d.manh.movienow.utils.StoreContract;
import d.manh.moviewnow.R;

public class MovieDetails extends AppCompatActivity {

    private TextView tvTitleDetail;
    private TextView tvReleaseDateDetail;
    private TextView tvRatingDetail;
    private TextView tvSummaryDetail;
    private CollapsingToolbarLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        tvTitleDetail = findViewById(R.id.tv_title_detail);
        tvReleaseDateDetail = findViewById(R.id.tv_release_date_detail);
        tvRatingDetail = findViewById(R.id.tv_top_rate_detail);
        tvSummaryDetail = findViewById(R.id.tv_summary_detail);

        toolbar = findViewById(R.id.collapsingToolbar);
        CustomAppBar();

        // Get Intent
        Intent i = getIntent();
        Movie movie = i.getParcelableExtra("MoviesObject");
        UpdateDetailActivity(movie);
    }
    @SuppressLint("SetTextI18n")
    private void UpdateDetailActivity(Movie movie){
        String url = StoreContract.URL_IMAGE + StoreContract.URL_PATH_BACKGROUND_IMAGE_SIZE + movie.getBackgroundPath();
//        Log.v("Detail: ", url);
        Picasso.with(this)
                .load(url)
//                .error(R.drawable.error)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable d = new BitmapDrawable(getResources(),bitmap);
                        toolbar.setBackground(d);
//                                setDisplayShowHomeEnabled(true);
//                        toolbar.setDisplayHomeAsUpEnabled(true);
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
    }
    private void CustomAppBar(){
        toolbar.setTitle("");
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
                    toolbar.setTitle(getString(R.string.movie_detail));
                    isShow = true;
                }else if(isShow){
                    toolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }
}
