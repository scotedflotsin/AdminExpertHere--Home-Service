package www.experthere.adminexperthere.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.dataModel.Taxes;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.ViewHolder> {

    ArrayList<Taxes> taxes;
    Activity activity;

    ProcessingDialog processingDialog;

    public TaxAdapter(ArrayList<Taxes> taxes, Activity activity) {
        this.taxes = taxes;
        this.activity = activity;

        processingDialog = new ProcessingDialog(activity);
    }

    @NonNull
    @Override
    public TaxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tax_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxAdapter.ViewHolder holder, int position) {


        holder.tax.setText(taxes.get(position).getPercentage());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                // Create custom dialog
                Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_delete_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Initialize views in the custom dialog
                TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
                TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnDelete = dialog.findViewById(R.id.btnDelete);
                btnDelete.setText("Delete");

                // Set dialog title and message
                dialogTitle.setText("Delete Tax?");
                dialogMessage.setText("Are you sure you want to Delete This Tax ?");


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

                            new DeleteSliderImage(taxes.get(position).getId(),position).execute();


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        dialog.dismiss();
                    }
                });

                dialog.show();




            }
        });


    }




    @Override
    public int getItemCount() {
        return taxes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tax;
        Button deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tax = itemView.findViewById(R.id.keyTxt);
            deleteBtn = itemView.findViewById(R.id.editCat);

        }




    }


    public class DeleteSliderImage extends AsyncTask<Void, Void, SuccessMessageResponse> {

        String id;
        int position;

        public DeleteSliderImage(String id, int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            processingDialog.show();
        }

        @Override
        protected SuccessMessageResponse doInBackground(Void... voids) {
            try {
                // Create the Retrofit instance
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                // Make the API call
                Call<SuccessMessageResponse> call = apiInterface.deleteTax(id);


                Response<SuccessMessageResponse> response = call.execute();

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
        protected void onPostExecute(SuccessMessageResponse apiResponse) {
            super.onPostExecute(apiResponse);

            processingDialog.dismiss();
            if (apiResponse != null) {

                if (apiResponse.isSuccess()) {

                    Toast.makeText(activity, "Tax Deleted Success!", Toast.LENGTH_SHORT).show();

                    taxes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, taxes.size());

                } else {
                    Toast.makeText(activity, "Err " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

        }

    }



}
