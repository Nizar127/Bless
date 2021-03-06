package com.blessapp.blessapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blessapp.blessapp.Model.Orders;
import com.blessapp.blessapp.R;
import com.blessapp.blessapp.ViewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    //To round up to 2 dec place
    DecimalFormat df = new DecimalFormat("#.00");
    private float totalPrice;
    private String userNameAsKey;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orderadminRecycler);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.back_btn_adminneworderArrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersRef, Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position, @NonNull Orders model) {
                        //To make sure they display the decimal places
                        totalPrice = Float.valueOf(model.getTotalAmount());
                        userNameAsKey = model.getUsername();

                        holder.name.setText("Name : " + model.getName());
                        holder.phone.setText("Phone Number : " + model.getPhone());
                        holder.totalPrice.setText("Total : RM " + df.format(totalPrice));
                        holder.dateTime.setText("Date & Time Ordered : " + model.getDate() + ", " + model.getTime());

                        holder.details.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID =  getRef(position).getKey();

                                Intent intent= new Intent(AdminNewOrdersActivity.this, AdminOrderDetailsActivity.class);
                                intent.putExtra("Order Primary Key", uID);
                                startActivity(intent);
                            }
                        });

                        holder.delivery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Yes", "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Packed and ready to be deliver?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0){
                                            //We want to display what we actually click, so:
                                            //getRef(position) = to use position as reference to pass to another activity
                                            //getKey() = to get key from database, so that the next activity will display data accurately instead of mixing with other user's order
                                            String uID =  getRef(position).getKey();

                                            RemoveOrder(uID, userNameAsKey);
                                        }
                                        //else admins pressed No
                                        else {
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        return new OrdersViewHolder(view);
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID, String userNameAsKey) {
    }
}
