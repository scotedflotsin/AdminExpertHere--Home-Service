package www.experthere.adminexperthere.helperUtils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.Statistics;
import www.experthere.adminexperthere.dataModel.StatisticsResponse;

public class ApiTask extends AsyncTask<String, Void, StatisticsResponse> {
    private static final String TAG = "ApiTask";
    private final ApiTaskListener listener;

    public ApiTask(ApiTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected StatisticsResponse doInBackground(String... params) {
        String key = params[0];



        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StatisticsResponse> call = apiInterface.getStatistics(key);

        try {
            Response<StatisticsResponse> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                Log.e(TAG, "Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error executing request: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(StatisticsResponse response) {
        if (listener != null) {
            if (response != null && response.isSuccess()) {
                listener.onSuccess(response.getStatistics());
            } else {
                listener.onError("Failed to fetch data");
            }
        }
    }

    public interface ApiTaskListener {
        void onSuccess(Statistics statistics);
        void onError(String errorMessage);
    }
}
