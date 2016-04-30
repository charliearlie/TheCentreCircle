package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.R;

/**
 * Created by charliewaite on 30/04/2016.
 */
public class LineupAdapter extends RecyclerView.Adapter<LineupAdapter.ViewHolder> {
    private JSONArray homeLineup;
    private JSONArray awayLineup;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public LineupAdapter(ArrayList<JSONArray> teams) {
        this.homeLineup = teams.get(0);
        this.awayLineup = teams.get(1);
    }

    @Override
    public LineupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_lineup_item, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.view;
        try {
            TextView homeNumberTextView = (TextView) view.findViewById(R.id.home_player_number);
            TextView homePlayerTextView = (TextView) view.findViewById(R.id.home_player_name);
            homePlayerTextView.setText(homeLineup.getJSONObject(position).getString("name"));
            TextView awayNumberTextView = (TextView) view.findViewById(R.id.away_player_number);
            TextView awayPlayerTextView = (TextView) view.findViewById(R.id.away_player_name);
            awayPlayerTextView.setText(awayLineup.getJSONObject(position).getString("name"));

            if(Integer.valueOf(homeLineup.getJSONObject(position).getString("number")) < 10) {
                String playerNumber = "  " + homeLineup.getJSONObject(position).getString("number");
                homeNumberTextView.setText(playerNumber);
            } else {
                homeNumberTextView.setText(homeLineup.getJSONObject(position).getString("number"));
            }
            if(Integer.valueOf(awayLineup.getJSONObject(position).getString("number")) < 10) {
                String playerNumber = "  " + awayLineup.getJSONObject(position).getString("number");
                awayNumberTextView.setText(playerNumber);
            } else {
                awayNumberTextView.setText(awayLineup.getJSONObject(position).getString("number"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return homeLineup.length();
    }


}
