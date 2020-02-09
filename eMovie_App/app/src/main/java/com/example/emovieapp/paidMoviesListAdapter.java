package com.example.emovieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class paidMoviesListAdapter extends BaseAdapter {
    private String[] moviesImages;
    private String[] moviesTitles, moviesTypes, moviesGenres, moviesId;

    private LayoutInflater inflter;
    private Context context;
    private ImageView movieImg;
    private TextView movieTitle, typesMovie, genresMovie, idMovie;
    private Button cancel,show_code;

    public paidMoviesListAdapter(String[] moviesImages, String[] moviesTitles, String[] moviesTypes,
                             String[] moviesGenres, String[] moviesId, Context context) {
        this.moviesImages = moviesImages;
        this.moviesTitles = moviesTitles;
        this.moviesTypes = moviesTypes;
        this.moviesGenres = moviesGenres;
        this.moviesId = moviesId;

        this.context = context;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return moviesTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflter.inflate(R.layout.paid_movie_row_view, null);

        movieTitle = view.findViewById(R.id.movieTitle);
        typesMovie = view.findViewById(R.id.yearMovie);
        genresMovie = view.findViewById(R.id.genresMovie);
        idMovie = view.findViewById(R.id.movie_id);
        cancel = view.findViewById(R.id.Cancel);
        show_code = view.findViewById(R.id.Display_Code);

        new DownloadMovieImage((ImageView) view.findViewById(R.id.movieImg))
                .execute(moviesImages[position]);
        movieTitle.setText(moviesTitles[position]);
        typesMovie.setText(moviesTypes[position]);
        genresMovie.setText(moviesGenres[position]);
        idMovie.setText(moviesId[position]);
        cancel.setTag(moviesId[position]);
        show_code.setTag(moviesId[position]);

        return view;
    }}