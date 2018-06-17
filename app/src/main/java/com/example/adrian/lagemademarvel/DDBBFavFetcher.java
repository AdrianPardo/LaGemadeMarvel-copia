package com.example.adrian.lagemademarvel;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DDBBFavFetcher extends AsyncTask<Void, Void, String> {

    private Exception exception;
    private TextView name, desc, copyRight;
    private String id;
    private Context context;
    private ImageView charImg;
    private ArrayList<String> chars = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private RecyclerView comicRV;
    private int type;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    String idToken;
    boolean wait;


    DDBBFavFetcher( Context ctx, RecyclerView rv, int tp){
        context = ctx;
        comicRV = rv;
        type = tp;

    }

    protected String doInBackground(Void... urls) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        try {
            String mail = user.getEmail().replace(".", "%20");
            URL url;
            wait = true;
            user.getIdToken(false)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                idToken = task.getResult().getToken();
                                Log.e("get token", "success");
                                Log.e("get token", ""+task.getResult().getToken());
                                Log.e("id token", ""+idToken);
                                wait = false;
                            } else {
                                // Handle error -> task.getException();
                                Log.e("get token", "fail");
                                wait = false;
                            }
                        }
                    });
            while(wait == true){ }
            if(type == 0) {
                url = new URL("https://proyecto-marvel.firebaseio.com/usuarios/"+mail+"/Favoritos/Personajes.json?auth="+idToken);
            }else{
                url = new URL("https://proyecto-marvel.firebaseio.com/usuarios/"+mail+"/Favoritos/Comics.json?auth="+idToken);
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
            JSONArray array = new JSONArray(result);
            test=array.length();
            Log.e("Array Length", "News fetcher: "+test);

            for (int x = 0; x < test ; x++) {
                JSONObject jo = array.getJSONObject(x);

                chars.add(jo.getString("Name"));
                ids.add(jo.getString("ID"));
            }

            comicRV.setLayoutManager(new LinearLayoutManager(context));
            FavAdapter fAdapter = new FavAdapter(chars, ids, context, type);
            comicRV.setAdapter(fAdapter);


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
