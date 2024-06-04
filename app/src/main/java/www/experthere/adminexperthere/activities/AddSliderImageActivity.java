package www.experthere.adminexperthere.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.CatAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.Category;
import www.experthere.adminexperthere.dataModel.CategoryRES;
import www.experthere.adminexperthere.dataModel.CategoryResponse;
import www.experthere.adminexperthere.dataModel.Subcategory;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.helperUtils.CatBottomSheet;
import www.experthere.adminexperthere.helperUtils.ImagePickerBottomSheet;
import www.experthere.adminexperthere.helperUtils.ProcessingDialog;
import www.experthere.adminexperthere.helperUtils.SubCatBottomSheet;

public class AddSliderImageActivity extends AppCompatActivity {


    boolean isOptionSelected, isLink, imageSelected, isCat, isSubcat;
    String dpUri, optionTxt;
    TextInputEditText etLink;


    boolean isCatSelected,isSubCatSelected;
    ProcessingDialog dialog;
    ArrayList<Category> categories = new ArrayList<>();

    List<Subcategory> subCategoriesList = new ArrayList<>();

    String selectedCat,selectedCatID,selectedSubCat,selectedSubCatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));


        setContentView(R.layout.activity_add_slider_image);

        etLink = findViewById(R.id.etLink);
        dialog = new ProcessingDialog(this);
        AppCompatSpinner spinner = findViewById(R.id.spinner);
        List<String> items = new ArrayList<>();
        items.add("Select Option");
        items.add("Category");
        items.add("Subcategory");
        items.add("External Link");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    String selectedOption = parent.getItemAtPosition(position).toString();

                    optionTxt = selectedOption;
                    isOptionSelected = true;


                    if (selectedOption.equals("External Link")) {
                        findViewById(R.id.emailTextInputLayout).setVisibility(View.GONE);
                        findViewById(R.id.catLay).setVisibility(View.GONE);
                        findViewById(R.id.emailTextInputLayout).setVisibility(View.VISIBLE);
                        isLink = true;
                        isCat = false;
                        isSubcat = false;


                    } else if (selectedOption.equals("Category")) {

                        isCat = true;
                        findViewById(R.id.emailTextInputLayout).setVisibility(View.GONE);
                        findViewById(R.id.subCatLay).setVisibility(View.GONE);
                        isLink = false;
                        isSubcat = false;
                        findViewById(R.id.catLay).setVisibility(View.VISIBLE);

                    } else if (selectedOption.equals("Subcategory")) {

                        isSubcat = true;
                        findViewById(R.id.emailTextInputLayout).setVisibility(View.GONE);
                        isLink = false;
                        isCat = false;
                        findViewById(R.id.catLay).setVisibility(View.VISIBLE);

                    }

                } else {
                    findViewById(R.id.emailTextInputLayout).setVisibility(View.GONE);
                    findViewById(R.id.catLay).setVisibility(View.GONE);
                    findViewById(R.id.subCatLay).setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                            Toast.makeText(AddSliderImageActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AddSliderImageActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
                        }
                    }).check();

                }

            }
        });


        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageSelected) {


                    if (isOptionSelected) {


                        if (isLink) {

                            if (!etLink.getText().toString().isEmpty()) {
                                startApi(optionTxt, etLink.getText().toString().trim(), dpUri,null,null);

                            } else {
                                etLink.setError("Enter Link");
                            }


                        } else if (isCat){


                            if (isCatSelected){

                                startApi(optionTxt, "www.experthere.in", dpUri,selectedCatID,null);

                            }else {
                                Toast.makeText(AddSliderImageActivity.this, "Select Category!", Toast.LENGTH_SHORT).show();
                            }





                        } else if (isSubcat) {

                            if (isCatSelected && isSubCatSelected){


                                startApi(optionTxt, "www.experthere.in", dpUri,selectedCatID,selectedSubCatID);


                            }else {
                                Toast.makeText(AddSliderImageActivity.this, "Please Select Category and Subcategory!", Toast.LENGTH_LONG).show();
                            }


                        }


                    } else {

                        Toast.makeText(AddSliderImageActivity.this, "Select Option!", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(AddSliderImageActivity.this, "Select Image!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.catLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCatDialog();

            }
        });   findViewById(R.id.subCatLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSubCatDialog();

            }
        });


        try {
            new GetCategories(AddSliderImageActivity.this).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void showSubCatDialog() {

        SubCatBottomSheet bottomSheet = new SubCatBottomSheet(AddSliderImageActivity.this, subCategoriesList);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

        bottomSheet.setSubCatBottomSheetListener(new SubCatBottomSheet.SubCatBottomSheetListener() {
            @Override
            public void onItemSelected(String id, String subCatName) {

                TextView selectedSubCatTxt = findViewById(R.id.subCatTxt);

                selectedSubCatTxt.setText(subCatName);
                isSubCatSelected = true;
                selectedSubCat = subCatName;
                selectedSubCatID = id;



            }
        });
    }



    private void showCatDialog() {

        CatBottomSheet bottomSheet = new CatBottomSheet(AddSliderImageActivity.this, categories);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

        bottomSheet.setCatBottomSheetListener(new CatBottomSheet.CatBottomSheetListener() {
            @Override
            public void onItemSelected(String id, String categoryName, List<Subcategory> subCatName) {


                TextView catTxt = findViewById(R.id.catTxt);

                catTxt.setText(categoryName);
                selectedCat = categoryName;
                selectedCatID = id;

                subCategoriesList.clear();
                subCategoriesList.addAll(subCatName);

                isCatSelected = true;

                if (isSubcat){

                    findViewById(R.id.subCatLay).setVisibility(View.VISIBLE);

                }else {
                    findViewById(R.id.subCatLay).setVisibility(View.GONE);

                }



                TextView selectedSubCatTxt = findViewById(R.id.subCatTxt);
                selectedSubCatTxt.setText("Select Subcategory");
                isSubCatSelected = false;
                selectedSubCat = "";
                selectedSubCatID = "";


            }
        });
    }

    private void selectDoc() {


        ImagePickerBottomSheet bottomSheet = new ImagePickerBottomSheet(1010, 1016);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        bottomSheet.setImageSelectionListener(new ImagePickerBottomSheet.ImageSelectionListener() {
            @Override
            public void onImageSelected(Uri imageUri) {

                Intent intent = new Intent(AddSliderImageActivity.this, ImageCompressActivity.class);

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


                Intent intent = new Intent(AddSliderImageActivity.this, ImageCompressActivity.class);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10001) {
            if (resultCode == RESULT_OK && data != null) {

                Bundle bundle = data.getExtras();

                if (bundle != null) {

                    String croppedImageUri = bundle.getString("croppedImageUri");

                    if (bundle.getString("image", "none").matches("doc")) {

                        ImageView catImg = findViewById(R.id.user_dp);

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


    private void startApi(String data, String externalUrl, String imageUrl,String catId,String subCatID) {

        try {
            new AddSliderImage(data, externalUrl, imageUrl,catId,subCatID).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    private class AddSliderImage extends AsyncTask<Void, Void, Void> {

        String data, link, img;
        String catId,subcatId;

        public AddSliderImage(String data, String link, String img, String catId, String subcatId) {
            this.data = data;
            this.link = link;
            this.img = img;
            this.catId = catId;
            this.subcatId = subcatId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            // Create RequestBody instances from parameters


            RequestBody dataBody = createRequestBody(data);
            RequestBody urlBody = createRequestBody(link);

            RequestBody catBody = createRequestBody(catId);
            RequestBody subcatBody = createRequestBody(subcatId);


            MultipartBody.Part imagePart = null;
            if (img != null) {

                imagePart = createImagePart(img);


            }

            apiInterface.addSliderImage(dataBody, urlBody,catBody,subcatBody,imagePart).enqueue(new Callback<SuccessMessageResponse>() {
                @Override
                public void onResponse(Call<SuccessMessageResponse> call, Response<SuccessMessageResponse> response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            dialog.dismiss();
                            SuccessMessageResponse successMessageResponse = response.body();

                            if (successMessageResponse != null) {

                                if (successMessageResponse.isSuccess()) {


                                    Toast.makeText(AddSliderImageActivity.this, "Image Added Successfully!", Toast.LENGTH_SHORT).show();

                                    finish();

                                }else {
                                    Toast.makeText(AddSliderImageActivity.this, "Err "+successMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }else {
                                Toast.makeText(AddSliderImageActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }

                @Override
                public void onFailure(Call<SuccessMessageResponse> call, Throwable t) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();

                            Toast.makeText(AddSliderImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
            return MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        } else {
            // Return a default MultipartBody.Part or handle it as per your API requirements
            // For example, return an empty MultipartBody.Part
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            return MultipartBody.Part.createFormData("image", "", requestFile);
        }
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

                    if (response.isSuccessful() && response.body() != null) {

                        CategoryResponse categoryResponse = response.body();

                        if (categoryResponse.isSuccess()) {

                            categories.addAll(categoryResponse.getCategories());



                        }


                    } else {

                        activity.runOnUiThread(() -> {
                            Log.d("NEWSERVICE", "Error 4 Res Fail");


                        });

                    }

                }

                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {

                    activity.runOnUiThread(() -> {
                        Log.d("NEWSERVICE", "Error 5 "+t.getMessage());


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