package org.os.cosmic_os;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangelogFragment extends Fragment {


    public ChangelogFragment() {
        // Required empty public constructor
    }

    final String CHANGELOG_PATH = "/system/etc/Changelog.txt";
    String text;
    TextView changelogTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_changelog, container, false);
        changelogTextView = view.findViewById(R.id.changelog);
        changelog();
        if (changelogTextView != null)
            changelogTextView.setText(text);
        return view;
    }

    public static ChangelogFragment newInstance(){
        return new ChangelogFragment();
    }

    //Read changelog from changelog file
    public void changelog() {
        InputStreamReader inputReader = null;
        StringBuilder data = new StringBuilder();
        char tmp[] = new char[2048];
        int numRead;

        try {
            inputReader = new FileReader(CHANGELOG_PATH);
            while ((numRead = inputReader.read(tmp)) >= 0) {
                data.append(tmp, 0, numRead);
            }
            text = data.toString();
        } catch (IOException e) {
            text = getString(R.string.changelog_error);
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

}
