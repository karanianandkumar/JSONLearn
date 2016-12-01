package com.anandkumar.jsonlearn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anand on 12/1/2016.
 */

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.MyViewHolder> {

    private final AlphabetOnClick alphabetOnClick;

    private List<String> alphabets;

    public interface AlphabetOnClick{
        void onclick(String s);
    }

    public AlphabetAdapter(List<String> alphabets,AlphabetOnClick alphabetOnClick) {
        this.alphabets = alphabets;
        this.alphabetOnClick=alphabetOnClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView alphabetTV;

        public MyViewHolder(View view) {
            super(view);
            alphabetTV = (TextView) view.findViewById(R.id.alphabetTV);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alphabets_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final String alphabet = alphabets.get(position);

        holder.alphabetTV.setText(alphabet);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   alphabetOnClick.onclick(alphabet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alphabets.size();
    }


}


