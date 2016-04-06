package uk.ac.prco.plymouth.thecentrecircle;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;
import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionFixturesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;

    public CompetitionFixturesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Constants constants = new Constants();
        final Bundle args = getArguments();
        Competition competition = (Competition) args.getSerializable("competition");

        //URL to retrieve data from external API - Currently hardcoded to a specific date
        String url = "http://football-api.com/api/?Action=today" +
                "&APIKey=" + constants.getFootballAPIKey() +
                "&comp_id=" + competition.getId() + "&IP=81.156.123.17";

        try {
            //Retrieve the fixtures from the URL in the background
            new retrieveFixtures().execute(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competition_fixtures, container, false);
    }

    /**
     * A class which allows the retrieval of the competition's fixtures on a different thread
     * from the main thread
     */
    public class retrieveFixtures extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            try {
                //URL passed to class is stored in 0th element of params
                String param = params[0];
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //Convert the byte code retrieved in to it's String representation
                String returned = new CCUtilities().readAllJson(bufferedReader);

                jsonObject = new JSONObject(returned);
                String errorMessage = jsonObject.getString("ERROR");
                if (errorMessage.equals("OK")) {
                    //Retrieve all the matches from the returned object
                    jsonArray = jsonObject.getJSONArray("matches");
                } else {
                    jsonArray = null;
                }

                return jsonArray;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //While the fixtures are loading, display a progress dialog
            progressDialog = ProgressDialog.show(getContext(), "The Centre Circle", "Loading...", true);

        }

        /**
         * What we do with the JSON array after it is fully populated
         * @param jsonArray the returned array of matches from the API
         */
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            View view = getView();
            final Bundle args = getArguments();

            Competition competition = (Competition) args.getSerializable("competition");
            ArrayList<Match> matches = new ArrayList<>();

            if(jsonArray != null) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Retrieve the specific details from each match
                        System.out.println(jsonArray.getJSONObject(i));
                        String homeTeam = String.valueOf(jsonArray.getJSONObject(i).get("match_localteam_name"));
                        String awayTeam = String.valueOf(jsonArray.getJSONObject(i).get("match_visitorteam_name"));
                        String homeScore = String.valueOf(jsonArray.getJSONObject(i).get("match_localteam_score"));
                        String awayScore = String.valueOf(jsonArray.getJSONObject(i).get("match_visitorteam_score"));
                        int matchId = Integer.parseInt(String.valueOf(jsonArray.getJSONObject(i).get("match_id")));
                        String matchStatus = String.valueOf(jsonArray.getJSONObject(i).get("match_status"));

                        if(homeScore.equals("?") && awayScore.equals("?")) {
                            homeScore = "-";
                            awayScore = "-";
                        }


                        //Create a new Match object with the retrieved details
                        Match match = new Match(homeTeam, awayTeam, homeScore, awayScore, matchId, R.drawable.arsenal,
                                R.drawable.barcelona, matchStatus, new ArrayList<Event>());

                        matches.add(match);

                        //Retrieve the recycler view and populate it with the matches through the ScoreCardAdapter

                    }
                    mRecyclerView = (RecyclerView) view.findViewById(R.id.fixture_recycler);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    ScoreCardAdapter scoreCardAdapter = new ScoreCardAdapter(matches);
                    mRecyclerView.setAdapter(scoreCardAdapter);

                    //Set default animation on recycler view
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    progressDialog.dismiss();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                progressDialog.dismiss();
                TextView noFix = (TextView) view.findViewById(R.id.no_fixtures_today);
                noFix.setVisibility(View.VISIBLE);
            }

        }

    }

}