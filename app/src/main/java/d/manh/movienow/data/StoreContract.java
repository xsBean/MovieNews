package d.manh.movienow.data;

import android.provider.BaseColumns;

public final class StoreContract implements BaseColumns {
    private StoreContract(){
        throw new AssertionError("No Instances for this class!");
    }
    //For movie's details
    public static final String URL = "https://api.themoviedb.org/3/movie/";
    public static final String URL_IMAGE = "http://image.tmdb.org/t/p/";
    public static final String URL_PATH_POSTER_SIZE = "w185/";
    public static final String URL_PATH_BACKGROUND_IMAGE_SIZE = "w500/";
    public static final String GET_POPULAR_PATH = "popular";
    public static final String GET_TOP_RATE_PATH = "top_rated";
    public static final String GET_LATEST_PATH = "latest";
    public static final String GET_REVIEWS = "reviews";
    public static final String GET_TRAILERS = "video";
    public static final String API_KEY = "b405479b5598012ae0ec2fc54dff97c7";
    public static final String API_PATH = "?api_key=";
    public static final String API_LANGUAGE ="&language=";
    public static final String API_PAGE = "&page=";
    public static final int MOVIE_LOADER_ID = 1;
    public static final int REVIEW_TRAILER_LOADER_ID = 2;

    //For trailer's details
    public static final String URL_YOUTUBE = "https://img.youtube.com/vi/";
    public static final String URL_YOUTUBE_PATH = "/0.jpg";

    //For database
    public static final String DATABASE_NAME = "MovieDb.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_MOVIE = "movies";
//    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_TITLES = "movie_title";
    public static final String COLUMN_MOVIE_RELEASE_DATE ="movie_release_date";
    public static final String COLUMN_MOVIE_RATING = "movie_dating";
    public static final String COLUMN_MOVIE_SUMMARY = "movie_summary";

}
