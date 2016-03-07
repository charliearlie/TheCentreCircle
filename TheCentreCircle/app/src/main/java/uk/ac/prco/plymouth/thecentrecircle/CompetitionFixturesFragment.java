package uk.ac.prco.plymouth.thecentrecircle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Competition;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompetitionFixturesFragment extends Fragment {


    public CompetitionFixturesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competition_fixtures, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            final Bundle args = getArguments();
            Competition competition = (Competition) args.getSerializable("competition");

            View view = getView();
            TextView textView = (TextView)view.findViewById(R.id.textTabView1);

            textView.setText(competition.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
