package www.experthere.adminexperthere.api;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Query;
import www.experthere.adminexperthere.dataModel.AdminsResponse;
import www.experthere.adminexperthere.dataModel.AdsResponse;
import www.experthere.adminexperthere.dataModel.CategoryRES;
import www.experthere.adminexperthere.dataModel.CategoryResponse;
import www.experthere.adminexperthere.dataModel.LoginResponse;
import www.experthere.adminexperthere.dataModel.MentainanceRes;
import www.experthere.adminexperthere.dataModel.NotificationImageResponse;
import www.experthere.adminexperthere.dataModel.PhpMailerRes;
import www.experthere.adminexperthere.dataModel.ProviderListResponse;
import www.experthere.adminexperthere.dataModel.SettingsRes;
import www.experthere.adminexperthere.dataModel.SliderResponse;
import www.experthere.adminexperthere.dataModel.StatisticsResponse;
import www.experthere.adminexperthere.dataModel.SuccessMessageResponse;
import www.experthere.adminexperthere.dataModel.TaxApiResponse;
import www.experthere.adminexperthere.dataModel.UsersListResponse;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("application/SendOtp/otp_sender.php")
        // Change this to the actual name of your PHP script
    Call<Void> sendOtp(
            @Field("email") String email,
            @Field("name") String name,
            @Field("otp") String otp,
            @Field("subject") String subject
    );


    @FormUrlEncoded
    @POST("admin/login.php")
        // Replace with the actual name of your login PHP script
    Call<LoginResponse> loginAdmin(
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("admin/get_admin_details.php")
        // Replace with the actual name of your login PHP script
    Call<LoginResponse> getAdminDetails(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("admin/update_password.php")
        // Replace with the actual name of your login PHP script
    Call<SuccessMessageResponse> updatePasswordAdmin(
            @Field("email") String email,
            @Field("new_password") String password
    );

    @GET("admin/get_sys_users.php")
    Call<AdminsResponse> getsysusers(
            @Query("page") int page,
            @Query("itemsPerPage") int itemsPerPage
    );

    @FormUrlEncoded
    @POST("admin/delete_admin.php")
    Call<SuccessMessageResponse> deleteAdmin(@Field("email") String email);


    @FormUrlEncoded
    @POST("admin/create_account.php")
    Call<SuccessMessageResponse> registerUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("categories") String categories,
            @Field("providers") String providers,
            @Field("users") String users,
            @Field("notifications") String notifications,
            @Field("ads") String ads,
            @Field("settings") String settings,
            @Field("system_users") String systemUsers,
            @Field("firebase") String firebase,
            @Field("agora") String agora,
            @Field("web") String web
    );


    @FormUrlEncoded
    @POST("admin/get_statistics.php")
    Call<StatisticsResponse> getStatistics(
            @Field("key") String key
    );

    @GET("application/get_categories.php")
    Call<CategoryResponse> getCategories();



    @GET("admin/get_settings.php")
    Call<SettingsRes> getSettings();


    @Multipart
    @POST("application/update_category.php")
        // Replace with the actual endpoint
    Call<SuccessMessageResponse> updateCategory(
            @Part("category_id") RequestBody id,
            @Part("category_name") RequestBody name,
            @Part MultipartBody.Part category_image // This is for sending the image file
    );

    @Multipart
    @POST("application/create_categories.php")
        // Replace with the actual endpoint
    Call<CategoryRES> createCategory(
            @Part("category_name") RequestBody name,
            @Part MultipartBody.Part category_image // This is for sending the image file
    );


    @Multipart
    @POST("application/add_image_slider.php")
        // Replace with the actual endpoint
    Call<SuccessMessageResponse> addSliderImage(
            @Part("data") RequestBody data,
            @Part("url") RequestBody url,
            @Part("cat") RequestBody cat,
            @Part("subcat") RequestBody subcat,
            @Part MultipartBody.Part image // This is for sending the image file
    );


    @FormUrlEncoded
    @POST("application/update_subcategory.php")
    Call<SuccessMessageResponse> updateSubcategory(
            @Field("subcategory_id") String id
            , @Field("subcategory_name") String name
    );


    @FormUrlEncoded
    @POST("application/delete_slider.php")
    Call<SuccessMessageResponse> deleteSliderImage(
            @Field("id") String sliderImageId

    );

    @FormUrlEncoded
    @POST("application/delete_tax.php")
    Call<SuccessMessageResponse> deleteTax(
            @Field("id") String taxId

    );

    @FormUrlEncoded
    @POST("application/create_subcategories.php")
    Call<SuccessMessageResponse> createSubcategory(
            @Field("category_id") int catId
            , @Field("subcategory_name") String subCatName
    );


    @FormUrlEncoded
    @POST("providers/get_all_provider_list.php")
    Call<ProviderListResponse> getProvidersList(

            @Field("page") int page,
            @Field("itemsPerPage") int itemsPerPage,
            @Field("searchQuery") String searchQuery

    );


    @POST("application/get_image_slider.php")
    Call<SliderResponse> getSliderData();


    @FormUrlEncoded
    @POST("providers/get_deactive_providers.php")
    Call<ProviderListResponse> getDeactiveProvidersList(

            @Field("page") int page,
            @Field("itemsPerPage") int itemsPerPage,
            @Field("searchQuery") String searchQuery

    );

    @FormUrlEncoded
    @POST("users/get_users_list.php")
    Call<UsersListResponse> getUsersList(

            @Field("page") int page,
            @Field("itemsPerPage") int itemsPerPage,
            @Field("searchQuery") String searchQuery

    );


    @FormUrlEncoded
    @POST("admin/block_provider.php")
    Call<SuccessMessageResponse> blockUnblockProvider(

            @Field("provider_id") int providerID,
            @Field("is_blocked") int isBlocked

    );

    @FormUrlEncoded
    @POST("admin/block_user.php")
    Call<SuccessMessageResponse> blockUnblockUser(

            @Field("user_id") int userId,
            @Field("is_blocked") int isBlocked

    );


    @FormUrlEncoded
    @POST("admin/get_auth_values.php")
    Call<PhpMailerRes> getAuthValues(

            @Field("api_key") String API_KEY
    );


    @FormUrlEncoded
    @POST("admin/get_maintenance.php")
    Call<MentainanceRes> getMaintanance(

            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("admin/get_admob.php")
    Call<AdsResponse> getAdmob(

            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("admin/set_admob.php")
    Call<SuccessMessageResponse> setAdmob(

            @Field("id") int id,
            @Field("status") String status,
            @Field("banner") String banner,
            @Field("interstitial") String interstitial,
            @Field("native") String nativeAds,
            @Field("open_app") String open_app
    );
    @FormUrlEncoded
    @POST("admin/auth_values.php")
    Call<SuccessMessageResponse> setAuthValues(

            @Field("smtp_username") String userName,@Field("smtp_password") String password

    );


   @FormUrlEncoded
    @POST("admin/maintenance.php")
    Call<SuccessMessageResponse> setMaintenance(

            @Field("id")int id
           ,@Field("message") String message
           ,@Field("status") String status

    );


    @FormUrlEncoded
    @POST("admin/set_admob_status.php")
    Call<SuccessMessageResponse> setAdmobStatus(

            @Field("id") int id,
            @Field("status") String status
    );


    @FormUrlEncoded
    @POST("admin/settings.php")
    Call<SuccessMessageResponse> setSettings(

            @Field("key") String key,
            @Field("value") String value
    );

    @Multipart
    @POST("Notifications/upload_image.php")
    Call<NotificationImageResponse> uploadImageNotification(
            @Part MultipartBody.Part image // This is for sending the image file
    );


    @GET("application/get_tax.php")
        // Replace with the actual path to your API
    Call<TaxApiResponse> getAllTaxes();


    @FormUrlEncoded
    @POST("application/set_tax.php")
    Call<SuccessMessageResponse> setTax(
            @Field("percentage") String taxValue
    );

}


