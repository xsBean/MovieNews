package d.manh.movienow.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    private static String makeHttpRequest(URL url) throws IOException{

        String JSONResponse = "";
        if(url == null) return JSONResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Check valid connection
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();

                // read the stream from the connection
                StringBuilder tempString = new StringBuilder();
                if(inputStream != null){
                    InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader br = new BufferedReader(streamReader);
                    String line =br.readLine();
                    while (line != null){
                        tempString.append(line);
                        line = br.readLine();
                    }
                    JSONResponse = tempString.toString();
                }
            }else{
                Log.e(LOG_TAG,"Error with URL Connection. Response Code: "+ urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the JSON result.",e);
        }finally {
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream !=null) inputStream.close();
        }

        return JSONResponse;
    }

    public static ArrayList<Movie> fetchMovieData(URL requestUrl) throws IOException {
        String jsonResponse = makeHttpRequest(requestUrl);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return extractMovies(jsonResponse);
    }


    /**
     * Return a list of {@link Movie objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Movie> extractMovies(String JSONResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            // String JSONResponse = makeHttpRequest(url);
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray jsonResults = root.getJSONArray("results");
            for (int i = 0 ; i < jsonResults.length();i++){
                JSONObject jsonCurrent = jsonResults.getJSONObject(i);
                String posterPath = jsonCurrent.getString("poster_path");
                String title = jsonCurrent.getString("original_title");
                String backdropPath = jsonCurrent.getString("backdrop_path");
                String description = jsonCurrent.getString("overview");
                String releaseDate = jsonCurrent.getString("release_date");
                Double rating = jsonCurrent.getDouble("vote_average");

                movieArrayList.add(new Movie(posterPath,backdropPath, title, releaseDate, rating, description, null,null));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        return movieArrayList;
    }
}
