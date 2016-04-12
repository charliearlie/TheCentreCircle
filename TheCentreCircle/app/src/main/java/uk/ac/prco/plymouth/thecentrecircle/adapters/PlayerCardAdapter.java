package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.classes.Player;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * Created by charliewaite on 11/04/2016.
 */
public class PlayerCardAdapter extends RecyclerView.Adapter<PlayerCardAdapter.ViewHolder> {
    private ArrayList<Player> players = new ArrayList<>();
    Constants cons = new Constants();
    Firebase ref = new Firebase(cons.getFirebaseUrl() + "/playerimages");

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public PlayerCardAdapter(ArrayList<Player> players) {
        this.players = players;
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
    public PlayerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()) .inflate(R.layout.player_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(PlayerCardAdapter.ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        Firebase playerImageRef = ref.child(players.get(position).getId());
        playerImageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                if (imageUrl != null) {
                    CircleImageView im = (CircleImageView) cardView.findViewById(R.id.player_image);
                    Picasso.with(cardView.getContext()).load(imageUrl).into(im);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        CircleImageView playerImage = (CircleImageView) cardView.findViewById(R.id.player_image);
        playerImage.setImageResource(R.drawable.messi);
        TextView playerName = (TextView) cardView.findViewById(R.id.player_name);
        playerName.setText(players.get(position).getName());
        ImageView injuryImage = (ImageView) cardView.findViewById(R.id.player_injury);
        injuryImage.setVisibility(View.GONE);
        if (players.get(position).getInjured().equals("True")) {
            injuryImage.setVisibility(View.VISIBLE);
        }

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
        return players.size();
    }

    public static interface Listener {
        public void onClick(int position);
    }
}
