package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.OneSignalPushNotification;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OneSignalPushNotificationRepository extends JpaRepository<OneSignalPushNotification, Long> {
    List<OneSignalPushNotification> findByUserIdIn(List<Integer> Ids);
    Optional<OneSignalPushNotification> findByOneSignalId(String oneSignalId);
}
