package com.example.emovieapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreditCardDetails extends AppCompatActivity {

    private RequestQueue mQueue;

    private String shows_id;
    private String customer_info;
    private String movieImage;
    private String movieTitle;
    private String movieType;
    private String movieInfo;
    private String movieTime;

    private ArrayList<String> chairs_list;
    private String total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_details);

        Toolbar toolbar = findViewById(R.id.credit_card_toolbar);
        setSupportActionBar(toolbar);

        final TextInputEditText expire_Data = findViewById(R.id.credit_card_exDate);
        expire_Data.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String mmyy = "MMYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int ii, int i1, int i2) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 4; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 4){
                        clean = clean + mmyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int year = Integer.parseInt(clean.substring(2,4));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<20)?20:(year>30)?30:year;
                        cal.set(Calendar.YEAR, year);

                        //day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    expire_Data.setText(current);
                    expire_Data.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

        getSupportActionBar().setTitle("Credit Card Details");

        Bundle b = getIntent().getExtras();

        if (b != null) {
            chairs_list = b.getStringArrayList("chairs_list");
            shows_id = b.getString("shows_id");
            customer_info = b.getString("customer_info");
            movieImage = b.getString("movie_img");
            movieTitle = b.getString("movie_title");
            movieType = b.getString("shows_type");
            movieInfo = b.getString("movie_Info");
            movieTime = b.getString("shows_time");
            total_price = b.getString("total_price");
        }

        mQueue = Volley.newRequestQueue(this);
    }

    public void next_onClick(View view) {
        TextView card_owner_Name_tv = findViewById(R.id.card_owner_Name);
        TextView credit_card_number_tv = findViewById(R.id.credit_card_number);
        TextView credit_card_exDate_tv = findViewById(R.id.credit_card_exDate);
        TextView cvvNumber_tv = findViewById(R.id.cvvNumber);

        String card_owner_Name = card_owner_Name_tv.getText().toString();
        String credit_card_number = credit_card_number_tv.getText().toString();
        String credit_card_exDate = credit_card_exDate_tv.getText().toString();
        String cvvNumber = cvvNumber_tv.getText().toString();

        if(card_owner_Name.trim().equals("")||credit_card_number.trim().equals("")||credit_card_exDate.trim().equals("")||cvvNumber.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"please fill in all details",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
                simpleDateFormat.setLenient(false);
                Date expiry = simpleDateFormat.parse(credit_card_exDate);
                boolean expired = expiry.before(new Date());
                if (expired == true)
                {
                    Toast.makeText(getApplicationContext(),"card has already expired",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String credit_card_info = card_owner_Name+";"+credit_card_number+";"+
                            credit_card_exDate+";"+cvvNumber_tv;

                    Confirm_Paid(credit_card_info);
                }
            }
            catch (Exception e){}

        }
    }

    private void check_reservation(String credit_card_data)
    {
        String chairs_arg = "";
        for(int i=0; i < chairs_list.size(); ++i)
        {
            if(i!=0)
            {
                chairs_arg += ","+chairs_list.get(i);
            }
            else
            {
                chairs_arg += chairs_list.get(i);
            }
        }

        try {
            jsonParse( DataValues.server_site + "?do=make_reservation&shows_id="+ shows_id
                    +"&chair_list="+chairs_arg+"&customer_Info="+customer_info+"&cridet_card_info="+credit_card_data);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
        }
    }

    private void jsonParse(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie_details_json = response.getJSONArray("Respond");
                            //
                            JSONObject movie_type = movie_details_json.getJSONObject(0);
                            String message = movie_type.getString("message");

                            if(message.equals("OK"))
                            {

                                String chairs_arg = "";
                                for(int i=0; i < chairs_list.size(); ++i)
                                {
                                    if(i!=0)
                                    {
                                        chairs_arg += ","+chairs_list.get(i);
                                    }
                                    else
                                    {
                                        chairs_arg += chairs_list.get(i);
                                    }
                                }

                                String ticket_id = movie_type.getString("ticket_id");
                                TicketsSQLite db = new TicketsSQLite(getApplicationContext());
                                long id = db.InsertTicket(ticket_id,movieTitle,movieImage,movieType,
                                        chairs_arg,movieInfo,movieType,movieTime);
                                Intent intent = new Intent();
                                intent.putExtra("done", "ok");
                                intent.putExtra("ticket_id",""+id);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            else if(message.equals("ERROR"))
                            {
                                Vibrate();
                                String reason = movie_type.getString("Reason");

                                Toast.makeText(getApplicationContext(),reason,
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Vibrate();
                            Log.i("ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Vibrate();
                Log.i("ERROR", error.getMessage());
            }
        });
        mQueue.add(request);
    }

    public void Confirm_Paid(final String credit_card_info)
    {
        Vibrate();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want To Pay "+total_price+" ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            check_reservation(credit_card_info);
                        }catch ( Exception e){
                            Toast.makeText(getApplicationContext(),"ERROR",
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

    private void Vibrate()
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
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
}
