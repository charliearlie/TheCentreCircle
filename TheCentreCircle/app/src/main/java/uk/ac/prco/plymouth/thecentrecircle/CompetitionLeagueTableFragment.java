package uk.ac.prco.plymouth.thecentrecircle;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import uk.ac.prco.plymouth.thecentrecircle.adapters.LeagueTableAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionLeagueTableFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Competition competition;
    public CompetitionLeagueTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competition_league_table, container, false);
    }

    public void onStart() {
        super.onStart();
        View view = getView();
        final Bundle args = getArguments();
        Competition competition = (Competition) args.getSerializable("competition");
        try {
            Constants constants = new Constants();

            //URL to retrieve data from external API
            String url = "http://football-api.com/api/?Action=standings&APIKey="
                    + constants.getFootballAPIKey() + "&comp_id=" + competition.getId() +
                    "&IP=81.156.123.17";

            //Retrieve the fixtures from the URL in the background
            new retrieveTable().execute(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public class retrieveTable extends AsyncTask<String, Void, JSONArray> {

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
                //Retrieve all the matches from the returned object
                jsonArray = jsonObject.getJSONArray("teams");


                return jsonArray;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            View view = getView();


            try {
                //Retrieve the recycler view and populate it with the LeagueTableAdapter
                mRecyclerView = (RecyclerView) view.findViewById(R.id.league_recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                LeagueTableAdapter leagueTableAdapter = new LeagueTableAdapter(jsonArray);
                mRecyclerView.setAdapter(leagueTableAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
