package com.example.shop_management.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_management.R;
import com.example.shop_management.activities.MainActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productImage.setImageResource(product.getImageResource());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));


        LinearLayout productItemLayout = holder.itemView.findViewById(R.id.LinearLayoutItem);

        holder.buttonMinus.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 0) {
                product.setQuantity(quantity - 1);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                updateBackground(product, productItemLayout);
            }
        });

        holder.buttonPlus.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            product.setQuantity(quantity + 1);
            holder.productQuantity.setText(String.valueOf(product.getQuantity()));
            updateBackground(product, productItemLayout);
        });

        holder.buttonAddToCart.setOnClickListener(v -> {
            CartManager cartManager = CartManager.getInstance();

            if (product.getQuantity() > 0) {
                CartItem cartItem = new CartItem(
                        product.getId(),
                        product.getName(),
                        Double.parseDouble(product.getPrice().replaceAll("[^0-9.]", "")),
                        product.getQuantity(),
                        product.getImageResource()
                );

                cartManager.addProduct(cartItem);
                product.setQuantity(0);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                updateBackground(product, productItemLayout);
                Toast.makeText(holder.itemView.getContext(), product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(holder.itemView.getContext(), "Cannot add 0 quantity to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        updateBackground(product, productItemLayout);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    private void updateBackground(Product product, LinearLayout layout) {
        if (product.getQuantity() > 0) {
            layout.setBackgroundResource(R.drawable.highlighted_background);
        } else {
            layout.setBackgroundResource(R.drawable.default_background);
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button buttonMinus, buttonPlus, buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productQuantity = itemView.findViewById(R.id.text_quantity);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
            buttonAddToCart = itemView.findViewById(R.id.button_add_to_cart);
        }
    }
}



