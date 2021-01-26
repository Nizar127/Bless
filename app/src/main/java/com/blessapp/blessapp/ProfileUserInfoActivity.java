package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blessapp.blessapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserInfoActivity extends AppCompatActivity {

    Toolbar toolbarprofileuser;
    ImageView cartbtn, backbtn;
    CircleImageView userImage;
    TextView userprofilename, userprofilephone, userprofilebirthdate, userprofileEmail;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = db.getReference("Users");

        backbtn = findViewById(R.id.back_btn_editprofileArrow);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbarprofileuser = findViewById(R.id.toolbar_editprofilepage);
        cartbtn = findViewById(R.id.cart_id);
        userImage = findViewById(R.id.user_profile_image);
        userprofilename = findViewById(R.id.profile_full_name);
        userprofileEmail = findViewById(R.id.profile_email);
        userprofilephone = findViewById(R.id.profile_phone_number);
        userprofilebirthdate = findViewById(R.id.profile_birthdate);

        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartActivity();
            }
        });

        mRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname = String.valueOf(dataSnapshot.getValue());
                String uphone = String.valueOf(dataSnapshot.getValue());
                String uemail = String.valueOf(dataSnapshot.getValue());
                String ubirth = String.valueOf(dataSnapshot.getValue());
                String img = String.valueOf(dataSnapshot.getValue());

                //Picasso.get(dataSnapshot.img).load(userImg)
               userprofilename.setText(uname);
               userprofileEmail.setText(uemail);
               userprofilebirthdate.setText(ubirth);
               userprofilephone.setText(uphone);
                if(dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);

                    String getUsername = users.getFullname();
                    String userprofileImage = users.getImage();
                    String getuserPhone = users.getPhone();
                    String getuserBirthdate = users.getBirthdate();
                    String getuserEmail = users.getEmail();

                    userprofilename.setText(getUsername);
                    Picasso.get().load(userprofileImage).into(userImage);
                    userprofilephone.setText(getuserPhone);
                    userprofilebirthdate.setText(getuserBirthdate);
                    userprofileEmail.setText(getuserEmail);
                    //userImg.se
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void cartActivity() {

        Intent intent = new Intent(ProfileUserInfoActivity.this, CartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileUserInfoActivity.this, ProfileMainActivity.class);
        startActivity(intent);
    }
}
