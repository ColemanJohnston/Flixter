package com.codepath.flixter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flixter.models.Config;
import com.codepath.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by colemanmav on 6/21/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //list of movies
    ArrayList<Movie> movies;

    Config config;

    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }

    //creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imageUrl = null;

        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(),movie.getPosterPath());
        }
        else{
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        int placeHolderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;

        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeHolderID)
                .error(R.drawable.flicks_movie_placeholder)
                .into(imageView);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // track view objects

        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;

        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;

        @BindView(R.id.tvTitle) TextView tvTitle;

        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);

                Intent intent = new Intent(context,MovieDetailsActivity.class);

                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                context.startActivity(intent);
            }
        }
    }
}
