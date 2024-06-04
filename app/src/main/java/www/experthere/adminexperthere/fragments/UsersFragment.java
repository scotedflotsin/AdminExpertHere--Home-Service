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
import www.experthere.adminexperthere.adapters.UsersListAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.ProviderListResponse;
import www.experthere.adminexperthere.dataModel.UsersList;
import www.experthere.adminexperthere.dataModel.UsersListResponse;

public class UsersFragment extends Fragment {


    Activity activity;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    UsersListAdapter adapter;


    private boolean isLoadingproviders = false;
    private boolean isLastPageproviders = false;
    private int currentPageproviders = 1;
    private  int itemsPerPageproviders = 4;

    List<UsersList> serviceListproviders;
    SearchView searchView;



    private final BroadcastReceiver updates = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            currentPageproviders = 1;
            itemsPerPageproviders = 5;
            isLoadingproviders = false;
            isLastPageproviders = false;
            serviceListproviders.clear();
            adapter.notifyDataSetChanged();
            serviceListproviders = new ArrayList<>();

            fetchData();

        }
    };

    private void fetchData() {

        try {

            new GetUsersList(currentPageproviders,itemsPerPageproviders,"").execute();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        activity.unregisterReceiver(updates);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        serviceListproviders=new ArrayList<>();

    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        progressBar = view.findViewById(R.id.progressBar3);
        recyclerView = view.findViewById(R.id.recyclerView);

        searchView = view.findViewById(R.id.searchView);


        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("www.experthere.adminexperthere.updateUsers");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(updates, filter1, Context.RECEIVER_NOT_EXPORTED);
        }else{

            activity.registerReceiver(updates, filter1);

        }

        adapter = new UsersListAdapter(serviceListproviders, activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        progressBar.setVisibility(View.VISIBLE);

                        fetchData();
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.setVisibility(View.GONE);
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
                adapter.filter(newText);

                // Fetch data when search query is empty
                currentPageproviders = 1;
                isLoadingproviders = false;
                isLastPageproviders = false;
                serviceListproviders.clear();
                adapter.notifyDataSetChanged();

                recyclerView.setVisibility(View.GONE);

                try {


                    new GetUsersList(currentPageproviders,itemsPerPageproviders,newText).execute();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });



        fetchData();



        return view;

    }


    public class GetUsersList extends AsyncTask<Void, Void, UsersListResponse> {


        private int page;
        private int itemsPerPage;

        String query;

        public GetUsersList(int page, int itemsPerPage, String query) {
            this.page = page;
            this.itemsPerPage = itemsPerPage;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected UsersListResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<UsersListResponse> call = apiInterface.getUsersList(

                        page, // page
                        itemsPerPage // itemsPerPage
                        , query

                );


                Response<UsersListResponse> response = call.execute();

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
        protected void onPostExecute(UsersListResponse apiResponse) {
            super.onPostExecute(apiResponse);

            swipeRefreshLayout.setRefreshing(false);

            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);


            if (apiResponse != null) {
                // Handle the API response

                isLoadingproviders = false;

                List<UsersList> newServices = apiResponse.getUsers();


                if (newServices != null && !newServices.isEmpty()) {


                    if (currentPageproviders == 1) {
                        // If it's the first page, clear the old list
                        serviceListproviders.clear();
                        adapter.notifyDataSetChanged();
                    }

                    serviceListproviders.addAll(newServices);
                    adapter.setData(serviceListproviders);
                    // Move notifyDataSetChanged outside the loop
                    adapter.notifyDataSetChanged();

                    if (newServices.size() < itemsPerPageproviders) {
                        isLastPageproviders = true;
                    }
                } else if (currentPageproviders > 1) {
                    // If no new data on a subsequent page, consider it the last page
                    isLastPageproviders = true;
                } else {


                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);


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