package com.example.charliewaite.testrecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Match> matches = new ArrayList<>();
    private ScoreCardAdapter adapter;
    //private RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.score_recycler);
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://charleswaitetest.firebaseio.com/premierleague/matches");


        mRecyclerView = (RecyclerView)findViewById(R.id.score_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        ref.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Match match = dataSnapshot.getValue(Match.class);
                matches.add(match);
                adapter = new ScoreCardAdapter(matches);
                mRecyclerView.setAdapter(adapter);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int matchId = dataSnapshot.child("matchId").getValue(int.class);
                int index = findMatchById(matchId);
                //matches.get(index)
                adapter = new ScoreCardAdapter(matches);
                mRecyclerView.setAdapter(adapter);
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private int findMatchById(int matchId) {
        int index = 0;
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchId() == matchId) {
                index = i;
                break;
            }
        }
        return index;
    }
}
