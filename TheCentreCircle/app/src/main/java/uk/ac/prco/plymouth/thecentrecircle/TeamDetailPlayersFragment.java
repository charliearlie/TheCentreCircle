package uk.ac.prco.plymouth.thecentrecircle;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.ac.prco.plymouth.thecentrecircle.adapters.PlayerCardAdapter;
import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.classes.Player;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailPlayersFragment extends Fragment {

    private Team team;
    Constants cons = new Constants();
    Firebase ref;
    private RecyclerView mRecyclerView;
    private ArrayList<Player> players = new ArrayList<>();
    private PlayerCardAdapter adapter;

    public TeamDetailPlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        team = (Team) bundle.getSerializable("team");

        //Get the reference for the team in Firebase
        ref = new Firebase(cons.getFirebaseUrl() + "/teams/" + team.getTeam_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_detail_players, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.player_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Query the team's squad object
        Query playerQuery = ref.child("squad");
        playerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    players.add(player);
                }
                //Sort array list by players appearances in ascending order
                Collections.sort(players, new Comparator<Player>() {
                    @Override
                    public int compare(Player lhs, Player rhs) {
                        return Integer.valueOf(lhs.getAppearences()).compareTo(Integer.valueOf(rhs.getAppearences()));
                    }
                });

                //Reverse the array list so the players are now displayed by appearances in descending
                // order. Most used players first.
                Collections.reverse(players);
                adapter = new PlayerCardAdapter(players);
                mRecyclerView.setAdapter(adapter);
                adapter.setListener(new PlayerCardAdapter.Listener() {

                    /**
                     * After the user has pressed a match, the information to retrieve
                     * further match details is passed to MatchDetailActivity
                     * @param position
                     */
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(getContext(), players.get(position).getName() + " pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
