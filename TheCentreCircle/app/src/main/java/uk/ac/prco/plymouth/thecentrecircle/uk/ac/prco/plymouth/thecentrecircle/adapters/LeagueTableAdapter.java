package uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import uk.ac.prco.plymouth.thecentrecircle.R;

/**
 * Created by charliewaite on 08/03/2016.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableAdapter.ViewHolder>{

    JSONArray leagueStandings;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public LeagueTableAdapter(JSONArray jsonArray) {
        this.leagueStandings = jsonArray;
    }

    @Override
    public LeagueTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_competition_table_pos, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(LeagueTableAdapter.ViewHolder holder, int position) {
//        "position":1,"teamName":"Leicester City FC","crestURI":"http://upload.wikimedia.org/wikipedia/en/6/63/Leicester02.png",
//                "playedGames":29,"points":60,"goals":52,"goalsAgainst":31,"goalDifference":21,"wins":17,"draws":9,"losses":3,
//                "home":{"goals":24,"goalsAgainst":15,"wins":8,"draws":5,"losses":1}

        try {
            TextView leaguePosition = (TextView) holder.view.findViewById(R.id.league_position);
            leaguePosition.setText(String.valueOf(position + 1));

            TextView teamName = (TextView) holder.view.findViewById(R.id.league_team_name);
            teamName.setText((String) leagueStandings.getJSONObject(position).get("teamName"));

            TextView teamWins = (TextView) holder.view.findViewById(R.id.league_games_won);
            teamWins.setText(String.valueOf(leagueStandings.getJSONObject(position).get("wins")));

            TextView teamDraws = (TextView) holder.view.findViewById(R.id.league_games_drawn);
            teamDraws.setText(String.valueOf(leagueStandings.getJSONObject(position).get("draws")));

            TextView teamLosses = (TextView) holder.view.findViewById(R.id.league_games_lost);
            teamLosses.setText(String.valueOf(leagueStandings.getJSONObject(position).get("losses")));

            TextView teamGoalsFor = (TextView) holder.view.findViewById(R.id.league_goals_for);
            teamGoalsFor.setText(String.valueOf(leagueStandings.getJSONObject(position).get("goals")));

            TextView teamGoalsAgainst = (TextView) holder.view.findViewById(R.id.league_goals_against);
            teamGoalsAgainst.setText(String.valueOf(leagueStandings.getJSONObject(position).get("goalsAgainst")));

            TextView teamPoints = (TextView)holder.view.findViewById(R.id.league_points);
            teamPoints.setText(String.valueOf(leagueStandings.getJSONObject(position).get("points")));


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return leagueStandings.length();
    }
}
