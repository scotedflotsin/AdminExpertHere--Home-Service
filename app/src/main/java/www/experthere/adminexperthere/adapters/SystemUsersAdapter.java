package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.PasswordResetActivity;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdminData;
import www.experthere.adminexperthere.dataModel.AdminsResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.KeyboardUtils;

public class SystemUsersAdapter extends RecyclerView.Adapter<SystemUsersAdapter.ViewHolder> {


    List<AdminData> adminsResponses;
    Activity activity;

    public SystemUsersAdapter(List<AdminData> adminsResponses, Activity activity) {
        this.adminsResponses = adminsResponses;
        this.activity = activity;
    }


    public void setData(List<AdminData> service) {
        this.adminsResponses = service;
        notifyDataSetChanged();
    }

    public void addData(List<AdminData> newServiceList) {
        if (adminsResponses != null) {
            adminsResponses.addAll(newServiceList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public SystemUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sys_users_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemUsersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.name.setText(adminsResponses.get(position).getName());
        holder.email.setText(adminsResponses.get(position).getEmail());

        String accessTxt = " ";


        if (adminsResponses.get(position).getCategories().equals("enable")) {
            accessTxt = accessTxt + "Categories";
        }
        if (adminsResponses.get(position).getProviders().equals("enable")) {
            accessTxt = accessTxt + ",Providers";
        }
        if (adminsResponses.get(position).getUsers().equals("enable")) {
            accessTxt = accessTxt + ",Users";
        }
        if (adminsResponses.get(position).getNotifications().equals("enable")) {
            accessTxt = accessTxt + ",Notifications";
        }
        if (adminsResponses.get(position).getAds().equals("enable")) {
            accessTxt = accessTxt + ",Ads";
        }
        if (adminsResponses.get(position).getSettings().equals("enable")) {
            accessTxt = accessTxt + ",Settings";
        }
        if (adminsResponses.get(position).getSystemUsers().equals("enable")) {
            accessTxt = accessTxt + ",Sys. Users";
        }
        if (adminsResponses.get(position).getFirebase().equals("enable")) {
            accessTxt = accessTxt + ",Firebase";
        }
        if (adminsResponses.get(position).getAgora().equals("enable")) {
            accessTxt = accessTxt + ",Agora";
        }
        if (adminsResponses.get(position).getWeb().equals("enable")) {
            accessTxt = accessTxt + ",Web Control";
        }


        if (adminsResponses.get(position).getCategories().equals("enable")
                && adminsResponses.get(position).getProviders().equals("enable")
                && adminsResponses.get(position).getUsers().equals("enable")

                && adminsResponses.get(position).getNotifications().equals("enable")
                && adminsResponses.get(position).getAds().equals("enable")
                && adminsResponses.get(position).getSettings().equals("enable")
                && adminsResponses.get(position).getSystemUsers().equals("enable")
                && adminsResponses.get(position).getFirebase().equals("enable")
                && adminsResponses.get(position).getAgora().equals("enable")
                && adminsResponses.get(position).getWeb().equals("enable")) {


            accessTxt = "Full Access";

        }


        holder.accessTxt.setText(accessTxt);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = activity.getSharedPreferences("admin", Context.MODE_PRIVATE);
                if (preferences.getString("id","0").equals(String.valueOf(adminsResponses.get(position).getId()))){

                    Toast.makeText(activity, "You Cant Delete Own Account!", Toast.LENGTH_SHORT).show();

                }else {

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
                    btnDelete.setText("Delete");

                    // Set dialog title and message
                    dialogTitle.setText("Delete Admin?");
                    dialogMessage.setText("Are you sure you want to Delete " + adminsResponses.get(position).getName() + " ?");


                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // User clicked the Cancel button
                            dialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // User clicked the Delete button


                            try {


                                new DeleteAdminTask(adminsResponses.get(position).getEmail(),position).execute();

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }




            }
        });


    }

    @Override
    public int getItemCount() {
        return adminsResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, email, accessTxt;
        ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.nameTxt);
            email = itemView.findViewById(R.id.emailTxt);
            accessTxt = itemView.findViewById(R.id.accessTxt);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }


    private class DeleteAdminTask extends AsyncTask<Void, Void, SuccessMessageResponse> {


        String email;
        int position;

        public DeleteAdminTask(String email, int position) {
            this.email = email;
            this.position = position;
        }

        @Override
        protected SuccessMessageResponse doInBackground(Void... strings) {


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<SuccessMessageResponse> call = apiInterface.deleteAdmin(email);

            try {
                Response<SuccessMessageResponse> response = call.execute();

                if (response.body() != null) {

                        return response.body();

                    } else {
                        return null;
                    }


            } catch (IOException e) {
                Log.e("LSKKJBSBKJ", "IOException: " + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(SuccessMessageResponse success) {
            super.onPostExecute(success);
            if (success!=null) {

                if (success.getMessage().equals("Admin data deleted successfully.")){


                    Toast.makeText(activity, "Admin data deleted successfully", Toast.LENGTH_SHORT).show();

                    adminsResponses.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,adminsResponses.size());


                    Intent intent = new Intent("www.experthere.adminexperthere.updateSysUsers");
                    activity.sendBroadcast(intent);



                }





            } else {
                Toast.makeText(activity, "Failed to delete admin data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
