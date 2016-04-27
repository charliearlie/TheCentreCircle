package uk.ac.prco.plymouth.thecentrecircle;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



        /**
         * This single value event listener is a mess due to how the Ultimate Recycler View is filled.
         * I am actually ashamed of this code
         *
         */
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot statsSnapshot = dataSnapshot.child("statistics");
                String totalWins = "", homeWins = "", awayWins = "",
                        totalDraws = "", homeDraws = "", awayDraws = "",
                        totalLosses = "", homeLosses = "", awayLosses = "",
                        totalGoalsScored = "", homeGoalsScored = "", awayGoalsScored = "",
                        totalGoalsConceded = "", homeGoalsConceded = "", awayGoalsConceded = "",
                        goalScoringMinutes015 = "", goalScoringMinutes015Percent = "",
                        goalScoringMinutes1530 = "", goalScoringMinutes1530Percent = "",
                        goalScoringMinutes3045 = "", goalScoringMinutes3045Percent = "",
                        goalScoringMinutes4560 = "", goalScoringMinutes4560Percent = "",
                        goalScoringMinutes6075 = "", goalScoringMinutes6075Percent = "",
                        goalScoringMinutes7590 = "", goalScoringMinutes7590Percent = "";
                for (DataSnapshot postSnapshot : statsSnapshot.getChildren()) {

                    //Retrieve win information about selected club
                    totalWins = postSnapshot.child("wins").getValue(String.class);
                    homeWins = postSnapshot.child("wins_home").getValue(String.class);
                    awayWins = postSnapshot.child("wins_away").getValue(String.class);

                    //Retrieve draw information
                    totalDraws = postSnapshot.child("draws").getValue(String.class);
                    homeDraws = postSnapshot.child("draws_home").getValue(String.class);
                    awayDraws = postSnapshot.child("draws_away").getValue(String.class);

                    //Retrieve loss information
                    totalLosses = postSnapshot.child("losses").getValue(String.class);
                    homeLosses = postSnapshot.child("losses_home").getValue(String.class);
                    awayLosses = postSnapshot.child("losses_away").getValue(String.class);

                    totalGoalsScored = postSnapshot.child("goals").getValue(String.class);
                    homeGoalsScored = postSnapshot.child("goals_home").getValue(String.class);
                    awayGoalsScored = postSnapshot.child("goals_away").getValue(String.class);

                    totalGoalsConceded = postSnapshot.child("goals_conceded").getValue(String.class);
                    homeGoalsConceded = postSnapshot.child("goals_conceded_home").getValue(String.class);
                    awayGoalsConceded = postSnapshot.child("goals_conceded_away").getValue(String.class);

                    goalScoringMinutes015 = postSnapshot.child("scoring_minutes_0_15_cnt")
                            .getValue(String.class);
                    goalScoringMinutes015Percent = postSnapshot.child("scoring_minutes_0_15_pct")
                            .getValue(String.class);

                    goalScoringMinutes1530 = postSnapshot.child("scoring_minutes_15_30_cnt")
                            .getValue(String.class);
                    goalScoringMinutes1530Percent = postSnapshot.child("scoring_minutes_15_30_pct")
                            .getValue(String.class);

                    goalScoringMinutes3045 = postSnapshot.child("scoring_minutes_30_45_cnt")
                            .getValue(String.class);
                    goalScoringMinutes3045Percent = postSnapshot.child("scoring_minutes_30_45_pct")
                            .getValue(String.class);

                    goalScoringMinutes4560 = postSnapshot.child("scoring_minutes_45_60_cnt")
                            .getValue(String.class);
                    goalScoringMinutes4560Percent = postSnapshot.child("scoring_minutes_45_60_pct")
                            .getValue(String.class);

                    goalScoringMinutes6075 = postSnapshot.child("scoring_minutes_60_75_cnt")
                            .getValue(String.class);
                    goalScoringMinutes6075Percent = postSnapshot.child("scoring_minutes_60_75_pct")
                            .getValue(String.class);

                    goalScoringMinutes7590 = postSnapshot.child("scoring_minutes_75_90_cnt")
                            .getValue(String.class);
                    goalScoringMinutes7590Percent = postSnapshot.child("scoring_minutes_75_90_pct")
                            .getValue(String.class);

                }

                String[] winsArray = { "Total wins: " + totalWins, "", "Home wins: " + homeWins, "",
                        "Away wins: " + awayWins, "", "", "" };
                String[] drawsArray = {"Total draws: " + totalDraws, "", "Home draws: " +
                        homeDraws, "", "Away draws: " + awayDraws, "", "", "" };
                String[] lossesArray = {"Total losses: " + totalLosses, "", "Home losses: " +
                        homeLosses, "", "Away losses: " + awayLosses, "", "", "" };
                String[] goalsScoredArray = {"Total goals scored: " + totalGoalsScored, "", "Home goals: " +
                        homeGoalsScored, "", "Away goals: " + awayGoalsScored, "", "", "" };
                String[] goalsConcededArray = {"Total goals conceded: " + totalGoalsConceded, "", "Home goals conceded: " +
                        homeGoalsConceded, "", "Away goals conceded: " + awayGoalsConceded, "", "", "" };


                String[] goalsByMinute = {"Goals scored in minutes 1 - 15:   " + goalScoringMinutes015,
                        "","    Percent of total goals:     " + goalScoringMinutes015Percent, "",
                        "Goals scored in minutes 16 - 30:   " + goalScoringMinutes1530, "",
                                "    Percent of total goals     : " + goalScoringMinutes1530Percent, "",
                        "Goals scored in minutes 31 - 45:   " + goalScoringMinutes3045, "",
                                "    Percent of total goals     : " + goalScoringMinutes3045Percent, "",
                        "Goals scored in minutes 45 - 60:   " + goalScoringMinutes4560, "",
                                "    Percent of total goals     : " + goalScoringMinutes4560Percent, "",
                        "Goals scored in minutes 61 - 75:   " + goalScoringMinutes6075, "",
                                "    Percent of total goals     : " + goalScoringMinutes6075Percent, "",
                        "Goals scored in minutes 76 - 90:   " + goalScoringMinutes7590, "",
                                "    Percent of total goals     : " + goalScoringMinutes7590Percent, "","",""};

                mRecyclerViewAdapter.addAll(StatisticExpansionAdapter.getPreCodeMenu(winsArray,
                        drawsArray, lossesArray, goalsScoredArray, goalsConcededArray, goalsByMinute), 0);
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
