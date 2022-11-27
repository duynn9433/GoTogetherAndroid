package duynn.gotogether.data_layer.fcm;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FcmNotificationsSender  {

    String userFcmToken;
    String title;
    String body;
    Map<String, String> data;
    Context mContext;
    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = duynn.gotogether.BuildConfig.FCM_SERVER_KEY;
    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        data = new HashMap<>();
    }

    /**
     * {
     *  "to" : "token",
     *  "collapse_key" : "type_a",
     *  "notification" : {
     *      "body" : "nội dung thông báo",
     *      "title": "tiêu đề thông báo"
     *  },
     *  "data" : {
     *      "body" : "nôi dung chi tiết",
     *      "title": "thông tin chi tiết",
     *      "key_1" : "giá trị key_1",
     *      "key_2" : "giá trị key_2"
     *  }
     * }
     * */
    public void SendNotifications() {
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);

            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            mainObj.put("notification", notiObject);

            JSONObject dataObject = new JSONObject();
            for(String s: data.keySet()){
                dataObject.put(s, data.get(s));
            }
            mainObj.put("data", dataObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // code run is got response
                    Log.d("FCM", "onResponse: " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error
                    Log.d("FCM", "onResponse: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}