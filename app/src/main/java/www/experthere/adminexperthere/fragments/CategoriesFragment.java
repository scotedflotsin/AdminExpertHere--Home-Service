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
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.CreateCategoryActivity;
import www.experthere.adminexperthere.adapters.CatAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.Category;
import www.experthere.adminexperthere.dataModel.CategoryResponse;

public class CategoriesFragment extends Fragment {

   Activity activity;

   RecyclerView recyclerView;
   CatAdapter adapter;
    ArrayList<Category> categories;

    SwipeRefreshLayout refreshLay;

    ProgressBar progressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        categories = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLay = view.findViewById(R.id.refreshLay);
        progressBar = view.findViewById(R.id.progressBar);

        view.findViewById(R.id.createCatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, CreateCategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });
        refreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                categories.clear();
                categories = new ArrayList<>();

                try {
                    new GetCategories(activity).execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        });



        try {
             new GetCategories(activity).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return view;

    }



    private class GetCategories extends AsyncTask<Void, Void, Void> {

        Activity activity;

        public GetCategories(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation, e.g., user

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<CategoryResponse> call = apiInterface.getCategories();


            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            refreshLay.setRefreshing(false);

                        }
                    });


                    if (response.isSuccessful() && response.body() != null) {

                        CategoryResponse categoryResponse = response.body();

                        if (categoryResponse.isSuccess()) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    categories.addAll(categoryResponse.getCategories());
                                    adapter = new CatAdapter( activity,categories);
                                    recyclerView.setAdapter(adapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }
                            });


                        } else {
                            activity.runOnUiThread(() -> {
                                Log.d("ndlndldns", "Error Getting Categories!");

                                progressBar.setVisibility(View.GONE);

                            });


                        }


                    } else {

                        activity.runOnUiThread(() -> {
                            Log.d("ndlndldns", "Category Response Fail");

                            progressBar.setVisibility(View.GONE);

                        });

                    }

                }

                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {

                    activity.runOnUiThread(() -> {
                        Log.d("ndlndldns", "Category Error Fail "+t.getMessage());
                        refreshLay.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);

                    });

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion
        }
    }



}