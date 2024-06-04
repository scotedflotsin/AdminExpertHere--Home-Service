package www.experthere.adminexperthere.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SystemUsersAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdminData;
import www.experthere.adminexperthere.dataModel.AdminsResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;

public class SysytemUsersFragment extends Fragment {

    Activity activity;

    RecyclerView recyclerView;
    SystemUsersAdapter adapter;


    private int currentPage = 1;
    int itemsPerPage = 5;

    List<AdminData> adminData;
    private ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    ProcessingDialog processingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        adminData = new ArrayList<>();

    }


    private final BroadcastReceiver update = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            currentPage = 1;
            itemsPerPage = 5;
            isLoading = false;
            isLastPage = false;
            adminData.clear();
            adapter.notifyDataSetChanged();
            adminData = new ArrayList<>();

            getSysUsers();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        activity.unregisterReceiver(update);
    }


    CheckBox categoryCheck, providerCheck, usersCheck, notificationsCheck, adsCheck, settingsCheck, sysUsersCheck, firebaseCheck, agoraCheck, webCheck;

    TextInputEditText etEmail, etPass, etName;

    @SuppressLint({"MissingInflatedId", "UnspecifiedRegisterReceiverFlag"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sysytem_users, container, false);

        processingDialog = new ProcessingDialog(activity);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("www.experthere.adminexperthere.updateSysUsers");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(update, filter1, Context.RECEIVER_NOT_EXPORTED);
        }else {
            activity.registerReceiver(update, filter1);

        }


        categoryCheck = view.findViewById(R.id.catCheck);
        providerCheck = view.findViewById(R.id.providersCheck);
        usersCheck = view.findViewById(R.id.usersCheck);
        notificationsCheck = view.findViewById(R.id.notificationsCheck);
        adsCheck = view.findViewById(R.id.adsCheck);
        settingsCheck = view.findViewById(R.id.settingsCheck);
        sysUsersCheck = view.findViewById(R.id.sysUsersCheck);
        firebaseCheck = view.findViewById(R.id.firebaseCheck);
        agoraCheck = view.findViewById(R.id.agoraCheck);
        webCheck = view.findViewById(R.id.webCheck);

        etEmail = view.findViewById(R.id.etEmail);
        etPass = view.findViewById(R.id.editTextPassword);
        etName = view.findViewById(R.id.etNames);


        recyclerView = view.findViewById(R.id.recyclerSysUsers);
        progressBar = view.findViewById(R.id.progressRecycler);
        adapter = new SystemUsersAdapter(adminData, activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= itemsPerPage

                    ) {

                        // Load more data when reaching the end
                        isLoading = true;
                        currentPage++;

                        progressBar.setVisibility(View.VISIBLE);

                        getSysUsers();


                    }
                }
            }
        });


        view.findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etEmail.getText().toString().isEmpty()) {


                    if (!etName.getText().toString().isEmpty()) {

                        if (!etPass.getText().toString().isEmpty()) {


                            registerNewUser(etEmail.getText().toString().trim(), etName.getText().toString().trim(), etPass.getText().toString().trim());


                        } else {

                            etPass.setError("Enter Password");
                        }
                    } else {

                        etName.setError("Enter Name");
                    }

                } else {

                    etEmail.setError("Enter Email");
                }


            }
        });


        getSysUsers();


        return view;
    }

    @SuppressLint("SetTextI18n")
    private void registerNewUser(String email, String name, String pass) {


        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize views in the custom dialog
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        btnDelete.setText("Create");

        // Set dialog title and message
        dialogTitle.setText("Create Admin?");
        dialogMessage.setText("Are you sure you want to Create Account With Details - \n\n"


                +"email: " + email
                +"\nPass: " + pass
                +"\nname: " + name
                +"\nCategory: " + getStatus(categoryCheck.isChecked())
                +"\nProviders: " +getStatus(providerCheck.isChecked())
                +"\nUsers: " +getStatus(usersCheck.isChecked())
                +"\nNotifications: " + getStatus(notificationsCheck.isChecked())
                +"\nAds: " +getStatus(adsCheck.isChecked())
                +"\nSettings: " +getStatus(settingsCheck.isChecked())
                +"\nSys. Users: " +getStatus(sysUsersCheck.isChecked())
                +"\nFirebase: " +getStatus(firebaseCheck.isChecked())
                +"\nAgora: " +getStatus(agoraCheck.isChecked())
                +"\nWeb: " +getStatus(webCheck.isChecked())

                + "\n ?");


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




                    new RegisterNewUser(
                            activity,
                            email,
                            pass,
                            name,
                            getStatus(categoryCheck.isChecked()),
                            getStatus(providerCheck.isChecked()),
                            getStatus(usersCheck.isChecked()),
                            getStatus(notificationsCheck.isChecked()),
                            getStatus(adsCheck.isChecked()),
                            getStatus(settingsCheck.isChecked()),
                            getStatus(sysUsersCheck.isChecked()),
                            getStatus(firebaseCheck.isChecked()),
                            getStatus(agoraCheck.isChecked()),
                            getStatus(webCheck.isChecked())

                    ).execute();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                dialog.dismiss();
            }
        });

        dialog.show();





    }

    private void getSysUsers() {


        try {
            new GetSysUsers(activity, currentPage, itemsPerPage).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    private class RegisterNewUser extends AsyncTask<Void, Void, Void> {

        Activity activity;
        String email, password, name, categories, providers, users, notifications, ads, settings, system_users, firebase, agora, web;

        public RegisterNewUser(Activity activity, String email, String password,
                               String name, String categories, String providers,
                               String users, String notifications, String ads,
                               String settings, String system_users, String firebase,
                               String agora, String web) {
            this.activity = activity;
            this.email = email;
            this.password = password;
            this.name = name;
            this.categories = categories;
            this.providers = providers;
            this.users = users;
            this.notifications = notifications;
            this.ads = ads;
            this.settings = settings;
            this.system_users = system_users;
            this.firebase = firebase;
            this.agora = agora;
            this.web = web;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            processingDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<SuccessMessageResponse> call = apiInterface.registerUser(email, password,name,categories,providers,users,notifications,ads,settings,system_users,firebase,agora,web);


            call.enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processingDialog.dismiss();

                        }
                    });

                    SuccessMessageResponse adminsResponse = response.body();

                    if (adminsResponse != null ) {

                        if (adminsResponse.isSuccess() && adminsResponse.getMessage().equals("Admin data saved successfully.")){


                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(activity, "Account Created", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent("www.experthere.adminexperthere.updateSysUsers");
                                    activity.sendBroadcast(intent);

                                    etEmail.setText("");
                                    etName.setText("");
                                    etPass.setText("");


                                }
                            });

                        }else {

                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "Not Success 1" , Toast.LENGTH_SHORT).show();

                            });
                        }

                    }else {

                        activity.runOnUiThread(() -> {
                            Toast.makeText(activity, "Not Success "+response.message() , Toast.LENGTH_SHORT).show();

                        });
                    }

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {

                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Category error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        processingDialog.dismiss();

                    });

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion
        }
    }

    private class GetSysUsers extends AsyncTask<Void, Void, Void> {

        Activity activity;
        int page, itemsPerPage;


        public GetSysUsers(Activity activity, int page, int itemsPerPage) {
            this.activity = activity;
            this.page = page;
            this.itemsPerPage = itemsPerPage;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<AdminsResponse> call = apiInterface.getsysusers(page, itemsPerPage);


            call.enqueue(new Callback<AdminsResponse>() {
                @Override
                public void onResponse(Call<AdminsResponse> call, Response<AdminsResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                    AdminsResponse adminsResponse = response.body();

                    if (adminsResponse != null && adminsResponse.isSuccess()) {

                        List<AdminData> newServices = adminsResponse.getAdmin();

                        if (newServices != null && !newServices.isEmpty() && adminsResponse.getMessage().equals("Users found.")) {


                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setVisibility(View.VISIBLE);

                                }
                            });

                            if (currentPage == 1) {
                                adminData.clear();
                                adapter.notifyDataSetChanged();

                            }

                            adminData.addAll(newServices);
                            adapter.setData(adminData);
                            adapter.notifyDataSetChanged();

                            if (newServices.size() < itemsPerPage) {
                                isLastPage = true;
                            }
                        } else if (currentPage > 1) {
                            // If no new data on a subsequent page, consider it the last page
                            isLastPage = true;
//                showNoServiceMessage();
                        } else {
                        }
                    } else {


//            Toast.makeText(activity, "Null response body or unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<AdminsResponse> call, Throwable t) {

                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Category error - " + t.getMessage(), Toast.LENGTH_SHORT).show();


                    });

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion
        }
    }


    private String getStatus(boolean check) {

        if (check) {
            return "enable";
        } else {
            return "disable";
        }

    }


}