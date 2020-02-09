package com.example.emovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class PaidMovieInfo extends AppCompatActivity {

    private String ticket_id;

    private LayoutInflater inflter;
    private Context context;
    private ImageView movieImg;
    private TextView titleMovie, timePurchase, typesMovie, infoMovie, genresMovie, chairs_list;
    private Button   codeTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_movie_info);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticket_id = b.getString("ticket_id");
            Load_Ticket(ticket_id);
        }
    }

    private void Load_Ticket(String ticket_id) {

        try {
            TicketsSQLite ticketsSQLite = new TicketsSQLite(getApplicationContext());
            HashMap<String, String> data = ticketsSQLite.SelectTickets(ticket_id);
            String image = data.get("movie_img");
            String type = data.get("ticket_type");
            String title = data.get("ticket_title");
            String chairs = data.get("ticket_chairs");
            String code = data.get("ticket_id");
            String info = data.get("movie_info");
            String gne = data.get("movie_gne");
            String purchase_time = data.get("purchase_time");

            titleMovie = findViewById(R.id.title_tv);
            typesMovie = findViewById(R.id.type_tv);
            timePurchase = findViewById(R.id.time_tv);
            infoMovie = findViewById(R.id.movie_info_tv);
            codeTicket = findViewById(R.id.movie_code);
            chairs_list = findViewById(R.id.chairs_list_tv);

            new DownloadMovieImage((ImageView) findViewById(R.id.movieImg))
                    .execute(image);
            titleMovie.setText(title);
            typesMovie.setText(type);
            //genresMovie.setText("A");
            timePurchase.setText(purchase_time);
            chairs_list.setText(chairs);
            infoMovie.setText(info);
            codeTicket.setText("Code is : "+code);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
            finish();
        }



    }
}
