package www.experthere.adminexperthere.FCM;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class FCMNotificationPayloadTopic {
    @SerializedName("message")
    private Message message;

    public FCMNotificationPayloadTopic(Message message) {
        this.message = message;
    }

    public static class Message {
        @SerializedName("topic")
        private String token;
//        @SerializedName("notification")
//        private Notification notification;
        @SerializedName("data")
        private Map<String,String> data;
        @SerializedName("android")
        private Android android;

        public Message(String token, Map<String, String> data, Android android) {
            this.token = token;
//            this.notification = notification;
            this.data = data;
            this.android = android;
        }
    }

    public static class Notification {
        @SerializedName("title")
        private String title;
        @SerializedName("body")
        private String body;




        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }


    public static class Android {
        @SerializedName("notification")
        private Notification2 androidNotification;

        public Android(Notification2 androidNotification) {
            this.androidNotification = androidNotification;
        }

    }

    public static class Notification2 {

        @SerializedName("click_action")
        private String click_action;

        String icon;

        public Notification2(String click_action, String icon) {
            this.click_action = click_action;
            this.icon = icon;
        }


        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }
    }
}
