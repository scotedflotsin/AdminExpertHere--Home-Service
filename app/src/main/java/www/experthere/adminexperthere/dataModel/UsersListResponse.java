package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersListResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("users")
    private List<UsersList> users;


    public List<UsersList> getUsers() {
        return users;
    }

    public boolean isSuccess() {
        return success;
    }
}
