package com.example.shop_management.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shop_management.R;
import com.example.shop_management.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link regFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class regFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public regFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment regFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static regFragment newInstance(String param1, String param2) {
        regFragment fragment = new regFragment();
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
        View view = inflater.inflate(R.layout.fragment_reg, container, false);
        Button btnReg = view.findViewById(R.id.btnFragmentregister);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) view.findViewById(R.id.editTextEmail1)).getText().toString();
                String pass1 = ((EditText) view.findViewById(R.id.editTextPassword1)).getText().toString();
                String pass2 = ((EditText) view.findViewById(R.id.editTextPassword2)).getText().toString();
                String phone = ((EditText) view.findViewById(R.id.editTextPhone)).getText().toString();
                String name = ((EditText) view.findViewById(R.id.editTextName)).getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (pass1.equals("") || !pass1.equals((pass2))) {
                        Toast.makeText(getActivity().getApplicationContext(), "The password not the same", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (phone.equals("") || phone.length() != 10) {
                            Toast.makeText(getActivity().getApplicationContext(), "Check your phone number", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.reg();
                            mainActivity.addDATA(email,name,phone);
                            Navigation.findNavController(view).navigate(R.id.action_regFragment_to_logInFragment);
                        }
                    }
                }
            }
        });

        Button btnBack = view.findViewById(R.id.btnFragmentBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_regFragment_to_logInFragment);
            }
        });

        return view;
    }
}