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

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.ProviderDetailsActivity;
import www.experthere.adminexperthere.activities.SubcatActivity;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.ProviderListResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.ViewHolder> implements Filterable {

    List<ProviderList> providerLists;
    List<ProviderList> filteredProviderList;
    Activity activity;

    private String searchQuery = "";


    public ProviderListAdapter(List<ProviderList> providerLists, Activity activity) {
        this.providerLists = providerLists;
        this.activity = activity;
        this.filteredProviderList = new ArrayList<>();
    }


    public void setData(List<ProviderList> service) {
        this.providerLists = service;
        notifyDataSetChanged();
    }

    public void addData(List<ProviderList> newServiceList) {
        if (newServiceList != null) {
            newServiceList.addAll(newServiceList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ProviderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if (providerLists!=null){



            Glide.with(activity)
                    .load(providerLists.get(position)
                            .getLogoImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .circleCrop()
                    .into(holder.providerLogoImg);

            holder.idTxt.setText("ID: " + providerLists.get(position).getId());
            holder.companyNametxt.setText("Comp. Name: " + providerLists.get(position).getCompanyName());
            holder.nameTxt.setText("Provider Name: " + providerLists.get(position).getName());
            holder.emailTxt.setText("Email: " + providerLists.get(position).getEmail());


            DecimalFormat df = new DecimalFormat("#.#");
            holder.serviceRatetxt.setText("Services: " + providerLists.get(position).getTotalServices() + ", Rate: " + df.format(providerLists.get(position).getAverage_rating()));


            holder.statusTxt.setText(providerLists.get(position).getStatus());


            if (providerLists.get(position).getStatus()!=null){
                if (providerLists.get(position).getStatus().equals("active")) {

                    holder.statusTxt.setTextColor(activity.getColor(R.color.green));
                } else {
                    holder.statusTxt.setTextColor(activity.getColor(R.color.red));

                }
            }else {
                holder.statusTxt.setTextColor(activity.getColor(R.color.red));

                holder.statusTxt.setText("Status Not Set By Provider!");

            }




            if (providerLists.get(position).getIsBlocked() == 0) {

                holder.blockTxt.setText("Block");
                holder.blockTxt.setTextColor(activity.getColor(R.color.black));
                holder.blokIcon.setColorFilter(activity.getColor(R.color.black));


            } else if (providerLists.get(position).getIsBlocked() == 1) {

                holder.blockTxt.setText("Unblock");

                holder.blockTxt.setTextColor(activity.getColor(R.color.red));
                holder.blokIcon.setColorFilter(activity.getColor(R.color.red));


            }


            holder.clickProviderCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle bundle = new Bundle();

                    Gson gson = new Gson();
                    String subcategoryListJson = gson.toJson(providerLists.get(position));

                    bundle.putString("list",subcategoryListJson);


                    Intent intent = new Intent(activity, ProviderDetailsActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);




                }
            });



            holder.blockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int status = 0;

                    if (providerLists.get(position).getIsBlocked()==0){

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
                    dialogTitle.setText(holder.blockTxt.getText().toString()+" Provider?");
                    dialogMessage.setText("Are you sure you want to "+holder.blockTxt.getText().toString() + providerLists.get(position).getName() + " ?");


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

                                new BlockProvider(providerLists.get(position).getId(), finalStatus).execute();
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

    }

    @Override
    public int getItemCount() {
        return providerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView providerLogoImg,blokIcon;
        TextView idTxt, nameTxt, companyNametxt, emailTxt, serviceRatetxt, statusTxt, blockTxt;

        LinearLayout blockBtn;

        CardView clickProviderCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            providerLogoImg = itemView.findViewById(R.id.serviceImage);

            idTxt = itemView.findViewById(R.id.idTxt);
            nameTxt = itemView.findViewById(R.id.providerNameTxt);
            companyNametxt = itemView.findViewById(R.id.companyNameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            serviceRatetxt = itemView.findViewById(R.id.servicesTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            blockTxt = itemView.findViewById(R.id.blockTxt);
            blockBtn = itemView.findViewById(R.id.blockBtn);
            blokIcon = itemView.findViewById(R.id.blokIcon);
            clickProviderCard = itemView.findViewById(R.id.clickProviderCard);


        }
    }


    public class BlockProvider extends AsyncTask<Void, Void, SuccessMessageResponse> {


        int id, status;

        public BlockProvider(int id, int status) {
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
                Call<SuccessMessageResponse> call = apiInterface.blockUnblockProvider(
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

                if (apiResponse.isSuccess()){

                    if (status == 1) {
                        Toast.makeText(activity, "Blocked!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "UnBlocked!", Toast.LENGTH_SHORT).show();

                    }

                    Intent intent = new Intent("www.experthere.adminexperthere.updateProviders");
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
                providerLists.addAll((List<ProviderList>) filterResults.values);
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
