package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.blessapp.Admin.AdminAddNewItemActivity;
import com.blessapp.blessapp.Admin.AdminManageActivity;
import com.blessapp.blessapp.Model.Product;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView prodImg, backBtn;
    private ElegantNumberButton numberButton;
    private TextView prodDesc, prodName, prodPrice, descriptiontext;
    private String productID = "";
    LinearLayout expandableview;
    //Button arrowbtn;
    LinearLayout cardview, suggestionView;
    private String productPrice;
    private String prodImgUrl;
    ImageButton plus, minus;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        toolbar = findViewById(R.id.toolbar_itemproductdetails);

        expandableview = findViewById(R.id.expanded_suggestion);
        cardview = findViewById(R.id.cardview);
        suggestionView = findViewById(R.id.suggestion_wishes);

        //descriptiontext = findViewById(R.id.description_text);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        numberButton = findViewById(R.id.number_btn);
        prodImg = findViewById(R.id.product_image_detail);
        prodName = findViewById(R.id.product_name_detail);
        prodDesc = findViewById(R.id.product_desc_detail);
        prodPrice = findViewById(R.id.product_price_detail);

        backBtn = findViewById(R.id.back_btn_itemproductdetails);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProductDetails();
        //getProductDetails(productID);
        prodImg.setImageResource(R.drawable.perfume);
        //Picasso.get().load(R.drawable.perfume).into(prodImg);

        suggestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandableview.getVisibility()== View.GONE){
                    TransitionManager.beginDelayedTransition(cardview, new AutoTransition());
                    expandableview.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(cardview, new AutoTransition());
                    expandableview.setVisibility(View.GONE);
                }
            }
        });


//<<<<<<< updated_20210126
//=======
//        addToCartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProductDetailsActivity.this, ExploreActivity.class);
//                startActivity(intent);
//            }
//        });
//>>>>>>> master
      // addToCartBtn.setOnClickListener(new View.OnClickListener() {
         //   @Override
       //  public void onClick(View v) {

               /* Intent intent = new Intent(ProductDetailsActivity.this, ExploreActivity.class);
               startActivity(intent);*/
         //      addToCartList();
         //   }
       // });

   // }

 /*   private void orderList() {
        Intent intent = new Intent(ProductDetailsActivity.this, ExploreActivity.class);
        startActivity(intent);
    }*/

    private void addToCartList() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("Orders").child("Cart List");


        String totalProdAmt = numberButton.getNumber();
        float sendTotalAmt = Float.valueOf(totalProdAmt) * Float.valueOf(productPrice);

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", prodName.getText().toString());
        cartMap.put("price", String.valueOf(sendTotalAmt));
        cartMap.put("amount", numberButton.getNumber());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("discount", "");
        cartMap.put("imageUrl", prodImgUrl);

        cartListRef.child("User View")
                .child(currentUserid)
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void getProductDetails() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");

       // productsRef.child(productID).addValueEventListener(new ValueEventListener()
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Product products = dataSnapshot.getValue(Product.class);

                    productPrice = products.getPrice();

                    prodName.setText(products.getName());
                    prodDesc.setText(products.getDescription());
                    prodPrice.setText(products.getPrice());
                    prodImgUrl = products.getImage();
                    Picasso.get().load(products.getImage()).into(prodImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailsActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}
