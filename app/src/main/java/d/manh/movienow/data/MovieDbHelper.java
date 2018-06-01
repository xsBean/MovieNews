package d.manh.movienow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    public MovieDbHelper(Context context){
        super(context,StoreContract.DATABASE_NAME,null,StoreContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create movies table
        final String CREATE_TABLE = "CREATE TABLE "             + StoreContract.TABLE_MOVIE + " (" +
                StoreContract.COLUMN_MOVIE_ID                   + " INTEGER PRIMARY KEY, " +
                StoreContract.COLUMN_MOVIE_TITLES               + " TEXT NOT NULL, " +
                StoreContract.COLUMN_MOVIE_RELEASE_DATE         + " TEXT NOT NULL, " +
                StoreContract.COLUMN_MOVIE_RATING               + " REAL NOT NULL, " +
                StoreContract.COLUMN_MOVIE_POSTER_PATH          + " TEXT NOT NULL, " +
                StoreContract.COLUMN_MOVIE_BACKGROUND_IMAGE_PATH    + " TEXT NOT NULL, " +
                StoreContract.COLUMN_MOVIE_SUMMARY              + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StoreContract.TABLE_MOVIE);
        onCreate(db);
    }
}
