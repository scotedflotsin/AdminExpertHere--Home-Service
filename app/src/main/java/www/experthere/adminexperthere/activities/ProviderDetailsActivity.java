package www.experthere.adminexperthere.activities;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.dataModel.ProviderList;
import www.experthere.adminexperthere.dataModel.Subcategory;
import www.experthere.adminexperthere.helperUtils.ImageDownloader;

public class ProviderDetailsActivity extends AppCompatActivity {

    ProviderList providerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));

        setContentView(R.layout.activity_provider_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String subcategoryListJson = bundle.getString("list");


            Gson gson = new Gson();

            ProviderList receivedList = gson.fromJson(subcategoryListJson, ProviderList.class);


            if (receivedList != null) {


                providerList = receivedList;
                setUpData(providerList);

            }

        }


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    private void setUpData(ProviderList providerList) {


        ImageView imageView = findViewById(R.id.providerImage);

        Glide.with(this).load(providerList.getLogoImage()).circleCrop().placeholder(R.mipmap.ic_launcher).into(imageView);


        TextView idTxt = findViewById(R.id.idTxt);
        TextView nameTxt = findViewById(R.id.nameTxt);
        TextView emailTxt = findViewById(R.id.emailTxt);
        TextView phoneTxt = findViewById(R.id.phoneTxt);
        TextView idTypeTxt = findViewById(R.id.idTypeTxt);
        TextView cNameTxt = findViewById(R.id.cNameTxt);
        TextView visitingChargesTxt = findViewById(R.id.visitingChargesTxt);
        TextView advanceBookingDaysTxt = findViewById(R.id.advanceBookingDaysTxt);
        TextView accounttypeTxt = findViewById(R.id.accounttypeTxt);
        TextView membersTxt = findViewById(R.id.membersTxt);
        TextView addressTxt = findViewById(R.id.addressTxt);
        TextView blockTxt = findViewById(R.id.blockTxt);
        TextView reviewTxt = findViewById(R.id.reviewTxt);
        TextView totalServiceTxt = findViewById(R.id.totalServiceTxt);
        TextView statusTxt = findViewById(R.id.statusTxt);
        TextView passTxt = findViewById(R.id.passTxt);


        idTxt.setText(String.valueOf(providerList.getId()));
        nameTxt.setText(providerList.getName());
        emailTxt.setText(providerList.getEmail());
        phoneTxt.setText(providerList.getCountryCode() + providerList.getPhone());
        idTypeTxt.setText(providerList.getIdType());
        cNameTxt.setText(providerList.getCompanyName());
        visitingChargesTxt.setText(providerList.getCurrency() + " " + String.valueOf(providerList.getVisitingCharges()));
        advanceBookingDaysTxt.setText(String.valueOf(providerList.getAdvanceBookingDays()));

        accounttypeTxt.setText(providerList.getAccountType());
        membersTxt.setText(String.valueOf(providerList.getMembers()));
        addressTxt.setText(providerList.getAddress());

        if (providerList.getIsBlocked() == 1) {
            blockTxt.setText("Yes");
            blockTxt.setTextColor(getColor(R.color.red));

        } else {

            blockTxt.setText("No");
            blockTxt.setTextColor(getColor(R.color.green));

        }

        reviewTxt.setText(providerList.getAverage_rating() + "(" + providerList.getTotal_reviews() + ")");

        totalServiceTxt.setText(String.valueOf(providerList.getTotalServices()));
        statusTxt.setText(providerList.getStatus());

//
//        if (providerList.getStatus().equals("deactive")) {
//            statusTxt.setTextColor(getColor(R.color.red));
//        } else {
//            statusTxt.setTextColor(getColor(R.color.green));
//
//        }
//



        if (providerList.getStatus()!=null){
            if (providerList.getStatus().equals("deactive")) {

                statusTxt.setTextColor(getColor(R.color.red));
            } else {
                statusTxt.setTextColor(getColor(R.color.green));

            }
        }else {
            statusTxt.setTextColor(getColor(R.color.red));
            statusTxt.setText("Status Not Set By Provider!");

        }





        RichEditor richEditor = findViewById(R.id.richEditor);

        richEditor.setHtml(providerList.getDescription());


        ImageView bannerImage = findViewById(R.id.bannerImage);
        Glide.with(this).load(providerList.getBannerImage()).placeholder(R.mipmap.ic_launcher).into(bannerImage);

        ImageView govtID = findViewById(R.id.idImage);
        Glide.with(this).load(providerList.getDocumentImage()).placeholder(R.mipmap.ic_launcher).into(govtID);

        TextView latitudeTxt = findViewById(R.id.latitudeTxt);
        TextView longitudetxt = findViewById(R.id.longitudeTxt);

        latitudeTxt.setText(String.valueOf(providerList.getLatitude()));
        longitudetxt.setText(String.valueOf(providerList.getLongitude()));

        passTxt.setText(providerList.getPassword());
        passTxt.setTextColor(getColor(R.color.red));

        richEditor.setEnabled(false);

        govtID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.downloadImage(ProviderDetailsActivity.this
                        , providerList.getDocumentImage()
                        , "Downloading Image"
                        , providerList.getName() + "_DOC.jpg"
                        , "Govt Id Image Downloading..");


                Toast.makeText(ProviderDetailsActivity.this, "Downloading..", Toast.LENGTH_SHORT).show();
                Toast.makeText(ProviderDetailsActivity.this, "Check Gallery..!", Toast.LENGTH_LONG).show();

            }
        });


    }
}