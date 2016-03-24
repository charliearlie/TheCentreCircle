package uk.ac.prco.plymouth.thecentrecircle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.classes.Match;

/**
 * Created by charliewaite on 19/02/2016.
 */
public class ScoreCardAdapter extends RecyclerView.Adapter<ScoreCardAdapter.ViewHolder>
{
    private ArrayList<Match> matches = new ArrayList<>();
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ScoreCardAdapter(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
        //Define the view holder }
    }
    @Override
    public ScoreCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()) .inflate(R.layout.score_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ScoreCardAdapter.ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;
        TextView homeTeam = (TextView) cardView.findViewById(R.id.home_team);
        homeTeam.setText(matches.get(position).getHomeTeam());
        TextView awayTeam = (TextView) cardView.findViewById(R.id.away_team);
        awayTeam.setText(matches.get(position).getAwayTeam());
        if(matches.get(position).getHomeScore().equals("?")) {
            TextView homeScore = (TextView) cardView.findViewById(R.id.home_score);
            homeScore.setText(" - ");
            TextView awayScore = (TextView) cardView.findViewById(R.id.away_score);
            awayScore.setText(" - ");
        } else {
            TextView homeScore = (TextView) cardView.findViewById(R.id.home_score);
            homeScore.setText(matches.get(position).getHomeScore());
            TextView awayScore = (TextView) cardView.findViewById(R.id.away_score);
            awayScore.setText(matches.get(position).getAwayScore());
        }
        TextView matchStatus = (TextView) cardView.findViewById(R.id.match_status);
        matchStatus.setText(matches.get(position).getMatchStatus());

//        ImageView im = (ImageView) cardView.findViewById(R.id.home_badge);
//        Picasso.with(cardView.getContext()).load(R.drawable.arsenalsmall).into(im);
//
//        ImageView im2 = (ImageView) cardView.findViewById(R.id.away_badge);
//        Picasso.with(cardView.getContext()).load(R.drawable.arsenalsmall).into(im2);

        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static interface Listener {
        public void onClick(int position);
    }
}
