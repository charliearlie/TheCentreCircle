package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.MatchDetailTabbedActivity;
import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixturesByDateFragment extends Fragment {

    Constants cons = new Constants();
    private String date;
    Firebase ref;
    //Variables needed for the list to be displayed
    private RecyclerView mRecyclerView;
    private ArrayList<Match> matches = new ArrayList<>();
    private ScoreCardAdapter adapter;

    public FixturesByDateFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        date = bundle.getString("date");
        ref = new Firebase(cons.getFirebaseUrl() + "/matches/" + date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fixtures_by_date, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.fixture_recycler_by_date);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query queryRef = ref.orderByChild("matchTime");

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    final ArrayList<Event> events = new ArrayList<Event>();
                    String homeTeam = postSnapShot.child("homeTeam").getValue(String.class);
                    String awayTeam = postSnapShot.child("awayTeam").getValue(String.class);
                    String homeScore = postSnapShot.child("homeScore").getValue(String.class);
                    String awayScore = postSnapShot.child("awayScore").getValue(String.class);
                    int matchId = postSnapShot.child("matchId").getValue(int.class);
                    int homeBadge = postSnapShot.child("homeBadge").getValue(int.class);
                    int awayBadge = postSnapShot.child("awayBadge").getValue(int.class);
                    String matchStatus = postSnapShot.child("matchStatus").getValue(String.class);
                    String competitionId = postSnapShot.child("matchCompId").getValue(String.class);
                    String homeTeamId = postSnapShot.child("homeTeamId").getValue(String.class);
                    String awayTeamId = postSnapShot.child("awayTeamId").getValue(String.class);
                    String date = postSnapShot.child("date").getValue(String.class);


                    Match match = new Match(homeTeam, awayTeam, homeScore, awayScore,
                            matchId, homeBadge, R.drawable.manutd, matchStatus, events, competitionId,
                            homeTeamId, awayTeamId, date);
                    matches.add(match);


                }
                adapter = new ScoreCardAdapter(matches);
                mRecyclerView.setAdapter(adapter);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_fixtures_by_date);
                progressBar.setVisibility(View.GONE);
                adapter.setListener(new ScoreCardAdapter.Listener() {

                    @Override
                    public void onClick(int position) {
                        Match detailedMatch = matches.get(position);
                        Intent intent = new Intent(getActivity(), MatchDetailTabbedActivity.class);
                        intent.putExtra("matchId", detailedMatch.getMatchId());
                        intent.putExtra("matchDate", detailedMatch.getDate());
                        intent.putExtra("matchHomeName", detailedMatch.getHomeTeam());
                        intent.putExtra("matchAwayName", detailedMatch.getAwayTeam());
                        intent.putExtra("matchStatus", detailedMatch.getMatchStatus());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
