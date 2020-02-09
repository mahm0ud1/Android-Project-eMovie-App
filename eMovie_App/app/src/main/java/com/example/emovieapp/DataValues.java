package com.example.emovieapp;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public final class DataValues {

    public static HashMap<String, String[]> v;
    private static Context context;

    public final static String server_site = "http://app.nano-soft.club/";//185.219.127.43

    public static void Collect()
    {
        v  = new HashMap<>();
        // Type => [0]=Eng , [1]=Ar, [2]=He
        v.put("ERROR",new String[]{"A","B","C"});
    }

    public static String Get(String type)
    {
        try {
            String[] values = v.get(type);
            return values[get_language_index()];
        }catch (Exception e){}

        return "";
    }

    private static int get_language_index()
    {
        switch (Locale.getDefault().getDisplayLanguage()) {
            case "العربية": return 1;
            case "עברית": return 2;
        }

        return 0;
    }

    public static void SetContext(Context c)
    {
        context = c;
    }
}
