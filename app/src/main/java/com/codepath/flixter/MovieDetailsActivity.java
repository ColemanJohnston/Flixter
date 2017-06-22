package com.codepath.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.flixter.models.Movie;

import org.parceler.Parcels;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
    }
}
