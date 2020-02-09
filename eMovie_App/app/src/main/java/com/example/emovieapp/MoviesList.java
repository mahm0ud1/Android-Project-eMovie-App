package com.example.emovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;

public class MoviesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = findViewById(R.id.moviesList_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logoapp);
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
}
