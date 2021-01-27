package com.blessapp.blessapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blessapp.blessapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewItemActivity extends AppCompatActivity {

    Button addNewProdBtn;
    ImageView backbtn, productImg;
    String categoryName, desc, price, name, saveCurrentDate, saveCurrentTime, productRandomKey, downloadImgUrl;
    EditText inputProdName, inputProdDesc, inputProdPrice;
    Uri imgURL;
    StorageReference prodImgRef;
    DatabaseReference prodRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_item);


        prodImgRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        prodRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProdBtn = findViewById(R.id.add_new_product);

        productImg = findViewById(R.id.select_product_image);
        backbtn = findViewById(R.id.back_btn_itemadd);
        inputProdName = findViewById(R.id.product_name);
        inputProdDesc = findViewById(R.id.product_desc);
        inputProdPrice = findViewById(R.id.product_price);

        loadingBar = new ProgressDialog(this);

        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OpenGallery();
                CropImage.activity(imgURL)
                        .setAspectRatio(1,1)
                        .start(AdminAddNewItemActivity.this);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });

    }

    //method to use crop image library
    //required on onactivityresult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgURL = result.getUri();

            productImg.setImageURI(imgURL);
        }
        else{
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidateProductData() {

        name = inputProdName.getText().toString().trim();
        desc = inputProdDesc.getText().toString().trim();
        price = inputProdPrice.getText().toString().trim();

        if (imgURL == null) {
            Toast.makeText(this, "Product image required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Product name required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Product description required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Product price required", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();  //<- called by method below
        }
    }

    //store product information

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;

        //upload image to database
        final StorageReference filePath = prodImgRef.child(imgURL.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imgURL);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewItemActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewItemActivity.this, "Product Image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewItemActivity.this, "Product image url received", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    //save item to database

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImgUrl);
        productMap.put("category", categoryName);
        productMap.put("name", name);
        productMap.put("nameLower", name.toLowerCase());
        productMap.put("description", desc);
        productMap.put("price", price);

        prodRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AdminAddNewItemActivity.this, AdminProductManageActivity.class);
                            startActivity(intent);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewItemActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewItemActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminAddNewItemActivity.this, AdminManageActivity.class);
        startActivity(intent);
        finish();
    }
}
