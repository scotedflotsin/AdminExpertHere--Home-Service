package www.experthere.adminexperthere.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SliderAdapter;
import www.experthere.adminexperthere.api.ApiClient;
import www.experthere.adminexperthere.api.ApiInterface;
import www.experthere.adminexperthere.dataModel.AdminData;
import www.experthere.adminexperthere.dataModel.LoginResponse;
import www.experthere.adminexperthere.fragments.AdsFragment;
import www.experthere.adminexperthere.fragments.AgoraFragment;
import www.experthere.adminexperthere.fragments.CategoriesFragment;
import www.experthere.adminexperthere.fragments.DashboardFragment;
import www.experthere.adminexperthere.fragments.FirebaseFragment;
import www.experthere.adminexperthere.fragments.NotificationsFragment;
import www.experthere.adminexperthere.fragments.ProvidersFragment;
import www.experthere.adminexperthere.fragments.SettingsFragment;
import www.experthere.adminexperthere.fragments.SysytemUsersFragment;
import www.experthere.adminexperthere.fragments.UsersFragment;
import www.experthere.adminexperthere.fragments.WebFragment;
import www.experthere.adminexperthere.helperUtils.AdminDetailsTask;
import www.experthere.adminexperthere.helperUtils.ExitBottomSheet;
import www.experthere.adminexperthere.helperUtils.KeyboardUtils;

public class HomeActivity extends AppCompatActivity {


    String name, email;

    TextView adminNameTxt;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    TextView titleTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_Blue, this.getTheme()));


        setContentView(R.layout.activity_home);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        adminNameTxt = findViewById(R.id.adminNameTxt);
        titleTxt = findViewById(R.id.titleTxt);

        SharedPreferences preferences = getSharedPreferences("admin", MODE_PRIVATE);
        name = preferences.getString("name", "Name");
        email = preferences.getString("email", "Admin Email");


        adminNameTxt.setText(name);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logOut();

                closeDrawer();


            }
        });






        AdminDetailsTask adminDetailsTask = new AdminDetailsTask(apiInterface, email, new AdminDetailsTask.OnLoginListener() {
            @Override
            public void onLoginSuccess(LoginResponse loginResponse) {


                if (loginResponse.isSuccess()) {


                    AdminData adminData = loginResponse.getAdmin();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            findViewById(R.id.progressNav).setVisibility(View.GONE);


                            if (adminData.getCategories().equals("disable")) {

                                findViewById(R.id.catBtn).setVisibility(View.GONE);
                            } else {
                                findViewById(R.id.catBtn).setVisibility(View.VISIBLE);

                            }


                            if (adminData.getProviders().equals("disable")) {
                                findViewById(R.id.providersBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.providersBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getUsers().equals("disable")) {
                                findViewById(R.id.usersBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.usersBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getNotifications().equals("disable")) {
                                findViewById(R.id.notificationBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.notificationBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getAds().equals("disable")) {
                                findViewById(R.id.adsBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.adsBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getSettings().equals("disable")) {
                                findViewById(R.id.settingsBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.settingsBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getSystemUsers().equals("disable")) {
                                findViewById(R.id.systemUsersBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.systemUsersBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getFirebase().equals("disable")) {
                                findViewById(R.id.firebaseBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.firebaseBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getAgora().equals("disable")) {

                                findViewById(R.id.agoraBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.agoraBtn).setVisibility(View.VISIBLE);

                            }
                            if (adminData.getWeb().equals("disable")) {
                                findViewById(R.id.webBtn).setVisibility(View.GONE);

                            } else {
                                findViewById(R.id.webBtn).setVisibility(View.VISIBLE);

                            }


                        }
                    });


                }


            }

            @Override
            public void onLoginFailure(String errorMessage) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progressNav).setVisibility(View.GONE);

                        Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        adminDetailsTask.execute();
        findViewById(R.id.sidebarMenuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KeyboardUtils.hideKeyboard(HomeActivity.this);

                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });


        replaceFragment(new DashboardFragment());
        titleTxt.setText("Dashboard");


        findViewById(R.id.dashboardBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragment(new DashboardFragment());
                titleTxt.setText("Dashboard");
                closeDrawer();

            }
        });

        findViewById(R.id.catBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragment(new CategoriesFragment());
                titleTxt.setText("Categories");

                closeDrawer();

            }
        });
        findViewById(R.id.providersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Providers");

                replaceFragment(new ProvidersFragment());
                closeDrawer();

            }
        });
        findViewById(R.id.usersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Users");

                replaceFragment(new UsersFragment());
                closeDrawer();

            }
        });

        findViewById(R.id.notificationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Notifications");

                replaceFragment(new NotificationsFragment());
                closeDrawer();

            }
        });
        findViewById(R.id.adsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Ads");

                replaceFragment(new AdsFragment());
                closeDrawer();

            }
        });
        findViewById(R.id.settingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Settings");

                replaceFragment(new SettingsFragment());
                closeDrawer();

            }
        });

        findViewById(R.id.systemUsersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Sys. Users");

                replaceFragment(new SysytemUsersFragment());
                closeDrawer();

            }
        });
        findViewById(R.id.firebaseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Firebase");

                replaceFragment(new FirebaseFragment());
                closeDrawer();

            }
        });

        findViewById(R.id.agoraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Agora");

                replaceFragment(new AgoraFragment());
                closeDrawer();

            }
        });

        findViewById(R.id.webBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleTxt.setText("Web Control");

                replaceFragment(new WebFragment());
                closeDrawer();

            }
        });


    }

    private void logOut() {

        // Create custom dialog
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize views in the custom dialog
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        btnDelete.setText("Yes");

        // Set dialog title and message
        dialogTitle.setText("Logout Admin?");
        dialogMessage.setText("Are you sure you want to Logout  ?");


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
                dialog.dismiss();

                SharedPreferences preferences = getSharedPreferences("admin",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

        dialog.show();



    }


    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentFrame, fragment)
                .commit();
    }


    private void closeDrawer() {


        if (drawerLayout != null) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {




        if (drawerLayout.isDrawerOpen(GravityCompat.START) ) {


            closeDrawer();

        }
        else {
            ExitBottomSheet exitBottomSheet = new ExitBottomSheet(true);
            exitBottomSheet.show(getSupportFragmentManager(), exitBottomSheet.getTag());

        }


    }




}