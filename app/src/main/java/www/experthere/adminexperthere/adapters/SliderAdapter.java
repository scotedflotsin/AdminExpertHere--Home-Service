package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SliderData;
import www.experthere.adminexperthere.dataModel.SliderResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {

    List<SliderData> sliderData;
    Activity activity;

    ProcessingDialog processingDialog;

    public SliderAdapter(List<SliderData> sliderData, Activity activity) {
        this.sliderData = sliderData;
        this.activity = activity;
        processingDialog = new ProcessingDialog(activity);
    }

    @NonNull
    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(activity).load(sliderData.get(position).getImage_url()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.idTxt.setText(sliderData.get(position).getId());
        holder.typeTxt.setText(sliderData.get(position).getData());
        holder.urlTxt.setText(sliderData.get(position).getUrl());
        holder.catId.setText(sliderData.get(position).getCatId());
        holder.subcatTxt.setText(sliderData.get(position).getSubCatId());


        holder.delBtn.setOnClickListener(new View.OnClickListener() {
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
                dialogTitle.setText("Delete Image?");
                dialogMessage.setText("Are you sure you want to Delete This Image ?");


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

                            new DeleteSliderImage(sliderData.get(position).getId(), position).execute();

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
        return sliderData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView, delBtn;

        TextView typeTxt,idTxt,urlTxt,catId,subcatTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageView);
            delBtn = itemView.findViewById(R.id.deleteBtn);

            typeTxt = itemView.findViewById(R.id.typeTxt);
            idTxt = itemView.findViewById(R.id.idTxt);
            urlTxt = itemView.findViewById(R.id.urlTxt);
            catId = itemView.findViewById(R.id.catId);
            subcatTxt = itemView.findViewById(R.id.subcatTxt);

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
                Call<SuccessMessageResponse> call = apiInterface.deleteSliderImage(id);


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

                    Toast.makeText(activity, "Image Deleted Success!", Toast.LENGTH_SHORT).show();

                    sliderData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, sliderData.size());

                } else {
                    Toast.makeText(activity, "Err " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

        }

    }

}
