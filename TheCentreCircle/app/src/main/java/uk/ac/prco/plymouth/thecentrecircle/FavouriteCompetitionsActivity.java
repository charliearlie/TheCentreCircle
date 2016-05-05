package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * Similar to AllCompetitionsActivity but displays only the user's favourite competitions
 * @author Charles Waite
 **/
public class FavouriteCompetitionsActivity extends AppCompatActivity {

    private ArrayList<Competition> competitions = new ArrayList<>();
    String[] favourites = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_competitions);
        Constants constants = new Constants();

        final ListView listView = (ListView) findViewById(R.id.competitionsListView);

        setupActionBar();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Competition competition = competitions.get(position);
                System.out.println(competition);
                Intent intent = new Intent(FavouriteCompetitionsActivity.this, CompetitionTabbedActivity.class);
                intent.putExtra("competition", competition);
                Toast.makeText(FavouriteCompetitionsActivity.this, "my id " + id,
                        Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


        Firebase ref = new Firebase(constants.getFirebaseUrl());
        AuthData authData = ref.getAuth();

        //Detect whether the user is logged in as an extra precaution but the user SHOULD not be able
        //to reach this activity if not logged in
        if (authData != null) {
            final Firebase userRef = new Firebase(constants.getFirebaseUrl());

            //Firebase reference to retrieve the user's favourite competitions
            userRef.child("/users/"
                    + authData.getUid() + "/favcompetitions").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String compKey = dataSnapshot.getKey();
                    userRef.child("/competitions/" + compKey).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    adapter.add((String) dataSnapshot.child("name").getValue());
                                    competitions.add(dataSnapshot.getValue(Competition.class));

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
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

            if (competitions == null) {
                TextView noComps = (TextView) findViewById(R.id.no_fav_comps_textView);
                //listView.setVisibility(View.GONE);
                noComps.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
