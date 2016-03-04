package com.example.charliewaite.firebasetesting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.tv);
        Firebase.setAndroidContext(this);

//        Firebase ref = new Firebase("https://charleswaitetest.firebaseio.com/accumulators/-KA29XzE-cBN67Tit30j");
//
//        ref.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData currentData) {
//                if(currentData.getValue() == null) {
//                    currentData.setValue(1);
//                } else {
//                    currentData.setValue((Long) currentData.getValue() + 1);
//                }
//
//                return Transaction.success(currentData);
//            }
//
//            @Override
//            public void onComplete(FirebaseError fbError, boolean commited, DataSnapshot currentData) {
//                //This will be called with the results of the transaction
//            }
//        });

        Firebase ref = new Firebase("https://charleswaitetest.firebaseio.com/accumulators");

        /////////////DESCRIPTION////////////
//        The value event is used to read a static snapshot of the contents at a given database path,
//                as they existed at the time of the read event. It is triggered once with the initial
//        data and again every time the data changes. The event callback is passed a snapshot
//        containing all data at that location, including child data. In our code example above,
//                value returned all of the blog posts in our app. Everytime a new blog post is added,
//                the callback function will return all of the posts.


//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
//                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    BlogPost post = postSnapshot.getValue(BlogPost.class);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                BlogPost newPost = snapshot.getValue(BlogPost.class);
                System.out.println("Author: " + newPost.getAuthor());
                System.out.println("Title: " + newPost.getTitle());
                String data = "New blog post by " + newPost.getAuthor() + " called " +
                        newPost.getTitle();
                textView.setText(data);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String title = (String) dataSnapshot.child("title").getValue();
                textView.setText("The updated post title is " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                textView.setText(title + " was removed from our system because it was a load of old bollocks");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase teamsRef = new Firebase("https://charleswaitetest.firebaseio.com/premierleague/teams");

//        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Team team = dataSnapshot.getValue(Team.class);
//                System.out.println("Team name: " + team.getTeamName());
//                System.out.println("Year established: " + team.getYearEstablished());
//                String data = "Last team alphabetically: " + team.getTeamName() + " based in " + team.getCity();
//                textView.setText(data);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println(firebaseError.getMessage());
//            }
//        });

        teamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " teams");
                int i = 1;
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    System.out.println("We found team: " + i);
                    System.out.println("Data key for : " + i + " = " + postSnapshot.getKey());

                    Team team = postSnapshot.getValue(Team.class);
                    System.out.println("Team name: " + team.getTeamName());
                    System.out.println("Year established: " + team.getYearEstablished());
                    textView.append(team.getTeamName());

                    i++;
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    public void viewTeams(View view)
    {
        Intent intent = new Intent(MainActivity.this, ViewTeamList.class);
        startActivity(intent);
    }

    public void viewTeamsRecycle(View view)
    {
        Intent intent = new Intent(MainActivity.this, ViewTeamsRecycler.class);
        startActivity(intent);
    }
}
