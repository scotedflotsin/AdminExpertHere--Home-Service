package www.experthere.adminexperthere.helperUtils;

import android.os.AsyncTask;
import retrofit2.Response;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.LoginResponse;

import java.io.IOException;

public class LoginTask extends AsyncTask<Void, Void, Response<LoginResponse>> {
    private ApiInterface apiService;
    private String email;
    private String password;
    private OnLoginListener listener;

    public LoginTask(ApiInterface apiService, String email, String password, OnLoginListener listener) {
        this.apiService = apiService;
        this.email = email;
        this.password = password;
        this.listener = listener;
    }

    @Override
    protected Response<LoginResponse> doInBackground(Void... voids) {
        try {
            // Perform the API call on the background thread
            return apiService.loginAdmin(email, password).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response<LoginResponse> response) {
        if (response != null) {
            if (response.isSuccessful()) {
                // Handle successful response
                LoginResponse loginResponse = response.body();
                listener.onLoginSuccess(loginResponse);
            } else {
                // Handle unsuccessful response
                listener.onLoginFailure("Error: " + response.code());
            }
        } else {
            // Handle network error
            listener.onLoginFailure("Network error occurred");
        }
    }

    // Interface to handle login callbacks
    public interface OnLoginListener {
        void onLoginSuccess(LoginResponse loginResponse);
        void onLoginFailure(String errorMessage);
    }
}
