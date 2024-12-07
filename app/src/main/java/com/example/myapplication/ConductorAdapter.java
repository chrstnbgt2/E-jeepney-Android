package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User;

import java.util.List;
import java.util.Map;

public class ConductorAdapter extends RecyclerView.Adapter<ConductorAdapter.ConductorViewHolder> {

    private List<User> conductors; // List of users with the "conductor" role
    private Context context;

    public ConductorAdapter(Context context, List<User> conductors) {
        this.context = context;
        this.conductors = conductors;
    }

    @NonNull
    @Override
    public ConductorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conductor, parent, false);
        return new ConductorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConductorViewHolder holder, int position) {
        User user = conductors.get(position);

        // Extract conductor details
        Map<String, Object> conductorDetails = user.getConductor();
        String transaction = conductorDetails.get("transaction").toString();
        double walletBalance = (double) conductorDetails.get("walletBalance");

        // Set name
        holder.conductorName.setText(user.getFirstName() + " " + user.getLastName());

        // Display status dot color based on wallet balance (example logic)
        if (walletBalance > 0) {
            holder.statusDot.setBackgroundResource(R.drawable.circle_dot_green);
        } else {
            holder.statusDot.setBackgroundResource(R.drawable.circle_dot_orange);
        }

        // Handle menu icon click (optional)
        holder.menuIcon.setOnClickListener(v -> {
            // Add functionality for the 3-dot menu
        });
    }

    @Override
    public int getItemCount() {
        return conductors.size();
    }

    public static class ConductorViewHolder extends RecyclerView.ViewHolder {
        View statusDot;
        TextView conductorName;
        ImageView menuIcon;

        public ConductorViewHolder(@NonNull View itemView) {
            super(itemView);
            statusDot = itemView.findViewById(R.id.statusDot);
            conductorName = itemView.findViewById(R.id.conductorName);
            menuIcon = itemView.findViewById(R.id.menuIcon);
        }
    }
}
