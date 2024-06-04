package www.experthere.adminexperthere.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import www.experthere.adminexperthere.helperUtils.KeyboardUtils;
import www.experthere.adminexperthere.helperUtils.LoginTask;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdminData;
import www.experthere.adminexperthere.dataModel.LoginResponse;

public class LoginActivity extends AppCompatActivity {


    ProgressBar progressBar;
    Button loginBtn;

    TextInputEditText emailEt,passEt;


    AdminData adminData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        progressBar = findViewById(R.id.progress_bar);
        loginBtn = findViewById(R.id.loginBtn);

        emailEt = findViewById(R.id.etEmail);
        passEt = findViewById(R.id.editTextPassword);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (!emailEt.getText().toString().isEmpty() && !passEt.getText().toString().isEmpty()){



                    try {

                        progressBar.setVisibility(View.VISIBLE);
                        loginBtn.setVisibility(View.GONE);


                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);



                        LoginTask loginTask = new LoginTask(apiInterface, emailEt.getText().toString().trim(), passEt.getText().toString().trim(), new LoginTask.OnLoginListener() {
                            @Override
                            public void onLoginSuccess(LoginResponse loginResponse) {


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        loginBtn.setVisibility(View.VISIBLE);

                                        KeyboardUtils.hideKeyboard(LoginActivity.this);

                                    }
                                });


                                if (loginResponse!=null){

                                    if (loginResponse.isSuccess() ){

                                        if (loginResponse.getMessage().equals("Login successful.")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();


                                                    SharedPreferences preferences = getSharedPreferences("admin",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();


                                                    editor.putString("id", String.valueOf(loginResponse.getAdmin().getId()));
                                                    editor.putString("email",loginResponse.getAdmin().getEmail());
                                                    editor.putString("name",loginResponse.getAdmin().getName());
                                                    editor.putString("categories",loginResponse.getAdmin().getCategories());
                                                    editor.putString("providers",loginResponse.getAdmin().getProviders());
                                                    editor.putString("users",loginResponse.getAdmin().getUsers());
                                                    editor.putString("notifications",loginResponse.getAdmin().getNotifications());
                                                    editor.putString("ads",loginResponse.getAdmin().getAds());
                                                    editor.putString("settings",loginResponse.getAdmin().getSettings());
                                                    editor.putString("system_users",loginResponse.getAdmin().getSystemUsers());
                                                    editor.putString("firebase",loginResponse.getAdmin().getFirebase());
                                                    editor.putString("agora",loginResponse.getAdmin().getAgora());
                                                    editor.putString("web",loginResponse.getAdmin().getWeb());

                                                    editor.apply();


                                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                                    finish();




                                                }
                                            });
                                        }else {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Toast.makeText(LoginActivity.this, "Wrong Email or Password ", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }




                                    }else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "Login Not Success", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }

                                }else {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "Login Null", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }


                            }

                            @Override
                            public void onLoginFailure(String errorMessage) {

                            }
                        });
                        loginTask.execute();



                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }else {

                    if (emailEt.getText().toString().isEmpty()){
                        emailEt.setError("Enter Email");

                    }
                    if (passEt.getText().toString().isEmpty()){

                        passEt.setError("Enter Password");
                    }

                }

            }
        });

        findViewById(R.id.forgotBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailEt.getText().toString().isEmpty()) {

                    Bundle bundle = new Bundle();
                    bundle.putString("email", emailEt.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, ForgotPassOtpVerifyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else {
                    emailEt.setError("Enter Email");
                }


            }
        });


    }



}