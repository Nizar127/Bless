package com.blessapp.blessapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Interface.ItemClickListener;
import com.blessapp.blessapp.Model.Favourite;
import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class ProductPageAdapter extends FirebaseRecyclerAdapter<Product, ProductPageAdapter.productViewHolder> {
  //Context mcon;

    Boolean fvrtChecker = false;
    public DatabaseReference favouriteRef, fvrt_listRef;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserid = user.getUid();

    Favourite favourite;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductPageAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {


        super(options);

        favourite = new Favourite();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ProductPageAdapter.productViewHolder holder, final int position, @NonNull final Product model) {
        final  String postkey = getRef(position).getKey();

        holder.itemName.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.itemImg);
        holder.itemPrice.setText("RM " + model.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mcon.startActivity(new Intent(mcon, ProductDetailsActivity.class))

                String getPID = model.getPid();

                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("pid", getPID);
                view.getContext().startActivity(intent);
            }
        });

//<<<<<<< updated_20210126

        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fvrtChecker = true;

                //String name =  username.getText().toString().trim();
                final String name = holder.itemName.getText().toString().trim();
                final String price = holder.itemPrice.getText().toString().trim();
                // mir code nie nk add data by time
                // final String time =
                //nie image
               // final String image = Picasso.get().load(model.getImage()).into(holder.itemImg);


                holder.favouriteChecker(postkey);
                favouriteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (fvrtChecker.equals(true)){
                            if (snapshot.child(postkey).hasChild(currentUserid)){
                                favouriteRef.child(postkey).child(currentUserid).removeValue();
                                delete(postkey);
                                Toast.makeText(view.getContext(), "Removed from favourite", Toast.LENGTH_SHORT).show();
                                fvrtChecker = false;
                            }else {

                                favouriteRef = db.getReference("Favourites").child(currentUserid);
//                                favouriteRef.setValue(true);
//                                favourite.setFav_title(name);
//                                favourite.setFav_price(price);
                                favourite.setFav_id(postkey);
                                //  String id = fvrt_listRef.push().getKey();
                                favouriteRef.setValue(favourite);
                                fvrtChecker = false;

                                Toast.makeText(view.getContext(), "Added to favourite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
///=======
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("pid", model.getPid());
                view.getContext().startActivity(intent);
//>>>>>>> master
            }
        });
    }

    private void addToCartList() {


    }

    @NonNull
    @Override
    public ProductPageAdapter.productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        return new ProductPageAdapter.productViewHolder(view);
    }

   public class productViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImg, favBtn, favImg, cartBtn,favDeleteBtn;
        TextView itemName, itemPrice, favName, favPrice;

        public ItemClickListener listener;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);

            favouriteRef = db.getReference("Favourites").child(currentUserid);
            itemName = itemView.findViewById(R.id.productitem_name);
            itemPrice = itemView.findViewById(R.id.productitem_price);
            itemImg = itemView.findViewById(R.id.productitem_image);
            //cartBtn = itemView.findViewById(R.id.add_to_cart_order_btn);
            favBtn = itemView.findViewById(R.id.favourite_btn);


        }

       public void favouriteChecker(final String postkey){
           favBtn = itemView.findViewById(R.id.favourite_btn);

           favouriteRef = db.getReference("favourites");
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           final String uid = user.getUid();

           favouriteRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.child(postkey).hasChild(uid)){
                       favBtn.setImageResource(R.drawable.favourite_border);
                   }else {
                       favBtn.setImageResource(R.drawable.favourite_red);
                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
    }

    void delete(String time){

        Query query = fvrt_listRef.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    //Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
