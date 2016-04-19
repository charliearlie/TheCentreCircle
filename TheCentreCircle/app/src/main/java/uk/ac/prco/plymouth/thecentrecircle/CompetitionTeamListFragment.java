package uk.ac.prco.plymouth.thecentrecircle;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionTeamListFragment extends ListFragment {

    private Constants cons = new Constants();
    private Firebase ref = new Firebase(cons.getFirebaseUrl() + "/teams");
    private ArrayList<Team> teams = new ArrayList<>();

    interface CompetitionTeamListListener {
        void itemClicked(long id);
    }

    ;

    private CompetitionTeamListListener listener;

    public CompetitionTeamListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Bundle args = getArguments();
        final Competition competition = (Competition) args.getSerializable("competition");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        setListAdapter(adapter);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (competition.getId().equals(dataSnapshot.child("leagues").getValue(String.class))) {
                    Team team = getTeamFromSnapshot(dataSnapshot);
                    adapter.add(team.getName());
                    teams.add(team);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (CompetitionTeamListListener) a;
        }

    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        if (listener != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("team", teams.get(position));
            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(getContext(), "The team you selected is " + teams.get(position).getName()
                    , Toast.LENGTH_LONG).show();
            listener.itemClicked(id);
        }
    }

    /**
     * Takes the data snapshot retrieved by Firebase and extracts the teams details from it
     *
     * @param dataSnapshot returned from Firebase query
     * @return the team retrieved from the snapshot
     */
    private Team getTeamFromSnapshot(DataSnapshot dataSnapshot) {
        String coach_id = dataSnapshot.child("coach_id").getValue(String.class);
        String coach_name = dataSnapshot.child("coach_name").getValue(String.class);
        String country = dataSnapshot.child("country").getValue(String.class);
        String founded = dataSnapshot.child("founded").getValue(String.class);
        String is_national = dataSnapshot.child("is_national").getValue(String.class);
        String leagues = dataSnapshot.child("leagues").getValue(String.class);
        String name = dataSnapshot.child("name").getValue(String.class);
        String team_id = dataSnapshot.child("team_id").getValue(String.class);
        String venue_address = dataSnapshot.child("venue_address").getValue(String.class);
        String venue_capacity = dataSnapshot.child("venue_capacity").getValue(String.class);
        String venue_city = dataSnapshot.child("venue_city").getValue(String.class);
        String venue_id = dataSnapshot.child("venue_id").getValue(String.class);
        String venue_name = dataSnapshot.child("venue_name").getValue(String.class);
        String venue_surface = dataSnapshot.child("venue_surface").getValue(String.class);
        Team team = new Team(coach_id, coach_name, country, founded, is_national,
                leagues, name, team_id, venue_address, venue_capacity, venue_city,
                venue_id, venue_name, venue_surface);

        return team;
    }

}
