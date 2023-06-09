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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private ArrayList<String> restaurantList = new ArrayList<>();
    private Context context;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    private double timisoaraLatitude = 45.7489;
    private double timisoaraLongitude = 21.2087;

    private double bucurestiLatitude = 44.4268;
    private double bucurestiLongitude = 26.1025;

    public RestaurantAdapter(ArrayList<String> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // getAddress(context, timisoaraLatitude, timisoaraLongitude, holder, position); //timisoara
        getAddress(context, bucurestiLatitude, bucurestiLongitude, holder, position); //bucuresti
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView restaurantLogo;
        private TextView restaurantName;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantLogo = itemView.findViewById(R.id.logoImageView);
            restaurantName = itemView.findViewById(R.id.restaurantNameTextView);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

    public void showTimisoaraRestaurants(ViewHolder holder, int position){
        reference.child("RestaurantsTimisoara").child(restaurantList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String logo = snapshot.child("Logo").getValue().toString();
                String name = snapshot.child("Name").getValue().toString();

                holder.restaurantName.setText(name);
                Picasso.get().load(logo).into(holder.restaurantLogo);

                Intent intent = new Intent(context, ProductsActivity.class);
                intent.putExtra("AdapterPosition", restaurantList.get(position));

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProductsActivity.class);
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showBucharestRestaurants(ViewHolder holder, int position){
        reference.child("RestaurantsBucuresti").child(restaurantList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String logo = snapshot.child("Logo").getValue().toString();
                String name = snapshot.child("Name").getValue().toString();

                holder.restaurantName.setText(name);
                Picasso.get().load(logo).into(holder.restaurantLogo);

                Intent intent = new Intent(context, ProductsActivity.class);
                intent.putExtra("AdapterPosition", restaurantList.get(position));

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProductsActivity.class);
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
                    showTimisoaraRestaurants(holder, position);
                }
                else if(city.equals("Bucharest")){
                    showBucharestRestaurants(holder, position);
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
