package d.manh.movienow.network;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import d.manh.movienow.data.MovieDbHelper;
import d.manh.movienow.data.StoreContract;
import d.manh.movienow.models.Movie;
import d.manh.movienow.utils.QueryUtils;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private URL url;


    MovieLoader(Context context, URL url) {
        super(context);
        this.url = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
            if(url == null) return null;
            try {
                return QueryUtils.fetchMovieData(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return null;
    }

}
