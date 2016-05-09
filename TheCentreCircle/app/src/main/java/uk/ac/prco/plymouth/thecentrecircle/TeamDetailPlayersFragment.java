package uk.ac.prco.plymouth.thecentrecircle;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import uk.ac.prco.plymouth.thecentrecircle.fragments.PlayerDetailFragment;
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
    private ArrayAdapter<String> arrayAdapter;
    private PlayerCardAdapter adapter;
    FloatingActionButton floatingActionButton;

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
        return inflater.inflate(R.layout.fragment_team_detail_players, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.player_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_sort_players);

        setUpFloatingActionButton();



        //Query the team's squad object
        Query playerQuery = ref.child("squad");
        playerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                players.clear();
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    players.add(player);
                }
                System.out.println(players);
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
                        PlayerDetailFragment pdf = new PlayerDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("team", players.get(position));
                        pdf.setArguments(bundle);
                        ((TeamDetailActivity) getActivity()).openPlayerFragment(pdf);
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

    private void setUpFloatingActionButton() {
        //Set the ripple to the accent colour of the app: blue
        floatingActionButton.setRippleColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        //Set the on click listener to respond to a press
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert the user that the favouriting has been successful
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setIcon(R.drawable.centrecirclelogo);
                alertBuilder.setTitle("Sort by..");

                arrayAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.select_dialog_singlechoice_custom);
                arrayAdapter.add("Name");
                arrayAdapter.add("Appearances - default");
                arrayAdapter.add("Goals");
                arrayAdapter.add("Assists");

                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = arrayAdapter.getItem(which);
                        Toast.makeText(getContext(), String.valueOf(which), Toast.LENGTH_LONG).show();

                        switch(which) {
                            case 0:
                                sortPlayersByName();
                                break;
                            case 1:
                                sortPlayersByGoalsOrAssists(which);
                                break;
                            case 2:
                                sortPlayersByGoalsOrAssists(which);
                                break;
                            case 3:
                                sortPlayersByGoalsOrAssists(which);
                                break;
                        }
                    }
                });

                alertBuilder.show();
            }
        });
    }

    private void sortPlayersByName() {
        Query playerNameRef = ref.child("squad").orderByChild("name");
        playerNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players.clear();
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    players.add(player);
                }
                //TODO: Fix the sorting by surname
                Collections.sort(players, new Comparator<Player>() {
                    @Override
                    public int compare(Player lhs, Player rhs) {
                        String lhsPlayerSurname = "";
                        String[] lhsPlayerSurnameArray = lhs.getName().split("\\.");
                        String rhsPlayerSurname = "";
                        String[] rhsPlayerSurnameArray = lhs.getName().split("\\.");

                        if (lhsPlayerSurnameArray.length > 1) {
                            lhsPlayerSurname = lhsPlayerSurnameArray[1];
                        } else {
                            lhsPlayerSurname = lhs.getName();
                        }
                        if (rhsPlayerSurnameArray.length > 1) {
                            rhsPlayerSurname = rhsPlayerSurnameArray[1];
                        } else {
                            rhsPlayerSurname = rhs.getName();
                        }
                        return lhsPlayerSurname.compareTo(rhsPlayerSurname);
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void sortPlayersByGoalsOrAssists(final int which) {
        Query playerNameRef = ref.child("squad").orderByChild("name");
        playerNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players.clear();
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    players.add(player);
                }

                Collections.sort(players, new Comparator<Player>() {
                    @Override
                    public int compare(Player lhs, Player rhs) {
                        if (which == 2) {
                            return Integer.valueOf(lhs.getGoals()).compareTo(Integer.valueOf(rhs.getGoals()));
                        }
                        else if (which == 3) {
                            return Integer.valueOf(lhs.getAssists()).compareTo(Integer.valueOf(rhs.getAssists()));
                        } else {
                            return Integer.valueOf(lhs.getAppearences()).compareTo(Integer.valueOf(rhs.getAppearences()));
                        }

                    }
                });

                Collections.reverse(players);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
