package com.example.shop_management.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_management.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final CartManager cartManager;

    public CartAdapter(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartManager.getCartItems().get(position);

        holder.productName.setText(item.getProductName());
        holder.productPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.productQuantity.setText(String.format("Quantity: %d", item.getQuantity()));
        holder.productImage.setImageResource(item.getImageResource());

        holder.deleteButton.setOnClickListener(v -> {
            cartManager.removeProduct(item.getProductId());
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartManager.getCartItems().size());
        });
    }

    @Override
    public int getItemCount() {
        return cartManager.getCartItems().size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        Button deleteButton;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cart_product_name);
            productPrice = itemView.findViewById(R.id.cart_product_price);
            productQuantity = itemView.findViewById(R.id.cart_product_quantity);
            deleteButton = itemView.findViewById(R.id.cart_delete_button);
            productImage = itemView.findViewById(R.id.cart_image);
        }
    }
}


