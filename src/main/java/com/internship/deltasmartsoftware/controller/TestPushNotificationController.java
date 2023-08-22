package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.service.PushNotificationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestPushNotificationController {

    @Value("${onesignal.api_key}")
    private String oneSignalApiKey;

    @Value("${onesignal.app_id}")
    private String oneSignalAppId;
    private final PushNotificationService pushNotificationService;



    @PostMapping("/push/{id}")
    public ResponseEntity<Response<Object>> sendNotification(@PathVariable String id) {
        try {
            URI uri = new URI("https://onesignal.com/api/v1/notifications");
            URL url = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + oneSignalApiKey);
            con.setRequestMethod("POST");



            String strJsonBody = "{\"app_id\": \""+oneSignalAppId+ "\",\"include_player_ids\": [\""+ id + "\"],\"contents\": {\"en\": \"Test Message\"}}";

//            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
//            con.setFixedLengthStreamingMode(sendBytes.length);
//
//            try (java.io.OutputStream outputStream = con.getOutputStream()) {
//                outputStream.write(sendBytes);
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            con.getResponseCode();

        } catch (Exception e) {
            return Response.badRequest(e.getMessage());
        }

        return Response.ok("Notification sent successfully", null);
    }
}
