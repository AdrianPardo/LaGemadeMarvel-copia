package com.example.adrian.lagemademarvel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GetPromotion extends DialogFragment implements AdapterView.OnItemSelectedListener{


    /**
     * Created by i7-4770 on 01/03/2018.
     */

    String id;
    private TextView title, character, desc;
    ImageView cover, fav;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    boolean printed = false;


    public static ViewComic newInstance(String param1, String param2) {
        ViewComic fragment = new ViewComic();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        id = getArguments().getString("ID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_comic, null);
        title = view.findViewById(R.id.comic_name);
        character = view.findViewById(R.id.NombrePersonajeComic);
        desc = view.findViewById(R.id.descripcioncomic);
        cover = view.findViewById(R.id.imagencomic);
        fav = view.findViewById(R.id.fav_button);
        Log.e("Type", ""+getArguments().getInt("Type"));
        if(getArguments().get("Type") == null) {
            title.setText(getArguments().getString("Title"));
            character.setText(getArguments().getString("MainChar"));
            desc.setText(getArguments().getString("Desc"));
            Picasso.with(getActivity()).load(getArguments().getString("Image")).into(cover);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fav.setImageResource(R.drawable.star_on);
                    String mail = user.getEmail().replace(".", " ");
                    Log.e("Email", "" + mail);
                    myRef = FirebaseDatabase.getInstance().getReference("usuarios/" + mail + "/Favoritos/Comics");

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            GenericTypeIndicator<List<Object>> t = new GenericTypeIndicator<List<Object>>() {
                            };
                            List messages = snapshot.getValue(t);

                            if (!printed) {
                                if (messages == null) {
                                    myRef.child("0").child("ID").setValue(getArguments().getString("ID"));
                                    myRef.child("0").child("Name").setValue(getArguments().getString("Title"));
                                } else {
                                    myRef.child("" + messages.size()).child("ID").setValue(getArguments().getString("ID"));
                                    myRef.child("" + messages.size()).child("Name").setValue(getArguments().getString("Title"));
                                }

                                printed = true;
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                }
            });
        }else if(getArguments().getInt("Type") == 0){
            fav.setImageResource(R.drawable.star_on);
            new FavFetcher(title, desc, cover, id, getActivity(), 0).execute();
            character.setText("Data provided by Marvel. © 2018 MARVEL");
        }else if(getArguments().getInt("Type") == 1){
            fav.setImageResource(R.drawable.star_on);
            new FavFetcher(title, desc, cover, id, getActivity(), 1).execute();
            character.setText("Data provided by Marvel. © 2018 MARVEL");
        }

        builder.setView(view);
        final AlertDialog alert = builder.create();


        // Create the AlertDialog object and return it
        return alert;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String mSpinnerLabel = adapterView.getItemAtPosition(i).toString();
        Log.e("Spinner Laber", mSpinnerLabel);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Log.e(TAG, "onNothingSelected: ");
    }
}
