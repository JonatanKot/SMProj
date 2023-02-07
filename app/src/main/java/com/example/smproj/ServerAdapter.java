package com.example.smproj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder>{

    ArrayList<LyricModel> lyricsList;
    Context context;

    public ServerAdapter(ArrayList<LyricModel> lyricsList, Context context) {
        this.lyricsList = lyricsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_lyric,parent,false);
        return new ServerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ServerAdapter.ViewHolder holder, int position) {
        LyricModel lyricData = lyricsList.get(position);
        holder.authorTextView.setText(lyricData.getArtist());
        holder.titleTextView.setText(lyricData.getSong());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another acitivty

                Intent intent = new Intent(context,LyricReaderActivity.class);
                intent.putExtra("LIST", lyricsList);
                intent.putExtra("POSITION", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return lyricsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView authorTextView;
        TextView titleTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.lyric_author_text);
            titleTextView = itemView.findViewById(R.id.lyric_title_text);
        }
    }
}
