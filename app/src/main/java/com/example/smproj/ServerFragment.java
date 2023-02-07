package com.example.smproj;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ServerFragment extends Fragment {
    RecyclerView recyclerView;
    TextView noLyricsTextView;
    EditText authorPrompt, titlePrompt;
    Button searchButton;
    ArrayList<LyricModel> lyricsList = new ArrayList<>();
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_server, container, false);
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getView();

        recyclerView = getView().findViewById(R.id.recycler_view_server);
        noLyricsTextView = getView().findViewById(R.id.no_songs_server_text);
        authorPrompt = getView().findViewById(R.id.lyric_author_text_prompt);
        titlePrompt = getView().findViewById(R.id.lyric_title_text_prompt);
        searchButton = getView().findViewById(R.id.lyrics_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                lyricsList.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                noLyricsTextView.setVisibility(View.INVISIBLE);
                new ExecuteTask().execute();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(new ServerAdapter(lyricsList, v.getContext().getApplicationContext()));

        if(lyricsList.size()==0){
            noLyricsTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new ServerAdapter(lyricsList,v.getContext().getApplicationContext()));
        }
    }

    class ExecuteTask extends AsyncTask<Void, Void, Integer>
    {

        @Override
        protected Integer doInBackground(Void... voids) {
            int result = 0;

            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://api.chartlyrics.com/apiv1.asmx/SearchLyric?artist=" + authorPrompt.getText() + "&song=" + titlePrompt.getText());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(in);
                    NodeList results = document.getElementsByTagName("SearchLyricResult");
                    for (int i = 0; i < results.getLength() - 1; i++) {
                        Element parent = (Element) results.item(i);
                        Node checksum = parent.getElementsByTagName("TrackChecksum").item(0);
                        if (checksum == null) checksum = parent.getElementsByTagName("LyricChecksum").item(0);
                        lyricsList.add(new LyricModel(
                                checksum.getTextContent(),
                                Integer.parseInt(parent.getElementsByTagName("TrackId").item(0).getTextContent()),
                                Integer.parseInt(parent.getElementsByTagName("LyricId").item(0).getTextContent()),
                                parent.getElementsByTagName("SongUrl").item(0).getTextContent(),
                                parent.getElementsByTagName("ArtistUrl").item(0).getTextContent(),
                                parent.getElementsByTagName("Artist").item(0).getTextContent(),
                                parent.getElementsByTagName("Song").item(0).getTextContent(),
                                Integer.parseInt(parent.getElementsByTagName("SongRank").item(0).getTextContent())
                        ));
                    }
                    //recyclerView.getAdapter().notifyItemRangeInserted(0, results.getLength() - 1);
                    //recyclerView.getAdapter().notifyDataSetChanged();
                    result = 1;
                }
                else
                {
                    System.out.println("GET request failed with code " + responseCode);
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer res) {
            if (res == 0){
                noLyricsTextView.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }

    }
}
