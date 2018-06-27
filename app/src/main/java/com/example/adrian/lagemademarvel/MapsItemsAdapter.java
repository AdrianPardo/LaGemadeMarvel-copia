package com.example.adrian.lagemademarvel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class MapsItemsAdapter extends
        RecyclerView.Adapter<MapsItemsAdapter.WordViewHolder>  {
    private final List<MapsItems> mapsList;
    private LayoutInflater mInflater;
    private SharedPreferences prefs;
    private Context context;

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mapsItemView;
        final MapsItemsAdapter nAdapter;

        WordViewHolder(View itemView, MapsItemsAdapter adapter) {
            super(itemView);
            mapsItemView = itemView.findViewById(R.id.MapsName);
            this.nAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            MapsItems element = mapsList.get(mPosition);
            Bundle bundle = new Bundle();
            bundle.putString("Nombre",element.getMapsName());
            bundle.putString("Direccion",element.getMapsdirec());
            bundle.putString("Email",element.getMapsEmail());
            bundle.putString("Telefono",element.getMapsTelefono());
            MapsItemFragment nif = new MapsItemFragment();
            nif.setArguments(bundle);

            android.app.FragmentManager fm = ((Activity) context).getFragmentManager();
            /*FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, nif);
            ft.addToBackStack("MapsItem");
            ft.commit();*/
            fm.beginTransaction().replace(R.id.content_frame, nif).addToBackStack("MapsItem").commit();
            //test=element.getDayInfo()+"Clicked!";
            //element.setDayInfo(test);
            //cAdapter.notifyDataSetChanged();
            Log.e("Adapter onClick", "test");
        }
    }
    @Override
    public MapsItemsAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        prefs = parent.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        View mItemView = mInflater.inflate(R.layout.maps_items, parent, false);
        TextView mapsName = mItemView.findViewById(R.id.MapsName);
        mapsName.setTextColor(getTextColor());
        context=parent.getContext();
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(MapsItemsAdapter.WordViewHolder holder, int position) {
        final MapsItems mapsItems = mapsList.get(position);
        holder.mapsItemView.setText(mapsItems.getMapsName());
        Log.e("Maps Name",mapsItems.getMapsName());
    }

    @Override
    public int getItemCount() {
        return mapsList.size();
    }

    MapsItemsAdapter(Context context, List<MapsItems> mapsList) {
        mInflater = LayoutInflater.from(context);
        Collections.reverse(this.mapsList = mapsList);
    }

    public int getTextColor(){
        return prefs.getInt("TColor", 0xFFFFFFFF);
    }
}