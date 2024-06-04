package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;


    @SerializedName("admin")

    List<AdminData> admin;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<AdminData> getAdmin() {
        return admin;
    }
}
