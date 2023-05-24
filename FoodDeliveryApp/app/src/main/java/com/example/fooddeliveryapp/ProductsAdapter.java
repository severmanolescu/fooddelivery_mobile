package com.example.fooddeliveryapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<String> productList = new ArrayList<>();
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    private double timisoaraLatitude = 45.7489;
    private double timisoaraLongitude = 21.2087;

    private double bucurestiLatitude = 44.4268;
    private double bucurestiLongitude = 26.1025;

    public ProductsAdapter(ArrayList<String> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // getAddress(context, timisoaraLatitude, timisoaraLongitude, holder, position); //timisoara
        getAddress(context, bucurestiLatitude, bucurestiLongitude, holder, position); // bucuresti
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productNameText);
            productPrice = itemView.findViewById(R.id.productPriceText);
            cardView = itemView.findViewById(R.id.productCardView);


        }
    }

    public void showTimisoaraProducts(ViewHolder holder, int position){
        reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Products").child(productList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("image").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String price = snapshot.child("price").getValue().toString();

                holder.productName.setText(name);
                holder.productPrice.setText(price);
                Picasso.get().load(image).into(holder.productImage);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderProductActivity.class);
                        intent.putExtra("productName", name);
                        intent.putExtra("productPrice", price);
                        intent.putExtra("productImage", image);
                        context.startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showBucharestProducts(ViewHolder holder, int position){
        reference.child("RestaurantsBucuresti").child("Again Burger").child("Products").child(productList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("image").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String price = snapshot.child("price").getValue().toString();

                holder.productName.setText(name);
                holder.productPrice.setText(price);
                Picasso.get().load(image).into(holder.productImage);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderProductActivity.class);
                        intent.putExtra("productName", name);
                        intent.putExtra("productPrice", price);
                        intent.putExtra("productImage", image);
                        context.startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAddress(Context context, double LATITUDE, double LONGITUDE, ViewHolder holder, int position) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                if(city.equals("Timișoara")){
                    showTimisoaraProducts(holder, position);
                }
                else if(city.equals("Bucharest")){
                    showBucharestProducts(holder, position);
                }

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


}
