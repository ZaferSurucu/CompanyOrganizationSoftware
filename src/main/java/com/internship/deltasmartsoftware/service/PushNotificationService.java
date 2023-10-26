package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.OneSignalPushNotification;
import com.internship.deltasmartsoftware.model.Role;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.OneSignalPushNotificationRepository;
import com.internship.deltasmartsoftware.repository.RoleRepository;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.util.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PushNotificationService {

    // import logger
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Value("${onesignal.api_key}")
    private String oneSignalApiKey;

    @Value("${onesignal.app_id}")
    private String oneSignalAppId;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final OneSignalPushNotificationRepository oneSignalPushNotificationRepository;

    public PushNotificationService(UserRepository userRepository, RoleRepository roleRepository, OneSignalPushNotificationRepository oneSignalPushNotificationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.oneSignalPushNotificationRepository = oneSignalPushNotificationRepository;
    }

    private void sendNotificationToUsers(String message, List<Integer> userIds) {
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

            String ids = getOneSignalIdsFromUserIds(userIds);
            logger.info("ids: " + ids);
            String strJsonBody = "{"
                    +   "\"app_id\": \""+ oneSignalAppId +"\","
                    +   "\"include_player_ids\": [\""+ ids + "\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \""+ message +"\"}"
                    + "}";

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            try (java.io.OutputStream outputStream = con.getOutputStream()) {
                outputStream.write(sendBytes);
            } catch (Throwable t) {
                t.printStackTrace();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private String getOneSignalIdsFromUserIds(List<Integer> userIds) {
        List<OneSignalPushNotification> oneSignalIds = oneSignalPushNotificationRepository.findByUserIdIn(userIds);
        StringBuilder oneSignalIdsString = new StringBuilder();
        for (OneSignalPushNotification oneSignalId : oneSignalIds) {
            oneSignalIdsString.append(oneSignalId.getOneSignalId()).append(",");
        }
        return oneSignalIdsString.substring(0, oneSignalIdsString.length() - 1);
    }

    private void sendNotificationToAdmins(String message) {
        Role admin = roleRepository.findByName("Admin").orElseThrow(() -> new ResourceNotFoundException("role not found"));
        List<User> admins = userRepository.findAllByRole(admin).orElseThrow(() -> new ResourceNotFoundException("users not found"));
        List<Integer> adminIds = admins.stream().map(User::getId).toList();
        sendNotificationToUsers(message, adminIds);
    }

    public void sendUserCreatedNotificationToAdmins(User creator,User created){
        sendNotificationToAdmins(creator.getName() + " " + Translator.toLocale("users.newUserJustCreated")+ " " + created.getName());
    }
}
