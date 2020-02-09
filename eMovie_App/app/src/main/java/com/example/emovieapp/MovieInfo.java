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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

public class MovieInfo extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 44;
    private RequestQueue mQueue;
    // page data
    String url;
    String movie_id;
    String movieImage;
    String movieTitle;
    String movieInfo;
    List<String> movie_2d = new ArrayList<>();
    List<String> movie_3d = new ArrayList<>();
    List<String> movie_4dx = new ArrayList<>();

    private int price_2d;
    private int price_3d;
    private int price_4dx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Toolbar toolbar = findViewById(R.id.moviesInfo_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movie Details");


        Bundle b = getIntent().getExtras();
        if (b != null)
            movie_id = b.getString("id");

        mQueue = Volley.newRequestQueue(this);

        try {
            jsonParse(DataValues.server_site + "?do=show_movie_info&movie_id=" + movie_id);

        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_info,menu);
        return true;
    }

    private void jsonParse(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie_detials_json = response.getJSONArray("movie_details");
                            JSONArray movie_type_json = response.getJSONArray("movie_shows");
                            JSONObject movie_detials = movie_detials_json.getJSONObject(0);

                            movieImage = movie_detials.getString("movie_image");
                            movieTitle = movie_detials.getString("movie_title");
                            movieInfo = movie_detials.getString("movie_info");

                            price_2d = movie_detials.getInt("price_2d");
                            price_3d = movie_detials.getInt("price_3d");
                            price_4dx = movie_detials.getInt("price_4dx");

                            new DownloadMovieImage((ImageView) findViewById(R.id.movieImg))
                                    .execute(movieImage);

                            TextView movie_title = findViewById(R.id.movie_title);
                            TextView movie_info = findViewById(R.id.movie_info);
                            //ImageView movie_image = findViewById(R.id.movieImg);

                            movie_title.setText(movieTitle);
                            movie_info.setText(movieInfo);

                            //movie_image.setImageResource(img);

                            //
                            for (int i = 0; i < movie_type_json.length(); i++) {
                                JSONObject shows = movie_type_json.getJSONObject(i);

                                String shows_id = shows.getString("show_id");
                                String type = shows.getString("movie_type");
                                String value = shows.getString("shows_time");

                                switch (type) {
                                    case "2d":
                                        movie_2d.add(value+";"+shows_id);
                                        break;
                                    case "3d":
                                        movie_3d.add(value+";"+shows_id);
                                        break;
                                    case "4dx":
                                        movie_4dx.add(value+";"+shows_id);
                                        break;
                                }
                            }

                            Load_List();

                        } catch (Exception e) {
                            Log.i("ERROR:1", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR:2", error.getMessage());
            }
        });
        mQueue.add(request);
    }

    private void Load_List() {
        for (String m2d:movie_2d
             ) {
            Add_Button("2d",m2d);
        }
        for (String m2d:movie_3d
        ) {
            Add_Button("3d",m2d);
        }
        for (String m2d:movie_4dx
        ) {
            Add_Button("4dx",m2d);
        }
    }

    private void Add_Button(String type, final String value) {
        final String data[] = value.split(";");
        switch (type) {
            case "2d": {

                LinearLayout _2d_shows = findViewById(R.id._2d_shows);
                Button btn = new Button(this);
                btn.setText(data[0]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Get_Chairs_List("2d",data[1],data[0]);
                    }
                });
                _2d_shows.addView(btn);
            }
            break;
            case "3d": {
                LinearLayout _3d_shows = findViewById(R.id._3d_shows);
                Button btn = new Button(this);
                btn.setText(data[0]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Get_Chairs_List("3d",data[1],data[0]);
                    }
                });
                _3d_shows.addView(btn);
            }
            break;
            case "4dx": {
                LinearLayout _4dx_shows = findViewById(R.id._4dx_shows);
                Button btn = new Button(this);
                btn.setText(data[0]);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Get_Chairs_List("4dx",data[1],data[0]);
                    }
                });
                _4dx_shows.addView(btn);
            }
            break;
        }
    }

    private void Get_Chairs_List(String type,String shows_id,String shows_time)
    {
        switch (type)
        {
            case "2d":
            {
                Intent intent = new Intent(getBaseContext(), HallLayout3d.class);
                Bundle b = new Bundle();
                b.putString("shows_id", shows_id);
                b.putString("movie_img", movieImage);
                b.putString("movie_title", movieTitle);
                b.putString("shows_type", type);
                b.putString("movie_Info", movieInfo);
                b.putString("shows_time",shows_time);
                b.putInt("chair_price",price_2d);
                intent.putExtras(b);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
            break;
            case "3d":
            {
                Intent intent = new Intent(getBaseContext(), HallLayout3d.class);
                Bundle b = new Bundle();
                b.putString("shows_id", shows_id);
                b.putString("movie_img", movieImage);
                b.putString("movie_title", movieTitle);
                b.putString("shows_type", type);
                b.putString("movie_Info", movieInfo);
                b.putString("shows_time",shows_time);
                b.putInt("chair_price",price_3d);
                intent.putExtras(b);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
                break;
            case "4dx":
            {
                Intent intent = new Intent(getBaseContext(), HallLayout4d.class);
                Bundle b = new Bundle();
                b.putString("shows_id", shows_id);
                b.putString("movie_img", movieImage);
                b.putString("movie_title", movieTitle);
                b.putString("shows_type", type);
                b.putString("movie_Info", movieInfo);
                b.putString("shows_time",shows_time);
                b.putInt("chair_price",price_4dx);
                intent.putExtras(b);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
                break;
        }
    }

    public void Back_onClick(View view) {
        finish();
    }

    public void Share_Whatsapp(MenuItem item) {

        String url = DataValues.server_site + "?do=url&movie_id="+movie_id;

        try {
            Intent shareWhatsapp = new Intent();
            shareWhatsapp.setAction(Intent.ACTION_SEND);
            shareWhatsapp.putExtra(Intent.EXTRA_TEXT,url);
            shareWhatsapp.setType("text/plain");
            shareWhatsapp.setPackage("com.whatsapp");
            startActivity(shareWhatsapp);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Whatsapp ERROR",Toast.LENGTH_SHORT).show();
        }

    }

    public void Share(MenuItem item) {

        String url = DataValues.server_site + "?do=url&movie_id="+movie_id;

        try {
            Intent shareWhatsapp = new Intent();
            shareWhatsapp.setAction(Intent.ACTION_SEND);
            shareWhatsapp.putExtra(Intent.EXTRA_TEXT,url);
            shareWhatsapp.setType("text/plain");
            startActivity(shareWhatsapp);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"ERROR while Trying to Share",Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if (requestCode == MY_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            String s = intent.getStringExtra("done");

            if(s.equals("ok"))
            {
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }*/

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
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
