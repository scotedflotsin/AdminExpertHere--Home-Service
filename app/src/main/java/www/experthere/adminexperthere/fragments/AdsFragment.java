package www.experthere.adminexperthere.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.AddSliderImageActivity;
import www.experthere.adminexperthere.activities.CreateSubcategoryActivity;
import www.experthere.adminexperthere.adapters.SliderAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdmobData;
import www.experthere.adminexperthere.dataModel.AdsResponse;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.ProviderListResponse;
import www.experthere.adminexperthere.dataModel.SliderData;
import www.experthere.adminexperthere.dataModel.SliderResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;


public class AdsFragment extends Fragment {

    Activity activity;

    List<SliderData> sliderData;

    RecyclerView recyclerView;
    SliderAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    SwitchMaterial materialSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sliderData = new ArrayList<>();
        activity = requireActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ads, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);


        materialSwitch = view.findViewById(R.id.admobSwitch);




        materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                setAdmobStatus(isChecked);


            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sliderData.clear();
                sliderData = new ArrayList<>();
                adapter.notifyDataSetChanged();

                materialSwitch.setText("Loading..");


                try {
                    new GetSliderList().execute();
                    new GetAdmobData().execute();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        });

        view.findViewById(R.id.addSliderImageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, AddSliderImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });


        try {
            new GetSliderList().execute();
            new GetAdmobData().execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return view;
    }

    private void setAdmobStatus(boolean enable) {

        String status = "disable";
        if (enable) {
            status = "enable";
        }



        materialSwitch.setText("Processing..");


        try {
            new SetAdmobStatus(1, status).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private class SetAdmobStatus extends AsyncTask<Void, Void, Void> {

        int id;
        String status;

        public SetAdmobStatus(int id, String status) {
            this.id = id;
            this.status = status;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setAdmobStatus(id, status).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (response.body() != null) {


                                if (response.body().isSuccess()) {


                                    if (status.equals("enable")){

                                        materialSwitch.setText("Admob Ads Enable!");
                                        materialSwitch.setTextColor(activity.getColor(R.color.green));

                                    }else {
                                        materialSwitch.setText("Admob Ads Disable!");
                                        materialSwitch.setTextColor(activity.getColor(R.color.red));

                                    }


                                } else {
                                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });


                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {

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

                    materialSwitch.setEnabled(true);

                    AdmobData admobData = apiResponse.getAdmob_data();

                    if (admobData.getStatus().equals("enable")) {


                        materialSwitch.setChecked(true);

                        materialSwitch.setText("Admob Ads Enable!");
                        materialSwitch.setTextColor(activity.getColor(R.color.green));


                    } else if (admobData.getStatus().equals("disable")) {

                        materialSwitch.setChecked(false);
                        materialSwitch.setText("Admob Ads Disable");
                        materialSwitch.setTextColor(activity.getColor(R.color.red));


                    }


                } else {
                    Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    materialSwitch.setEnabled(false);
                    materialSwitch.setText("Please Set Admob Values First in settings! ");
                    materialSwitch.setTextColor(activity.getColor(R.color.red));

                }

            } else {
                materialSwitch.setEnabled(false);
                materialSwitch.setText("Please Set Admob Values First in settings! ");
                materialSwitch.setTextColor(activity.getColor(R.color.red));

            }

        }

    }

    public class GetSliderList extends AsyncTask<Void, Void, SliderResponse> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected SliderResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<SliderResponse> call = apiInterface.getSliderData();


                Response<SliderResponse> response = call.execute();

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
        protected void onPostExecute(SliderResponse apiResponse) {
            super.onPostExecute(apiResponse);

            swipeRefreshLayout.setRefreshing(false);

            sliderData.addAll(apiResponse.getImage_slider_data());


            if (sliderData != null) {
                // Handle the API response
                adapter = new SliderAdapter(sliderData, activity);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(adapter);


            } else {

                Toast.makeText(activity, "No Slider Data Found", Toast.LENGTH_SHORT).show();
            }

        }

    }

}