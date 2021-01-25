package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.FocusFinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    Button signUpButton;
    EditText emailID, pass, phoneNum, birth, uname;
    String phonenum, birthdate, email, image, name;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loadingBar = new ProgressDialog(this);

        signUpButton = findViewById(R.id.register_btn);
        emailID = findViewById(R.id.register_username_input);
        uname = findViewById(R.id.register_uname_input);
        pass  = findViewById(R.id.register_password_input);
        phoneNum = findViewById(R.id.register_phone_input);
        birth = findViewById(R.id.register_birthdate_input);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNewUser();
            }
        });
    }

    private void RegisterNewUser() {
        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);
        CreateAccount();
    }

    private void CreateAccount() {
        //uid = FirebaseDatabase.getInstance().getReference().getKey();
        final String email = emailID.getText().toString().trim();
        final String username = uname.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String phone = phoneNum.getText().toString().trim();
        final String birthdate = birth.getText().toString().trim();
        int passLength = password.length();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(birthdate)){
            Toast.makeText(this, "Please enter your birth date.", Toast.LENGTH_SHORT).show();
        } else {

            mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Email authentication successful, proceeding to create profile...",
                                    Toast.LENGTH_LONG)
                                    .show();

                            loadingBar.setTitle("Create Account");
                            loadingBar.setMessage("Checking the credentials...");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();

                            ValidatePhoneNumber(mAuth.getCurrentUser().getUid(), email, username, phone, birthdate, password);
                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!" + " Please try again later. Error : " + task.getException().toString(),
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        }

    }

    private void ValidatePhoneNumber(final String uid, final String email, final String username, final String phone, final String birthdate, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(uid).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();

                    userdataMap.put("userID", uid);
                    userdataMap.put("email", email);
                    userdataMap.put("fullname", username);
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("birthdate",birthdate);

                    //.child("Users") - create new child/table in db with "Users" as name
                    //.child(name) - create a child under Users child/table in db with named after user's name number
                    rootRef.child("Users").child(uid).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Account Created!" + name, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }

                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SignupActivity.this, "We're sorry, Something happen, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignupActivity.this, "This username already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignupActivity.this, "Please use other username.", Toast.LENGTH_SHORT).show();

                    /*
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

  /*  private void RegisterNewUser() {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        //String email, password;
        final String email = emailID.getText().toString();
        final String password = pass.getText().toString();
        final String username = uname.getText().toString();
        final String phone =  phoneNum.getText().toString();
        final String birthdate = birth.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(birthdate)){
            Toast.makeText(this, "Please enter your birth date.", Toast.LENGTH_SHORT).show();
        }


        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }

        Query queryUsername = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username);
        queryUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                Toast.makeText(SignupActivity.this,"Choose different username", Toast.LENGTH_SHORT).show();
            } else{
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this,  new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"Sign up error", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String user_id = mAuth.getCurrentUser().getUid();
                            final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            final String userEmail = emailID.getText().toString();
                            //final String userPass = pass.getText().toString();
                            final String userwithName = uname.getText().toString();
                            final String phone = phoneNum.getText().toString();
                            final String birthday = birth.getText().toString();

                            Map userData = new HashMap();
                            userData.put("email", userEmail);
                            userData.put("username", userwithName);
                            userData.put("phone", phone);
                            userData.put("birthday",birthday);

                            current_user_db.setValue(userData);

                            loadingBar.dismiss();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);



                        }
                    }
                });
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        // Validations for input email and password
/*        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    //CreateAccount(mAuth.getCurrentUser().getUid());

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    // if the user created intent to login activity


                   *//* Intent intent
                            = new Intent(SignupActivity.this,
                            ProductPageActivity.class);
                    startActivity(intent);*//*
                }
                else {

                    // Registration failed
                    Toast.makeText(
                            getApplicationContext(),
                            "Registration failed!!"
                                    + " Please try again later",
                            Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/

   // }

/*    private void CreateAccount(String uid) {

        //String name =  username.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String phone = phoneNum.getText().toString().trim();
        String birthdate = birth.getText().toString().trim();
        //String birthdate =
        int passLength = password.length();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(birthdate)){
            Toast.makeText(this, "Please enter your birth date.", Toast.LENGTH_SHORT).show();
        }
        

        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(uid, name, phone, birthdate, password);
        }

    }

    private void ValidatePhoneNumber(final String uid, final String name, final String phone, final String birthdate, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(uid).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();

                    userdataMap.put("uid", uid);
                    userdataMap.put("email", name);
                    userdataMap.put("fullname", name);
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("birthdate",birthdate);

                    //.child("Users") - create new child/table in db with "Users" as name
                    //.child(name) - create a child under Users child/table in db with named after user's name number
                    rootRef.child("Users").child(uid).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Account Created!" + name, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }

                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SignupActivity.this, "We're sorry, Something happen, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignupActivity.this, "This username already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignupActivity.this, "Please use other username.", Toast.LENGTH_SHORT).show();

                    *//*
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    *//*
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
