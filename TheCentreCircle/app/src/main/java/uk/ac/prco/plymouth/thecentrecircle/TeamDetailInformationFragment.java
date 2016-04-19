package uk.ac.prco.plymouth.thecentrecircle;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailInformationFragment extends Fragment {


    private Team team;
    private String teamBadgeUrl;
    Constants cons = new Constants();

    public TeamDetailInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        team = (Team) bundle.getSerializable("team");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_detail_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.v("TEAM INFO", team.getTeam_id());
        TextView teamNameTextView = (TextView) view.findViewById(R.id.team_detail_information_name);
        teamNameTextView.setText(team.getName());
        TextView teamManagerTextView = (TextView) view.findViewById(R.id.team_manager);
        teamManagerTextView.setText(team.getCoach_name());
        TextView teamYearEstTextView = (TextView) view.findViewById(R.id.team_year_established);
        teamYearEstTextView.setText(team.getFounded());
        TextView teamStadiumTextView = (TextView) view.findViewById(R.id.team_stadium);
        teamStadiumTextView.setText(team.getVenue_name());
        TextView teamStadiumCapacityTextView = (TextView) view.findViewById(R.id.team_stadium_capacity);
        String capacity = "Capacity: " + team.getVenue_capacity();
        teamStadiumCapacityTextView.setText(capacity);
        final ImageView im = (ImageView) view.findViewById(R.id.team_detail_badge);
        Firebase badgeRef = new Firebase(cons.getFirebaseUrl() + "/badges/" + team.getTeam_id());

        badgeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamBadgeUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                Log.v("TEAM BADGE URL", "url: " + teamBadgeUrl);
                Picasso.with(getContext()).load(teamBadgeUrl).into(im);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void onStart() {
        super.onStart();

        try {
            Constants constants = new Constants();
            String[] splitString = team.getName().split(" ");
            String teamName = splitString[0];
            for (int i = 1; i < splitString.length; i++) {
                teamName += "+" + splitString[i];
            }

            //URL to retrieve data from external API
            String url = "https://kgsearch.googleapis.com/v1/entities:search" +
                    "?key=" + constants.getGoogleAPIServerKey() + "&query=" + teamName + "&types=SportsTeam";

            //Retrieve the fixtures from the URL in the background
            new retrieveClubInformation().execute(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class retrieveClubInformation extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray;
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
                jsonArray = jsonObject.getJSONArray("itemListElement");
                jsonObject = jsonArray.getJSONObject(0);
                JSONObject results = jsonObject.getJSONObject("result");
                String name = results.getString("name");
                String description = results.getString("description");
                JSONObject detailedDescription = results.getJSONObject("detailedDescription");
                String articleBody = detailedDescription.getString("articleBody");
                System.out.println("ARTICLE BODY: " + articleBody);


                return detailedDescription;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            View view = getView();


            try {
                TextView teamInfoTextView = (TextView) view.findViewById(R.id.team_information_wiki);
                TextView knowledgreGraphTextView = (TextView) view.findViewById(R.id.google_knowledge_graph_disclaimer);
                String articleBody = jsonObject.getString("articleBody");
                teamInfoTextView.setText(articleBody);
                String disclaimer = "This paragraph is provided by the Google Knowledge Graph API and is being used for testing";
                knowledgreGraphTextView.setText(disclaimer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }


}
