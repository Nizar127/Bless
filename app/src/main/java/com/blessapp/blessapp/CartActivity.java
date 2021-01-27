package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.blessapp.Model.Cart;
import com.blessapp.blessapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button confPurcBtn;
    private TextView totalPrice;
    //To round up to 2 dec place
    DecimalFormat df = new DecimalFormat("#.00");

    private int checkEmpty = 0;
    private float totalAmountPrice = 0;

    private DatabaseReference cartCheckRef;

    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        recyclerView = findViewById(R.id.cartRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        confPurcBtn = findViewById(R.id.conf_purchase);
        totalPrice = findViewById(R.id.total_price);

        backBtn = findViewById(R.id.back_btn_cart);

        calculateTotalPrices();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confPurcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To prevent users from ordering without products
                CheckCartIfEmpty();
            }
        });

    }

    private void CheckCartIfEmpty() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        cartCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("Cart List")
                .child("User View")
                .child(uid);

        cartCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.exists()){
                        Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("Total Price", String.valueOf(totalAmountPrice));
                        startActivity(intent);
                    }
                    else {
                        displayMsg();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayMsg() {
        Toast.makeText(CartActivity.this, "No items in cart, please add at least 1 item.", Toast.LENGTH_SHORT).show();
    }
//this code below is for calculate total prices
    private void calculateTotalPrices() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("Cart List")
                .child("User View")
                .child(uid)
                .child("Products");

        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float sum=0;

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("price");
                    float sumAll = Float.parseFloat(String.valueOf(price));
                    totalAmountPrice = sum += sumAll;

                    totalPrice.setText("RM " + df.format(totalAmountPrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(uid)
                .child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                        Picasso.get().load(model.getImageUrl()).into(holder.prodImg);
                        //To hold the data into the model
                        holder.prodName.setText(model.getName());
                        holder.prodQuan.setText("Quantity = " + model.getAmount());
                        //To round up to 2 dec place, df.format('ur variable here')
                        holder.prodPrice.setText("Subtotal = RM " + df.format(Float.valueOf(model.getPrice())));

                        //for edit and delete the items
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Edit",
                                        "Remove"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0){
                                            Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                            intent.putExtra("pid", model.getPid());
                                            startActivity(intent);
                                        }
                                        if(which == 1){
                                            cartListRef.child("User View")
                                                    .child(uid)
                                                    .child("Products")
                                                    .child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();

                                                            cartListRef.child("User View")
                                                                    .child(uid)
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (!dataSnapshot.exists()){
                                                                                startActivity(new Intent(CartActivity.this, CartActivity.class));
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CartActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}
