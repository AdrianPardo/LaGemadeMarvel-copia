package com.example.adrian.lagemademarvel;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends android.app.Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FrameLayout mScreen;
    boolean isConnected, dataSet;
    Button promocionarse;
    FirebaseUser user;
    FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalenderFragment.
     */
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
    public int getBackgroundColor(){
        Context context = getActivity();
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return prefs.getInt("BGColor", 0xffffffff);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IsConnected();
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        if(isConnected) {
            promocionarse = view.findViewById(R.id.promocionarse);
            promocionarse.setVisibility(View.GONE);
            promocionarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment newFragment = new GetPromotion();
                    newFragment.show(getFragmentManager(),"missiles");
                }
            });
            IsComercial(promocionarse);
            // Get a handle to the RecyclerView.
            RecyclerView mRecyclerView = view.findViewById(R.id.recyclermaps);
            // Create an adapter and supply the data to be displayed.
            // Connect the adapter with the RecyclerView.
            // Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            mScreen = view.findViewById(R.id.myScreen);
            //mScreen.setBackgroundColor(getBackgroundColor());
            new MapsFetcher(mRecyclerView, getActivity()).execute();
        }else{
            Toast.makeText(getActivity(), "No hay conexion, comprueba tu conexion a internet!", Toast.LENGTH_LONG).show();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NoConnectionFragment()).commit();
        }
        return view;
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void IsConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public void IsComercial(final Button btn){
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("usuarios");
        final String mail = user.getEmail().replace("."," ");

        DatabaseReference profileRef = myRef.child(mail).child("Comercial").child("UsoComercial");
        dataSet = false;
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean data = dataSnapshot.getValue(Boolean.class);
                if(data == true) {
                    btn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
