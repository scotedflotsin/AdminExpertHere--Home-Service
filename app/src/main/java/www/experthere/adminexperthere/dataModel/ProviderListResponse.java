package www.experthere.adminexperthere.dataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProviderListResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("providers")
    private List<ProviderList> providers;

    public boolean isSuccess() {
        return success;
    }

    public List<ProviderList> getProviders() {
        return providers;
    }


}
