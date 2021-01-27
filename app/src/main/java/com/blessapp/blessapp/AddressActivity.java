package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {

    private EditText houseNo, street, houseArea, city/*, postcode, state*/;
    private TextView updateBtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        houseNo = findViewById(R.id.address_house_no);
        street = findViewById(R.id.address_street);
        houseArea = findViewById(R.id.address_house_area);
        city = findViewById(R.id.address_city);

        backBtn = findViewById(R.id.back_btn_addressArrow);
        updateBtn = findViewById(R.id.update_account_address_btn);

        userAddressDisplay(houseNo, street, houseArea, city /*postcode, state*/);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserAddress();
            }
        });
    }

    private void saveUserAddress() {
        if (TextUtils.isEmpty(houseNo.getText().toString())) {
            Toast.makeText(this, "Please enter your house number.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(street.getText().toString())) {
            Toast.makeText(this, "Please enter your street address.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(houseArea.getText().toString())) {
            Toast.makeText(this, "Please enter your house area.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city.getText().toString())) {
            Toast.makeText(this, "Please enter your city.",Toast.LENGTH_SHORT).show();
        }
        else{
            updateUserAddress();
        }
    }

    private void updateUserAddress() {

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update User Address");
        progressDialog.setMessage("Updating account information...");
        progressDialog.setCanceledOnTouchOutside(false);

        final DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        HashMap<String, Object> addressMap = new HashMap<>();

        addressMap.put("houseNo", houseNo.getText().toString().trim());
        addressMap.put("street", street.getText().toString().trim());
        addressMap.put("houseArea", houseArea.getText().toString().trim());
        addressMap.put("city", city.getText().toString().trim());
        /*addressMap.put("postcode", postcode.getText().toString());
        addressMap.put("state", state.getText().toString());*/

        addressRef.child("address")
                .updateChildren(addressMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddressActivity.this, "Address updated!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddressActivity.this, ProfileMainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void userAddressDisplay(final EditText houseNo, final EditText street, final EditText houseArea, final EditText city) {

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference addressDisplayMap = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("address");

        addressDisplayMap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String houseNoDisplay = dataSnapshot.child("houseNo").getValue().toString();
                    String streetDisplay = dataSnapshot.child("street").getValue().toString();
                    String houseAreaDisplay = dataSnapshot.child("houseArea").getValue().toString();
                    String cityDisplay = dataSnapshot.child("city").getValue().toString();
                    /*String postcodeDisplay = dataSnapshot.child("postcode").getValue().toString();
                    String stateDisplay = dataSnapshot.child("state").getValue().toString();*/

                    houseNo.setText(houseNoDisplay);
                    street.setText(streetDisplay);
                    houseArea.setText(houseAreaDisplay);
                    city.setText(cityDisplay);
                    /*postcode.setText(postcodeDisplay);
                    state.setText(stateDisplay);*/

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddressActivity.this, ProfileMainActivity.class);
        startActivity(intent);
    }
}
