package com.example.FoodRescueApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FoodRescueApp.model.Food;

import java.io.File;
import java.util.List;

public class FoodRowAdapter extends RecyclerView.Adapter<FoodRowAdapter.FoodItemViewHolder>
{

    // Declaring Variables
    private List<Food> foodList;
    private Context context;
    private OnFoodClickListener listener;


    // Constructor
    public FoodRowAdapter(List<Food> foodList, Context context, OnFoodClickListener listener) {
        this.foodList = foodList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodRowAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_row, parent, false);
        return new FoodItemViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRowAdapter.FoodItemViewHolder holder, int position)
    {
        holder.foodImage.setImageBitmap(foodList.get(position).getImage());
        holder.foodTitle.setText(foodList.get(position).getTitle());
        holder.foodDesc.setText(foodList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount()
    {
        return foodList.size();
    }


    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Variables
        private ImageView foodImage;
        private TextView foodTitle, foodDesc;
        private ImageButton shareButton;
        private OnFoodClickListener foodClickListener;


        public FoodItemViewHolder(@NonNull View itemView, OnFoodClickListener foodClickListener)
        {
            super(itemView);

            foodImage = itemView.findViewById(R.id.foodImageView);
            foodTitle = itemView.findViewById(R.id.foodTitleTextView);
            foodDesc = itemView.findViewById(R.id.foodDescTextView);
            shareButton = itemView.findViewById(R.id.shareImageButton);

            itemView.setOnClickListener(n -> foodClickListener.onFoodClick(this.getAdapterPosition()));
            shareButton.setOnClickListener(n -> foodClickListener.onShareClick(this.getAdapterPosition()));
        }

        @Override
        public void onClick(View v)
        {
            foodClickListener.onFoodClick(getAdapterPosition());
            foodClickListener.onShareClick(getAdapterPosition());
        }
    }

    public interface OnFoodClickListener
    {
        void onFoodClick(int position);
        void onShareClick(int position);
    }


}
