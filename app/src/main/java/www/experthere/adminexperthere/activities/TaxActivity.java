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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SettingsAdapter;
import www.experthere.adminexperthere.adapters.TaxAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SettingsRes;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.dataModel.TaxApiResponse;
import www.experthere.adminexperthere.dataModel.Taxes;

public class TaxActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    Dialog dialog;

    ArrayList<Taxes> taxes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_tax);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);

        new GetTax(TaxActivity.this).execute();


        findViewById(R.id.addSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Create custom dialog
                dialog = new Dialog(TaxActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_tax_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                // Initialize views in the custom dialog

                TextInputEditText etKey = dialog.findViewById(R.id.etKey);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);


                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!etKey.getText().toString().isEmpty()) {


                            new SetTax(TaxActivity.this, etKey.getText().toString().trim()).execute();

                            dialog.dismiss();


                        } else {

                            etKey.setError("Enter Tax Value");

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


    }


    private class SetTax extends AsyncTask<Void, Void, Void> {

        Activity activity;
        String value;

        public SetTax(Activity activity, String value) {
            this.activity = activity;
            this.value = value;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<SuccessMessageResponse> call = apiInterface.setTax(value);

            call.enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        SuccessMessageResponse taxApiResponse = response.body();

                        if (taxApiResponse.isSuccess()) {


                            Toast.makeText(TaxActivity.this, "Tax Added Success!", Toast.LENGTH_SHORT).show();
                            taxes.clear();
                            new GetTax(TaxActivity.this).execute();

                        }


                    } else {

                        Log.d("NEWSERVICE", "Error 2 RES FAIL ");


                    }

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {
                    activity.runOnUiThread(() -> {
                        Log.d("NEWSERVICE", "Error 3 " + t.getMessage());


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

    private class GetTax extends AsyncTask<Void, Void, Void> {

        Activity activity;

        public GetTax(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<TaxApiResponse> call = apiInterface.getAllTaxes();

            call.enqueue(new Callback<TaxApiResponse>() {
                @Override
                public void onResponse(Call<TaxApiResponse> call, Response<TaxApiResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        TaxApiResponse taxApiResponse = response.body();

                        if (taxApiResponse.isSuccess()) {

                            taxes.addAll(taxApiResponse.getTaxes());


                            TaxAdapter taxAdapter = new TaxAdapter(taxes, TaxActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(TaxActivity.this);

                            recyclerView.setAdapter(taxAdapter);
                            recyclerView.setLayoutManager(layoutManager);


                        }


                    } else {

                        Log.d("NEWSERVICE", "Error 2 RES FAIL ");


                    }

                }

                @Override
                public void onFailure(Call<TaxApiResponse> call, Throwable t) {
                    activity.runOnUiThread(() -> {
                        Log.d("NEWSERVICE", "Error 3 " + t.getMessage());


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