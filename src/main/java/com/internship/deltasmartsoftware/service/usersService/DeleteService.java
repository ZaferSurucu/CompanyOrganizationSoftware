package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.Exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class DeleteService {

    private UserRepository userRepository;

    public DeleteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // function for soft delete

    public ResponseEntity<User> deleteUser(int userId){
        User user = userRepository.findOneActive(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user.getId());
        return ResponseEntity.ok(user);
    }
}
