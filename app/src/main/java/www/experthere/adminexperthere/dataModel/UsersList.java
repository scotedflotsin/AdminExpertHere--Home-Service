package www.experthere.adminexperthere.dataModel;

public class UsersList {


    int id,is_blocked;
    String name,profile_picture,profession,phone,email,password,full_address;
    Double latitude,longitude;

    public int getId() {
        return id;
    }

    public int getIs_blocked() {
        return is_blocked;
    }

    public String getName() {
        return name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getProfession() {
        return profession;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFull_address() {
        return full_address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
