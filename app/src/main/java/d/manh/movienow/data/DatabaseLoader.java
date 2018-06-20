package d.manh.movienow.data;



import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.app.LoaderManager;
import d.manh.movienow.MainActivity;
import d.manh.movienow.R;
import d.manh.movienow.utils.RecyclerViewAdapterCursor;

public class DatabaseLoader implements  LoaderManager.LoaderCallbacks<Cursor>{
    private static final int ID_MOVIE_LOADER = 3 ;
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MainActivity view;
    private RecyclerViewAdapterCursor rvAdapterCursor;
    public static final String[] MAIN_MOVIE_PROJECTION = {
            StoreContract.COLUMN_MOVIE_ID,
            StoreContract.COLUMN_MOVIE_TITLES,
            StoreContract.COLUMN_MOVIE_POSTER_PATH,
    };

    public DatabaseLoader(MainActivity view){
        this.view = view;
        recyclerView = view.findViewById(R.id.rv_contain_main);
        rvAdapterCursor = (RecyclerViewAdapterCursor) recyclerView.getAdapter();
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_MOVIE_LOADER:

                Uri queryUri = StoreContract.CONTENT_URI;
//                return (Loader<Cursor>) context.getContentResolver().query(queryUri,null,null,null,null);
                return new CursorLoader(view,
                        queryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        recyclerView.setVisibility(View.VISIBLE);
        rvAdapterCursor.swapCursor(data);
        rvAdapterCursor.notifyDataSetChanged();
//        if(rvAdpaterCursor)
//        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
////      COMPLETED (30) Smooth scroll the RecyclerView to mPosition
//        mRecyclerView.smoothScrollToPosition(mPosition);
//
////      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
//        if (data.getCount() != 0) showWeatherDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        rvAdapterCursor.swapCursor(null);
    }

}
