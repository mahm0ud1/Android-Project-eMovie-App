package com.example.emovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 47;
    private ListView movies_list;
    private RequestQueue mQueue;


    private List<String> moviesImagesL = new ArrayList<>();
    private List<String> moviesTitlesL = new ArrayList<>();
    private List<String> moviesTypesL = new ArrayList<>();
    private List<String> moviesGenresL = new ArrayList<>();
    private List<String> moviesIdsL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.moviesList_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logoapp);

        movies_list = findViewById(R.id.listView);
        movies_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                click_movie(view, position);
            }
        });

        mQueue = Volley.newRequestQueue(this);

        try {
            //https://api.myjson.com/bins/w8hie
            jsonParse(DataValues.server_site + "?do=show_movies");

        } catch (Exception e) {

        }
    }

    /**
     * show the toolbar in the activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_list,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();

            String movie_id = uri.getQueryParameter("movie_id");

            if(!movie_id.trim().equals(""))
                show_movie(movie_id);
        }
    }

    public void Show_Movies_Type(String type)
    {
        List<String> moviesImages = new ArrayList<>();
        List<String> moviesTitles = new ArrayList<>();
        List<String> moviesTypes = new ArrayList<>();
        List<String> moviesGenres = new ArrayList<>();
        List<String> moviesIds = new ArrayList<>();

        try {

            for(int i=0 ;i < moviesIdsL.size(); ++i)
            {
                String movieType = moviesTypesL.get(i);

                if(movieType.contains(type.toUpperCase()))
                {
                    moviesImages.add(moviesImagesL.get(i));
                    moviesTitles.add(moviesTitlesL.get(i));
                    moviesTypes.add(moviesTypesL.get(i));
                    moviesGenres.add(moviesGenresL.get(i));
                    moviesIds.add(moviesIdsL.get(i));

                }
            }

            Load_List(moviesImages,moviesTitles,moviesTypes,moviesGenres,moviesIds);

        }catch (Exception e){}
    }

    private void jsonParse(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("movies");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);

                                String movieImage = movie.getString("movie_image");
                                String movieTitle = movie.getString("movie_title");
                                String movieYear = movie.getString("movie_type");
                                String movieGenre = movie.getString("movie_genre");
                                String movieId = movie.getString("movie_id");

                                moviesImagesL.add(movieImage);
                                moviesTitlesL.add(movieTitle);
                                moviesTypesL.add(movieYear);
                                moviesGenresL.add(movieGenre);
                                moviesIdsL.add(movieId);

                            }
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(), "aa",Toast.LENGTH_SHORT).show();
                        }

                        Load_List(moviesImagesL,moviesTitlesL,moviesTypesL,moviesGenresL,moviesIdsL);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "connection error",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    private void Load_List(List<String> moviesImagesL,List<String> moviesTitlesL,List<String> moviesTypesL,
                           List<String> moviesGenresL,List<String> moviesIdsL) {

        String[] moviesImages = new String[]{};
        String[] moviesTitles = new String[]{};
        String[] moviesYears = new String[]{};
        String[] moviesGenres = new String[]{};
        String[] moviesIds = new String[]{};

        try {
            moviesImages = moviesImagesL.toArray(new String[moviesImagesL.size()]);
            moviesTitles = moviesTitlesL.toArray(new String[moviesTitlesL.size()]);
            moviesYears = moviesTypesL.toArray(new String[moviesTypesL.size()]);
            moviesGenres = moviesGenresL.toArray(new String[moviesGenresL.size()]);
            moviesIds = moviesIdsL.toArray(new String[moviesIdsL.size()]);

        }catch (Exception e){}

        MoviesListAdapter movieListAdapter = new MoviesListAdapter(
                moviesImages,moviesTitles,moviesYears,moviesGenres,moviesIds,this);
        movies_list.setAdapter(movieListAdapter);
    }

    private void click_movie(View view, int position) {

        Intent intent = new Intent(getBaseContext(), MovieInfo.class);
        Bundle b = new Bundle();
        TextView movie_id = view.findViewById(R.id.movie_id);
        String id = (String) movie_id.getText();
        b.putString("id", id);
        intent.putExtras(b);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    private void show_movie(String movie_id) {

        Intent intent = new Intent(getBaseContext(), MovieInfo.class);
        Bundle b = new Bundle();
        b.putString("id", movie_id);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void Show_Paid_Tickets(MenuItem item)
    {
        Intent intent = new Intent(this, PaidTickets.class);
        startActivity(intent);
    }

    public void Display_Movies(MenuItem item) {
        String type = item.getTitle().toString();

        switch (type)
        {
            case "2D": Show_Movies_Type("2d");
                break;
            case "3D": Show_Movies_Type("3d");
                break;
            case "4DX": Show_Movies_Type("4dx");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if (requestCode == MY_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            String s = intent.getStringExtra("done");

            if(s.equals("ok"))
            {
                String ticket_id = intent.getStringExtra("ticket_id");
                Intent intnt = new Intent(this, PaidTickets.class);
                Bundle b = new Bundle();
                b.putString("ticket_id", ticket_id);
                intnt.putExtras(b);
                startActivity(intnt);
            }
        }
    }

}
