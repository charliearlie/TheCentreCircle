package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.prco.plymouth.thecentrecircle.MatchDetailTabbedActivity;
import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.TeamDetailActivity;
import uk.ac.prco.plymouth.thecentrecircle.TeamDetailTabbedActivity;
import uk.ac.prco.plymouth.thecentrecircle.adapters.MatchEventAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchDetailInfoFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private MatchEventAdapter matchEventAdapter;

    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<Event> matchEvents = new ArrayList<>();

    private int matchId = 0;

    boolean firstTimeUse = false;

    ImageView im;
    ImageView im2;

    public MatchDetailInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);

        if(settings.getBoolean("my_first_time", true)) {
            firstTimeUse = true;
            settings.edit().putBoolean("my_first_time", false).apply();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_detail_info, container, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        //Initialise the recycler view and set it's layout manager
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final Constants consts = new Constants();
        final CCUtilities utils = new CCUtilities();

        //Retrieve the arguments from the activity
        final Bundle args = getArguments();
        String matchDate = args.getString("matchDate");
        int matchId = args.getInt("matchId");

        //Set the Firebase reference as the match selected
        //Firebase ref = new Firebase(cons.getFirebaseUrl() + "/matches/" + date);
        Firebase ref = new Firebase(consts.FIREBASE_URL + "/matches/" + matchDate);
        final Firebase matchRef = ref.child(String.valueOf(matchId));

        //Add value event listener to update the score and time
        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Retrieve match data
                String homeTeam = dataSnapshot.child("homeTeam").getValue(String.class);
                String awayTeam = dataSnapshot.child("awayTeam").getValue(String.class);
                String homeScore = dataSnapshot.child("homeScore").getValue(String.class);
                String awayScore = dataSnapshot.child("awayScore").getValue(String.class);
                String matchStatus = dataSnapshot.child("matchStatus").getValue(String.class);
                final String homeTeamId = dataSnapshot.child("homeTeamId").getValue(String.class);
                final String awayTeamId = dataSnapshot.child("awayTeamId").getValue(String.class);
                String competitionId = dataSnapshot.child("matchCompId").getValue(String.class);
                String venue = dataSnapshot.child("venue").getValue(String.class);

                //Update the text views
                TextView homeTeamTextView = (TextView) view.findViewById(R.id.detail_home_team);
                homeTeamTextView.setText(homeTeam);
                TextView awayTeamTextView = (TextView) view.findViewById(R.id.detail_away_team);
                awayTeamTextView.setText(awayTeam);
                final TextView homeScoreTextView = (TextView) view.findViewById(R.id.detail_home_score);
                homeScoreTextView.setText(homeScore);
                final TextView awayScoreTextView = (TextView) view.findViewById(R.id.detail_away_score);
                awayScoreTextView.setText(awayScore);
                TextView matchStatusTextView = (TextView) view.findViewById(R.id.detail_match_status);
                matchStatusTextView.setText(matchStatus);
                TextView matchVenueTextView = (TextView) view.findViewById(R.id.match_detail_stadium);
                matchVenueTextView.setText(venue);



                Firebase badgeRefHome = new Firebase(consts.FIREBASE_URL + "/badges/" + homeTeamId);
                Firebase badgeRefAway = new Firebase(consts.FIREBASE_URL + "/badges/" + awayTeamId);

                badgeRefHome.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                        im = (ImageView) view.findViewById(R.id.detail_home_badge);
                        Picasso.with(getContext()).load(imageUrl).into(im);

                        if (im != null) {
                            im.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Firebase teamRef = new Firebase(consts.FIREBASE_URL + "/teams/" + homeTeamId);
                                    teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Team team = utils.getTeamFromSnapshot(dataSnapshot);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("team", team);
                                            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                badgeRefAway.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                        im2 = (ImageView) view.findViewById(R.id.detail_away_badge);
                        Picasso.with(getContext()).load(imageUrl).into(im2);

                        if (im2 != null) {
                            im2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Firebase teamRef = new Firebase(consts.FIREBASE_URL + "/teams/" + awayTeamId);
                                    teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Team team = utils.getTeamFromSnapshot(dataSnapshot);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("team", team);
                                            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                if (getActivity() == null) {
                    matchRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        /**
         * This would avoid the events being up to date when returning from another tab but
         * it would prevent us connecting to Firebase and refilling the matchEvents every single time
         * the tab is returned too.
         * May be removed..
         */
        if (matchEvents.size() < 1) {
            Firebase eventRef = matchRef.child("events");
            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    matchEvents.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String eventAssist = postSnapshot.child("eventAssist").getValue(String.class);
                        String eventAssistId = postSnapshot.child("eventAssistId").getValue(String.class);
                        String eventExtraMin = postSnapshot.child("eventExtraMin").getValue(String.class);
                        String eventId = postSnapshot.child("eventId").getValue(String.class);
                        String eventMinute = postSnapshot.child("eventMinute").getValue(String.class);
                        String eventPlayer = postSnapshot.child("eventPlayer").getValue(String.class);
                        String eventPlayerId = postSnapshot.child("eventplayerId").getValue(String.class);
                        String eventTeam = postSnapshot.child("eventTeam").getValue(String.class);
                        String eventType = postSnapshot.child("eventType").getValue(String.class);
                        Event event = new Event(eventAssist, eventAssistId, eventExtraMin, eventId,
                                eventMinute, eventPlayer, eventPlayerId, eventTeam, eventType);
                        matchEvents.add(event);
//                        events.add(event.getEventType() + " : " + event.getEventPlayer() + " ('" + event.getEventMinute()
//                                + ")");
                    }

                    matchEventAdapter = new MatchEventAdapter(matchEvents);
                    mRecyclerView.setAdapter(matchEventAdapter);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            matchEventAdapter = new MatchEventAdapter(matchEvents);
            mRecyclerView.setAdapter(matchEventAdapter);
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        //if (true) {
        if (firstTimeUse) {
            showTutorial(getView());
        }
    }

    private void showTutorial(final View view) {

        Target showcaseTarget = new Target() {
            @Override
            public Point getPoint() {
                return new ViewTarget(view.findViewById(R.id.detail_home_badge)).getPoint();
            }
        };

        ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
                .setTarget(showcaseTarget)
                .setStyle(R.style.CustomShowcaseTheme)
                .setContentTitle("View team information")
                .setContentText("Press team badges to view")
                .hideOnTouchOutside()
                .build();

        showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                ((MatchDetailTabbedActivity)getActivity()).showFavouriteTutorial();
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

            }
        });
    }

}
