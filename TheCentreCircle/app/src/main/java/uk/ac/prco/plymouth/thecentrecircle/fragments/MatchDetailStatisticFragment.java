package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private int matchId;


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

        final Bundle args = getArguments();
        matchId = args.getInt("matchId");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.football-api.com")
                .appendPath("2.0")
                .appendPath("commentaries")
                .appendPath(String.valueOf(matchId))
                .appendQueryParameter("Authorization", consts.getFootballApiKeyV2());

        String url = builder.build().toString();

        new RetrieveStatistics().execute(url);

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

            if (jsonArrays != null) {
                try {
                    JSONArray homeArray = jsonArrays.get(0);
                    JSONArray awayArray = jsonArrays.get(1);
                    JSONObject homeStats = homeArray.getJSONObject(0);
                    JSONObject awayStats = awayArray.getJSONObject(0);

                    if (getView() != null) {
                        View view = getView();

                        TextView homeShotsTextView = (TextView) view.findViewById(R.id.shots_total_home);
                        TextView awayShotsTextView = (TextView) view.findViewById(R.id.shots_total_away);
                        homeShotsTextView.setText(homeStats.getString("shots_total"));
                        awayShotsTextView.setText(awayStats.getString("shots_total"));

                        TextView homeShotsOnTargetTextView = (TextView) view.findViewById(R.id.shots_on_target_home);
                        TextView awayShotsOnTargetTextView = (TextView) view.findViewById(R.id.shots_on_target_away);
                        homeShotsOnTargetTextView.setText(homeStats.getString("shots_ongoal"));
                        awayShotsOnTargetTextView.setText(awayStats.getString("shots_ongoal"));

                        TextView homePossessionTextView = (TextView) view.findViewById(R.id.possession_home);
                        TextView awayPossessionTextView = (TextView) view.findViewById(R.id.possession_away);
                        homePossessionTextView.setText(homeStats.getString("possesiontime"));
                        awayPossessionTextView.setText(awayStats.getString("possesiontime"));

                        TextView homeCornersTextView = (TextView) view.findViewById(R.id.corners_home);
                        TextView awayCornersTextView = (TextView) view.findViewById(R.id.corners_away);
                        homeCornersTextView.setText(homeStats.getString("corners"));
                        awayCornersTextView.setText(awayStats.getString("corners"));

                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
