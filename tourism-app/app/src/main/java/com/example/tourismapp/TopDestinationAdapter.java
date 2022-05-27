package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TopDestinationAdapter extends RecyclerView.Adapter<TopDestinationAdapter.TopViewHolder>
{
    private List<TopDestination> topDestinationList;
    private Context context;

    // Constructor
    public TopDestinationAdapter(List<TopDestination> topDestinationList, Context context)
    {
        this.topDestinationList = topDestinationList;
        this.context = context;
    }

    @NonNull
    @Override
    public TopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.destination_row, parent, false);
        return new TopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopViewHolder holder, int position)
    {
        holder.imageViewHorizontal.setImageResource(topDestinationList.get(position).getImage());
    }

    @Override
    public int getItemCount()
    {
        return topDestinationList.size();
    }

    public class TopViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageViewHorizontal;

        public TopViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageViewHorizontal = itemView.findViewById(R.id.imageViewHorizontal);
        }
    }
}
