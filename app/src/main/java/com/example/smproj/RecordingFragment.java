package com.example.smproj;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RecordingFragment extends Fragment {

    Button play_button, pause_button, record_button, stop_button;
    View v;
    String pathsave;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_recorder, container, false);
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getView();

        play_button = v.findViewById(R.id.recorder_play);
        pause_button = v.findViewById(R.id.recorder_pause);
        record_button = v.findViewById(R.id.recorder_record);
        stop_button = v.findViewById(R.id.recorder_stop);

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathsave = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+"/audio_record_"
                        + UUID.randomUUID().toString() + ".3gp";
                setupMediaRecorder();
                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                play_button.setEnabled(false);
                record_button.setEnabled(false);
                pause_button.setEnabled(false);
                stop_button.setEnabled(true);

                Toast.makeText(v.getContext(), "Recording...", Toast.LENGTH_SHORT).show();
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                stop_button.setEnabled(false);
                play_button.setEnabled(true);
                record_button.setEnabled(true);
                pause_button.setEnabled(false);
            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause_button.setEnabled(true);
                stop_button.setEnabled(false);
                record_button.setEnabled(false);
                play_button.setEnabled(false);

                mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(pathsave);
                    mediaPlayer.prepare();
                } catch(IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(v.getContext(), "Playing...", Toast.LENGTH_SHORT).show();
            }
        });

        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_button.setEnabled(false);
                record_button.setEnabled(true);
                pause_button.setEnabled(false);
                play_button.setEnabled(true);

                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });

    }

    private void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }
}
