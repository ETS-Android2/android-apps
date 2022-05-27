package com.example.FoodRescueApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.FoodRescueApp.model.Food;

import java.util.ArrayList;
import java.util.List;

public class CartRowAdapter extends RecyclerView.Adapter<CartRowAdapter.CartItemViewHolder>
{

    // Declaring Variables
    private Context context;
    private List<Food> foodList;

    public CartRowAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public CartRowAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        return new CartItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRowAdapter.CartItemViewHolder holder, int position)
    {
        holder.cartFoodName.setText(foodList.get(position).getTitle());
        holder.cartFoodQuantity.setText(foodList.get(position).getQuantity()+"");
        holder.cartFoodPrice.setText("Price");
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView cartFoodName, cartFoodQuantity, cartFoodPrice;

        public CartItemViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cartFoodName = itemView.findViewById(R.id.cartFoodName);
            cartFoodQuantity = itemView.findViewById(R.id.cartFoodQuantity);
            cartFoodPrice = itemView.findViewById(R.id.cartFoodPrice);
        }
    }
}
