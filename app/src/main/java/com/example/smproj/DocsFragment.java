package com.example.smproj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;

public class DocsFragment extends Fragment {
    PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_docs, container, false);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pdfView=getView().findViewById(R.id.pdfView);
        pdfView.fromAsset("dokumentacja.pdf").load();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        //pdfView=getView().findViewById(R.id.pdfView);
        //pdfView.fromAsset("dokumentacja.pdf").load();
    }

    public void onPause(){
        super.onPause();
        pdfView.recycle();
        System.out.println("onPause");
    }

    public void onStop(){
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("onDestroy");
    }



}