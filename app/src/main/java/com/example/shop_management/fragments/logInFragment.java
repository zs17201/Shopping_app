package com.example.shop_management.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shop_management.R;
import com.example.shop_management.activities.MainActivity;
import com.example.shop_management.classes.CartManager;
import com.example.shop_management.models.CurrentUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link logInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class logInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public logInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment logInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static logInFragment newInstance(String param1, String param2) {
        logInFragment fragment = new logInFragment();
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
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        Button btnLogin = view.findViewById(R.id.btnFragmentLogin);
        Button btnReg = view.findViewById(R.id.btnFragmentReg);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getView().findViewById(R.id.editTextEmail)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.editTextPassword)).getText().toString();

                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.login(email, password, new MainActivity.OnLoginResultListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        mainActivity.getName(new MainActivity.OnGetNameListener() {
                            @Override
                            public void onNameReceived(String userName) {
                                CurrentUser curr_user = CurrentUser.getInstance(email,userName);
                                CartManager cartManager = CartManager.getInstance();
                                cartManager.uploadCart();
                                Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_shoppingFragment);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });






        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to fragment3 for sign-in
                Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_regFragment);
            }
        });

        return view;
    }
}