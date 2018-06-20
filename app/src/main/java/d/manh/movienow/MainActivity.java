package d.manh.movienow;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import d.manh.movienow.models.Movie;
import d.manh.movienow.network.NetworkModule;
import d.manh.movienow.data.StoreContract;


public class MainActivity extends AppCompatActivity{

    private static final String CURRENT_RECYCLERVIEW_STATE =  "CURRENT_RECYCLERVIEW_STATE";
    private static final String CURRENT_OPTION ="CURRENT_OPTION";
    private static final String CURRENT_MOVIE_LIST = "CURRENT_MOVIE_LIST";
    private static final String CURRENT_PAGE = "CURRENT_PAGE" ;
    private static Parcelable currentRecyclerViewState = null;
    NetworkModule networkModule;
    private int currentOption;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerViewMainMovie = findViewById(R.id.rv_contain_main);
        networkModule = new NetworkModule(this,this,this);

        int value = getResources().getConfiguration().orientation;
        if(value == Configuration.ORIENTATION_PORTRAIT){
            recyclerViewMainMovie.setLayoutManager(new GridLayoutManager(this,3));
        }else {
            recyclerViewMainMovie.setLayoutManager(new GridLayoutManager(this,5));
        }
        if(savedInstanceState != null){
            ArrayList<Movie> listMovies = savedInstanceState.getParcelableArrayList(CURRENT_MOVIE_LIST);
            currentOption = savedInstanceState.getInt(CURRENT_OPTION);

            if(currentOption !=3){
                networkModule.setUp(listMovies,currentOption);
                networkModule.setCurrentPage(savedInstanceState.getInt(CURRENT_PAGE));
            }else {
                // load data from database
                networkModule.firstLoadData(currentOption);
            }
        }
        layoutManager = recyclerViewMainMovie.getLayoutManager();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // First load
        if(currentRecyclerViewState ==null){
            if(networkModule.checkInternetConnection())
            //Load data from internet
            {
                networkModule.firstLoadData(1);
                currentOption = 1;
            }else // Load data from database
                {
                networkModule.firstLoadData(3);
                currentOption = 3;
            }
        }
        else{
            layoutManager.onRestoreInstanceState(currentRecyclerViewState);
            currentRecyclerViewState = null;
            // Update cursor
            if(currentOption == 3){
                networkModule.swapCursorAdapter();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.top_rated) {
            // Load the page if the current option is not 1;
            if(currentOption != 1){
                loadMovie(1);
                currentOption = 1;
                return true;
            }
        }else if(id == R.id.most_popular){
            // Load the page if the current option is not 2;
            if(currentOption !=2){
                loadMovie(2);
                currentOption = 2;
                return true;
            }
        }else if(id == R.id.it_favorite){
            if(currentOption !=3){
                loadMovie(3);
                currentOption = 3;
                return true;
            }
        }
        return false;
    }

    //Load first page.
    private void loadMovie(int option){
        // Option 1 means: requiring popular movie, 2 means requiring top rating
        // Clear Loader, if exist -> restart.
        // Clear data
        if(option != 3){
            // Load data from internet
            if(networkModule.checkInternetConnection()){
                if(getLoaderManager().getLoader(StoreContract.MOVIE_LOADER_ID) != null && getLoaderManager().getLoader(StoreContract.MOVIE_LOADER_ID).isStarted() ){
                    getLoaderManager().destroyLoader(StoreContract.MOVIE_LOADER_ID);
                    networkModule.cleanData();
                }
                networkModule.firstLoadData(option);
            }else {
                Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
        }else{
            //Load data from database
            networkModule.firstLoadData(option);
        }
        currentOption = option;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        currentRecyclerViewState = layoutManager.onSaveInstanceState();
        outState.putInt(CURRENT_OPTION,currentOption);
        outState.putParcelable(CURRENT_RECYCLERVIEW_STATE, currentRecyclerViewState);
        ArrayList<Movie> listMovies = networkModule.getData();
        outState.putParcelableArrayList(CURRENT_MOVIE_LIST, listMovies);
        int currentPage = networkModule.getCurrentPage(); // current page loaded from API
        outState.putInt(CURRENT_PAGE, currentPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(currentRecyclerViewState != null){
            currentRecyclerViewState = savedInstanceState.getParcelable(CURRENT_RECYCLERVIEW_STATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
