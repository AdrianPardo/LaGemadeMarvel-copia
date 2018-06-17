package com.example.adrian.lagemademarvel;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CharacterViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterViewFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView searchBTN, charImg, fav;
    EditText searchBar;
    TextView charName, copyright, charDesc, id;
    RecyclerView rv;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    boolean printed = false;

    private OnFragmentInteractionListener mListener;

    public CharacterViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CharacterViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterViewFragment newInstance(String param1, String param2) {
        CharacterViewFragment fragment = new CharacterViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_character_view, container, false);

        searchBar = view.findViewById(R.id.Search_char);
        charName = view.findViewById(R.id.NombrePersonaje);
        copyright = view.findViewById(R.id.alterego);
        charDesc = view.findViewById(R.id.descripcionpersonaje);
        charImg= view.findViewById(R.id.imagenpersonaje);
        rv = view.findViewById(R.id.character_comics);
        fav = view.findViewById(R.id.fav_button);
        id = view.findViewById(R.id.charID);
        id.setVisibility(View.GONE);

        searchBTN = view.findViewById(R.id.Search_char_btn);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CharacterConnection(charName, charDesc, copyright, searchBar.getText().toString(), getActivity(), charImg, rv, id).execute();
                Log.e("in onclick", "Buscando....");
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.setImageResource(R.drawable.star_on);
                String mail = user.getEmail().replace("."," ");
                Log.e("Email", ""+mail);
                myRef = FirebaseDatabase.getInstance().getReference("usuarios/"+mail+"/Favoritos/Personajes");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        GenericTypeIndicator<List<Object>> t = new GenericTypeIndicator<List<Object>>() {
                        };
                        List messages = snapshot.getValue(t);

                        if (!printed) {
                            if(messages == null){
                                myRef.child("0").child("ID").setValue(id.getText());
                                myRef.child("0").child("Name").setValue(charName.getText());
                            }else {
                                myRef.child("" + messages.size()).child("ID").setValue(id.getText());
                                myRef.child("" + messages.size()).child("Name").setValue(charName.getText());
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




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
