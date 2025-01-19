package com.example.shop_management.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_management.classes.CartItem;
import com.example.shop_management.classes.CartManager;
import com.example.shop_management.fragments.shoppingFragment;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.shop_management.R;
import com.example.shop_management.fragments.logInFragment;
import com.example.shop_management.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartManager.CartActionListener {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        CartManager cartManager = CartManager.getInstance();
        cartManager.setCartActionListener(this);
    }

    public void reg() {

        String email = ((EditText) findViewById(R.id.editTextEmail1)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword1)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "reg ok", Toast.LENGTH_SHORT).show();
                        }
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(MainActivity.this, "Email already in use. Try logging in.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "reg failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void login(String email, String password, OnLoginResultListener listener) {

        if (email.equals("") ||  password.equals("")){
            listener.onFailure();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                });
    }

    public interface OnLoginResultListener {
        void onSuccess();

        void onFailure();
    }

    public void addDATA(String email, String name, String phone) {

        if (email.isEmpty() || phone.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String sanitizedEmail = email.replace(".", ",");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(sanitizedEmail);

        User u = new User(email, phone, name);

        myRef.setValue(u).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getName(OnGetNameListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();

        if (!email.isEmpty()) {

            String sanitizedEmail = email.replace(".", ",");

            usersRef.child(sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        listener.onNameReceived(userName);
                    } else {
                        Log.d("Firebase", "User not found.");
                        listener.onNameReceived("Guest");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching user data", databaseError.toException());
                    listener.onNameReceived("Guest");
                }
            });
        } else {
            Log.d("Firebase", "No email found in SharedPreferences.");
            listener.onNameReceived("Guest");
        }
    }

    @Override
    public void onCartUpdatedDB(String email) {
        String sanitizedEmail = email.replace(".", ",");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("users").child(sanitizedEmail).child("CartItems");

        List<CartItem> itemsToUpload = new ArrayList<>(CartManager.getInstance().getCartItems());
        cartRef.setValue(itemsToUpload).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Cart updated successfully!", Toast.LENGTH_SHORT).show();
                Log.d("CartUpdate", "Cart items being uploaded: " + itemsToUpload);
            } else {
                Toast.makeText(MainActivity.this, "Failed to update cart: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void loadCart(String email, OnCartLoadedListener listener) {
        String sanitizedEmail = email.replace(".", ",");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(sanitizedEmail).child("CartItems");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartItem> cartItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItems.add(cartItem);
                    }
                }
                listener.onCartLoaded(cartItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCartLoaded(new ArrayList<>());
            }
        });
    }

    public interface OnCartLoadedListener {
        void onCartLoaded(List<CartItem> cartItems);
    }




    public interface OnGetNameListener {
        void onNameReceived(String userName);
    }
}

