package com.codepath.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //Constants

    //Base url for api
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    public static final String API_KEY_PARAM = "api_key";

    // the api key TODO: move to a more secure location

    //tag for logging
    public final String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;

    //the base url for loading images
    String imageBaseUrl;

    //poster size for base url
    String posterSize;

    ArrayList<Movie> movies;

    //the recycler view
    RecyclerView rvMovies;

    //the adapter wired to the recycler view
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();
        movies = new ArrayList<Movie>();

        adapter = new MovieAdapter(movies);

        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        rvMovies.setAdapter(adapter);

        getConfiguration();
    }

    private void getConfiguration(){
        String url = API_BASE_URL + "/configuration";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        //execute get
        client.get(url, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");
                    imageBaseUrl = images.getString("secure_base_url");
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");

                    posterSize = posterSizeOptions.optString(3,"w342");
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", imageBaseUrl, posterSize));

                    //get the now playing movie list after getting the configuration
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configurations",throwable, true);

            }
        });
    }

    private void getNowPlaying(){
        String url = API_BASE_URL + "/movie/now_playing";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for(int i = 0; i < results.length(); ++i){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);

                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG,String.format("Loaded  %s movies", results.length()));

                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }


    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);

        if(alertUser){
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
        }
    }

}
