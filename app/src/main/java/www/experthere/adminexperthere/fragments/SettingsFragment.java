package www.experthere.adminexperthere.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.SettingsActivity;
import www.experthere.adminexperthere.activities.TaxActivity;
import www.experthere.adminexperthere.adapters.ProviderListAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdmobData;
import www.experthere.adminexperthere.dataModel.AdsResponse;
import www.experthere.adminexperthere.dataModel.MantanaceData;
import www.experthere.adminexperthere.dataModel.MentainanceRes;
import www.experthere.adminexperthere.dataModel.PhpMailerRes;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;


public class SettingsFragment extends Fragment {
    Activity activity;

    TextInputEditText etBanner, etInterstitial, etNative, etAppOpen, etUserName, etPass,etMantanance;

    ProcessingDialog processingDialog;

    SwitchMaterial mantainanceSwitch;


    boolean mantainaceCheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();

        processingDialog = new ProcessingDialog(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        etBanner = view.findViewById(R.id.etBanner);
        etInterstitial = view.findViewById(R.id.etInterstitial);
        etNative = view.findViewById(R.id.etNative);
        etAppOpen = view.findViewById(R.id.etAppOpen);

        etUserName = view.findViewById(R.id.etUserName);
        etPass = view.findViewById(R.id.etPass);
        etMantanance = view.findViewById(R.id.etMantanance);

        mantainanceSwitch = view.findViewById(R.id.mantainanceSwitch);


        view.findViewById(R.id.taxBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, TaxActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        view.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!etBanner.getText().toString().isEmpty()) {
                    if (!etNative.getText().toString().isEmpty()) {


                        if (!etInterstitial.getText().toString().isEmpty()) {

                            if (!etAppOpen.getText().toString().isEmpty()) {




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
                                btnDelete.setText("Submit");

                                // Set dialog title and message
                                dialogTitle.setText("Submit Ads Details ?");
                                dialogMessage.setText("Are you sure you want to Submit these ads id to server ?");


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


                                        startAdmobApi(etBanner.getText().toString().trim()
                                                , etInterstitial.getText().toString().trim()
                                                , etNative.getText().toString().trim()
                                                , etAppOpen.getText().toString().trim());

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();










                            } else {
                                etAppOpen.setText("Enter App Open ID");
                            }


                        } else {
                            etInterstitial.setText("Enter Interstitial ID");
                        }


                    } else {
                        etNative.setText("Enter Native Ads ID");
                    }

                } else {
                    etBanner.setText("Enter Banner ID");
                }
            }
        });
        view.findViewById(R.id.submitBtnPhpMailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etUserName.getText().toString().isEmpty() && !etPass.getText().toString().isEmpty()){




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
                    btnDelete.setText("Submit");

                    // Set dialog title and message
                    dialogTitle.setText("Submit PHP MAILER details ?");
                    dialogMessage.setText("Are you sure you want to Submit these php mailer details to server ?");


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
                                new SetAuthData(etUserName.getText().toString().trim(),etPass.getText().toString().trim()).execute();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            dialog.dismiss();
                        }
                    });

                    dialog.show();















                }else {

                    Toast.makeText(activity, "Username and Password Required!", Toast.LENGTH_LONG).show();
                }



            }
        });



        mantainanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mantainaceCheck = isChecked;


                if (isChecked){

                    mantainanceSwitch.setText("Maintenance Mode Will Turn On!");
                    mantainanceSwitch.setTextColor(activity.getColor(R.color.red));


                }else {

                    mantainanceSwitch.setText("Maintenance Mode Will Turn Off!");
                    mantainanceSwitch.setTextColor(activity.getColor(R.color.green));


                }


            }
        });


        view.findViewById(R.id.submitBtnMaintainance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etMantanance.getText().toString().isEmpty()){


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
                    btnDelete.setText("Yes");

                    // Set dialog title and message
                    dialogTitle.setText("Change Mantainance Mode Settings?");
                    dialogMessage.setText("Are you sure you want to Change Mantainance Mode Settings ?");


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

                            startMaintanance(etMantanance.getText().toString().trim(),mantainaceCheck);

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
















                }else {
                    Toast.makeText(activity, "Enter Message", Toast.LENGTH_SHORT).show();
                }



            }
        });



        view.findViewById(R.id.settingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);


            }
        });


        try {
            new GetAdmobData().execute();
            new GetAuthData(getString(R.string.api_secrete)).execute();
            new GetMantainance().execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }





        return view;

    }

    private void startMaintanance(String message,boolean isChecked) {
        String status = "disable";
        if (isChecked){
            status = "enable";
        }



        try {
            new SetMaintenance(1,message,status).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void startAdmobApi(String banner, String interstitial, String nativeId, String appOpen) {

        try {

            new SetAdmobData(1, banner, interstitial, nativeId, appOpen).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private class SetAdmobData extends AsyncTask<Void, Void, Void> {

        int id;
        String banner, interstitial, nativeAds, appOpen;

        public SetAdmobData(int id, String banner, String interstitial, String nativeAds, String appOpen) {
            this.id = id;
            this.banner = banner;
            this.interstitial = interstitial;
            this.nativeAds = nativeAds;
            this.appOpen = appOpen;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setAdmob(id, "disable", banner, interstitial, nativeAds, appOpen).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (response.body() != null) {


                                if (response.body().isSuccess()) {

                                    Toast.makeText(activity, "Data Updated!", Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }


                        }
                    });


                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            processingDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

            processingDialog.dismiss();

        }


    }
    private class SetMaintenance extends AsyncTask<Void, Void, Void> {

        int id;
        String message, status;

        public SetMaintenance(int id, String message, String status) {
            this.id = id;
            this.message = message;
            this.status = status;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setMaintenance(id,message,status).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SuccessMessageResponse successMessageResponse = response.body();

                            if (successMessageResponse!=null){



                                if (successMessageResponse.isSuccess()){

                                    Toast.makeText(activity, "Submitted Successfully!", Toast.LENGTH_SHORT).show();


                                    if (mantainaceCheck){

                                        mantainanceSwitch.setText("App Is Under Maintenance Mode!");
                                        mantainanceSwitch.setTextColor(activity.getColor(R.color.red));

                                    }else {

                                        mantainanceSwitch.setText("Maintenance Mode Is Off!");
                                        mantainanceSwitch.setTextColor(activity.getColor(R.color.green));

                                    }


                                }else {
                                    Toast.makeText(activity, successMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }





                        }
                    });


                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
        return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            processingDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

            processingDialog.dismiss();

        }


    }

    public class GetAdmobData extends AsyncTask<Void, Void, AdsResponse> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected AdsResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<AdsResponse> call = apiInterface.getAdmob(1);


                Response<AdsResponse> response = call.execute();

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
        protected void onPostExecute(AdsResponse apiResponse) {
            super.onPostExecute(apiResponse);


            if (apiResponse != null) {

                if (apiResponse.isSuccess()) {

                    AdmobData admobData = apiResponse.getAdmob_data();

                    etBanner.setText(admobData.getBanner());
                    etInterstitial.setText(admobData.getInterstitial());
                    etNative.setText(admobData.getNativeAds());
                    etAppOpen.setText(admobData.getOpen_app());


                } else {
                    Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }

            } else {


            }

        }

    }
    public class GetMantainance extends AsyncTask<Void, Void, MentainanceRes> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected MentainanceRes doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<MentainanceRes> call = apiInterface.getMaintanance(1);


                Response<MentainanceRes> response = call.execute();

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
        protected void onPostExecute(MentainanceRes apiResponse) {
            super.onPostExecute(apiResponse);


            mantainanceSwitch.setEnabled(true);

            if (apiResponse != null) {

                if (apiResponse.isSuccess()) {

                    MantanaceData admobData = apiResponse.getData();

                    etMantanance.setText(admobData.getMessage());

                    String status = admobData.getStatus();

                    if (status.equals("enable")){

                        mantainanceSwitch.setChecked(true);
                        mantainanceSwitch.setText("App Is Under Maintenance Mode!");
                        mantainanceSwitch.setTextColor(activity.getColor(R.color.red));

                    }
                    if (status.equals("disable")){

                        mantainanceSwitch.setChecked(false);
                        mantainanceSwitch.setText("Maintenance Mode Is Off!");
                        mantainanceSwitch.setTextColor(activity.getColor(R.color.green));

                    }



                } else {
                    Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }

            } else {


            }

        }

    }

    public class GetAuthData extends AsyncTask<Void, Void, PhpMailerRes> {

        String ApiKey;

        public GetAuthData(String apiKey) {
            ApiKey = apiKey;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected PhpMailerRes doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<PhpMailerRes> call = apiInterface.getAuthValues(ApiKey);


                Response<PhpMailerRes> response = call.execute();

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
        protected void onPostExecute(PhpMailerRes apiResponse) {
            super.onPostExecute(apiResponse);


            if (apiResponse != null) {

                String userName = apiResponse.getSmtp_username();
                String pass = apiResponse.getSmtp_password();


                etUserName.setText(userName);
                etPass.setText(pass);


            }
        }

    }

    private class SetAuthData extends AsyncTask<Void, Void, Void> {


        String userName, password;

        public SetAuthData(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setAuthValues(userName, password).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SuccessMessageResponse successMessageResponse = response.body();

                            if (successMessageResponse!=null){

                                if (successMessageResponse.isSuccess()){

                                    Toast.makeText(activity, "Details Updated Success!", Toast.LENGTH_SHORT).show();






                                }else {
                                    Toast.makeText(activity, successMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }



                            }


                        }
                    });


                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            processingDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

            processingDialog.dismiss();

        }


    }

}
