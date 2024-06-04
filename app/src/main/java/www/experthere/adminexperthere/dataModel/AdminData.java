package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

public class AdminData {


    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;

    @SerializedName("categories")
    private String categories;

    @SerializedName("providers")
    private String providers;

    @SerializedName("users")
    private String users;

    @SerializedName("notifications")
    private String notifications;

    @SerializedName("ads")
    private String ads;

    @SerializedName("settings")
    private String settings;

    @SerializedName("system_users")
    private String systemUsers;

    @SerializedName("firebase")
    private String firebase;

    @SerializedName("agora")
    private String agora;

    @SerializedName("web")
    private String web;

    // Getters and setters


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCategories() {
        return categories;
    }

    public String getProviders() {
        return providers;
    }

    public String getUsers() {
        return users;
    }

    public String getNotifications() {
        return notifications;
    }

    public String getAds() {
        return ads;
    }

    public String getSettings() {
        return settings;
    }

    public String getSystemUsers() {
        return systemUsers;
    }

    public String getFirebase() {
        return firebase;
    }

    public String getAgora() {
        return agora;
    }

    public String getWeb() {
        return web;
    }
}
