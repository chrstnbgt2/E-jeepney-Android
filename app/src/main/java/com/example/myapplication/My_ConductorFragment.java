package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_ConductorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_ConductorFragment extends Fragment {

    // Firebase references
    private FirebaseAuth mAuth;
    private TextView text1;

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Declare status dots
    private View statusDot;
    private View statusDot2;

    public My_ConductorFragment() {
        // Required empty public constructor
    }

    public static My_ConductorFragment newInstance(String param1, String param2) {
        My_ConductorFragment fragment = new My_ConductorFragment();
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my__conductor, container, false);

        // Initialize TextView
        text1 = view.findViewById(R.id.text1);

        // Fetch the user's first and last name from Firebase
        fetchUserFullName();

        // Initialize status dots
        statusDot = view.findViewById(R.id.statusDot);
        statusDot2 = view.findViewById(R.id.statusDot2);

        // Find the button in the inflated layout
        Button button = view.findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConductor2Fragment fragment = new MyConductor2Fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Find the image view in the inflated layout
        ImageView imageView = view.findViewById(R.id.imageView13);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // Find linear2 in the inflated layout and set click listener for opening View_MoreFragment
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View_moreFragment viewMoreFragment = new View_moreFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, viewMoreFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void fetchUserFullName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the first name and last name from Firebase
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);

                        // Set the full name to text1 TextView
                        text1.setText(firstName + " " + lastName);
                    } else {
                        Log.e("UserData", "User data not found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found.");
        }
    }

    // Method to show the dialog
    private void showDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_conductor_options);

        LinearLayout setStatusOption = dialog.findViewById(R.id.set_status_option);
        LinearLayout editDetailsOption = dialog.findViewById(R.id.edit_details_option);
        LinearLayout viewMoreOption = dialog.findViewById(R.id.view_more_option);

        setStatusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusDialog();
                dialog.dismiss();
            }
        });

        editDetailsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_DetailsFragment editDetailsFragment = new Edit_DetailsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, editDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                dialog.dismiss();
            }
        });

        viewMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View_moreFragment viewMoreFragment = new View_moreFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, viewMoreFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Method to show the status dialog
    private void showStatusDialog() {
        Dialog statusDialog = new Dialog(getActivity());
        statusDialog.setContentView(R.layout.dialog_set_status);

        Button btnActive = statusDialog.findViewById(R.id.btn_active);
        Button btnInactive = statusDialog.findViewById(R.id.btn_inactive);

        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDot.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dot_green));
                Toast.makeText(getActivity(), "Status set to Active", Toast.LENGTH_SHORT).show();
                statusDialog.dismiss();
            }
        });

        btnInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDot.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dot_orange));
                Toast.makeText(getActivity(), "Status set to Inactive", Toast.LENGTH_SHORT).show();
                statusDialog.dismiss();
            }
        });

        statusDialog.show();
    }
}
