package com.example.plantclicker.shop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plantclicker.R;

import java.util.Set;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.ViewHolder> {

    public final ShopCategory category;
    private final Set<String> bought;
    private final int balance;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ShopRecyclerViewAdapter(Context context, ShopCategory category, Set<String> bought, int balance) {
        this.mInflater = LayoutInflater.from(context);
        this.category = category;
        this.bought = bought;
        this.balance = balance;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shop_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameView.setText(category.items[position].getName(mInflater.getContext()));
        holder.imageView.setImageDrawable(category.items[position].getDrawable(mInflater.getContext()));
        if (bought.contains(category.items[position].getId())) {
            holder.priceView.setVisibility(View.INVISIBLE);
            holder.coinsView.setVisibility(View.INVISIBLE);
        } else {
            holder.priceView.setText(String.valueOf(category.items[position].price));

            if (category.items[position].price > balance)
                holder.priceView.setTextColor(Color.RED);
            else
                holder.priceView.setTextColor(ContextCompat.getColor(mInflater.getContext(), R.color.Yellow));
            holder.boughtView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return category.items.length;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView, priceView, coinsView, boughtView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.itemName);
            priceView = itemView.findViewById(R.id.itemPrice);
            coinsView = itemView.findViewById(R.id.coinsView);
            boughtView = itemView.findViewById(R.id.itemBought);
            imageView = itemView.findViewById(R.id.itemImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}
