package com.example.shop_management.classes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_management.R;
import com.example.shop_management.activities.MainActivity;
import com.example.shop_management.models.CurrentUser;

public class CartDialogFragment extends DialogFragment implements CartManager.OnCartUpdatedListener {

    private CartManager cartManager;


    public static CartDialogFragment newInstance(CartManager cartManager) {
        CartDialogFragment fragment = new CartDialogFragment();
        fragment.cartManager = cartManager;
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {

                WindowManager.LayoutParams params = window.getAttributes();
                params.width = 1300;
                params.height = 2000;
                window.setAttributes(params);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_dialog, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CartAdapter adapter = new CartAdapter(cartManager);
        recyclerView.setAdapter(adapter);

        cartManager.setOnCartUpdatedListener(this);

        view.findViewById(R.id.close_cart_button).setOnClickListener(v -> dismiss());

        TextView totalPriceTextView = view.findViewById(R.id.total_price);
        updateTotalPrice(totalPriceTextView);


        return view;
    }
    @Override
    public void onCartUpdated() {
        View view = getView();
        if (view != null) {
            TextView totalPriceTextView = view.findViewById(R.id.total_price);
            updateTotalPrice(totalPriceTextView);
            RecyclerView recyclerView = view.findViewById(R.id.cart_recycler_view);
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private void updateTotalPrice(TextView totalPriceTextView) {
        double totalPrice = cartManager.getTotalPrice();
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }
}


