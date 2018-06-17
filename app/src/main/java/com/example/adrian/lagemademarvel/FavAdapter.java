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

public class FavAdapter  extends RecyclerView.Adapter<com.example.adrian.lagemademarvel.FavAdapter.myViewHolder> {

        List<String> nameList;
        List<String> idList;
        int type;
        Context context;


        public FavAdapter(List<String> name, List<String> id, Context ctx, int tp) {
            this.nameList = name;
            this.idList = id;
            context = ctx;
            type = tp;
        }

        @Override
        public com.example.adrian.lagemademarvel.FavAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.char_comic, parent, false);
            com.example.adrian.lagemademarvel.FavAdapter.myViewHolder holder = new com.example.adrian.lagemademarvel.FavAdapter.myViewHolder(itemView);
            return holder;
        }


        @Override
        public void onBindViewHolder(com.example.adrian.lagemademarvel.FavAdapter.myViewHolder holder, int position) {
            int itemNumber = position + 1;
            Log.e("viewholder", ""+ nameList.get(position));
            holder.itemTextView.setText(nameList.get(position));
        }

        @Override
        public int getItemCount() {
            return nameList.size();
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
                        bundle.putString("ID", idList.get(mPosition));
                        bundle.putInt("Type",type);
                        DialogFragment vc = new ViewComic();
                        vc.setArguments(bundle);

                        android.app.FragmentManager fm = ((Activity) context).getFragmentManager();
                        vc.show(fm, "Boom");
                    }
                });
            }
        }
    }
