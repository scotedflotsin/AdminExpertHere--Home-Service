package www.experthere.adminexperthere.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import www.experthere.adminexperthere.helperUtils.EmailMaskUtil;
import www.experthere.adminexperthere.helperUtils.ExitBottomSheet;
import www.experthere.adminexperthere.helperUtils.KeyboardUtils;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;

public class PasswordResetActivity extends AppCompatActivity {


    String email = " ";
    boolean isProvider = false;
    TextView emailTxt;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExitBottomSheet exitBottomSheet = new ExitBottomSheet(true);
        exitBottomSheet.show(getSupportFragmentManager(), exitBottomSheet.getTag());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_reset);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });






        emailTxt = findViewById(R.id.emailTxt);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            email = bundle.getString("email").toLowerCase();
            isProvider = bundle.getBoolean("isProvider", false);

            EmailMaskUtil.maskEmailAndSetToTextView(email, emailTxt);
        }

        EditText passEt = findViewById(R.id.passEt);
        TextInputEditText confirmPassEt = findViewById(R.id.confirmPassEt);


        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!passEt.getText().toString().isEmpty() && !confirmPassEt.getText().toString().isEmpty()) {

                    if (passEt.getText().toString().equals(confirmPassEt.getText().toString())) {


                        showConfirmDialog(  email, passEt.getText().toString().trim());


                    } else {


                        Log.d("JGJGHJGJH", "pass: " + passEt.getText().toString());
                        Log.d("JGJGHJGJH", "confirm Pass: " + confirmPassEt.getText().toString());


                        Toast.makeText(PasswordResetActivity.this, "Password Don't Match!", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(PasswordResetActivity.this, "Enter Password And Confirm Password!", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void showConfirmDialog( String email, String newPass) {


        // Create custom dialog
        Dialog dialog = new Dialog(PasswordResetActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize views in the custom dialog
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        btnDelete.setText("Reset");

        // Set dialog title and message
        dialogTitle.setText("Reset Password?");
        dialogMessage.setText("Are you sure you want to Reset Password ?");


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

                    new UpdateAdminPassword(getApplicationContext(),PasswordResetActivity.this,email,newPass).execute();

                    KeyboardUtils.hideKeyboard(PasswordResetActivity.this);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                dialog.dismiss();
            }
        });

        dialog.show();





    }


    private static class UpdateAdminPassword extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final Activity activity;
        private final String email;
        private final String newValue;


        UpdateAdminPassword(Context context, Activity activity, String email, String newValue) {
            this.context = context;
            this.email = email;
            this.newValue = newValue;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.updatePasswordAdmin(email, newValue).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {
                    if (response.isSuccessful()) {
                        SuccessMessageResponse apiResponse = response.body();

                        if (apiResponse != null && apiResponse.isSuccess()) {
                            
                            
                            if (apiResponse.getMessage().equals("Password updated successfully.")){


                                // Handle success, if needed

                              activity.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                     activity. finish();
                                      Toast.makeText(context, "Password Reset Successful!", Toast.LENGTH_SHORT).show();


                                  }
                              });
                                
                            }else {

                                Toast.makeText(context, "Cant Update Password Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                            
                          


                        } else {

                                Toast.makeText(context, "Error updating providers details:", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "Error Getting Response!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {
                    Toast.makeText(context, "Server Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion
            // You can perform any UI updates or navigate to another activity if needed
        }

    }


}