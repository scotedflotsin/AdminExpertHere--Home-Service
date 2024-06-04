package www.experthere.adminexperthere.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.ProviderListAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.ProviderListResponse;


public class ProvidersFragment extends Fragment {

    Activity activity;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView providersRecyclerView;
    ProgressBar providersProgressBar;
    ProviderListAdapter providersAdapter;


    private boolean isLoadingproviders = false;
    private boolean isLastPageproviders = false;
    private int currentPageproviders = 1;
    private  int itemsPerPageproviders = 4;

    List<ProviderList> serviceListproviders;
    SearchView searchView;



    private final BroadcastReceiver updates = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            currentPageproviders = 1;
            itemsPerPageproviders = 5;
            isLoadingproviders = false;
            isLastPageproviders = false;
            serviceListproviders.clear();
            providersAdapter.notifyDataSetChanged();
            serviceListproviders = new ArrayList<>();

            fetchData();

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activity = requireActivity();
        serviceListproviders = new ArrayList<>();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        activity.unregisterReceiver(updates);
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_providers, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        providersProgressBar = view.findViewById(R.id.progressBar2);
        providersRecyclerView = view.findViewById(R.id.recyclerView);

        searchView = view.findViewById(R.id.searchView);


        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("www.experthere.adminexperthere.updateProviders");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(updates, filter1, Context.RECEIVER_NOT_EXPORTED);
        }else {

            activity.registerReceiver(updates, filter1);

        }

        providersAdapter = new ProviderListAdapter(serviceListproviders, activity);
        providersRecyclerView.setAdapter(providersAdapter);
        providersRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        providersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoadingproviders && !isLastPageproviders) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= itemsPerPageproviders) {
                        // Load more data when reaching the end

                        isLoadingproviders = true;
                        currentPageproviders++;
                        providersProgressBar.setVisibility(View.VISIBLE);

                        fetchData();
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                providersRecyclerView.setVisibility(View.GONE);
                isLoadingproviders = false;
                isLastPageproviders = false;
                currentPageproviders = 1;
                itemsPerPageproviders = 4;

                serviceListproviders.clear();
                serviceListproviders=new ArrayList<>();

                fetchData();

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                providersAdapter.filter(newText);

                // Fetch data when search query is empty
                currentPageproviders = 1;
                isLoadingproviders = false;
                isLastPageproviders = false;
                serviceListproviders.clear();
                providersAdapter.notifyDataSetChanged();

                providersRecyclerView.setVisibility(View.GONE);

                try {
                    new GetProvidersList(currentPageproviders, itemsPerPageproviders, newText).execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });



        fetchData();


        return view;

    }

    private void fetchData() {

        try {
            new GetProvidersList(currentPageproviders, itemsPerPageproviders, "").execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public class GetProvidersList extends AsyncTask<Void, Void, ProviderListResponse> {


        private int page;
        private int itemsPerPage;

        String query;

        public GetProvidersList(int page, int itemsPerPage, String query) {
            this.page = page;
            this.itemsPerPage = itemsPerPage;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ProviderListResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<ProviderListResponse> call = apiInterface.getProvidersList(

                        page, // page
                        itemsPerPage // itemsPerPage
                        , query

                );


                Response<ProviderListResponse> response = call.execute();

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
        protected void onPostExecute(ProviderListResponse apiResponse) {
            super.onPostExecute(apiResponse);

            swipeRefreshLayout.setRefreshing(false);

            providersProgressBar.setVisibility(View.GONE);
            providersRecyclerView.setVisibility(View.VISIBLE);


            if (apiResponse != null) {
                // Handle the API response

                isLoadingproviders = false;

                List<ProviderList> newServices = apiResponse.getProviders();


                if (newServices != null && !newServices.isEmpty()) {


                    if (currentPageproviders == 1) {
                        // If it's the first page, clear the old list
                        serviceListproviders.clear();
                        providersAdapter.notifyDataSetChanged();
                    }

                    serviceListproviders.addAll(newServices);
                    providersAdapter.setData(serviceListproviders);
                    // Move notifyDataSetChanged outside the loop
                    providersAdapter.notifyDataSetChanged();

                    if (newServices.size() < itemsPerPageproviders) {
                        isLastPageproviders = true;
                    }
                } else if (currentPageproviders > 1) {
                    // If no new data on a subsequent page, consider it the last page
                    isLastPageproviders = true;
                } else {


                    providersProgressBar.setVisibility(View.GONE);
                    providersRecyclerView.setVisibility(View.GONE);


                }

                // Notify the adapter outside the if condition
//                adapter.notifyDataSetChanged();
            } else {
                // Handle error
                Toast.makeText(activity, "Error 1", Toast.LENGTH_SHORT).show();
            }
        }

    }

}