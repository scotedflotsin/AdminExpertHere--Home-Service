package www.experthere.adminexperthere.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.SettingsActivity;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.SettingsData;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {


    List<SettingsData> settingsData;
    Activity activity;
    Dialog dialog;

    public interface ItemClickListener {
        void isUpdate(boolean isUpadate);
    }

    private ItemClickListener itemClickListener;

    public SettingsAdapter(List<SettingsData> settingsData, Activity activity, ItemClickListener itemClickListener) {
        this.settingsData = settingsData;
        this.activity = activity;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.key.setText("KEY: '' " + settingsData.get(position).getKey_name() + " ''");

        holder.value.setText("VALUE: '' " + settingsData.get(position).getValue() + " ''");

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_add_settings_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Initialize views in the custom dialog
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);


                TextInputEditText etKey = dialog.findViewById(R.id.etKey);
                TextInputEditText etValue = dialog.findViewById(R.id.etValue);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);

                etKey.setText(settingsData.get(position).getKey_name());
                etValue.setText(settingsData.get(position).getValue());


                if (etKey.getText() != null) {
                    if (
                                    etKey.getText().toString().trim().equals("map_api_key") ||
                                    etKey.getText().toString().trim().equals("agora_app_id") ||
                                    etKey.getText().toString().trim().equals("agora_certificate") ||
                                    etKey.getText().toString().trim().equals("geo_places_api") ||
                                    etKey.getText().toString().trim().equals("about_us_url") ||
                                    etKey.getText().toString().trim().equals("privacy_url") ||
                                    etKey.getText().toString().trim().equals("contact_url") ||
                                    etKey.getText().toString().trim().equals("terms_url") ||
                                    etKey.getText().toString().trim().equals("url_review_learn_more") ||
                                    etKey.getText().toString().trim().equals("providers_url")
                    ) {

                        etKey.setEnabled(false);

                    }
                }


                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!etKey.getText().toString().isEmpty()) {

                            if (!etValue.getText().toString().isEmpty()) {




                                saveSettingsApi(etKey.getText().toString().trim(), etValue.getText().toString().trim());


                            } else {
                                etValue.setError("Enter Value");

                            }


                        } else {

                            etKey.setError("Enter Key");

                        }


                    }
                });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();


            }
        });


    }

    private void saveSettingsApi(String key, String value) {

        try {
            new SetSettings(key, value).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return settingsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView key, value;
        Button editBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            key = itemView.findViewById(R.id.keyTxt);
            value = itemView.findViewById(R.id.valueTxt);
            editBtn = itemView.findViewById(R.id.editCat);

        }
    }


    private class SetSettings extends AsyncTask<Void, Void, Void> {

        String key, value;

        public SetSettings(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            apiInterface.setSettings(key, value).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();

                            if (response.body() != null) {

                                if (response.body().isSuccess()) {

                                    Toast.makeText(activity, "Data Updated Successfully!", Toast.LENGTH_SHORT).show();

                                    itemClickListener.isUpdate(true);


                                } else {

                                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
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

}
