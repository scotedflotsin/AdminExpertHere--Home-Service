package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {


    @SerializedName("success")
    private boolean success;

    @SerializedName("categories")
    private List<Category> categories;

    public boolean isSuccess() {
        return success;
    }

    public List<Category> getCategories() {
        return categories;
    }

}
