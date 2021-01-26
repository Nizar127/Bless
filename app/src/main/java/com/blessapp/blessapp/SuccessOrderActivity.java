package com.blessapp.blessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SuccessOrderActivity extends AppCompatActivity {

    Button viewprofile, viewproduct;
    ImageView checksuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);

        viewproduct = findViewById(R.id.productPageBtn);
        viewprofile = findViewById(R.id.viewProfileBtn);

        viewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productpage();
            }
        });

        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilepage();
            }
        });

        checksuccess = findViewById(R.id.checkIMAGE);
        int image = getResources().getIdentifier("@drawable/check", null, this.getPackageName());
        checksuccess.setImageResource(image);
    }

    private void profilepage() {
        Intent intent = new Intent(SuccessOrderActivity.this, ProfileMainActivity.class);
        startActivity(intent);
    }

    private void productpage() {

        Intent intent = new Intent(SuccessOrderActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}
