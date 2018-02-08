package org.os.cosmic_os;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    TextView cosmicVersion, cosmicFeaturesText, updateCheck, updateChangelog, updateDownload;
    CardView cosmicFeatures;
    ImageView arrows;
    String Url = "https://raw.githubusercontent.com/Cosmic-OS/platform_vendor_ota/oreo-mr1/";
    String[] serverNodes;
    DownloadManager downloadManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cosmicVersion = view.findViewById(R.id.cosmic_version);
        cosmicFeatures = view.findViewById(R.id.cosmic_features);
        cosmicFeaturesText = view.findViewById(R.id.cosmic_features_text);
        updateCheck = view.findViewById(R.id.update_check);
        updateChangelog = view.findViewById(R.id.changelog_new);
        updateDownload = view.findViewById(R.id.download_update);
        arrows = view.findViewById(R.id.arrows);
        if (getContext() != null) { downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE); }
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
            this.cosmicVersion.setText(String.format("%s %s", getString(R.string.cosmic_version), line));
            process = Runtime.getRuntime().exec("/system/bin/getprop persist.ota.version");
            stdin = process.getInputStream();
            isr = new InputStreamReader(stdin);
            br = new BufferedReader(isr);
            String localVersion = br.readLine();

            process = Runtime.getRuntime().exec("/system/bin/getprop ro.product.device");
            stdin = process.getInputStream();
            isr = new InputStreamReader(stdin);
            br = new BufferedReader(isr);
            String device = br.readLine();
            XMLParser xmlParser = new XMLParser();
            serverNodes = xmlParser.execute(Url+device+".xml").get();

            if (!localVersion.equals(serverNodes[0])) {
                updateCheck.setText(String.format("%s %s", getString(R.string.update_available), serverNodes[0]));
                updateChangelog.setVisibility(View.VISIBLE);
                updateChangelog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Light_Dialog);
                        builder.setTitle("New Changelog")
                                .setMessage(serverNodes[1])
                                .show();
                    }
                });
                updateDownload.setVisibility(View.VISIBLE);
                updateDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (getActivity() != null)
                            ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    0);
                        }
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(serverNodes[2]));
                            request.setTitle("Cosmic Update");
                            request.setDescription(serverNodes[0]);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Cosmic Update/" + "/" + serverNodes[0] + ".zip");
                            downloadManager.enqueue(request);
                        }
                    }
                });
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    public static class XMLParser extends AsyncTask<String, Integer, String[]>{
        NodeList nodeList;
        String[] xmlValues = new String[3];
        @Override
        protected String[] doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(new InputSource(url.openStream()));
                document.getDocumentElement().normalize();
                nodeList = document.getElementsByTagName("ROM");
                Node node = nodeList.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList versionNL = element.getElementsByTagName("VersionNumber").item(0).getChildNodes();
                    Node versionNode = versionNL.item(0);
                    NodeList changelogNL = element.getElementsByTagName("Changelog").item(0).getChildNodes();
                    Node changelogNode = changelogNL.item(0);
                    NodeList urlNL = element.getElementsByTagName("DirectUrl").item(0).getChildNodes();
                    Node urlNode = urlNL.item(0);
                    xmlValues[0] = versionNode.getNodeValue();
                    xmlValues[1] = changelogNode.getNodeValue();
                    xmlValues[2] = urlNode.getNodeValue();
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            return xmlValues;
        }
    }
}