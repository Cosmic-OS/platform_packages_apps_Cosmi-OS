package org.os.cosmic_os;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    TextView cosmicVersion, cosmicFeaturesText;
    CardView cosmicFeatures;
    ImageView arrows;
    String Url = "https://raw.githubusercontent.com/Cosmic-OS/platform_vendor_ota/oreo-mr1/bacon.xml";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cosmicVersion = view.findViewById(R.id.cosmic_version);
        cosmicFeatures = view.findViewById(R.id.cosmic_features);
        cosmicFeaturesText = view.findViewById(R.id.cosmic_features_text);
        arrows = view.findViewById(R.id.arrows);
        cosmicFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cosmicFeaturesText.getVisibility() == View.VISIBLE)
                {
                    cosmicFeaturesText.setVisibility(View.GONE);
                    arrows.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
                else
                {
                    cosmicFeaturesText.setVisibility(View.VISIBLE);
                    arrows.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
            }
        });

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
        XMLParser xmlParser = new XMLParser();
        xmlParser.execute(Url);
        return view;
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    public static class XMLParser extends AsyncTask<String, Integer, String> {
        NodeList nodeList;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(new InputSource(url.openStream()));
                document.getDocumentElement().normalize();
                nodeList = document.getElementsByTagName("ROM");
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList nodeList = element.getElementsByTagName("VersionNumber").item(0).getChildNodes();
                Node node1 = nodeList.item(0);
                //Log.e("zeromod",node1.getNodeValue());
            }
        }
    }
}
