package com.example.adrian.lagemademarvel;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CharComicAdapter extends RecyclerView.Adapter<CharComicAdapter.myViewHolder> {

    List<String> comicList;


    public CharComicAdapter(List<String> passedListItem) {
        this.comicList = passedListItem;
        Log.e("test", "inside adapter");
    }

    @Override
    public CharComicAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("test", "inside on create view holder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.char_comic, parent, false);
        CharComicAdapter.myViewHolder holder = new CharComicAdapter.myViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(CharComicAdapter.myViewHolder holder, int position) {
        int itemNumber = position + 1;
        Log.e("viewholder", ""+comicList.get(position));
        holder.itemTextView.setText(comicList.get(position));
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;


        public myViewHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.comic_title);
        }
    }
}
