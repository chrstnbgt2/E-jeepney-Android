// Inside your DiscountFragment.java file
package com.example.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DiscountFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private TextView uploadFileTxt;
    private EditText discountFname, discountMname, discountLname, discountBday, discountAdd, discountCity, discountProv, postalId, discountEmail, discountNum;
    private TextView genderTextView;

    private DatabaseReference databaseReference;
    private Uri fileUri;

    public DiscountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        fileUri = result.getData().getData();
                        String fileName = fileUri.getLastPathSegment();
                        uploadFileTxt.setText(fileName);
                        Toast.makeText(getContext(), "File selected: " + fileName, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        uploadFileTxt = view.findViewById(R.id.uploadFileTxt);
        discountFname = view.findViewById(R.id.discount_fname);
        discountMname = view.findViewById(R.id.discount_mname);
        discountLname = view.findViewById(R.id.discount_lname);
        discountBday = view.findViewById(R.id.discount_bday);
        discountAdd = view.findViewById(R.id.discount_add);
        discountCity = view.findViewById(R.id.discount_city);
        discountProv = view.findViewById(R.id.discount_prov);
        postalId = view.findViewById(R.id.postal_id);
        discountEmail = view.findViewById(R.id.discount_email);
        discountNum = view.findViewById(R.id.discount_num);
        genderTextView = view.findViewById(R.id.gender_text_view);

        genderTextView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), genderTextView);
            popupMenu.getMenuInflater().inflate(R.menu.gender_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedGender = item.getTitle().toString();
                genderTextView.setText(selectedGender);
                Toast.makeText(getContext(), "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
                return true;
            });
            popupMenu.show();
        });

        discountBday.setOnClickListener(v -> showDatePickerDialog());

        Button browseFileButton = view.findViewById(R.id.browseFileButton);
        browseFileButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                } else {
                    openFilePicker();
                }
            } else {
                openFilePicker();
            }
        });

        Button saveButton = view.findViewById(R.id.discount_submit);
        saveButton.setOnClickListener(v -> saveDiscountDetails());

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) ->
                        discountBday.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day
        );
        datePickerDialog.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select a file"));
    }

    private void saveDiscountDetails() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (validateFields()) {
            databaseReference.child("users").child("passenger").child(uid).child("discount_details")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                Toast.makeText(getContext(), "You have already applied for the discount.", Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFileAndSaveDetails(uid);
                            }
                        } else {
                            Toast.makeText(getContext(), "Error checking application status.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateFields() {
        if (discountFname.getText().toString().isEmpty() ||
                discountLname.getText().toString().isEmpty() ||
                discountBday.getText().toString().isEmpty() ||
                discountEmail.getText().toString().isEmpty() ||
                discountNum.getText().toString().isEmpty() ||
                genderTextView.getText().toString().equals("Select Gender") ||
                fileUri == null) {
            Toast.makeText(getContext(), "Please fill out all fields and select a file.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadFileAndSaveDetails(String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("discount_profile")
                .child(uid + "_" + System.currentTimeMillis());

        storageReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> saveNewDiscountDetails(uid, uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "File upload failed.", Toast.LENGTH_SHORT).show());
    }

    private void saveNewDiscountDetails(String uid, String fileUrl) {
        Map<String, Object> discountDetails = new HashMap<>();
        discountDetails.put("firstname", discountFname.getText().toString());
        discountDetails.put("middlename", discountMname.getText().toString());
        discountDetails.put("lastname", discountLname.getText().toString());
        discountDetails.put("birthday", discountBday.getText().toString());
        discountDetails.put("address", discountAdd.getText().toString());
        discountDetails.put("city", discountCity.getText().toString());
        discountDetails.put("province", discountProv.getText().toString());
        discountDetails.put("postal_id", postalId.getText().toString());
        discountDetails.put("email", discountEmail.getText().toString());
        discountDetails.put("contact_number", discountNum.getText().toString());
        discountDetails.put("gender", genderTextView.getText().toString());
        discountDetails.put("file_url", fileUrl);

        databaseReference.child("users").child("passenger").child(uid).child("discount_details")
                .setValue(discountDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Discount details saved successfully", Toast.LENGTH_SHORT).show();
                        clearAllFields();
                    } else {
                        Toast.makeText(getContext(), "Failed to save discount details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAllFields() {
        discountFname.setText("");
        discountMname.setText("");
        discountLname.setText("");
        discountBday.setText("");
        discountAdd.setText("");
        discountCity.setText("");
        discountProv.setText("");
        postalId.setText("");
        discountEmail.setText("");
        discountNum.setText("");
        genderTextView.setText("Select Gender");
        uploadFileTxt.setText("No file selected");
        fileUri = null;
    }
}
