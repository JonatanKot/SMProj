package com.example.smproj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LyricReaderActivity extends AppCompatActivity {
    TextView title, content;
    ArrayList<LyricModel> lyricsList;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyric_reader);
        title = findViewById(R.id.lyric_reader_title);
        content = findViewById(R.id.lyric_reader_content);

        lyricsList = (ArrayList<LyricModel>) getIntent().getSerializableExtra("LIST");
        pos = (int) getIntent().getSerializableExtra("POSITION");

        title.setText(lyricsList.get(pos).song);
        content.setMovementMethod(new ScrollingMovementMethod());
        new ExecuteTask().execute();
    }

    class ExecuteTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://api.chartlyrics.com/apiv1.asmx/GetLyric?lyricId="+ (lyricsList.get(pos).trackId == 0 ? lyricsList.get(pos).lyricId : lyricsList.get(pos).trackId)
                                    + "&lyricCheckSum=" + lyricsList.get(pos).lyricChecksum);
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
                    NodeList results = document.getElementsByTagName("Lyric");
                    return results.item(0).getTextContent();

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
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            if (res != null) content.setText(res);
        }

    }
}
