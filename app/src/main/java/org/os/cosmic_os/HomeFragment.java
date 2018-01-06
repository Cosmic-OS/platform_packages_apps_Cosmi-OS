package org.os.cosmic_os;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    TextView cosmicVersion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cosmicVersion = view.findViewById(R.id.cosmic_version);

        try {
            Process process = Runtime.getRuntime().exec("/system/bin/getprop ro.cos.version");
            InputStream stdin = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            cosmicVersion.setText(String.format("%s %s", getString(R.string.cosmic_version), line));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//
//        try {
//            // Create a URL for the desired page
//            URL url = new URL("https://raw.githubusercontent.com/Cosmic-OS/platform_vendor_cos/oreo/common.mk");
//
//            // Read all the text returned by the server
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            String str;
//            while ((str = in.readLine()) != null) {
//                // str is one line of text; readLine() strips the newline character(s)
//                int found = str.indexOf("COS_VERSION_NUMBER :=");
//                if(found >=0)
//                {
//                    cosmicVersion.setText(str.substring(str.lastIndexOf("=")+1));
//                }
//            }
//            in.close();
//        } catch (IOException ignored) {
//        }

        return view;
    }
    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

}
