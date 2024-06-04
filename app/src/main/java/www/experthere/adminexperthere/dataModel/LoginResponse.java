package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;


    @SerializedName("admin")

    AdminData admin;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AdminData getAdmin() {
        return admin;
    }
}
