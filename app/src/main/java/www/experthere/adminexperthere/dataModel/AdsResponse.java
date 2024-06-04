package www.experthere.adminexperthere.dataModel;

public class AdsResponse {

    boolean success;
    String message;

    AdmobData admob_data;


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AdmobData getAdmob_data() {
        return admob_data;
    }
}
