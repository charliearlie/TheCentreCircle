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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.adapters.LeagueTableAdapter;
import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Match;


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
        try {

            new retrieveTable().execute("http://api.football-data.org/v1/soccerseasons/398/leagueTable");



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public class retrieveTable extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            try {
                String param = params[0];
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String returned = readAll(bufferedReader);

                jsonObject = new JSONObject(returned);
                jsonArray = jsonObject.getJSONArray("standing");


                return jsonArray;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            View view = getView();
            final Bundle args = getArguments();
            Competition competition = (Competition) args.getSerializable("competition");

            try {

                mRecyclerView = (RecyclerView) view.findViewById(R.id.league_recycler_view);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                LeagueTableAdapter leagueTableAdapter = new LeagueTableAdapter(jsonArray);
                mRecyclerView.setAdapter(leagueTableAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//                TextView position1 = (TextView)view.findViewById(R.id.leaguePosition1TextView);
//                position1.setText((String) jsonArray.getJSONObject(0).get("teamName"));
//                TextView position2 = (TextView)view.findViewById(R.id.leaguePosition2TextView);
//                position2.setText((String) jsonArray.getJSONObject(1).get("teamName"));
//                TextView position3 = (TextView)view.findViewById(R.id.leaguePosition3TextView);
//                position3.setText((String) jsonArray.getJSONObject(2).get("teamName"));
//                TextView position4 = (TextView)view.findViewById(R.id.leaguePosition4TextView);
//                position4.setText((String) jsonArray.getJSONObject(3).get("teamName"));
//                TextView position5 = (TextView)view.findViewById(R.id.leaguePosition5TextView);
//                position5.setText((String) jsonArray.getJSONObject(4).get("teamName"));
//                TextView position6 = (TextView)view.findViewById(R.id.leaguePosition6TextView);
//                position6.setText((String) jsonArray.getJSONObject(5).get("teamName"));
//                TextView position7 = (TextView)view.findViewById(R.id.leaguePosition7TextView);
//                position7.setText((String) jsonArray.getJSONObject(6).get("teamName"));
//                TextView position8 = (TextView)view.findViewById(R.id.leaguePosition8TextView);
//                position8.setText((String) jsonArray.getJSONObject(7).get("teamName"));
//                TextView position9 = (TextView)view.findViewById(R.id.leaguePosition9TextView);
//                position9.setText((String) jsonArray.getJSONObject(8).get("teamName"));
//                TextView position10 = (TextView)view.findViewById(R.id.leaguePosition10TextView);
//                position10.setText((String) jsonArray.getJSONObject(9).get("teamName"));
//                TextView position11 = (TextView)view.findViewById(R.id.leaguePosition11TextView);
//                position11.setText((String) jsonArray.getJSONObject(10).get("teamName"));
//                TextView position12 = (TextView)view.findViewById(R.id.leaguePosition12TextView);
//                position12.setText((String) jsonArray.getJSONObject(11).get("teamName"));
//                TextView position13 = (TextView)view.findViewById(R.id.leaguePosition13TextView);
//                position13.setText((String) jsonArray.getJSONObject(12).get("teamName"));
//                TextView position14 = (TextView)view.findViewById(R.id.leaguePosition14TextView);
//                position14.setText((String) jsonArray.getJSONObject(13).get("teamName"));
//                TextView position15 = (TextView)view.findViewById(R.id.leaguePosition15TextView);
//                position15.setText((String) jsonArray.getJSONObject(14).get("teamName"));
//                TextView position16 = (TextView)view.findViewById(R.id.leaguePosition16TextView);
//                position16.setText((String) jsonArray.getJSONObject(15).get("teamName"));
//                TextView position17 = (TextView)view.findViewById(R.id.leaguePosition17TextView);
//                position17.setText((String) jsonArray.getJSONObject(16).get("teamName"));
//                TextView position18 = (TextView)view.findViewById(R.id.leaguePosition18TextView);
//                position18.setText((String) jsonArray.getJSONObject(17).get("teamName"));
//                TextView position19 = (TextView)view.findViewById(R.id.leaguePosition19TextView);
//                position19.setText((String) jsonArray.getJSONObject(18).get("teamName"));
//                TextView position20 = (TextView)view.findViewById(R.id.leaguePosition20TextView);
//                position20.setText((String) jsonArray.getJSONObject(19).get("teamName"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
