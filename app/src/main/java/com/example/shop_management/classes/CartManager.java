package com.example.shop_management.classes;


import com.example.shop_management.activities.MainActivity;
import com.example.shop_management.models.CurrentUser;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems;
    private OnCartUpdatedListener cartUpdatedListener;
    private CartActionListener actionListener;


    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void uploadCart() {
        if (actionListener != null) {
            CurrentUser currentUser = CurrentUser.getInstance("", "");
            actionListener.loadCart(currentUser.getEmail(), new MainActivity.OnCartLoadedListener() {
                @Override
                public void onCartLoaded(List<CartItem> newCartItems) {
                    cartItems.clear();
                    cartItems.addAll(newCartItems);
                    notifyCartUpdated();
                }
            });
        }
    }


    public void addProduct(CartItem cartItem) {
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(cartItem);
        }
        notifyCartUpdated();
        updateCartInDB();
    }

    public void removeProduct(String productId) {
        cartItems.removeIf(cartItem -> cartItem.getProductId().equals(productId));
        notifyCartUpdated();
        updateCartInDB();
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public void clearCart() {
        cartItems.clear();
        notifyCartUpdated();
        updateCartInDB();
    }

    public void setOnCartUpdatedListener(OnCartUpdatedListener listener) {
        this.cartUpdatedListener = listener;
    }

    public void setCartActionListener(CartActionListener listener) {
        this.actionListener = listener;
    }

    private void notifyCartUpdated() {
        if (cartUpdatedListener != null) {
            cartUpdatedListener.onCartUpdated();
        }
    }

    private void updateCartInDB() {
        if (actionListener != null) {
            CurrentUser currentUser = CurrentUser.getInstance("", "");
            actionListener.onCartUpdatedDB(currentUser.getEmail());
        }
    }

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public interface CartActionListener {
        void onCartUpdatedDB(String email);
        void loadCart(String email, MainActivity.OnCartLoadedListener onCartLoadedListener);
    }

}




