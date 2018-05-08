package d.manh.movienow;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import d.manh.movienow.network.NetworkModule;
import d.manh.movienow.utils.StoreContract;
import d.manh.moviewnow.R;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    NetworkModule networkModule;
    private int currentOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        // Option 1 means: requiring popular movie, 2 means requiring top rating
        Context context = getApplicationContext();
        networkModule = new NetworkModule(this,this,context);
        networkModule.firstLoadData(1);
        currentOption = 1;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Load first page.
    private void loadMovie(int option){
        // Option 1 means: requiring popular movie, 2 means requiring top rating

        // Clear Loader, if exist -> restart.
        // Clear data
        if(getLoaderManager().getLoader(StoreContract.MOVIE_LOADER_ID).isStarted()){
            getLoaderManager().destroyLoader(StoreContract.MOVIE_LOADER_ID);
            networkModule.cleanData();
        }
        networkModule.implementLoader(option);
    }

}
