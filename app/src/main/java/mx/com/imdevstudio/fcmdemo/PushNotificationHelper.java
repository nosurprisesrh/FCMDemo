package mx.com.imdevstudio.fcmdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;


/**
 * Created by imunoz on 11/03/2018.
 */

public class PushNotificationHelper extends AsyncTask<String, Void, Void> {

    public final static String API_URL_FCM = "https://fcm.googleapis.com/v1/projects/fcmdemo-3a008/messages:send";
    public final static String AUTH_KEY_FCM = "AIzaSyCmyPGH2kKm3_c5JZ5TnAOiVxPQaio1-FQ";
    public final static String API_URL_FCM_LEG = "https://fcm.googleapis.com/fcm/send";
    Context contexto;

    public PushNotificationHelper(Context contexto) {

        this.contexto = contexto;
    }


    public String sendPushNotification(String deviceToken)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM_LEG);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("to", deviceToken.trim());
        JSONObject info = new JSONObject();
        info.put("title", "UBICAB"); // Notification title
        info.put("body", "IVAN ha enviado un mensaje"); // Notification
        System.out.println("IVAN ha enviado un mensaje");
        // body
        json.put("notification", info);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS"; //CommonConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE"; //CommonConstants.FAILURE;
        }
        System.out.println("GCM Notification is sent successfully");

        return result;
    }


    public String sendPushNotification (String deviceToken, String tipoNotif)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        JSONObject msg = new JSONObject();
        JSONObject notif = new JSONObject();

        if (null != tipoNotif) switch (tipoNotif.trim()) {
            case "1": {
                notif.put("title", "UBICAB"); // Notification title
                notif.put("body", "SERGIO ha INICIADO un viaje"); // Notification
                System.out.println("SERGIO ha INICIADO un viaje");
                break;
            }
            case "2": {
                notif.put("title", "UBICAB"); // Notification title
                notif.put("body", "SERGIO ha FINALIZADO un viaje"); // Notification
                System.out.println("SERGIO ha FINALIZADO un viaje");
                break;
            }
            default: {
                notif.put("title", "UBICAB"); // Notification title
                notif.put("body", "SERGIO ha CANCELADO un viaje"); // Notification
                System.out.println("SERGIO ha CANCELADO un viaje");
                break;
            }
        }
        msg.put("topic", deviceToken.trim());
        msg.put("notification", notif);
        json.put("message", msg);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS"; //CommonConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE"; //CommonConstants.FAILURE;
        }
        System.out.println("FCM Notification is sent successfully");

        return result;
    }


    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/firebase.messaging https://www.googleapis.com/auth/cloud-platform");

    private String getAccessToken() throws IOException {

       AssetManager am = contexto.getAssets();
        InputStream inputStream = am.open("fcmdemo-3a008-firebase-adminsdk-kmxwu-52f6d05059.json");
        //File file = createFileFromInputStream(inputStream);
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(inputStream)
                .createScoped(SCOPES);
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String respuesta = sendPushNotification("FCMDemoTopic", strings[0]);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
