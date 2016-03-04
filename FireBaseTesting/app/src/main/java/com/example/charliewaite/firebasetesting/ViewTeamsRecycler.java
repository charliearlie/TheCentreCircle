package com.example.charliewaite.firebasetesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Objects;

public class ViewTeamsRecycler extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Team> teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams_recycler);

        Firebase ref = new Firebase("https://charleswaitetest.firebaseio.com/premierleague/teams");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ref.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Team team = dataSnapshot.getValue(Team.class);
                teams.add(team);
                mAdapter = new MyAdapter(teams);
                mRecyclerView.setAdapter(mAdapter);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String tn = (String) dataSnapshot.child("teamName").getValue();
                int index = findTeamByName(tn);
                teams.get(index).setStadium((String) dataSnapshot.child("stadium").getValue());
                mAdapter = new MyAdapter(teams);
                mRecyclerView.setAdapter(mAdapter);
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    public int findTeamByName(String name) {
        int index = 0;
        for (int i = 0; i < teams.size(); i++) {
            if(name.equals(teams.get(i).getTeamName())) {
                index = i;
            }
        }
        return index;
    }
}
