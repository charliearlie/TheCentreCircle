package uk.ac.prco.plymouth.thecentrecircle;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;
import uk.ac.prco.plymouth.thecentrecircle.adapters.TeamSearchAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Team> searchResults = new ArrayList<>();
    private boolean newIntent = true;
    private TeamSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        Firebase mainRef = new Firebase(new Constants().FIREBASE_URL);
        Firebase teamRef = mainRef.child("teams");
        Query teamQuery = teamRef.orderByChild("name");

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new TeamSearchAdapter(searchResults);
        mRecyclerView.setAdapter(adapter);

        teamQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Team team = new CCUtilities().getTeamFromSnapshot(postSnapshot);
                    teams.add(team);
                }
                System.out.println(dataSnapshot);
                if (newIntent) {
                    handleIntent(getIntent());
                    newIntent = false;
                }

                adapter.setOnItemClickListener(new TeamSearchAdapter.ClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("team", searchResults.get(position));
                        Intent intent = new Intent(SearchResultsActivity.this, TeamDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        newIntent = true;
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        searchResults.clear();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).toUpperCase().trim().replaceAll("\\s+","");
            for(Team searchTeam : teams) {
                if (searchTeam.getName().toUpperCase().trim().replaceAll("\\s+","").equals(query)) {
                    startActivity(new Intent(SearchResultsActivity.this, TeamDetailActivity.class)
                    .putExtra("team", searchTeam));

                    finish();
                }
                else if (searchTeam.getName().toUpperCase().trim().replaceAll("\\s+","").contains(query)
                        || searchTeam.getVenue_city().toUpperCase().trim().replaceAll("\\s+","")
                        .contains(query)) {
                    searchResults.add(searchTeam);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_search_results).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResults.clear();
                newText = newText.toUpperCase().trim().replaceAll("\\s+","");
                for(Team searchTeam : teams) {
                    if (searchTeam.getName().toUpperCase().trim().replaceAll("\\s+","").contains(newText)
                            || searchTeam.getVenue_city().toUpperCase().trim().replaceAll("\\s+","")
                            .contains(newText)) {
                        searchResults.add(searchTeam);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return true;
    }
}
