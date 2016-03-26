package uk.ac.prco.plymouth.thecentrecircle;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionTeamListFragment extends ListFragment {

    Constants cons = new Constants();
    Firebase ref = new Firebase(cons.getFirebaseUrl() + "/teams");

    interface CompetitionTeamListListener{
        void itemClicked(long id);
    };

    private CompetitionTeamListListener listener;

    public CompetitionTeamListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Bundle args = getArguments();
        final Competition competition = (Competition) args.getSerializable("competition");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        setListAdapter(adapter);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (competition.getId().equals(dataSnapshot.child("leagues").getValue(String.class))) {
                    System.out.println("PHAGGOTS II: " + dataSnapshot.child("name"));
                    adapter.add(dataSnapshot.child("name").getValue(String.class));
                }

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


        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (CompetitionTeamListListener) a;
        }

    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        if(listener != null) {
            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
            startActivity(intent);
            Toast.makeText(getContext(), "ID is " + position, Toast.LENGTH_LONG).show();
            listener.itemClicked(id);
        }
    }

}
