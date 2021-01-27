package com.blessapp.blessapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blessapp.blessapp.LoginActivity;
import com.blessapp.blessapp.R;

public class AdminManageActivity extends AppCompatActivity {

    CardView additem, manage_order;
    Toolbar toolbar;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage);


        toolbar = findViewById(R.id.toolbar_adminmanagepage);

        backbtn = findViewById(R.id.back_btn_adminmanageArrow);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //initialize data by matching it with code in the layout

        additem = findViewById(R.id.addItem);
        manage_order = findViewById(R.id.manageOrder);

        //set this view into setonclicklistener (to be clickable)
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageActivity.this, AdminAddNewItemActivity.class);
                startActivity(intent);
            }
        });

        manage_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    //to go back
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminManageActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
