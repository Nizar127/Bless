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

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(birthdate)) {
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
                            } else {
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
        //initialize realtime database
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


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
}
