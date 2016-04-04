package uk.ac.prco.plymouth.thecentrecircle;


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

import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;


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
        final ImageView im = (ImageView)view.findViewById(R.id.team_detail_badge);
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



}
