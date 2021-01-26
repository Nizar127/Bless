package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.blessapp.Model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileMainActivity extends AppCompatActivity {


    Toolbar toolbar;
    ImageView backbtnarrow, cartID;
    TextView username;
    Button editBtn;
    CircleImageView userImg;
    LinearLayout userprofile, wishlist, orderlist, logoutlayout;
    String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = db.getReference("Users");

        toolbar = findViewById(R.id.toolbar_profilepage);
        backbtnarrow = findViewById(R.id.back_btn_profileArrow);
        logoutlayout = findViewById(R.id.userprofilelogout);
        backbtnarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        username =findViewById(R.id.userprofileName);
        userprofile = findViewById(R.id.userProfileLol);
        wishlist = findViewById(R.id.userprofilelfav);
        orderlist = findViewById(R.id.userprofileorder);
        cartID = findViewById(R.id.cart_id);
        userImg = findViewById(R.id.userProfileImg);
/*        emailInput = findViewById(R.id.emailName);
        phoneNumber = findViewById(R.id.phoneNum);
        birthdate = findViewById(R.id.birthdate);
        editBtn = findViewById(R.id.editProfileBtn);*/
        //address = findViewById(R.id.addressname);

        cartID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartActivity();
            }
        });

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userprofileitem();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favlist();
            }
        });

        orderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myorder();
            }
        });

        logoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });



//        editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editProfile();
//            }
//        });

        //userInfoDisplay(currentUserid);

        mRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname = String.valueOf(dataSnapshot.getValue());
                String img = String.valueOf(dataSnapshot.getValue());
                //Picasso.get(dataSnapshot.img).load(userImg)
                username.setText(uname);
                if (dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);

                    String getUsername = users.getFullname();
                    String userImage = users.getImage();

                    username.setText(getUsername);
                    Picasso.get().load(userImage).into(userImg);
                    //userImg.se
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void logout() {

        CharSequence options[] = new CharSequence[]{
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
        builder.setTitle("Log Out??");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //If admins press Yes
                if (which == 0) {
                    Toast.makeText(ProfileMainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    finish();

                    //unchecked the remember box
/*                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    finish();*/

                    Intent intent = new Intent(ProfileMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        });
        builder.show();


        //startActivity(new Intent(this, LoginActivity.class));
    }




    private void myorder() {
        Intent intent = new Intent(ProfileMainActivity.this, OrderActivity.class);
        startActivity(intent);
    }

    private void favlist() {
        Intent intent = new Intent(ProfileMainActivity.this, FavouriteMainActivity.class);
        startActivity(intent);
    }

    private void userprofileitem() {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileUserInfoActivity.class);
        startActivity(intent);
    }

    private void cartActivity() {
        Intent intent = new Intent(ProfileMainActivity.this, CartActivity.class);
        startActivity(intent);
    }

/*    private void userInfoDisplay(String currentUserid) {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Users user = dataSnapshot.getValue(Users.class);

                    Picasso.get().load(user.getImage()).into(userImg);
                    username.setText(user.getUsername());
                    phoneNumber.setText(user.getPhone());
                    emailInput.setText(user.getEmail());
                    birthdate.setText(user.getBirthdate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

/*    private void editProfile() {
        Intent intent = new Intent(ProfileMainActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileMainActivity.this, ProductPageActivity.class);
        startActivity(intent);
    }
}
