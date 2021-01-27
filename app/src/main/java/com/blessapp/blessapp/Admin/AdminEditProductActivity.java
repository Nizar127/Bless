package com.blessapp.blessapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.blessapp.blessapp.Model.Product;
import com.blessapp.blessapp.R;
import com.blessapp.blessapp.ViewHolder.EditProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminEditProductActivity extends AppCompatActivity {


    private RecyclerView editProductsList;
    private DatabaseReference productsRef;
    private AutoCompleteTextView inputTextEditProd;
    private RecyclerView.LayoutManager layoutManager;

    private String searchInputEdit = "";
    private FloatingActionButton editSearchBtn;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        inputTextEditProd = findViewById(R.id.edit_search_input);
        editSearchBtn = findViewById(R.id.edit_search_products);

        editProductsList = findViewById(R.id.edit_products_list);
        layoutManager = new GridLayoutManager(this, 2);
        editProductsList.setLayoutManager(layoutManager);

        backBtn = findViewById(R.id.back_btn_itemedit);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    //Get the suggestion by childing the key of the string you want to get.
                    String suggestion = suggestionSnapshot.child("nameLower").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        inputTextEditProd.setAdapter(autoComplete);
        //AutoCompleteTextView Adapter ends

        //For when we click on the suggested item, it actually go search right away
        inputTextEditProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchInputEdit = inputTextEditProd.getText().toString();
                //Call back so that recyclerview can be refreshed
                onStart();
            }
        });

        editSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInputEdit = inputTextEditProd.getText().toString();
                //Call back so that recyclerview can be refreshed
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(productsRef.orderByChild("nameLower").startAt(searchInputEdit.toLowerCase()).endAt(searchInputEdit.toLowerCase() + "\uf8ff"), Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, EditProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, EditProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EditProductsViewHolder holder, final int position, @NonNull Product product) {
                        Picasso.get().load(product.getImage()).into(holder.editImgView);
                        holder.editProdName.setText(product.getName());
                        holder.editProdDesc.setText(product.getDescription());
                        holder.editProdPrice.setText(product.getPrice());

                        holder.editProd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //String uID = getRef(position).getKey();
                                String productID =  getRef(position).getKey();
                                editProductGo(productID);
                                finish();
                            }
                        });

                        holder.removeProd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //We want to display what we actually click, so:
                                //getRef(position) = to use position as reference to pass to another activity
                                //getKey() = to get key from database, so that the next activity will display data accurately instead of mixing with other user's order
                                //String uID = getRef(position).getKey();
                                String productID =  getRef(position).getKey();
                                removeProductGo(productID);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EditProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_items_layout, parent,  false);
                        EditProductsViewHolder holder = new EditProductsViewHolder(view);
                        return holder;
                    }
                };

    }


    private void editProductGo(String productID) {
        Intent intent = new Intent(AdminEditProductActivity.this, AdminEditProductFormActivity.class);
        intent.putExtra("Product ID", productID);
        startActivity(intent);
    }

    private void removeProductGo(final String productID) {
        CharSequence options[] = new CharSequence[]{
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditProductActivity.this);
        builder.setTitle("Are you sure to remove this product?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //If admins press Yes
                if (which == 0){
                    //We want to display what we actually click, so:
                    //getRef(position) = to use position as reference to pass to another activity
                    //getKey() = to get key from database, so that the next activity will display data accurately instead of mixing with other user's order
                    productsRef.child(productID).removeValue();
                }
                //else admins pressed No
                else {
                }
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminEditProductActivity.this, AdminProductManageActivity.class);
        startActivity(intent);
    }
}



//this code is additional code for edit product data
//but we only do work on add item only to cut time