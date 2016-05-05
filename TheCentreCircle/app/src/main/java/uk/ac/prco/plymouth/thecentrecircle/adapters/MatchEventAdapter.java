package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;

/**
 * Created by charliewaite on 24/03/2016.
 */
public class MatchEventAdapter extends RecyclerView.Adapter<MatchEventAdapter.ViewHolder> {

    ArrayList<Event> events = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MatchEventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public MatchEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_listview_item, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(MatchEventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        TextView homeTeamEventMinute = (TextView) holder.view.findViewById(R.id.home_team_event_minute);
        TextView awayTeamEventMinute = (TextView) holder.view.findViewById(R.id.away_team_event_minute);
        TextView homeTeamEventPlayer = (TextView) holder.view.findViewById(R.id.home_team_event_player);
        TextView awayTeamEventPlayer = (TextView) holder.view.findViewById(R.id.away_team_event_player);
        ImageView homeTeamEventType = (ImageView) holder.view.findViewById(R.id.home_team_event_type);
        ImageView awayTeamEventType = (ImageView) holder.view.findViewById(R.id.away_team_event_type);

        TextView homeTeamEventAssist = (TextView) holder.view.findViewById(R.id.home_team_event_assist);
        TextView awayTeamEventAssist = (TextView) holder.view.findViewById(R.id.away_team_event_assist);

        if (event.getEventTeam().equals("localteam")) {
            if (Integer.parseInt(event.getEventMinute()) < 10) {
                String eventMin = "0" + event.getEventMinute();
                homeTeamEventMinute.setText(eventMin);
            } else {
                homeTeamEventMinute.setText(event.getEventMinute());
            }

            homeTeamEventPlayer.setText(event.getEventPlayer());

            if (event.getEventType().equals("goal") && event.getEventAssist().length() > 2) {
                String assist = "(" + event.getEventAssist() + ")";
                homeTeamEventAssist.setText(assist);
                homeTeamEventAssist.setVisibility(View.VISIBLE);
            }

            awayTeamEventMinute.setVisibility(View.GONE);
            awayTeamEventPlayer.setVisibility(View.GONE);
            awayTeamEventType.setVisibility(View.GONE);

            switch (event.getEventType()) {
                case "yellowcard" :
                    Picasso.with(holder.view.getContext()).load(R.drawable.yellow_card).into(homeTeamEventType);
                    break;
                case "redcard" :
                    Picasso.with(holder.view.getContext()).load(R.drawable.red_card).into(homeTeamEventType);
                    break;
                case "goal" :
                    Picasso.with(holder.view.getContext()).load(R.drawable.goalevent).into(homeTeamEventType);
                    break;
                case "yellowred" :
                    Picasso.with(holder.view.getContext()).load(R.drawable.yellow_red_card).into(homeTeamEventType);
                    break;
            }

        } else {
            if (Integer.parseInt(event.getEventMinute()) < 10) {
                String eventMin = "0" + event.getEventMinute();
                awayTeamEventMinute.setText(eventMin);
            } else {
                awayTeamEventMinute.setText(event.getEventMinute());
            }

            awayTeamEventPlayer.setText(event.getEventPlayer());


            if (event.getEventType().equals("goal") && event.getEventAssist().length() > 2) {
                String assist = "(" + event.getEventAssist() + ")";
                awayTeamEventAssist.setText(assist);
                awayTeamEventAssist.setVisibility(View.VISIBLE);
            }
            homeTeamEventMinute.setVisibility(View.GONE);
            homeTeamEventPlayer.setVisibility(View.GONE);
            //homeTeamEventType.setVisibility(View.GONE);

            switch (event.getEventType()) {
            case "yellowcard" :
                Picasso.with(holder.view.getContext()).load(R.drawable.yellow_card).into(awayTeamEventType);
                break;
            case "redcard" :
                Picasso.with(holder.view.getContext()).load(R.drawable.red_card).into(awayTeamEventType);
                break;
            case "goal" :
                Picasso.with(holder.view.getContext()).load(R.drawable.goalevent).into(awayTeamEventType);
                break;
                case "yellowred" :
                    Picasso.with(holder.view.getContext()).load(R.drawable.yellow_red_card).into(awayTeamEventType);
                    break;
            }

        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
