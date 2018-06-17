package com.example.adrian.lagemademarvel;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<com.example.adrian.lagemademarvel.ComicAdapter.myViewHolder> {

    List<String> comicList;
    List<String> charList;
    List<String> imgList;
    List<String> descList;
    List<String> idList;
    Context context;


    public ComicAdapter(List<String> comics, List<String> chars, List<String> imgs, List<String> descs, Context ctx, List<String> ids) {
        this.comicList = comics;
        this.charList = chars;
        this.imgList = imgs;
        this.descList = descs;
        this.idList = ids;
        context = ctx;
        Log.e("test", "inside adapter");
    }

    @Override
    public com.example.adrian.lagemademarvel.ComicAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("test", "inside on create view holder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.char_comic, parent, false);
        com.example.adrian.lagemademarvel.ComicAdapter.myViewHolder holder = new com.example.adrian.lagemademarvel.ComicAdapter.myViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(com.example.adrian.lagemademarvel.ComicAdapter.myViewHolder holder, int position) {
        int itemNumber = position + 1;
        Log.e("viewholder", ""+comicList.get(position));
        holder.itemTextView.setText(comicList.get(position));
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView itemTextView;


        public myViewHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.comic_title);
            layout = view.findViewById(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mPosition = getLayoutPosition();
                    Bundle bundle = new Bundle();
                    bundle.putString("Title",comicList.get(mPosition));
                    bundle.putString("MainChar",charList.get(mPosition));
                    bundle.putString("Image",imgList.get(mPosition));
                    bundle.putString("Desc",descList.get(mPosition));
                    bundle.putString("ID",idList.get(mPosition));
                    DialogFragment vc = new ViewComic();
                    vc.setArguments(bundle);

                    android.app.FragmentManager fm = ((Activity) context).getFragmentManager();
                    vc.show(fm, "Boom");
                }
            });
        }
    }
}
