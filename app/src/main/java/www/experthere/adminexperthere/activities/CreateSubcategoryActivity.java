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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;

public class CreateSubcategoryActivity extends AppCompatActivity {


    TextInputEditText etSubCatName;

    ProcessingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));


        setContentView(R.layout.activity_create_subcategory);

        TextView titleTxt = findViewById(R.id.titleTxt);

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
        etSubCatName = findViewById(R.id.etSubCatName);
        dialog = new ProcessingDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){

            String id = bundle.getString("id");
            String title = bundle.getString("catName");


            titleTxt.setText("Add Subcategory For: "+title);

           findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if (id!=null && !etSubCatName.getText().toString().isEmpty()){


                       try {
                           new CreateSubcategory(Integer.parseInt(id),etSubCatName.getText().toString().trim()).execute();
                       } catch (Exception e) {
                           throw new RuntimeException(e);
                       }

                   }else {
                       etSubCatName.setError("Enter Subcategory Name!");
                       Toast.makeText(CreateSubcategoryActivity.this, "Enter Subcategory Name!", Toast.LENGTH_SHORT).show();
                   }


               }
           });
        }



    }

    private class CreateSubcategory extends AsyncTask<Void, Void, Void> {

        int id;
        String name;

        public CreateSubcategory(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters



            apiInterface.createSubcategory(id,name).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();

                            SuccessMessageResponse successMessageResponse = response.body();

                            if (successMessageResponse!=null){

                                if (successMessageResponse.isSuccess()){

                                    Toast.makeText(CreateSubcategoryActivity.this, "Created Successfully!", Toast.LENGTH_SHORT).show();

                                    finish();

                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(CreateSubcategoryActivity.this, "Fail 1", Toast.LENGTH_SHORT).show();
                                }


                            }else {

                                dialog.dismiss();
                                Toast.makeText(CreateSubcategoryActivity.this, "Fail 2", Toast.LENGTH_SHORT).show();

                            }




                            finish();


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            dialog.dismiss();
                            Toast.makeText(CreateSubcategoryActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();



                        }
                    });
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

        }


    }

}