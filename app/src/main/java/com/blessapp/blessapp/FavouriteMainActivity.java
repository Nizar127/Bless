package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blessapp.blessapp.Model.Favourite;
import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouriteMainActivity extends AppCompatActivity {

    RecyclerView favrecyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference, mAuth;
    ImageView cartBtn, arrowBtn, profileBtn;
    FavouriteAdapter favouriteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_main);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        cartBtn = findViewById(R.id.cart_id);
        profileBtn = findViewById(R.id.profile_id);
        mAuth = FirebaseDatabase.getInstance().getReference("Products").child(currentUserid).child("User View").child("favourites").child("favourite_list");

        arrowBtn = findViewById(R.id.back_btn_favouriteArrow);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FavouriteMainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavouriteMainActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });


        favrecyclerView = findViewById(R.id.FavouriteRecycler);
       favrecyclerView.setHasFixedSize(true);
        // favrecyclerView.setHasFixedSize(true);
        favrecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        reference = database.getReference("favoriteList").child(currentUserid);


      FirebaseRecyclerOptions<Favourite> options =
              new FirebaseRecyclerOptions.Builder<Favourite>()
              .setQuery(mAuth, Favourite.class)
              .build();

      favouriteAdapter = new FavouriteAdapter(options);

      favrecyclerView.setAdapter(favouriteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        favouriteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        favouriteAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavouriteMainActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}
