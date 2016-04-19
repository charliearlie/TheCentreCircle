package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.R;

/**
 * Created by charliewaite on 17/04/2016.
 */
public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {

    private ArrayList<String> stats = new ArrayList<>();

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public StatisticAdapter(ArrayList<String> stats) {
        this.stats = stats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
        //Define the view holder }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.statistic_recycler_pos,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView statNameTextView = (TextView) holder.view.findViewById(R.id.team_statistic_name);
        TextView statValueTextView = (TextView) holder.view.findViewById(R.id.team_statistic_value);

        statNameTextView.setText("Statistic: ");
        statValueTextView.setText(stats.get(position));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }



    public static interface Listener {
        public void onClick(int position);
    }
}
