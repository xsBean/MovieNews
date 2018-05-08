package d.manh.movienow.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import d.manh.movienow.utils.Movie;
import d.manh.movienow.utils.QueryUtils;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private URL url;

    public MovieLoader(Context context, URL url) {
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
        List<Movie> result = new ArrayList<>();
        try {
            result = QueryUtils.fetchMovieData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
