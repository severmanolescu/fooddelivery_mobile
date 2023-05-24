package com.example.fooddeliveryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<String> orderList = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public OrderAdapter(ArrayList<String> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        reference.child("Users").child(auth.getUid()).child("Orders").child(orderList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("image").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String deliveryStatus = snapshot.child("deliveryStatus").getValue().toString();

                Picasso.get().load(image).into(holder.image);
                holder.name.setText(name);
                holder.price.setText(price);
                holder.address.setText(address);
                holder.phone.setText(phone);
                holder.deliveryStatus.setText(deliveryStatus);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name, price, address, phone, deliveryStatus;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.burgerImage);
            name = itemView.findViewById(R.id.nameTextViewOrder);
            price = itemView.findViewById(R.id.priceTextViewOrder);
            address = itemView.findViewById(R.id.addressTextViewOrder);
            phone = itemView.findViewById(R.id.phoneTextViewOrder);
            deliveryStatus = itemView.findViewById(R.id.orderStatusTextView);
            cardView = itemView.findViewById(R.id.orderCard);


        }
    }


}
