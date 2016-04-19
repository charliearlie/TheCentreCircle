package uk.ac.prco.plymouth.thecentrecircle;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.prco.plymouth.thecentrecircle.adapters.StatisticAdapter;
import uk.ac.prco.plymouth.thecentrecircle.adapters.StatisticExpansionAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailStatisticsFragment extends Fragment {

    private Team team;

    private Constants consts = new Constants();

    private Firebase ref;

    private UltimateRecyclerView mRecyclerView;

    private StatisticExpansionAdapter mRecyclerViewAdapter = null;

    private LinearLayoutManager linearLayoutManager;

    private ActionMode actionMode;

    final ArrayList<String> stats= new ArrayList<>();

    final ArrayList<String> teamWins = new ArrayList<>();

    private String[] wins = {"Home wins: 9", "https://google", "Away wins: 7", "https://google",
    "Ultimate recycler view ignores last two elements.. Why?", "path"};
    private String[] losses = {"Home losses: 3", "away losses: 1"};
    private String[] goals;

    public TeamDetailStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        team = (Team) bundle.getSerializable("team");

        ref = new Firebase(consts.FIREBASE_URL + "/teams/" + team.getTeam_id());


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_detail_statistics, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        mRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.stats_recycler);
        mRecyclerView.setHasFixedSize(false);

        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        addExpandableFeatures();

        mRecyclerViewAdapter = new StatisticExpansionAdapter(getContext());


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot statsSnapshot = dataSnapshot.child("statistics");
                String homeWins = "", awayWins = "", totalDraws = "", homeDraws = "", awayDraws = "",
                        homeLosses = "", awayLosses = "";
                for (DataSnapshot postSnapshot : statsSnapshot.getChildren()) {
                    homeWins = postSnapshot.child("wins_home").getValue(String.class);
                    awayWins = postSnapshot.child("wins_away").getValue(String.class);
                    totalDraws = postSnapshot.child("draws").getValue(String.class);
                    homeDraws = postSnapshot.child("draws_home").getValue(String.class);
                    awayDraws = postSnapshot.child("draws_away").getValue(String.class);
                    stats.add(postSnapshot.child("wins").getValue(String.class));
                    stats.add(postSnapshot.child("draws").getValue(String.class));
                    stats.add(postSnapshot.child("losses").getValue(String.class));
                    stats.add(postSnapshot.child("goals").getValue(String.class));
                    stats.add(postSnapshot.child("goals_conceded").getValue(String.class));
                    stats.add(postSnapshot.child("clean_sheets").getValue(String.class));
                }

                String[] winsArray = { "Home wins: " + homeWins, "path", "Away wins: " + awayWins, "path", "", "" };
                String[] drawsArray = {"Total draws:" + totalDraws, "path", "Home draws: " +
                        homeDraws, "path", "Away draws: " + awayDraws, "path", "", "" };

                mRecyclerViewAdapter.addAll(StatisticExpansionAdapter.getPreCodeMenu(winsArray,
                        drawsArray, losses), 0);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void toggleSelection(int position) {
        mRecyclerViewAdapter.toggleSelection(position);
        actionMode.setTitle("Selected " + "1");
    }

    private void addExpandableFeatures() {
        mRecyclerView.getItemAnimator().setAddDuration(100);
        mRecyclerView.getItemAnimator().setRemoveDuration(100);
        mRecyclerView.getItemAnimator().setMoveDuration(200);
        mRecyclerView.getItemAnimator().setChangeDuration(100);
    }

}
