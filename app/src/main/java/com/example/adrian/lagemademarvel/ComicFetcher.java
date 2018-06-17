package com.example.adrian.lagemademarvel;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ComicFetcher extends AsyncTask<Void, Void, String> {

    private Exception exception;
    private TextView name, desc, copyRight;
    private String comic;
    private Context context;
    private ImageView charImg;
    private ArrayList<String> comics = new ArrayList<>();
    private ArrayList<String> chars = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> descs = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private RecyclerView comicRV;


    ComicFetcher(String comicStr, Context ctx,  RecyclerView rv){
        context = ctx;
        comicRV = rv;
        comic = comicStr;

    }

    protected String doInBackground(Void... urls) {

        // Do some validation here
        try{
            comic = comic.replace(" ", "%20");
        }catch (Exception e){

        }

        try {
            URL url;
            if(isNumeric(comic)) {
                url = new URL("https://gateway.marvel.com/v1/public/comics?issueNumber=" + comic + "&ts=1&apikey=d5fd434a699b0842a4f042e61edb749d&hash=71c50fe570ccfc894f2641b42166d4d2");
            }else{
                url = new URL("https://gateway.marvel.com/v1/public/comics?title=" + comic + "&ts=1&apikey=d5fd434a699b0842a4f042e61edb749d&hash=71c50fe570ccfc894f2641b42166d4d2");
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result == null) {
            result = "THERE WAS AN ERROR";
        }
        Log.e("INFO", result);
        int test, test2;
        try {
            JSONObject jsonO = new JSONObject(result);
            JSONArray array = jsonO.getJSONObject("data").getJSONArray("results");
            test=array.length();
            Log.e("Array Length", "News fetcher: "+test);

            for (int x = 0; x < test ; x++) {
                JSONObject jo = array.getJSONObject(x);
                comics.add(jo.getString("title"));
                descs.add(jo.getString("description"));
                imgs.add(jo.getJSONObject("thumbnail").getString("path")+"."+jo.getJSONObject("thumbnail").getString("extension"));
                Log.e("char array size", ""+jo.getJSONObject("characters").getJSONArray("items").length());
                if(jo.getJSONObject("characters").getJSONArray("items").length()==0){
                    chars.add("Hackeado por Hydra.");
                }else {
                    chars.add(jo.getJSONObject("characters").getJSONArray("items").getJSONObject(0).getString("name"));
                }
                ids.add(jo.getString("id"));


            }
            Log.e("test", "before adapter");
            comicRV.setLayoutManager(new LinearLayoutManager(context));
            ComicAdapter ccAdapter = new ComicAdapter(comics, chars, imgs, descs, context, ids);
            comicRV.setAdapter(ccAdapter);
            Log.e("test", "after adapter");

        } catch (JSONException je){
            je.printStackTrace();
            Toast toast = Toast.makeText(context, "No hay mas informacion", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
