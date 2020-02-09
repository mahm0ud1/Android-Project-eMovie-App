package com.example.emovieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerDetails extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 40;

    //private ArrayList<String> chairs_list;
    //private String shows_id;
    //private String movieImage;
    //private String movieTitle;
    //private String movieType;
    //private String movieInfo;

    Bundle bData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Details");

        bData = getIntent().getExtras();

        if (bData != null) {
            //chairs_list = bData.getStringArrayList("chairs_list");
            //shows_id = bData.getString("shows_id");
            //movieImage = bData.getString("movie_img");
            //movieTitle = bData.getString("movie_title");
            //movieType = bData.getString("shows_type");
            //movieInfo = bData.getString("movie_Info");
        }
    }
    /**
     * show the toolbar in the activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_details,menu);
        return true;
    }

    public void next_onClick(View view) {
        TextView first_name_tv = findViewById(R.id.customer_First_Name);
        TextView last_name_tv = findViewById(R.id.customer_Last_Name);
        TextView phone_tv = findViewById(R.id.customer_phone_Number);
        TextView email_tv = findViewById(R.id.customer_eMail);

        String first_name = first_name_tv.getText().toString();
        String last_name = last_name_tv.getText().toString();
        String phone = phone_tv.getText().toString();
        String email = email_tv.getText().toString();

        if(first_name.trim().equals("")||last_name.trim().equals("")||phone.trim().equals("")||email.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill in all details",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(getBaseContext(), CreditCardDetails.class);
            //Bundle b = new Bundle();
            String customer_info = first_name+";"+last_name+";"+phone+";"+email;
            //b.putString("shows_id", shows_id);
            ///b.putStringArrayList("chairs_list", chairs_list);
            bData.putString("customer_info", customer_info);
            //b.putString("movie_type", movieType);
            //b.putString("movie_title", movieTitle);
            //b.putString("movie_img", movieImage);
            //b.putString("movie_Info", movieInfo);
            intent.putExtras(bData);
            startActivityForResult(intent, MY_REQUEST_CODE);
        }

    }

    public void back_onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To Go Back?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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
