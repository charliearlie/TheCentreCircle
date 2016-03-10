package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

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
//       [{"stand_id":"12049240","stand_competition_id":"1204","stand_season":"2015\/2016",
// "stand_round":"29","stand_stage_id":"12041081","stand_group":"","stand_country":"England",
// "stand_team_id":"9240","stand_team_name":"Leicester","stand_status":"same",
// "stand_recent_form":"WDWLW","stand_position":"1","stand_overall_gp":"29",
// "stand_overall_w":"17","stand_overall_d":"9","stand_overall_l":"3",
// "stand_overall_gs":"52","stand_overall_ga":"31","stand_home_gp":"14",
// "stand_home_w":"8","stand_home_d":"5","stand_home_l":"1",
// "stand_home_gs":"24","stand_home_ga":"15","stand_away_gp":"15",
// "stand_away_w":"9","stand_away_d":"4","stand_away_l":"2",
// "stand_away_gs":"28","stand_away_ga":"16","stand_gd":"21",
// "stand_points":"60","stand_desc":"Promotion - Champions League (Group Stage)"},

        try {
            TextView leaguePosition = (TextView) holder.view.findViewById(R.id.league_position);
            leaguePosition.setText(String.valueOf(position + 1));

            TextView teamName = (TextView) holder.view.findViewById(R.id.league_team_name);
            teamName.setText((String) leagueStandings.getJSONObject(position).get("stand_team_name"));

            TextView teamGamesPlayed = (TextView) holder.view.findViewById(R.id.league_games_played);
            teamGamesPlayed.setText((String) leagueStandings.getJSONObject(position).get("stand_overall_gp"));

            TextView teamWins = (TextView) holder.view.findViewById(R.id.league_games_won);
            teamWins.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_overall_w")));

            TextView teamDraws = (TextView) holder.view.findViewById(R.id.league_games_drawn);
            teamDraws.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_overall_d")));

            TextView teamLosses = (TextView) holder.view.findViewById(R.id.league_games_lost);
            teamLosses.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_overall_l")));

            TextView teamGoalsFor = (TextView) holder.view.findViewById(R.id.league_goals_for);
            teamGoalsFor.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_overall_gs")));

            TextView teamGoalsAgainst = (TextView) holder.view.findViewById(R.id.league_goals_against);
            teamGoalsAgainst.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_overall_ga")));

            TextView teamPoints = (TextView)holder.view.findViewById(R.id.league_points);
            teamPoints.setText(String.valueOf(leagueStandings.getJSONObject(position).get("stand_points")));


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return leagueStandings.length();
    }
}
