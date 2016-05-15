package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchDetailStatisticFragment extends Fragment {
    private Constants consts = new Constants();
    ProgressDialog progressDialog;



    public MatchDetailStatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_detail_statistic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int matchId;
        String homeTeamName;
        String awayTeamName;
        String matchStatus;


        final Bundle args = getArguments();
        matchId = args.getInt("matchId");
        homeTeamName = args.getString("matchHomeName");
        awayTeamName = args.getString("matchAwayName");
        matchStatus = args.getString("matchStatus");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.football-api.com")
                .appendPath("2.0")
                .appendPath("commentaries")
                .appendPath(String.valueOf(matchId))
                .appendQueryParameter("Authorization", consts.getFootballApiKeyV2());

        String url = builder.build().toString();

        TextView homeTeamNameTextView = (TextView) view.findViewById(R.id.home_name_statistic_header);
        homeTeamNameTextView.setText(homeTeamName);
        TextView awayTeamNameTextView = (TextView) view.findViewById(R.id.away_name_statistic_header);
        awayTeamNameTextView.setText(awayTeamName);

        if (matchStatus != null) {
            if (!matchStatus.contains(":") && !matchStatus.equals("postp.")) {
                new RetrieveStatistics().execute(url);
            } else {
                TextView noStatsTextView = (TextView) view.findViewById(R.id.stats_not_available);
                noStatsTextView.setVisibility(View.VISIBLE);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.match_statistic_view);
                for ( int i = 1; i < linearLayout.getChildCount();  i++ ){
                    View childView = linearLayout.getChildAt(i);
                    System.out.println("child: " + childView);
                    childView.setVisibility(View.GONE);
                }

            }
        }




    }

    public class RetrieveStatistics extends AsyncTask<String, Void, ArrayList<JSONArray>> {

        @Override
        protected ArrayList<JSONArray> doInBackground(String... params) {
            JSONObject jsonObject;
            JSONArray localTeamJSONArray= new JSONArray();
            JSONArray visitorTeamJSONArray = new JSONArray();
            ArrayList<JSONArray> lineups = new ArrayList<>();

            try {
                String param = params[0];
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);
                int lineupAvailable = urlConnection.getInputStream().available();
                System.out.println(lineupAvailable);
                InputStream inputStream = urlConnection.getInputStream();
                System.out.println("INPUT STREAM: " + inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                //Convert the byte code retrieved in to it's String representation
                String returned = new CCUtilities().readAllJson(bufferedReader);

                jsonObject = new JSONObject(returned);
                jsonObject = jsonObject.getJSONObject("match_stats");
                localTeamJSONArray = jsonObject.getJSONArray("localteam");
                visitorTeamJSONArray = jsonObject.getJSONArray("visitorteam");

                lineups.add(localTeamJSONArray);
                lineups.add(visitorTeamJSONArray);

                System.out.println("Home team: + " + localTeamJSONArray);
                System.out.println("away team: + " + visitorTeamJSONArray);

                return lineups;


            } catch(IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONArray> jsonArrays) {

            super.onPostExecute(jsonArrays);
            progressDialog = ProgressDialog.show(getContext(), "The Centre Circle", "Loading...", true);

            if (jsonArrays != null) {
                try {
                    JSONArray homeArray = jsonArrays.get(0);
                    JSONArray awayArray = jsonArrays.get(1);
                    JSONObject homeStats = homeArray.getJSONObject(0);
                    JSONObject awayStats = awayArray.getJSONObject(0);

                    if (getView() != null) {

                        View view = getView();

                        //Shots
                        int totalShots = Integer.valueOf(homeStats.getString("shots_total")) +
                                Integer.valueOf(awayStats.getString("shots_total"));
                        ProgressBar homeShots = (ProgressBar) view.findViewById(R.id.shots_total_home_bar);
                        ProgressBar awayShots = (ProgressBar) view.findViewById(R.id.shots_total_away_bar);
                        homeShots.setRotation(180);
                        homeShots.setMax(totalShots);
                        awayShots.setMax(totalShots);
                        homeShots.setProgress(Integer.valueOf(homeStats.getString("shots_total")));
                        awayShots.setProgress(Integer.valueOf(awayStats.getString("shots_total")));

                        TextView homeShotsTextView = (TextView) view.findViewById(R.id.shots_total_home);
                        TextView awayShotsTextView = (TextView) view.findViewById(R.id.shots_total_away);
                        homeShotsTextView.setText(homeStats.getString("shots_total"));
                        awayShotsTextView.setText(awayStats.getString("shots_total"));

                        //Shots on target
                        int totalShotsOnTarget = Integer.valueOf(homeStats.getString("shots_ongoal")) +
                                Integer.valueOf(awayStats.getString("shots_ongoal"));
                        ProgressBar homeShotsOnTarget = (ProgressBar) view.findViewById(R.id.shots_on_target_home_bar);
                        ProgressBar awayShotsOnTarget = (ProgressBar) view.findViewById(R.id.shots_on_target_away_bar);
                        homeShotsOnTarget.setRotation(180);
                        homeShotsOnTarget.setMax(totalShotsOnTarget);
                        awayShotsOnTarget.setMax(totalShotsOnTarget);
                        homeShotsOnTarget.setProgress(Integer.valueOf(homeStats.getString("shots_ongoal")));
                        awayShotsOnTarget.setProgress(Integer.valueOf(awayStats.getString("shots_ongoal")));
                        TextView homeShotsOnTargetTextView = (TextView) view.findViewById(R.id.shots_on_target_home);
                        TextView awayShotsOnTargetTextView = (TextView) view.findViewById(R.id.shots_on_target_away);
                        homeShotsOnTargetTextView.setText(homeStats.getString("shots_ongoal"));
                        awayShotsOnTargetTextView.setText(awayStats.getString("shots_ongoal"));

                        //Possession
                        ProgressBar homePossession = (ProgressBar) view.findViewById(R.id.possession_home_bar);
                        ProgressBar awayPossession = (ProgressBar) view.findViewById(R.id.possession_away_bar);
                        homePossession.setRotation(180);
                        homePossession.setProgress(Integer.valueOf(homeStats.getString("possesiontime")
                                .replaceAll("[\\D]", "")));
                        awayPossession.setProgress(Integer.valueOf(awayStats.getString("possesiontime")
                                .replaceAll("[\\D]", "")));
                        TextView homePossessionTextView = (TextView) view.findViewById(R.id.possession_home);
                        TextView awayPossessionTextView = (TextView) view.findViewById(R.id.possession_away);
                        homePossessionTextView.setText(homeStats.getString("possesiontime"));
                        awayPossessionTextView.setText(awayStats.getString("possesiontime"));

                        //Corners
                        int totalCorners = Integer.valueOf(homeStats.getString("corners")) +
                                Integer.valueOf(awayStats.getString("corners"));
                        ProgressBar homeCorners = (ProgressBar) view.findViewById(R.id.corners_home_bar);
                        ProgressBar awayCorners = (ProgressBar) view.findViewById(R.id.corners_away_bar);
                        homeCorners.setRotation(180);
                        homeCorners.setMax(totalCorners);
                        awayCorners.setMax(totalCorners);
                        homeCorners.setProgress(Integer.valueOf(homeStats.getString("corners")));
                        awayCorners.setProgress(Integer.valueOf(awayStats.getString("corners")));
                        TextView homeCornersTextView = (TextView) view.findViewById(R.id.corners_home);
                        TextView awayCornersTextView = (TextView) view.findViewById(R.id.corners_away);
                        homeCornersTextView.setText(homeStats.getString("corners"));
                        awayCornersTextView.setText(awayStats.getString("corners"));

                        //Offsides
                        int totalOffsides = Integer.valueOf(homeStats.getString("offsides")) +
                                Integer.valueOf(awayStats.getString("offsides"));
                        ProgressBar homeOffsides = (ProgressBar) view.findViewById(R.id.offsides_home_bar);
                        ProgressBar awayOffsides = (ProgressBar) view.findViewById(R.id.offsides_away_bar);
                        homeOffsides.setRotation(180);
                        homeOffsides.setMax(totalOffsides);
                        awayOffsides.setMax(totalOffsides);
                        homeOffsides.setProgress(Integer.valueOf(homeStats.getString("offsides")));
                        awayOffsides.setProgress(Integer.valueOf(awayStats.getString("offsides")));
                        TextView homeOffsidesTextView = (TextView) view.findViewById(R.id.offsides_home);
                        TextView awayOffsidesTextView = (TextView) view.findViewById(R.id.offsides_away);
                        homeOffsidesTextView.setText(homeStats.getString("offsides"));
                        awayOffsidesTextView.setText(awayStats.getString("offsides"));

                        //Fouls
                        int totalFouls = Integer.valueOf(homeStats.getString("fouls")) +
                                Integer.valueOf(awayStats.getString("fouls"));
                        ProgressBar homeFouls = (ProgressBar) view.findViewById(R.id.fouls_home_bar);
                        ProgressBar awayFouls = (ProgressBar) view.findViewById(R.id.fouls_away_bar);
                        homeFouls.setRotation(180);
                        homeFouls.setMax(totalFouls);
                        awayFouls.setMax(totalFouls);
                        homeFouls.setProgress(Integer.valueOf(homeStats.getString("fouls")));
                        awayFouls.setProgress(Integer.valueOf(awayStats.getString("fouls")));
                        TextView homeFoulsTextView = (TextView) view.findViewById(R.id.fouls_home);
                        TextView awayFoulsTextView = (TextView) view.findViewById(R.id.fouls_away);
                        homeFoulsTextView.setText(homeStats.getString("fouls"));
                        awayFoulsTextView.setText(awayStats.getString("fouls"));

                        int totalYellows = Integer.valueOf(homeStats.getString("yellowcards")) +
                                Integer.valueOf(awayStats.getString("yellowcards"));
                        ProgressBar homeYellows = (ProgressBar) view.findViewById(R.id.yellow_cards_home_bar);
                        ProgressBar awayYellows = (ProgressBar) view.findViewById(R.id.yellow_cards_away_bar);
                        homeYellows.setRotation(180);
                        homeYellows.setMax(totalYellows);
                        awayYellows.setMax(totalYellows);
                        homeYellows.setProgress(Integer.valueOf(homeStats.getString("yellowcards")));
                        awayYellows.setProgress(Integer.valueOf(awayStats.getString("yellowcards")));
                        TextView homeYellowsTextView = (TextView) view.findViewById(R.id.yellow_cards_home);
                        TextView awayYellowsTextView = (TextView) view.findViewById(R.id.yellow_cards_away);
                        homeYellowsTextView.setText(homeStats.getString("yellowcards"));
                        awayYellowsTextView.setText(awayStats.getString("yellowcards"));

                        TextView homeRedsTextView = (TextView) view.findViewById(R.id.red_cards_home);
                        TextView awayRedsTextView = (TextView) view.findViewById(R.id.red_cards_away);
                        homeRedsTextView.setText(homeStats.getString("redcards"));
                        progressDialog.dismiss();
                        awayRedsTextView.setText(awayStats.getString("redcards "));



                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
