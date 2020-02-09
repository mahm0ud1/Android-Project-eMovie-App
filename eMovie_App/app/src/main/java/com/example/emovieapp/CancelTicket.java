package com.example.emovieapp;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CancelTicket extends AsyncTask<String, Integer, String> {

    private ArrayList<JsonObjectRequest> mQueue;
    private Activity activity;

    protected String doInBackground(String... tickets_code) {
        int count = tickets_code.length;

        for (int i = 0; i < count; i++) {
            try {//https://api.myjson.com/bins/uewjy
                jsonParse("https://api.myjson.com/bins/uewjy",tickets_code[i]);
            }catch (Exception e)
            {
            }

            if (isCancelled()) break;
        }
        return "";
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String r) {
    }

    private void jsonParse(String url, final String code) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie_details_json = response.getJSONArray("Respond");
                            //
                            JSONObject movie_type = movie_details_json.getJSONObject(0);

                            String message = movie_type.getString("message");

                            if(message.equals("ok"))
                            {
                                ShowToast("Canceling sent for Ticket Code " + code);
                            }
                            else
                            {
                                ShowToast("not set Ticket Code " + code);
                            }

                        } catch (Exception e) {
                            ShowToast("not set Ticket Code " + code);
                            Log.i("ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ERROR", error.getMessage());
            }
        });

        mQueue.add(request);
    }

    private void ShowToast(final String msg)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
