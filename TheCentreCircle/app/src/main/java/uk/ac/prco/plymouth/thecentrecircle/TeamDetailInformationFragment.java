package uk.ac.prco.plymouth.thecentrecircle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.prco.plymouth.thecentrecircle.classes.Team;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailInformationFragment extends Fragment {


    private Team team;

    public TeamDetailInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        team = (Team) bundle.getSerializable("team");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_detail_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView textView = (TextView) view.findViewById(R.id.team_detail_information_name);
        textView.setText(team.getName());
    }



}
