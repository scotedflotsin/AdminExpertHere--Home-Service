package www.experthere.adminexperthere.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SettingsAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.CategoryResponse;
import www.experthere.adminexperthere.dataModel.SettingsRes;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;

public class SettingsActivity extends AppCompatActivity {


    RecyclerView recyclerView;


    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_settings);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);


        findViewById(R.id.addSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create custom dialog
                 dialog = new Dialog(SettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_add_settings_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                // Initialize views in the custom dialog

                TextInputEditText etKey =dialog.findViewById(R.id.etKey);
                TextInputEditText etValue =dialog.findViewById(R.id.etValue);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);






                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!etKey.getText().toString().isEmpty()){

                            if (!etValue.getText().toString().isEmpty()){



                                    if (
                                                    etKey.getText().toString().trim().equals("map_api_key") ||
                                                    etKey.getText().toString().trim().equals("agora_app_id") ||
                                                    etKey.getText().toString().trim().equals("agora_certificate") ||
                                                    etKey.getText().toString().trim().equals("geo_places_api") ||
                                                    etKey.getText().toString().trim().equals("about_us_url") ||
                                                    etKey.getText().toString().trim().equals("privacy_url") ||
                                                    etKey.getText().toString().trim().equals("contact_url") ||
                                                    etKey.getText().toString().trim().equals("terms_url") ||
                                                    etKey.getText().toString().trim().equals("url_review_learn_more") ||
                                                    etKey.getText().toString().trim().equals("providers_url")
                                    ){

                                        Toast.makeText(SettingsActivity.this, "This Key Cant Be Added!", Toast.LENGTH_SHORT).show();

                                    }else {

                                        saveSettingsApi(etKey.getText().toString().trim(), etValue.getText().toString().trim());

                                    }


                            }else {
                                etValue.setError("Enter Value");

                            }


                        }else {

                            etKey.setError("Enter Key");

                        }



                    }
                });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                dialog.show();




            }
        });



        try {
            new GetSettings().execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void saveSettingsApi(String key, String value) {


        try {
            new SetSettings(key,value).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    private class SetSettings extends AsyncTask<Void, Void, Void> {

      String key,value;

        public SetSettings(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setSettings(key,value).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            dialog.dismiss();

                            if (response.body()!=null){

                                if (response.body().isSuccess()){

                                    Toast.makeText(SettingsActivity.this, "Data Added Successfully!", Toast.LENGTH_SHORT).show();
                                    try {
                                        new GetSettings().execute();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }



                                }else {

                                    Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }



                            }


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });

        return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

        }


    }



    private class GetSettings extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<SettingsRes> call = apiInterface.getSettings();


            call.enqueue(new Callback<SettingsRes>() {
                @Override
                public void onResponse(Call<SettingsRes> call, Response<SettingsRes> response) {

                    if (response.body() != null) {

                        SettingsRes categoryResponse = response.body();

                        if (categoryResponse.isSuccess()) {


                            SettingsAdapter settingsAdapter = new SettingsAdapter(categoryResponse.getSettings(), SettingsActivity.this, new SettingsAdapter.ItemClickListener() {
                                @Override
                                public void isUpdate(boolean isUpadate) {

                                    if (isUpadate) {

                                        try {
                                            new GetSettings().execute();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }


                                }
                            });
                            recyclerView.setAdapter(settingsAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));


                        }


                    } else {

                        runOnUiThread(() -> {
                            Log.d("NEWSERVICE", "Error 4 Res Fail");

                            Toast.makeText(SettingsActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

                        });

                    }

                }

                @Override
                public void onFailure(Call<SettingsRes> call, Throwable t) {

                    runOnUiThread(() -> {
                        Log.d("NEWSERVICE", "Error 5 "+t.getMessage());
                        Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


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


}