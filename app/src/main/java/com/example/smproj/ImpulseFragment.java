package com.example.smproj;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImpulseFragment extends Fragment {
    RecyclerView recyclerView;
    TextView noImpTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_impulse, container, false);
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getView();

        recyclerView = getView().findViewById(R.id.recycler_view_imp);
        noImpTextView = getView().findViewById(R.id.no_imp_text);

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(v.getContext().getApplicationContext(), Uri.parse("android.resource://com.example.easytutomusicapp/" + R.raw.stalbans_a_mono));
        songsList.add(new AudioModel("android.resource://com.example.easytutomusicapp/" + R.raw.stalbans_a_mono,
                "stalbans_a_mono",
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        mmr.setDataSource(v.getContext().getApplicationContext(), Uri.parse("android.resource://com.example.easytutomusicapp/" + R.raw.stalbans_omni));
        songsList.add(new AudioModel("android.resource://com.example.easytutomusicapp/" + R.raw.stalbans_omni,
                "stalbans_omni",
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));


        if(songsList.size()==0){
            noImpTextView.setVisibility(View.VISIBLE);
        }else{
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
            recyclerView.setAdapter(new MusicListAdapter(songsList, v.getContext().getApplicationContext()));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MusicListAdapter(songsList,v.getContext().getApplicationContext()));
        }
    }
}
