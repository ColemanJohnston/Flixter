package com.codepath.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by colemanmav on 6/21/17.
 */

public class Movie {

    //values form api

    private String title;
    private String overview;
    private String posterPath; // only the path

    private String backdropPath;

    public Movie(JSONObject object) throws JSONException{
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
