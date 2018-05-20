package d.manh.movienow.network;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import d.manh.movienow.data.StoreContract;

public class URLCreate{

    private int option;
    private int page;

    public URLCreate(int option, int page){
        this.option = option;
        this.page = page;
    }
    public URL joinURLPath(){

//      Ex:  "https://api.themoviedb.org/3/movie/popular?api_key=b405479b5598012ae0ec2fc54dff97c7&language=en-US&page=2"
        String optionPath = getOptionPath(option);
        String apiKey = StoreContract.API_KEY;
        String apiPath = StoreContract.API_PATH;
        String language = "en-US";
        try {
            return new URL(StoreContract.URL
                    + optionPath
                    + apiPath+apiKey
                    + StoreContract.API_LANGUAGE + language
                    + StoreContract.API_PAGE + page
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public URL createGetReviewOrVideoURL(String movieId){
//      Ex:  "https://api.themoviedb.org/3/movie/{movieID}/reviews?api_key=b405479b5598012ae0ec2fc54dff97c7&language=en-US&page=1"
        String optionPath = getOptionPath(option);
        String apiKey = StoreContract.API_KEY;
        String apiPath = StoreContract.API_PATH;
        String language = "en-US";
        try {
            return new URL(StoreContract.URL
                    + movieId+"/"
                    + optionPath
                    + apiPath+apiKey
                    + StoreContract.API_LANGUAGE + language
                    + StoreContract.API_PAGE + page
            );
        } catch (MalformedURLException e) {
            Log.v("Create Get Review URL: ", "Cannot create URL");
            e.printStackTrace();
            return null;
        }
    }
    private String getOptionPath(int option){
        if(option == 2)
            return StoreContract.GET_POPULAR_PATH;
        else if(option == 1)
            return StoreContract.GET_TOP_RATE_PATH;
        else if(option == 3)
            return StoreContract.GET_TRAILERS;
        else if(option == 4)
            return StoreContract.GET_REVIEWS;
        else
            return StoreContract.GET_LATEST_PATH;
    }
}
