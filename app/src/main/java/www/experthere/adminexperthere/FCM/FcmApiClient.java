package www.experthere.adminexperthere.FCM;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FcmApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

//    public static void sendFcmMessageToTopic(String url, String authToken, String topic, String title,
//                                      String body, String extraData, String clickAction,
//                                      Callback callback) {
//
//
//        String jsonBody = "{"
//                + "\"message\": {"
//                + "\"topic\": \"" + topic + "\","
//                + "\"notification\": {"
//                + "\"title\": \"" + title + "\","
//                + "\"body\": \"" + body + "\""
//                + "},"
//                + "\"data\": {"
//                + "\"extra_data\": \"" + extraData + "\""
//                + "},"
//                + "\"android\": {"
//                + "\"notification\": {"
//                + "\"click_action\": \"" + clickAction + "\""
//                + "}"
//                + "}"
//                + "}"
//                + "}";
//
//        RequestBody requestBody = RequestBody.create(JSON,jsonBody);
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + authToken)
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(callback);
//    }


    public static void sendFcmMessageToTopic(String url, String authToken, String DEVICE_TOKEN_HERE, String title,
                                      String body, Map<String,String> EXTRA_DATA, String clickAction,
                                      Callback callback) {


        FCMNotificationPayloadTopic.Notification notification = new FCMNotificationPayloadTopic.Notification(title,body);



        FCMNotificationPayloadTopic.Notification2 notification2 = new FCMNotificationPayloadTopic.Notification2(clickAction,"app_icon");

        FCMNotificationPayloadTopic.Android android = new FCMNotificationPayloadTopic.Android(notification2);


        FCMNotificationPayloadTopic.Message message = new FCMNotificationPayloadTopic.Message(DEVICE_TOKEN_HERE,EXTRA_DATA,null);

        FCMNotificationPayloadTopic payload = new FCMNotificationPayloadTopic(message);


        Gson gson = new Gson();
        String jsonBody = gson.toJson(payload);


        Log.d("JSONOAYLOIAD", "sendFcmMessageToDevice: "+ jsonBody.toString());

        RequestBody requestBody = RequestBody.create(JSON,jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }




}
