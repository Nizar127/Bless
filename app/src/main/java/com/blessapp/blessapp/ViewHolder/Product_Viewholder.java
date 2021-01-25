package com.blessapp.blessapp;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.blessapp.blessapp.Interface.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Product_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView itemImg, favBtn, favImg, favDeleteBtn;
    TextView itemName, itemPrice, favName, favPrice;
    DatabaseReference favouriteRef;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    public ItemClickListener listener;


    public Product_Viewholder(@NonNull View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.productitem_name);
        itemPrice = itemView.findViewById(R.id.product_price);
        itemImg = itemView.findViewById(R.id.productitem_image);
        favBtn = itemView.findViewById(R.id.favourite_btn);
    }

/*    public void setitem(Application activity, String name, String description, String price,
                        String image, String category, String pid, String userid, String date, String time, String nameLower);

        itemName = itemView.findViewById(R.id.productitem_name);
        itemPrice = itemView.findViewById(R.id.product_price);

        Picasso.get().load(image).into(itemImg);
        itemName.setText(name);
        itemPrice.setText(price);*/


   // }
/*
    public void favouriteChecker(final String postkey){
        favBtn = itemView.findViewById(R.id.favourite_btn);

        favouriteRef = db.getReference("favourites");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        favouriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(uid)){
                    favBtn.setImageResource(R.drawable.favourite_border);
                }else {
                    favBtn.setImageResource(R.drawable.favourite_red);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setItemFavourite(Application activity, String name, String description, String price,
                                 String image, String category, String pid, String userid, String date, String time, String nameLower){

     favName = itemView.findViewById(R.id.favitem_name);
     favPrice = itemView.findViewById(R.id.favitem_price);

     Picasso.get().load(image).into(favImg);
     favName.setText(name);
     favPrice.setText(price);
    }

    public void setDeleteItemFavourite(Application activity, String name, String description, String price,
                                       String image, String category, String pid, String userid, String date, String ime, String nameLower){

        favName = itemView.findViewById(R.id.favitem_name);
        favPrice = itemView.findViewById(R.id.favitem_price);
        favDeleteBtn = itemView.findViewById(R.id.deletefavourite_btn);

        Picasso.get().load(image).into(favImg);
        favName.setText(name);
        favPrice.setText(price);
    }*/


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
