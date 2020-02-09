package com.example.emovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class HallLayout4d extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 42;
    private RequestQueue mQueue;

    private TextView tv_chairs_count;
    private TextView tv_total_price;

    private String shows_id;
    private int chair_price;
    private int chairs_count;
    private int total_price;

    private List<String> res_chairs = new ArrayList<>();

    Bundle bData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_layout4d);

        Toolbar toolbar = findViewById(R.id.hall4D_toolbar);

        TextView tv_chair_price = findViewById(R.id.chair_price);
        tv_chairs_count = findViewById(R.id.chair_count);
        tv_total_price = findViewById(R.id.total_price);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choosing Chairs");

        bData = getIntent().getExtras();
        if (bData != null) {
            shows_id = bData.getString("shows_id");
            chair_price = bData.getInt("chair_price");
            tv_chair_price.setText(String.valueOf(chair_price));
            //movieImage = bData.getString("movie_img");
            //movieTitle = bData.getString("movie_title");
            //movieType = bData.getString("shows_type");
            //movieInfo = bData.getString("movie_Info");
        }

        mQueue = Volley.newRequestQueue(this);

        try {
            jsonParse( DataValues.server_site + "?do=show_res_chairs&shows_id=" + shows_id);

        } catch (Exception e) {

        }
    }

    private void jsonParse(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie_detials_json = response.getJSONArray("res_chairs");
                            //
                            for (int i = 0; i < movie_detials_json.length(); i++) {
                                JSONObject movie_type = movie_detials_json.getJSONObject(i);

                                String res_chair_id = movie_type.getString("chair_id");
                                int btnID = getResources().getIdentifier("_"+res_chair_id, "id", getPackageName());
                                Button btn = findViewById(btnID);
                                if(btn!=null)
                                {
                                    btn.setBackgroundColor(Color.GRAY);
                                }

                            }

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
        mQueue.add(request);
    }

    public void click(View view) {
        Button btn = (Button)view;

        ColorDrawable buttonColor = (ColorDrawable) btn.getBackground();
        int colorId = buttonColor.getColor();

        String[] id_s = view.getResources().getResourceName(view.getId()).split("/_");

        switch (colorId)
        {
            case Color.RED:
            {
                btn.setBackgroundColor(-13447886);
                res_chairs.remove(id_s[1]);
                chairs_count--;
            }
            break;
            case -13447886:
            {
                btn.setBackgroundColor(Color.RED);
                res_chairs.add(id_s[1]);
                chairs_count++;
            }
            break;
        }

        tv_chairs_count.setText(String.valueOf(chairs_count));
        total_price = chair_price * chairs_count;
        tv_total_price.setText(String.valueOf(total_price));
    }

    public void Next_onClick(View view) {
        if(res_chairs.size() == 0)
        {
            Toast.makeText(getApplicationContext(),"please select a chair",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getBaseContext(), CustomerDetails.class);
        //Bundle b = new Bundle();
        //b.putString("shows_id", shows_id);
        //b.putString("movie_type", movieType);
        //b.putString("movie_title", movieTitle);
        //b.putString("movie_img", movieImage);
        //b.putString("movie_Info", movieInfo);
        bData.putStringArrayList("chairs_list", (ArrayList<String>) res_chairs);
        bData.putString("total_price",tv_total_price.getText().toString());
        intent.putExtras(bData);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    public void Back_onClick(View view) {
        finish();
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
