package com.blessapp.blessapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Interface.ItemClickListener;
import com.blessapp.blessapp.Model.Favourite;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FavouriteAdapter extends FirebaseRecyclerAdapter<Favourite, FavouriteAdapter.FavouriteViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavouriteAdapter(@NonNull FirebaseRecyclerOptions<Favourite> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FavouriteViewHolder holder, final int position, @NonNull final Favourite model) {
        holder.favName.setText(model.getFav_title());
        holder.favPrice.setText(model.getFav_price());
        Picasso.get().load(model.getFav_img()).into(holder.favImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("pid", model.getPid());
                view.getContext().startActivity(intent);
            }
        });

        holder.favDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("pid", model.getPid());
                view.getContext().startActivity(intent);*/
                notifyItemRemoved(position);
            }
        });

    }

    @NonNull
    @Override
    public  FavouriteAdapter.FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouriteAdapter.FavouriteViewHolder(view);
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{

        ImageView favBtn, favImg, favDeleteBtn;
        TextView  favName, favPrice;
        DatabaseReference favouriteRef;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        public ItemClickListener listener;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favName = itemView.findViewById(R.id.favitem_name);
            favPrice = itemView.findViewById(R.id.favitem_price);
            favImg = itemView.findViewById(R.id.favitem_image);
            favDeleteBtn = itemView.findViewById(R.id.deletefavourite_btn);
        }
    }
}
