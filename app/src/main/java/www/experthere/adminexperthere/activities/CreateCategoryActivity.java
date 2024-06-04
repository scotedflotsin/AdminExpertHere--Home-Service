package www.experthere.adminexperthere.activities;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.CategoryRES;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.ImagePickerBottomSheet;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;

public class CreateCategoryActivity extends AppCompatActivity {

    TextInputEditText etCatName, etSubcatName;

    boolean imageSelected;
    String dpUri;
    ImageView catImg;
    ProcessingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_create_category);


        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        dialog = new ProcessingDialog(this);
        etCatName = findViewById(R.id.etCatName);
        etSubcatName = findViewById(R.id.etSubCatName);
        catImg = findViewById(R.id.user_dp);


        findViewById(R.id.selectImgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withContext(getApplicationContext()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                            if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                selectDoc();

                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            Toast.makeText(CreateCategoryActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();

                        }
                    }).check();

                } else {
                    Dexter.withContext(getApplicationContext()).withPermissions(Manifest.permission.READ_MEDIA_IMAGES).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                            if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                selectDoc();

                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            Toast.makeText(CreateCategoryActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
                        }
                    }).check();

                }

            }
        });


        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etCatName.getText().toString().isEmpty()) {

                    if (!etSubcatName.getText().toString().isEmpty()) {


                        if (imageSelected) {

                            hitCreateCategoryApi();


                        } else {
                            Toast.makeText(CreateCategoryActivity.this, "Select Image!", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        etSubcatName.setError("Enter Subategory Name");
                    }


                } else {

                    etCatName.setError("Enter Category Name");
                }


            }
        });

    }

    private void hitCreateCategoryApi() {


        try {
            new CreateNewCategory(etCatName.getText().toString().trim(),dpUri).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



    private class CreateNewCategory extends AsyncTask<Void, Void, Void> {

        String name,img;

        public CreateNewCategory(String name, String img) {
            this.name = name;
            this.img = img;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            RequestBody nameBody = createRequestBody(name);



            MultipartBody.Part profilePicturePart = null;
            if (img != null) {

                profilePicturePart = createImagePart(img);


            }

            apiInterface.createCategory(nameBody,profilePicturePart).enqueue(new Callback<CategoryRES>() {
                @Override
                public void onResponse(Call<CategoryRES> call, Response<CategoryRES> response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            CategoryRES CategoryRES = response.body();

                            if (CategoryRES!=null){

                                if (CategoryRES.isSuccess()){


                                    int id = CategoryRES.getId();


                                    try {
                                         new CreateSubcategory(id,etSubcatName.getText().toString()).execute();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }


                                }else {

                                    Toast.makeText(CreateCategoryActivity.this, " Fail! "+CategoryRES.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }


                            }


                        }
                    });


                }

                @Override
                public void onFailure(Call<CategoryRES> call, Throwable t) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(CreateCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

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

        }


    }
    private class CreateSubcategory extends AsyncTask<Void, Void, Void> {

        int id;
        String name;

        public CreateSubcategory(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters



            apiInterface.createSubcategory(id,name).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();

                            SuccessMessageResponse successMessageResponse = response.body();

                            if (successMessageResponse!=null){

                                if (successMessageResponse.isSuccess()){

                                    Toast.makeText(CreateCategoryActivity.this, "Created Successfully!", Toast.LENGTH_SHORT).show();


                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(CreateCategoryActivity.this, "Fail 1", Toast.LENGTH_SHORT).show();
                                }


                            }else {

                                dialog.dismiss();
                                Toast.makeText(CreateCategoryActivity.this, "Fail 2", Toast.LENGTH_SHORT).show();

                            }




                            finish();


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            dialog.dismiss();
                            Toast.makeText(CreateCategoryActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();



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

        }


    }


    private void selectDoc() {


        ImagePickerBottomSheet bottomSheet = new ImagePickerBottomSheet(1010, 1016);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        bottomSheet.setImageSelectionListener(new ImagePickerBottomSheet.ImageSelectionListener() {
            @Override
            public void onImageSelected(Uri imageUri) {

                Intent intent = new Intent(CreateCategoryActivity.this, ImageCompressActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("image", "doc");
                bundle.putString("originalUri", String.valueOf(imageUri));
                bundle.putInt("length", 3);
                bundle.putInt("height", 4);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10001);


            }

            @Override
            public void onImagePath(Uri imageUri) {


                Intent intent = new Intent(CreateCategoryActivity.this, ImageCompressActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("image", "doc");
                bundle.putString("originalUri", String.valueOf(imageUri));
                bundle.putInt("length", 3);
                bundle.putInt("height", 4);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10001);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10001) {
            if (resultCode == RESULT_OK && data != null) {

                Bundle bundle = data.getExtras();

                if (bundle != null) {

                    String croppedImageUri = bundle.getString("croppedImageUri");

                    if (bundle.getString("image", "none").matches("doc")) {


                        Glide.with(getApplicationContext()).load(croppedImageUri).into(catImg);
                        dpUri = getImageRealPathFromURI(Uri.parse(croppedImageUri));

                        imageSelected = true;


                    }


                } else {
                    Toast.makeText(this, "Null Bundle", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }

    public String getImageRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return contentUri.getPath(); // Fallback to the URI itself
    }



    private RequestBody createRequestBody(String value) {
        // Check for null and return appropriate RequestBody
        if (value != null) {
            return RequestBody.create(MediaType.parse("text/plain"), value);
        } else {
            return null; // or handle it in a way that makes sense for your API
        }
    }

    private MultipartBody.Part createImagePart(String imagePath) {
        // Check if imagePath is not null
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            return MultipartBody.Part.createFormData("category_image", imageFile.getName(), requestFile);
        } else {
            // Return a default MultipartBody.Part or handle it as per your API requirements
            // For example, return an empty MultipartBody.Part
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            return MultipartBody.Part.createFormData("category_image", "", requestFile);
        }
    }

}