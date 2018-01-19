package org.os.cosmic_os;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TeamFragment extends Fragment {


    public TeamFragment() {
        // Required empty public constructor
    }

    TeamAdapter teamAdapter;
    ArrayList<TeamData> devList;
    RecyclerView teamRecyclerView;
    String url = "https://raw.githubusercontent.com/Cosmic-OS/platform_packages_apps_Cosmic-OS/master/app/src/main/assets/team.json";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        devList = TeamData.getDevelopersFromFile(getContext());
        teamAdapter = new TeamAdapter(getContext(),devList);

        //Download the json file from repo
        final DownloadTask downloadTask = new DownloadTask(getContext());
        downloadTask.execute(url);

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

    public static class DownloadTask extends AsyncTask<String, Integer, String> {
        String file;
        DownloadTask(Context getContext) {
            file = getContext.getExternalFilesDir(null)+"/"+"team.json";
        }


        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream;
            OutputStream outputStream;
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + httpURLConnection.getResponseCode()
                            + " " + httpURLConnection.getResponseMessage();
                }
                inputStream = httpURLConnection.getInputStream();
                outputStream = new FileOutputStream(new File(file));
                byte data[] = new byte[4096];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
