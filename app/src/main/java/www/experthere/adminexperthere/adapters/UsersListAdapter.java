package www.experthere.adminexperthere.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.ProviderDetailsActivity;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.dataModel.UsersList;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> implements Filterable {

    List<UsersList> providerLists;
    List<UsersList> filteredProviderList;
    Activity activity;

    private String searchQuery = "";


    public UsersListAdapter(List<UsersList> providerLists, Activity activity) {
        this.providerLists = providerLists;
        this.activity = activity;
        this.filteredProviderList = new ArrayList<>();
    }


    public void setData(List<UsersList> service) {
        this.providerLists = service;
        notifyDataSetChanged();
    }

    public void addData(List<UsersList> newServiceList) {
        if (newServiceList != null) {
            newServiceList.addAll(newServiceList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public UsersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Glide.with(activity)
                .load(providerLists.get(position)
                        .getProfile_picture())
                .placeholder(R.mipmap.ic_launcher)
                .circleCrop()
                .into(holder.providerLogoImg);

        holder.idTxt.setText("ID: " + providerLists.get(position).getId());
        holder.nameTxt.setText("User Name: " + providerLists.get(position).getName());
        holder.professionTxt.setText("Profession: " + providerLists.get(position).getProfession());
        holder.emailTxt.setText("Email: " + providerLists.get(position).getEmail());
        holder.phoneTxt.setText("Phone: " + providerLists.get(position).getPhone());
        holder.passwordTxt.setText("Password: " + providerLists.get(position).getPassword());


        if (providerLists.get(position).getIs_blocked() == 0) {

            holder.isBlockedTxt.setText("Blocked: No");
            holder.isBlockedTxt.setTextColor(activity.getColor(R.color.green));

        } else {

            holder.isBlockedTxt.setText("Blocked: YES");
            holder.isBlockedTxt.setTextColor(activity.getColor(R.color.red));
        }


        holder.latTxt.setText(String.valueOf(providerLists.get(position).getLatitude()));
        holder.longTxt.setText(String.valueOf(providerLists.get(position).getLongitude()));
        holder.addressTxt.setText(providerLists.get(position).getFull_address());


        if (providerLists.get(position).getIs_blocked() == 0) {

            holder.blockTxt.setText("Block");
            holder.blockTxt.setTextColor(activity.getColor(R.color.black));
            holder.blokIcon.setColorFilter(activity.getColor(R.color.black));


        } else if (providerLists.get(position).getIs_blocked() == 1) {

            holder.blockTxt.setText("Unblock");

            holder.blockTxt.setTextColor(activity.getColor(R.color.red));
            holder.blokIcon.setColorFilter(activity.getColor(R.color.red));


        }


        holder.blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int status = 0;

                if (providerLists.get(position).getIs_blocked() == 0) {

                    status = 1;

                }


                // Create custom dialog
                Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_delete_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Initialize views in the custom dialog
                TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
                TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnDelete = dialog.findViewById(R.id.btnDelete);
                btnDelete.setText(holder.blockTxt.getText().toString());

                // Set dialog title and message
                dialogTitle.setText(holder.blockTxt.getText().toString() + " User?");
                dialogMessage.setText("Are you sure you want to " + holder.blockTxt.getText().toString() + providerLists.get(position).getName() + " ?");


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked the Cancel button
                        dialog.dismiss();
                    }
                });

                int finalStatus = status;
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked the Delete button


                        try {


                            new BlockUser(providerLists.get(position).getId(), finalStatus).execute();

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return providerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView providerLogoImg, blokIcon;
        TextView idTxt, nameTxt, professionTxt, emailTxt, phoneTxt, passwordTxt, isBlockedTxt, blockTxt, latTxt, longTxt, addressTxt;

        LinearLayout blockBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            providerLogoImg = itemView.findViewById(R.id.serviceImage);

            idTxt = itemView.findViewById(R.id.idTxt);
            nameTxt = itemView.findViewById(R.id.userNameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            passwordTxt = itemView.findViewById(R.id.passwordTxt);
            professionTxt = itemView.findViewById(R.id.professionTxt);


            isBlockedTxt = itemView.findViewById(R.id.blockedTxt);
            blockTxt = itemView.findViewById(R.id.blockTxt);
            latTxt = itemView.findViewById(R.id.latTxt);
            longTxt = itemView.findViewById(R.id.longTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);


            blockBtn = itemView.findViewById(R.id.blockBtn);
            blokIcon = itemView.findViewById(R.id.blokIcon);


        }
    }


    public class BlockUser extends AsyncTask<Void, Void, SuccessMessageResponse> {


        int id, status;

        public BlockUser(int id, int status) {
            this.id = id;
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected SuccessMessageResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<SuccessMessageResponse> call = apiInterface.blockUnblockUser(
                        id, status

                );


                Response<SuccessMessageResponse> response = call.execute();

                if (response.isSuccessful()) {
                    return response.body();
                } else {
                    // Handle error
                    Log.e("ApiServiceTask", "API call failed. Code: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SuccessMessageResponse apiResponse) {
            super.onPostExecute(apiResponse);


            if (apiResponse != null) {

                if (apiResponse.isSuccess()) {

                    if (status == 1) {
                        Toast.makeText(activity, "Blocked!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "UnBlocked!", Toast.LENGTH_SHORT).show();

                    }

                    Intent intent = new Intent("www.experthere.adminexperthere.updateUsers");
                    activity.sendBroadcast(intent);

                }


            } else {
                // Handle error
                Toast.makeText(activity, "Error 1", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = searchQuery.toLowerCase(); // Use searchQuery instead of charSequence

                List<ProviderList> filteredList = new ArrayList<>();

                for (ProviderList item : filteredList) {
                    if (item.getName().toLowerCase().contains(query)) {
                        filteredList.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                providerLists.clear();
                providerLists.addAll((List<UsersList>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    // Method to filter data based on search query
    public void filter(String query) {
        searchQuery = query;
        getFilter().filter(query);
    }


}
