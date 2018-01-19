package org.os.cosmic_os;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class TeamFragment extends Fragment {


    public TeamFragment() {
        // Required empty public constructor
    }

    TeamAdapter teamAdapter;
    ArrayList<TeamData> devList;
    RecyclerView teamRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        devList = TeamData.getDevelopersFromFile(getContext());
        teamAdapter = new TeamAdapter(getContext(),devList);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_team, container, false);
        teamRecyclerView = view.findViewById(R.id.team_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        teamRecyclerView.setLayoutManager(layoutManager);
        teamRecyclerView.setAdapter(teamAdapter);

        return view;
    }

    public static TeamFragment newInstance(){
        return new TeamFragment();
    }

}
