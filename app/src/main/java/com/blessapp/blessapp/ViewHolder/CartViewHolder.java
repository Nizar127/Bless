package com.blessapp.blessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Interface.ItemClickListener;
import com.blessapp.blessapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//this is the recyclerview viewholder
    public ImageView prodImg;
    public TextView prodName, prodPrice, prodQuan;
    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        prodImg = itemView.findViewById(R.id.cart_prod_img);
        prodName = itemView.findViewById(R.id.cart_prod_name);
        prodQuan = itemView.findViewById(R.id.cart_prod_quan);
        prodPrice = itemView.findViewById(R.id.cart_prod_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
