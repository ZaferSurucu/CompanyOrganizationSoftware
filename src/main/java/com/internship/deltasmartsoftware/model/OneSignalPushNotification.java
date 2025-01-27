package com.internship.deltasmartsoftware.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class OneSignalPushNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;
    private String oneSignalId;

    public OneSignalPushNotification(int userId, String oneSignalId) {
        this.userId = userId;
        this.oneSignalId = oneSignalId;
    }
}
