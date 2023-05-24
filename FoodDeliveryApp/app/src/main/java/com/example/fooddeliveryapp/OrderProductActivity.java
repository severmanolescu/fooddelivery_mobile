package com.example.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class OrderProductActivity extends AppCompatActivity {

    private ImageView backImageView, productImage;
    private TextView productNameTextView, priceTextView;
    private EditText userText, addressText, phoneText;
    private Button orderButton;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    private String productName, productPrice, image, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        backImageView = findViewById(R.id.backImageView2);
        productImage = findViewById(R.id.productImageViewOrder);
        productNameTextView = findViewById(R.id.yourorders);
        priceTextView = findViewById(R.id.textViewPriceOrder);
        userText = findViewById(R.id.userOrderText);
        addressText = findViewById(R.id.addressOrderText);
        phoneText = findViewById(R.id.phoneOrderText);
        orderButton = findViewById(R.id.orderButton);

        Intent intent = getIntent();
        productName = intent.getStringExtra("productName");
        productPrice = intent.getStringExtra("productPrice");
        image = intent.getStringExtra("productImage");

        getUsername();
        productNameTextView.setText(productName);
        priceTextView.setText(productPrice);
        Picasso.get().load(image).into(productImage);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderProductActivity.this, ProductsActivity.class);
                startActivity(intent);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //orderProductTmisioara();
                orderProductBucharest();
            }
        });
    }

    public void getUsername(){
        reference.child("Users").child(auth.getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getValue().toString();
                userText.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void orderProductTmisioara(){
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productImage = intent.getStringExtra("productImage");
        String username = userText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = df.format(c);
        OrderModel order = new OrderModel(productImage, productName, productPrice, "Order Placed",username, address, phone, date);
        if(!address.equals("") && !phone.equals("")){
            //UUID id = UUID.randomUUID();
            //String orderID = id.toString();
            Random random = new Random();
            int randomNuber = random.nextInt(20);
            String orderNumber = String.valueOf(randomNuber);
            // writing order so the restaurant can see it
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("image").setValue(order.getProductImage());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("name").setValue(order.getProductName());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("price").setValue(order.getProductPrice());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("deliveryStatus").setValue(order.getDeliveryStatus());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("username").setValue(order.getUsername());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("address").setValue(order.getAddress());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("phone").setValue(order.getPhone());
            reference.child("RestaurantsTimisoara").child("Mc Donalt's").child("Orders").child(orderNumber).child("date").setValue(order.getDate());
            //writing order in the users profile
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("image").setValue(order.getProductImage());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("name").setValue(order.getProductName());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("price").setValue(order.getProductPrice());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("deliveryStatus").setValue(order.getDeliveryStatus());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("address").setValue(order.getAddress());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("phone").setValue(order.getPhone());
            Toast.makeText(this, "Product Ordered!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Cannot make the order!", Toast.LENGTH_SHORT).show();
        }
    }

    public void orderProductBucharest(){
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productImage = intent.getStringExtra("productImage");
        String username = userText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = df.format(c);
        OrderModel order = new OrderModel(productImage, productName, productPrice, "Order Placed",username, address, phone, date);
        if(!address.equals("") && !phone.equals("")){
            //UUID id = UUID.randomUUID();
            //String orderID = id.toString();
            Random random = new Random();
            int randomNuber = random.nextInt(20);
            String orderNumber = String.valueOf(randomNuber);
            // writing order so the restaurant can see it
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("image").setValue(order.getProductImage());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("name").setValue(order.getProductName());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("price").setValue(order.getProductPrice());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("deliveryStatus").setValue(order.getDeliveryStatus());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("username").setValue(order.getUsername());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("address").setValue(order.getAddress());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("phone").setValue(order.getPhone());
            reference.child("RestaurantsBucuresti").child("Again Burger").child("Orders").child(orderNumber).child("date").setValue(order.getDate());
            //writing order in the users profile
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("image").setValue(order.getProductImage());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("name").setValue(order.getProductName());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("price").setValue(order.getProductPrice());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("deliveryStatus").setValue(order.getDeliveryStatus());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("address").setValue(order.getAddress());
            reference.child("Users").child(auth.getUid()).child("Orders").child(orderNumber).child("phone").setValue(order.getPhone());
            Toast.makeText(this, "Product Ordered!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Cannot make the order!", Toast.LENGTH_SHORT).show();
        }
    }

}
