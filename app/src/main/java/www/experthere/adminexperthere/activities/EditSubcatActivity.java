package www.experthere.adminexperthere.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;

public class EditSubcatActivity extends AppCompatActivity {

    TextInputEditText subCatEt;
    TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_edit_subcat);


        titleTxt = findViewById(R.id.titleTxt);
        subCatEt = findViewById(R.id.etCatName);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String id = bundle.getString("id");
            String subCatName = bundle.getString("name");


            titleTxt.setText("Edit: " + subCatName);

            subCatEt.setText(subCatName);


            findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!subCatEt.getText().toString().isEmpty()) {

                        startProcess(id);


                    } else {
                        subCatEt.setError("Enter Subcategory Name");
                    }
                }
            });


        }


    }

    private void startProcess(String id) {

        try {

            new SubcategoryUpdate(id, subCatEt.getText().toString().trim()).execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    private class SubcategoryUpdate extends AsyncTask<Void, Void, Void> {

        String id, name;

        public SubcategoryUpdate(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.updateSubcategory(id, name).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SuccessMessageResponse successMessageResponse = response.body();
                            if (successMessageResponse != null) {

                                if (successMessageResponse.isSuccess()) {

                                    Toast.makeText(EditSubcatActivity.this, "Subcategory Updated Successfully!", Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(EditSubcatActivity.this, "Error Updating!", Toast.LENGTH_SHORT).show();
                                }

                                finish();

                            }else {
                                Toast.makeText(EditSubcatActivity.this, "Error Updating Sub Category!", Toast.LENGTH_SHORT).show();

                                finish();

                            }


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(EditSubcatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

}