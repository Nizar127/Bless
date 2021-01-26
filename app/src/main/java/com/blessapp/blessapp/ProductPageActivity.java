package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductPageActivity extends AppCompatActivity {


    RecyclerView homerecycler;
    //FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference mAuth, dbreference, favRef, favListRef;
    Boolean fvrtChecker = false;
    Product productItem;
    ProductPageAdapter adapter;
    ImageView cartBtn, profileBtn, arrowBtn, lovebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        mAuth = FirebaseDatabase.getInstance().getReference("Products");
        cartBtn = findViewById(R.id.cart_id);
        profileBtn = findViewById(R.id.profile_id);
        arrowBtn = findViewById(R.id.back_btn_productArrow);
        lovebtn = findViewById(R.id.favourite_id);

        lovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductPageActivity.this, FavouriteMainActivity.class);
                startActivity(intent);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProductPageActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductPageActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        homerecycler = findViewById(R.id.HomeRecycler);
        homerecycler.setHasFixedSize(true);
        homerecycler.setLayoutManager(new GridLayoutManager(this, 2));

        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mAuth, Product.class)
                .build();

        adapter = new ProductPageAdapter(options);

        homerecycler.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductPageActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}





