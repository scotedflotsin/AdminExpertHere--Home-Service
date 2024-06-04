package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

public class Subcategory  {
    @SerializedName("subcategory_id")
    private String subcategoryId;

    @SerializedName("subcategory_name")
    private String subcategoryName;

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }
}