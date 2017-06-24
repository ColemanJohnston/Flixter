package com.codepath.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by colemanmav on 6/22/17.
 */

@Parcel
public class Config {
    //the base url for loading images
    String imageBaseUrl;

    //poster size for base url
    String posterSize;

    String backdropSize;

    public Config(){}

    public Config(JSONObject object) throws JSONException{
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3,"w342");

        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper to construct url
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s",imageBaseUrl, size, path); // concatenate
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
