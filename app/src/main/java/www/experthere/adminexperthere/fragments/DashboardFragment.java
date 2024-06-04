package www.experthere.adminexperthere.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.DeactiveProvidersActivity;
import www.experthere.adminexperthere.dataModel.Statistics;
import www.experthere.adminexperthere.helperUtils.ApiTask;


public class DashboardFragment extends Fragment implements ApiTask.ApiTaskListener{

    Activity activity;

    TextView totalUsersTxt,totalServiceProvidersTxt,totalCustomersTxt,deactiveServiceProvidersTxt;

    SwipeRefreshLayout swipeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        swipeLayout = view.findViewById(R.id.swipeLayout);

        totalUsersTxt = view.findViewById(R.id.totalUsersTxt);
        totalServiceProvidersTxt = view.findViewById(R.id.totalServiceProvidersTxt);
        totalCustomersTxt = view.findViewById(R.id.totalCustomersTxt);
        deactiveServiceProvidersTxt = view.findViewById(R.id.deactiveServiceProvidersTxt);


        view.findViewById(R.id.deactiveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, DeactiveProvidersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);


            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchData();


            }
        });


        fetchData();



        return view;

    }


    private void fetchData() {
        ApiTask apiTask = new ApiTask(this);
        apiTask.execute(getString(R.string.api_secrete));
    }

    @Override
    public void onSuccess(Statistics statistics) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                swipeLayout.setRefreshing(false);
                setData(statistics);


            }
        });

    }

    private void setData(Statistics statistics) {


        int totalCustomers = statistics.getTotal_users();
        int totalProviders = statistics.getTotal_providers();
        int deactiveProviders = statistics.getDeactive_providers();
        int totalUsers = totalCustomers +  totalProviders;


        totalUsersTxt.setText(String.valueOf(totalUsers));
        totalServiceProvidersTxt.setText(String.valueOf(totalProviders));
        totalCustomersTxt.setText(String.valueOf(totalCustomers));
        deactiveServiceProvidersTxt.setText(String.valueOf(deactiveProviders));

    }

    @Override
    public void onError(String errorMessage) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeLayout.setRefreshing(false);

                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

    }
}