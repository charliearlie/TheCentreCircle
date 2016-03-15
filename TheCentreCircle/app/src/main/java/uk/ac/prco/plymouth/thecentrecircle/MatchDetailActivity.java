package uk.ac.prco.plymouth.thecentrecircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Match;

public class MatchDetailActivity extends AppCompatActivity {

    public static final int EXTRA_MATCHID = 0;
    public static final ArrayList<Match> EXTRA_MATCHES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
//        ArrayList<Match> matches = (ArrayList<Match>) getIntent().getSerializableExtra("matches");
//        int position = (Integer) getIntent().getExtras().get("matchId");

        Match match = (Match) getIntent().getSerializableExtra("match");

        TextView homeTeam = (TextView) findViewById(R.id.detail_home_team);
        TextView awayTeam = (TextView) findViewById(R.id.detail_away_team);
        TextView homeScore = (TextView) findViewById(R.id.detail_home_score);
        TextView awayScore = (TextView) findViewById(R.id.detail_away_score);
        ImageView homeBadge = (ImageView) findViewById(R.id.detail_home_badge);
        ImageView awayBadge = (ImageView) findViewById(R.id.detail_away_badge);

       // homeScore.setText(Integer.toString(position));
        homeTeam.setText(match.getHomeTeam());
        awayTeam.setText(match.getAwayTeam());
        homeScore.setText(match.getHomeScore());
        awayScore.setText(match.getAwayScore());
        homeBadge.setImageResource(match.getHomeBadge());
        awayBadge.setImageResource(match.getAwayBadge());

//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
