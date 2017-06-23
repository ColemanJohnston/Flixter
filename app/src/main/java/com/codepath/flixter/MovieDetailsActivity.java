package com.codepath.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MovieDetailsActivity extends AppCompatActivity {

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    public static final String API_KEY_PARAM = "api_key";

    public static final String TAG = "MovieDetailsActivity";

    Movie movie;
    AsyncHttpClient client;
    String trailerId;

    //set up to bind fields to views
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        client = new AsyncHttpClient();

        //bind fields to views
        ButterKnife.bind(this);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        getTrailerId();
    }


    private void getTrailerId(){
        //construct url with movie id to get list of YouTube trailer ids
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    if(results.length() > 0){
                        trailerId = results.getJSONObject(0).getString("key");

                        Log.i(TAG,String.format("Loaded YouTube key %s for %s", trailerId, movie.getTitle()));
                    }
                    else{
                        logError("No trailer to display for" + movie.getTitle(), new Throwable(), true);
                    }

                } catch (JSONException e) {
                    logError("Failed to parse trailer information", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from videos endpoint", throwable, true);
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
