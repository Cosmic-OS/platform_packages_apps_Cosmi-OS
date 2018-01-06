package org.os.cosmic_os;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class DonateFragment extends Fragment {


    public DonateFragment() {
        // Required empty public constructor
    }

    Button donateLink;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        //open donate link in a browser
        donateLink = view.findViewById(R.id.donate);
        donateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent donationIntent = new Intent(Intent.ACTION_VIEW);
                donationIntent.setData(Uri.parse("https://www.paypal.me/CosmicOS"));
                startActivity(donationIntent);
                Toast.makeText(getContext(),"Thank You for the oreo",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public static DonateFragment newInstance(){
        return new DonateFragment();
    }

}
