package uk.ac.prco.plymouth.thecentrecircle.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import uk.ac.prco.plymouth.thecentrecircle.R;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * Created by charliewaite on 09/05/2016.
 */
public class TrackMatchDialogFragment extends DialogFragment {

    private Match match;
    private boolean isTracked = false;
    private AuthData authData;
    private String message = "Would you like to track this match?";
    Firebase mainRef = new Firebase(new Constants().FIREBASE_URL);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        match = (Match) args.getSerializable("trackedMatch");
        authData = mainRef.getAuth();

        if(args.getBoolean("isTracked")) {
            message = "Would you like to stop tracking this match?";
            isTracked = true;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (authData != null) {
            if (!isTracked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setIcon(R.drawable.centrecirclelogosmall)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainRef.child("users").child(authData.getUid()).child("trackedMatches")
                                        .child(String.valueOf(match.getMatchId())).setValue(true);

                                //Alert the user that the favouriting has been successful
                                Toast.makeText(getContext(), "You have started tracking " +
                                        match.getHomeTeam() + " - " + match.getAwayTeam(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                return dialog;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setIcon(R.drawable.centrecirclelogosmall)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainRef.child("users").child(authData.getUid()).child("trackedMatches")
                                        .child(String.valueOf(match.getMatchId())).removeValue();

                                //Alert the user that they stopped tracking the match
                                Toast.makeText(getContext(), "You have stopped tracking " +
                                        match.getHomeTeam() + " - " + match.getAwayTeam(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                return dialog;
            }


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You need to be logged in to track matches")
                    .setIcon(R.drawable.centrecirclelogosmall)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            return dialog;
        }
    }
}
