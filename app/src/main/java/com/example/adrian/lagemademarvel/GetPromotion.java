package com.example.adrian.lagemademarvel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


    String id;
    private TextView title, character, desc;
    ImageView cover, fav;
    FirebaseUser user;
    FirebaseAuth mAuth;
    EditText nombre, email, direccion, telefono;
    TextView correo;
    DatabaseReference myRef;
    boolean printed = false;
    Button save;


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
        //id = getArguments().getString("ID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.get_promotion, null);


        builder.setView(view);
        final AlertDialog alert = builder.create();

        nombre = view.findViewById(R.id.store_name);
        email = view.findViewById(R.id.store_email);
        telefono = view.findViewById(R.id.store_number);
        direccion = view.findViewById(R.id.store_location);
        save = view.findViewById(R.id.promotion_save_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = user.getEmail().replace(".", " ");
                myRef = FirebaseDatabase.getInstance().getReference("tiendas");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        GenericTypeIndicator<List<Object>> t = new GenericTypeIndicator<List<Object>>() {
                        };
                        List messages = snapshot.getValue(t);

                        if (!printed) {
                            if(messages == null){
                                myRef.child("0").child("Direccion").setValue(direccion.getText().toString());
                                myRef.child("0").child("Email").setValue(email.getText().toString());
                                myRef.child("0").child("Nombre").setValue(nombre.getText().toString());
                                myRef.child("0").child("Telefono").setValue(telefono.getText().toString());

                            }else {
                                myRef.child("").child("Direccion").setValue(direccion.getText().toString());
                                myRef.child("").child("Email").setValue(email.getText().toString());
                                myRef.child("").child("Nombre").setValue(nombre.getText().toString());
                                myRef.child("").child("Telefono").setValue(telefono.getText().toString());
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
