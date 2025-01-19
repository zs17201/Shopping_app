package com.example.shop_management.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_management.R;
import com.example.shop_management.classes.CartDialogFragment;
import com.example.shop_management.classes.CartItem;
import com.example.shop_management.classes.CartManager;
import com.example.shop_management.classes.Product;
import com.example.shop_management.classes.ProductAdapter;
import com.example.shop_management.classes.myData;
import com.example.shop_management.models.CurrentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link shoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shoppingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;

    private SearchView searchView;
    private TextView userName;
    private Button cartButton;


    public shoppingFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment shoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static shoppingFragment newInstance(String param1, String param2) {
        shoppingFragment fragment = new shoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        userName = view.findViewById(R.id.user_name);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartButton = view.findViewById(R.id.cart_button);
        Button btnLogout = view.findViewById(R.id.btnFragmentLogout);

        CurrentUser curr_user = CurrentUser.getInstance("","");
        String name = curr_user.getName();
        userName.setText("Welcome, " + name);


        resetList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    productAdapter.updateList(productList);
                } else {
                    filterProducts(newText);
                }
                return true;
            }
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Log Out", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_shoppingFragment_to_logInFragment);

        });


        cartButton.setOnClickListener(v -> {
            CartManager cartManager = CartManager.getInstance();
            CartDialogFragment cartDialogFragment = CartDialogFragment.newInstance(cartManager);
            cartDialogFragment.show(getParentFragmentManager(), "CartDialog");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //CartManager cartManager = CartManager.getInstance();
        resetList();
        //cartManager.clearCart();
        productAdapter.notifyDataSetChanged();
    }


    private void resetList(){

        productList = new ArrayList<>();

        for (int i = 0; i < myData.nameArray.length; i++) {
            Product product = new Product(
                    myData.nameArray[i],
                    myData.prices[i],
                    myData.drawableArray[i],
                    myData.id_[i]
            );
            productList.add(product);
        }

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }


    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }
}