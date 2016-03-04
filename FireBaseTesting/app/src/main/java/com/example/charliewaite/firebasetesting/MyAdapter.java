package com.example.charliewaite.firebasetesting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by charl on 09/02/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Team> mDataset = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MyAdapter(ArrayList<Team> myDataSet) {
        mDataset = myDataSet;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        TextView teamName = (TextView) holder.view.findViewById(R.id.textView);
        TextView stadium = (TextView) holder.view.findViewById(R.id.textView2);

        teamName.setText(mDataset.get(position).getTeamName());
        stadium.setText(mDataset.get(position).getStadium());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(ArrayList<Team> data) {
        mDataset = data;
        notifyDataSetChanged();
    }
}
