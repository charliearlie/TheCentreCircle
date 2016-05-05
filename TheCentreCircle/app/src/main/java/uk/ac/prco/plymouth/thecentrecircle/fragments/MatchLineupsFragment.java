package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.app.ProgressDialog;
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
import uk.ac.prco.plymouth.thecentrecircle.adapters.LineupAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchLineupsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;

    ArrayList<String> events = new ArrayList<>();
    private ArrayList<Event> matchEvents = new ArrayList<>();

    private Constants consts = new Constants();

    private int matchId;




    public MatchLineupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_lineups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lineup_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final Bundle args = getArguments();
        matchId = args.getInt("matchId");
        String matchStatus = args.getString("matchStatus");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.football-api.com")
                .appendPath("2.0")
                .appendPath("commentaries")
                .appendPath(String.valueOf(matchId))
                .appendQueryParameter("Authorization", consts.getFootballApiKeyV2());

        String url = builder.build().toString();

        if (!matchStatus.contains(":") && !matchStatus.equals("postp.")) {
            new RetrieveTeamLineup().execute(url);
        }

    }

    public class RetrieveTeamLineup extends AsyncTask<String, Void, ArrayList<JSONArray>> {

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
                System.out.println("apiiiii-" + lineupAvailable);
                InputStream inputStream = urlConnection.getInputStream();
                System.out.println("INPUT STREAM: " + inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                //Convert the byte code retrieved in to it's String representation
                String returned = new CCUtilities().readAllJson(bufferedReader);

                jsonObject = new JSONObject(returned);
                jsonObject = jsonObject.getJSONObject("lineup");
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
        protected void onPostExecute(ArrayList<JSONArray> teams) {
            super.onPostExecute(teams);

            if(getView() != null) {
                if (teams != null) {
                    JSONArray homeArray = teams.get(0);
                    JSONArray awayArray = teams.get(1);

                    mRecyclerView = (RecyclerView) getView().findViewById(R.id.lineup_recycler);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    LineupAdapter lineupAdapter = new LineupAdapter(teams);
                    mRecyclerView.setAdapter(lineupAdapter);
                } else {
                    TextView noLineupsAvailable = (TextView) getView().findViewById(R.id.no_lineups_available);
                    noLineupsAvailable.setVisibility(View.VISIBLE);
                    noLineupsAvailable.setText("No lineups available");
                }
            }



        }
    }
}
