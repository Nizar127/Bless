package com.blessapp.blessapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blessapp.blessapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminProductManageActivity extends AppCompatActivity {


    private ImageView imageView, backBtn;
    private EditText inputProdName, inputProdPrice, inputProdDesc;
    private Button confirmChanges, editItem;
    private DatabaseReference productsRef;
    RecyclerView productmgmtrecycler;

    private String productID = "";

    private String displayImage, displayName, displayPrice, displayDesc, inputProdNameLower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        productID = getIntent().getStringExtra("Product ID");

        imageView = findViewById(R.id.edit_display_product_image);
        productmgmtrecycler = findViewById(R.id.productmgmtrecycler);
        productmgmtrecycler.setHasFixedSize(true);
        productmgmtrecycler.setLayoutManager(new GridLayoutManager(this, 3));

        inputProdName = findViewById(R.id.edit_input_product_name);
        inputProdPrice = findViewById(R.id.edit_input_product_price);
        inputProdDesc = findViewById(R.id.edit_input_product_desc);

        confirmChanges = findViewById(R.id.edit_confirm_changes_btn);
        editItem = findViewById(R.id.edit_products_btn);

        backBtn = findViewById(R.id.back_btn_item_product_manage);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProductManageActivity.this, AdminEditProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
