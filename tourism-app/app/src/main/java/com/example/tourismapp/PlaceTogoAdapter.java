package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceTogoAdapter extends RecyclerView.Adapter<PlaceTogoAdapter.PlaceViewHolder>
{
    private List<PlaceTogo> placeTogoList;
    private Context context;
    private OnRowClickListener listener;

    public PlaceTogoAdapter(List<PlaceTogo> placeTogoList, Context context, OnRowClickListener clickListener)
    {
        this.placeTogoList = placeTogoList;
        this.context = context;
        this.listener = clickListener;
    }


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.placetogo_row, parent, false);

        return new PlaceViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position)
    {
        holder.placeImageView.setImageResource(placeTogoList.get(position).getImage());
        holder.nameTextView.setText(placeTogoList.get(position).getName());
        holder.locationTextView.setText(placeTogoList.get(position).getLocation());
        holder.descTextView.setText(placeTogoList.get(position).getDescription());
    }

    @Override
    public int getItemCount()
    {
        return placeTogoList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView placeImageView;
        TextView nameTextView, locationTextView, descTextView;
        OnRowClickListener onRowClickListener;

        public PlaceViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener)
        {
            super(itemView);
            placeImageView = itemView.findViewById(R.id.placeImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            onRowClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnRowClickListener
    {
        void onItemClick(int position);
    }
}
