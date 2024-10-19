package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DiscountFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 1; // Request code for file picker
    private String mParam1;
    private String mParam2;

    private ActivityResultLauncher<Intent> filePickerLauncher; // For file picker
    private TextView uploadFileTxt; // TextView to display selected file name

    public DiscountFragment() {
        // Required empty public constructor
    }

    public static DiscountFragment newInstance(String param1, String param2) {
        DiscountFragment fragment = new DiscountFragment();
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

        // Initialize the ActivityResultLauncher for file picker
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        // Handle the file selected by the user
                        String fileName = uri.getLastPathSegment(); // Get the file name
                        uploadFileTxt.setText(fileName); // Display the file name in the TextView
                        Toast.makeText(getContext(), "File selected: " + fileName, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        // Initialize the TextView for displaying selected file name
        uploadFileTxt = view.findViewById(R.id.uploadFileTxt); // Reference to the TextView

        // Initialize the TextView for gender selection
        TextView genderTextView = view.findViewById(R.id.gender_text_view);

        // Set up the click listener for the TextView
        genderTextView.setOnClickListener(v -> {
            // Create a PopupMenu
            PopupMenu popupMenu = new PopupMenu(getContext(), genderTextView);
            popupMenu.getMenuInflater().inflate(R.menu.gender_menu, popupMenu.getMenu());

            // Set a click listener for the menu items
            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedGender = item.getTitle().toString();
                genderTextView.setText(selectedGender); // Update the TextView with selected gender
                Toast.makeText(getContext(), "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show(); // Optional
                return true;
            });

            // Show the PopupMenu
            popupMenu.show();
        });

        // Find the imageView24
        ImageView imageView24 = view.findViewById(R.id.imageView24);

        // Set an OnClickListener on imageView24
        imageView24.setOnClickListener(v -> {
            // Create a new instance of DiscountFragment
            DiscountFragment discountFragment = new DiscountFragment();

            // Replace the current fragment with DiscountFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, discountFragment);
            transaction.addToBackStack(null);  // Optional: Adds the transaction to the back stack
            transaction.commit();
        });

        // Set up the button to browse files
        Button browseFileButton = view.findViewById(R.id.browseFileButton);
        browseFileButton.setOnClickListener(v -> {
            // Check for permission for Android 13 and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                } else {
                    // Permission already granted, open file picker
                    openFilePicker();
                }
            } else {
                // No permission required for PDFs, just open the file picker
                openFilePicker();
            }
        });

        return view;
    }

    // Method to open the file picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // This allows selecting all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE); // To ensure only openable files are shown
        filePickerLauncher.launch(Intent.createChooser(intent, "Select a file"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open file picker
                openFilePicker();
            } else {
                // Handle the case where permission is denied
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
