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
import java.util.List;

public class CharacterConnection extends AsyncTask<Void, Void, String> {

    private Exception exception;
    private TextView name, desc, copyRight;
    private String character;
    private Context context;
    private ImageView charImg;
    private ArrayList<String> comics = new ArrayList<>();
    private RecyclerView comicRV;


    CharacterConnection(TextView nameTV, TextView descTV, TextView cpr, String charStr, Context ctx, ImageView img, RecyclerView rv){
        name=nameTV;
        desc=descTV;
        character=charStr;
        copyRight = cpr;
        context = ctx;
        charImg = img;
        comicRV = rv;

    }

    protected String doInBackground(Void... urls) {

        // Do some validation here
        try{
            character = character.replace(" ", "%20");
        }catch (Exception e){

        }

        try {
            URL url = new URL("https://gateway.marvel.com/v1/public/characters?name=" + character + "&ts=1&apikey=d5fd434a699b0842a4f042e61edb749d&hash=71c50fe570ccfc894f2641b42166d4d2");
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
            copyRight.setText(jsonO.getString("attributionText"));

            JSONArray array = jsonO.getJSONObject("data").getJSONArray("results");
            test=array.length();
            Log.e("Array Length", "News fetcher: "+test);

            for (int x = 0; x < test ; x++) {
                JSONObject jo = array.getJSONObject(x);
                name.setText(jo.getString("name"));
                desc.setText(jo.getString("description"));
                Log.e("name", ""+jo.getString("name"));
                Log.e("desc", ""+jo.getString("description"));
                Picasso.with(context).load(jo.getJSONObject("thumbnail").getString("path")+"."+jo.getJSONObject("thumbnail").getString("extension")).into(charImg);

                JSONArray array2 = jo.getJSONObject("comics").getJSONArray("items");
                test2 = array2.length();
                for (int y = 0; y < test2 ; y++) {
                    JSONObject jo2 = array2.getJSONObject(y);
                    comics.add(jo2.getString("name"));
                    Log.e("comic", ""+jo2.getString("name"));
                }
            }
            Log.e("test", "before adapter");
            comicRV.setLayoutManager(new LinearLayoutManager(context));
            CharComicAdapter ccAdapter = new CharComicAdapter(comics);
            comicRV.setAdapter(ccAdapter);
            Log.e("test", "after adapter");

        } catch (JSONException je){
            je.printStackTrace();
            Toast toast = Toast.makeText(context, "No hay mas informacion", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
