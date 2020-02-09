package com.example.emovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketsSQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME        = "Paid_Ticket.db";

    public static final String Tickets_Table        = "Paid_tickets_table";

    public static final String cel_ticket_id        = "ticket_id";
    public static final String cel_movie_title      = "ticket_title";
    public static final String cel_image_url        = "movie_img";
    public static final String cel_ticket_chairs    = "ticket_chairs";
    public static final String cel_ticket_type      = "ticket_type";
    public static final String cel_movie_info       = "movie_info";
    public static final String cel_movie_gne        = "movie_gne";
    public static final String cel_purchase_time    = "purchase_time";

    public TicketsSQLite(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+Tickets_Table+" (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                cel_ticket_id + " TEXT, " +
                cel_movie_title + " TEXT,"+
                cel_image_url + " TEXT,"+
                cel_ticket_chairs + " TEXT,"+
                cel_movie_info + " TEXT,"+
                cel_movie_gne + " TEXT,"+
                cel_purchase_time + " TEXT,"+
                cel_ticket_type + " TEXT"+
                ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS "+Tickets_Table);
        onCreate(db);
    }
    public long InsertTicket(String ticket_id,String movie_title,String image_url,
                             String ticket_type,String ticket_chairs,String movie_info
                            ,String movie_gne,String purchase_time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(cel_ticket_id, ticket_id);
        cValues.put(cel_movie_title, movie_title);
        cValues.put(cel_image_url, image_url);
        cValues.put(cel_ticket_type, ticket_type);
        cValues.put(cel_ticket_chairs, ticket_chairs);
        cValues.put(cel_movie_info, movie_info);
        cValues.put(cel_movie_gne, movie_gne);
        cValues.put(cel_purchase_time, purchase_time);
        long newRowId = db.insert(Tickets_Table, null, cValues);
        db.close();

        return newRowId;
    }

    public ArrayList<HashMap<String, String>> SelectTickets()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + Tickets_Table;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                HashMap<String, String> user = new HashMap<>();
                user.put("ID", cursor.getString(cursor.getColumnIndex("ID")));
                user.put("ticket_id", cursor.getString(cursor.getColumnIndex(cel_ticket_id)));
                user.put("ticket_title", cursor.getString(cursor.getColumnIndex(cel_movie_title)));
                user.put("ticket_image",cursor.getString(cursor.getColumnIndex(cel_image_url)));
                user.put("ticket_type",cursor.getString(cursor.getColumnIndex(cel_ticket_type)));
                user.put("ticket_chairs", cursor.getString(cursor.getColumnIndex(cel_ticket_chairs)));
                userList.add(user);
            }
        }catch (Exception e){}

        return  userList;
    }

    public HashMap<String, String> SelectTickets(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT * FROM " + Tickets_Table + " WHERE ID = ?";
            String argv[] = new String[]{id};
            Cursor cursor = db.rawQuery(query, new String[] { id });
            HashMap<String, String> user = new HashMap<>();
            while (cursor.moveToNext()) {
                user.put(cel_ticket_id, cursor.getString(cursor.getColumnIndex(cel_ticket_id)));
                user.put(cel_movie_title, cursor.getString(cursor.getColumnIndex(cel_movie_title)));
                user.put(cel_image_url, cursor.getString(cursor.getColumnIndex(cel_image_url)));
                user.put(cel_ticket_type, cursor.getString(cursor.getColumnIndex(cel_ticket_type)));
                user.put(cel_ticket_chairs, cursor.getString(cursor.getColumnIndex(cel_ticket_chairs)));
                user.put(cel_movie_info, cursor.getString(cursor.getColumnIndex(cel_movie_info)));
                user.put(cel_movie_gne, cursor.getString(cursor.getColumnIndex(cel_movie_gne)));
                user.put(cel_purchase_time, cursor.getString(cursor.getColumnIndex(cel_purchase_time)));

                return user;
            }
        }catch (Exception e){}

        return  null;
    }
}
