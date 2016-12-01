package com.anandkumar.jsonlearn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Anand on 12/1/2016.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    private List<Song> songsList;

    public SongsAdapter(List<Song> songsList) {
        this.songsList = songsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView songName;

        public MyViewHolder(View view) {
            super(view);
            songName = (TextView) view.findViewById(R.id.songNameTV);
           }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songname_recycleview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Song song = songsList.get(position);
        holder.songName.setText(song.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),song.getLyric(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }


}

