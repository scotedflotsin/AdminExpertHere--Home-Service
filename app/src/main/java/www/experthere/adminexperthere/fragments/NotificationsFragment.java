package www.experthere.adminexperthere.fragments;

import static android.app.Activity.RESULT_OK;

import static www.experthere.adminexperthere.api.ApiClient.BASE_URL;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import www.experthere.adminexperthere.FCM.FcmApiClient;
import www.experthere.adminexperthere.FCM.FcmBearerTokenGenerator;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.activities.CreateCategoryActivity;
import www.experthere.adminexperthere.activities.ImageCompressActivity;
import www.experthere.adminexperthere.adapters.ProviderListAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.CategoryRES;
import www.experthere.adminexperthere.dataModel.NotificationImageResponse;
import www.experthere.adminexperthere.helperUtils.ImagePickerBottomSheet;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;


public class NotificationsFragment extends Fragment {

    Activity activity;

    boolean toSelected, isImageSelected;

    String sendToString;

    TextInputEditText etTitle, etMessage;
    AppCompatSpinner spinner;
    ProcessingDialog dialog;

    ImageView imageView;

    String imageUri,fullImageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
        dialog = new ProcessingDialog(activity);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etMessage = view.findViewById(R.id.etMessage);
        imageView = view.findViewById(R.id.user_dp);


        spinner = view.findViewById(R.id.spinner);
        List<String> items = new ArrayList<>();
        items.add("Select Option");
        items.add("all");
        items.add("users");
        items.add("providers");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {
                    toSelected = true;
                    sendToString = parent.getItemAtPosition(position).toString();


                } else {
                    toSelected = false;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        view.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTitle.getText().toString().isEmpty()) {

                    if (!etMessage.getText().toString().isEmpty()) {


                        if (toSelected) {

                            if (isImageSelected) {

                                sendNotification(etTitle.getText().toString().trim(), etMessage.getText().toString().trim(), sendToString, true);


                            } else {

                                sendNotification(etTitle.getText().toString().trim(), etMessage.getText().toString().trim(), sendToString, false);
                            }

                        } else {

                            Toast.makeText(activity, "Select Option!", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        etMessage.setError("Enter Message");
                    }

                } else {
                    etTitle.setError("Enter Title");
                }


            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withContext(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                            if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                selectDoc();

                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            Toast.makeText(activity, "Permission Required", Toast.LENGTH_SHORT).show();

                        }
                    }).check();

                } else {
                    Dexter.withContext(activity).withPermissions(Manifest.permission.READ_MEDIA_IMAGES).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                            if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                selectDoc();

                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            Toast.makeText(activity, "Permission Required", Toast.LENGTH_SHORT).show();
                        }
                    }).check();

                }


            }
        });


        return view;

    }

    private void sendNotification(String title, String message, String selectedOption, boolean imageSelected) {


        dialog.show();


        FcmBearerTokenGenerator fcmBearerTokenGenerator = new FcmBearerTokenGenerator(activity);
        fcmBearerTokenGenerator.generateAccessTokenAsync(new FcmBearerTokenGenerator.TokenGenerationListener() {
            @Override
            public void onTokenGenerated(String token) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Map<String, String> extraData = new HashMap<>();
                        extraData.put("event", "APP_NOTIFICATION");
                        extraData.put("title", title);
                        extraData.put("message", message);

                        if (imageSelected){
                            extraData.put("image",fullImageUrl);

                            Log.d("ckjcbdiwjcb", "img url: "+fullImageUrl);

                        }else {
                            extraData.put("image","null");
                        }


                        FcmApiClient.sendFcmMessageToTopic(getString(R.string.FcmSendUrl), token, selectedOption, title, message, extraData, "SPLASH", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(activity, "E2 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });


                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        dialog.dismiss();

                                        if (response.isSuccessful()) {

                                            Toast.makeText(activity, "Notification Sent!", Toast.LENGTH_SHORT).show();
                                            etMessage.setText("");
                                            etTitle.setText("");
                                            toSelected = false;
                                            sendToString = "";
                                            spinner.setSelection(0);

                                        } else {
                                            Toast.makeText(activity, " Error!", Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });


                            }
                        });

                    }
                });


            }

            @Override
            public void onTokenGenerationFailed(Exception e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }


    private void selectDoc() {


        ImagePickerBottomSheet bottomSheet = new ImagePickerBottomSheet(1010, 1016);
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        bottomSheet.setImageSelectionListener(new ImagePickerBottomSheet.ImageSelectionListener() {
            @Override
            public void onImageSelected(Uri imageUri) {

                Intent intent = new Intent(activity, ImageCompressActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("image", "doc");
                bundle.putString("originalUri", String.valueOf(imageUri));
                bundle.putInt("length", 16);
                bundle.putInt("height", 9);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10001);


            }

            @Override
            public void onImagePath(Uri imageUri) {


                Intent intent = new Intent(activity, ImageCompressActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("image", "doc");
                bundle.putString("originalUri", String.valueOf(imageUri));
                bundle.putInt("length", 16);
                bundle.putInt("height", 9);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10001);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10001) {
            if (resultCode == RESULT_OK && data != null) {

                Bundle bundle = data.getExtras();

                if (bundle != null) {

                    String croppedImageUri = bundle.getString("croppedImageUri");

                    if (bundle.getString("image", "none").matches("doc")) {


                         imageUri = getImageRealPathFromURI(Uri.parse(croppedImageUri));


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
                        btnDelete.setText("Upload");

                        // Set dialog title and message
                        dialogTitle.setText("Upload Image?");
                        dialogMessage.setText("Are you sure you want to Upload Selected Image To Server For Sending Notitifications ?");


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

                                Glide.with(activity).load(croppedImageUri).into(imageView);
                                isImageSelected = true;

                                uploadImage();


                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                    }


                } else {
                    Toast.makeText(activity, "Null Bundle", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private void uploadImage() {


        try {
            new UploadImage(imageUri).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {

        String img;

        public UploadImage(String img) {
            this.img = img;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters





            MultipartBody.Part  profilePicturePart = createImagePart(img);




            apiInterface.uploadImageNotification(profilePicturePart).enqueue(new retrofit2.Callback<NotificationImageResponse>() {
                @Override
                public void onResponse(retrofit2.Call<NotificationImageResponse> call, retrofit2.Response<NotificationImageResponse> response) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (response.body()!=null){


                                if (response.body().isSuccess()){

                                    Toast.makeText(activity, "Image Uploaded !", Toast.LENGTH_SHORT).show();

                                     fullImageUrl = BASE_URL+"Notifications/NotificationImages/"+response.body().getImage_name();




                                }else {
                                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }



                            }



                        }
                    });

                }

                @Override
                public void onFailure(retrofit2.Call<NotificationImageResponse> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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


            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // This method runs on the UI thread and can be used to update the UI after background task completion

            dialog.dismiss();

        }


    }

    private MultipartBody.Part createImagePart(String imagePath) {
        // Check if imagePath is not null
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            return MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        } else {
            // Return a default MultipartBody.Part or handle it as per your API requirements
            // For example, return an empty MultipartBody.Part
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            return MultipartBody.Part.createFormData("image", "", requestFile);
        }
    }


    public String getImageRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return contentUri.getPath(); // Fallback to the URI itself
    }
}