package www.experthere.adminexperthere.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import www.experthere.adminexperthere.helperUtils.BottomSheetFragment;
import www.experthere.adminexperthere.helperUtils.EmailMaskUtil;
import www.experthere.adminexperthere.helperUtils.ExitBottomSheet;
import www.experthere.adminexperthere.helperUtils.OTPGenerator;
import www.experthere.adminexperthere.listeners.OnSubmitListener;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.helperUtils.SendOtpTask;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;

public class ForgotPassOtpVerifyActivity extends AppCompatActivity implements OnSubmitListener {

    long duration = 90000;
    long interval = 1000;
    TextView otpTimerTxt, resendTxt, emailForOtp;
    private CountDownTimer countDownTimer;
    String sentOtp, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_forgot_pass_otp_verify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getDataFromIntentBundle();

        emailForOtp = findViewById(R.id.emailForOtp);
        otpTimerTxt = findViewById(R.id.otpTimerTxt);
        resendTxt = findViewById(R.id.resendTxt);
        TextInputEditText otpEt = findViewById(R.id.otpEt);

        resendTxt.setEnabled(false);
        resendTxt.setClickable(false);


        if (!email.isEmpty()) {
            EmailMaskUtil.maskEmailAndSetToTextView(email, emailForOtp);
            sendOtp(email);
            startCountdownTimer(duration, interval);

        }

        resendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendOtp(email);
                startCountdownTimer(duration, interval);
                otpTimerTxt.setVisibility(View.VISIBLE);
                resendTxt.setEnabled(false);
                resendTxt.setClickable(false);
                resendTxt.setText("Resend in ");

            }
        });

        findViewById(R.id.changeEmailTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Show the BottomSheetFragment when needed
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

            }
        });

        findViewById(R.id.completeVerificationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!otpEt.getText().toString().isEmpty()) {
                    if (sentOtp.matches(otpEt.getText().toString().trim())) {

                        Toast.makeText(ForgotPassOtpVerifyActivity.this, "Verification Complete!", Toast.LENGTH_SHORT).show();

                        openResetPassActivity();


                    } else {

                        Toast.makeText(ForgotPassOtpVerifyActivity.this, "Wrong OTP !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    otpEt.setError("Enter Otp");
                }
            }
        });
    }


    private void openResetPassActivity() {

        Bundle bundle = new Bundle();

        bundle.putString("email", email);


        Intent intent = new Intent(ForgotPassOtpVerifyActivity.this, PasswordResetActivity.class);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();


    }

    private void sendOtp(String mail) {

        String OTP = OTPGenerator.generateOTP();
        sentOtp = OTP;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        SendOtpTask sendOtpTask = new SendOtpTask(apiInterface, new SendOtpTask.SendOtpListener() {
            @Override
            public void onSendOtpSuccess(String res) {
                // Handle success in the UI thread
                runOnUiThread(() -> {
                    // Update UI or perform any success-related tasks
                    Toast.makeText(ForgotPassOtpVerifyActivity.this, "Code Sent Successfully!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onSendOtpFailure(String errorMessage) {
                // Handle failure in the UI thread
                runOnUiThread(() -> {
                    // Update UI or perform any failure-related tasks
                    System.out.println(errorMessage);
                    Toast.makeText(ForgotPassOtpVerifyActivity.this, "Error - " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });

// Execute the task with parameters
        sendOtpTask.execute(mail, " ", OTP, "OTP Verification : Expert Here");


    }

    private void getDataFromIntentBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            email = bundle.getString("email");


        }
    }


    // Declare a global variable to hold the reference to the CountDownTimer

    private void startCountdownTimer(long duration, long interval) {
        // Cancel the existing timer if it's running
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Create a new countdown timer
        countDownTimer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate minutes and seconds
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                // Update the EditText with the remaining time in "minute:seconds" format
                otpTimerTxt.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                // Do work when the timer finishes

                // Optionally, reset the EditText to an initial state
                otpTimerTxt.setText("");
                otpTimerTxt.setVisibility(View.GONE);
                resendTxt.setText("Resend Now!");
                resendTxt.setEnabled(true);
                resendTxt.setClickable(true);
            }
        };

        // Start the new countdown timer
        countDownTimer.start();
    }


    @Override
    public void onSubmit(String emails) {


        email = emails;
        sendOtp(email);


        EmailMaskUtil.maskEmailAndSetToTextView(email, emailForOtp);
        otpTimerTxt.setText("");
        resendTxt.setText("Resend in ");

        otpTimerTxt.setVisibility(View.VISIBLE);
        resendTxt.setEnabled(false);
        resendTxt.setClickable(false);
        startCountdownTimer(duration, interval);


    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        ExitBottomSheet exitBottomSheet = new ExitBottomSheet(true);
        exitBottomSheet.show(getSupportFragmentManager(), exitBottomSheet.getTag());


    }
}