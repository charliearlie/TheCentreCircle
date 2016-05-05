package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * An activity to list all supported competitions
 *
 * @author Charles Waite
 **/
public class AllCompetitionsActivity extends AppCompatActivity {

    Constants constants = new Constants();
    private ArrayList<Competition> competitions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_competitions);

        //Retrieve the list view to display all the competitions available
        final ListView listView = (ListView) findViewById(R.id.competitionsListView);

        setupActionBar();
        setTitle("All competitions");

        //Create an adapter fot the competition names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        //Set the adapter for the list view to populate it
        listView.setAdapter(adapter);

        /**
         * On item click listener to take the user to a competitions details
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Competition competition = competitions.get(position);

                //Set the intent to take us to the CompetitionTabbedActivity
                Intent intent = new Intent(AllCompetitionsActivity.this, CompetitionTabbedActivity.class);
                intent.putExtra("competition", competition);
                startActivity(intent);
            }
        });

        Firebase ref = new Firebase(constants.getFirebaseUrl() + "/competitions");
        Query queryRef = ref.orderByChild("coefficientPoints");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    competitions.add(childSnapshot.getValue(Competition.class));
                }
                Collections.reverse(competitions);

                for (Competition competition : competitions) {
                    String listItem = competition.getRegion() + ": "
                            + competition.getName();
                    adapter.add(listItem);
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
