package com.example.emovieapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaidTickets extends AppCompatActivity {

    private ListView movies_list;
    private String ticket_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_tickets);
        Toolbar toolbar = findViewById(R.id.paidTicketsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Paid Tickets");

        movies_list = findViewById(R.id.listView);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ticket_id = b.getString("ticket_id");
            open_ticket_info(ticket_id);
        }
    }

    private void open_ticket_info(String ticket_id) {
        Intent intent = new Intent(getBaseContext(), PaidMovieInfo.class);
        Bundle b = new Bundle();
        b.putString("ticket_id", ticket_id);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<String> image_list = new ArrayList<>();
        List<String> type_list = new ArrayList<>();
        List<String> title_list = new ArrayList<>();
        List<String> chairs_List = new ArrayList<>();
        List<String> ticketIP_List = new ArrayList<>();

        try {
            TicketsSQLite ticketsSQLite = new TicketsSQLite(getApplicationContext());
            ArrayList<HashMap<String, String>> data = ticketsSQLite.SelectTickets();

            for (HashMap<String,String> d: data
            ) {
                String image = d.get("ticket_image");
                String type = d.get("ticket_type");
                String title = d.get("ticket_title");
                String chairs = d.get("ticket_chairs");
                String ticket_id = d.get("ID");

                image_list.add(image);
                type_list.add(type);
                title_list.add(title);
                chairs_List.add(chairs);
                ticketIP_List.add(ticket_id);
            }
        }catch (Exception e){}

        Load_List(image_list,title_list,type_list,chairs_List,ticketIP_List);

    }

    private void Load_List(List<String> moviesImagesL, List<String> moviesTitlesL, List<String> moviesTypesL,
                           List<String> moviesChairsL, List<String> ticketsIdsL) {

        String[] moviesImages = new String[]{};
        String[] moviesTitles = new String[]{};
        String[] moviesTypes = new String[]{};
        String[] moviesChairs = new String[]{};
        String[] ticketsIds = new String[]{};

        try {
            moviesImages = moviesImagesL.toArray(new String[moviesImagesL.size()]);
            moviesTitles = moviesTitlesL.toArray(new String[moviesTitlesL.size()]);
            moviesTypes = moviesTypesL.toArray(new String[moviesTypesL.size()]);
            moviesChairs = moviesChairsL.toArray(new String[moviesChairsL.size()]);
            ticketsIds = ticketsIdsL.toArray(new String[ticketsIdsL.size()]);

        }catch (Exception e){}
        //MoviesListAdapter
        paidMoviesListAdapter paidMovieListAdapter = new paidMoviesListAdapter(
                moviesImages,moviesTitles,moviesTypes,moviesChairs,ticketsIds,this);
        movies_list.setAdapter(paidMovieListAdapter);
    }

    public void Cancel_Ticket(final View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To CANCEL Ticket?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String code = (String)view.getTag();
                            DataValues.SetContext(getApplicationContext());
                            new CancelTicket().execute(code);
                        }catch ( Exception e){
                            Toast.makeText(getApplicationContext(),"Problem While Canceling",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // some code if you want
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Show_Code(View view)
    {
        Button movie_id_btn = (Button)view;
        String movie_id = movie_id_btn.getTag().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Code Is " + movie_id)
                .setCancelable(false)
                .setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Open_Ticket(View view)
    {
        Intent intent = new Intent(getBaseContext(), PaidMovieInfo.class);
        Bundle b = new Bundle();
        String ticket_id = (String) view.getTag();
        b.putString("ticket_id", ticket_id);
        intent.putExtras(b);
        startActivity(intent);
    }
}
