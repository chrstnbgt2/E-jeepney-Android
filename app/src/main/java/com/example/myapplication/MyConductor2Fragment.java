package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyConductor2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyConductor2Fragment extends Fragment {

    private EditText firstNameEditText, middleNameEditText, lastNameEditText, emailEditText, phoneNumberEditText, passwordEditText;
    private Button createAccountButton;
    private ProgressDialog progressDialog;

    public MyConductor2Fragment() {
        // Required empty public constructor
    }

    public static MyConductor2Fragment newInstance(String param1, String param2) {
        MyConductor2Fragment fragment = new MyConductor2Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_conductor2, container, false);

        // Initialize UI components
        firstNameEditText = view.findViewById(R.id.firstName);
        middleNameEditText = view.findViewById(R.id.middleName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.email);
        phoneNumberEditText = view.findViewById(R.id.phoneNumber);
        passwordEditText = view.findViewById(R.id.password);
        createAccountButton = view.findViewById(R.id.createAccountButton);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Creating account...");
        progressDialog.setCancelable(false);

        ImageView backButton = view.findViewById(R.id.imageView24);

        // Set up the back button to navigate to the previous fragment
        backButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_conductor, new My_ConductorFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Handle account creation
        createAccountButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String middleName = middleNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            createFirebaseAccount(firstName, middleName, lastName, email, phoneNumber, password);
        });

        return view;
    }

    private void createFirebaseAccount(String firstName, String middleName, String lastName, String email, String phoneNumber, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            storeUserData(user.getUid(), firstName, middleName, lastName, email, phoneNumber);
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Account creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeUserData(String userId, String firstName, String middleName, String lastName, String email, String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users/conductor").child(userId);

        HashMap<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData.put("middleName", middleName);
        userData.put("lastName", lastName);
        userData.put("email", email);
        userData.put("phoneNumber", phoneNumber);

        userRef.setValue(userData)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conductor, new My_ConductorFragment());
                        transaction.commit();
                    } else {
                        Toast.makeText(getContext(), "Failed to store user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
