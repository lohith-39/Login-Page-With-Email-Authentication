package com.app.User.Service;

import com.app.User.Entity.User;
import com.app.User.Model.Request;
import com.app.User.Model.UserDto;
import com.app.User.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private final Map<String, String> verificationCodes = new HashMap<>();

    @Override
    public UserDto createUser(Request request) {
        User user= userRepo.save(toUserEntity(request));
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private User toUserEntity(Request request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    @Override
    public UserDto getUser(Integer id) {
        Optional<User> optional = userRepo.findById(id);
        return UserDto.builder()
                .id(id)
                .username(optional.get().getUsername())
                .email(optional.get().getEmail())
                .password(optional.get().getPassword())
                .build();
    }

    @Override
    public String deleteUser(Integer id) {
        if(userRepo.existsById(id)){
            userRepo.deleteById(id);
            return "User Deleted with ID:" + id;
        }
        return "User Not Found";
    }

    @Override
    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    @Override
    public void sendVerificationCode(String email) throws Exception {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) throw new Exception("Email not found");

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        verificationCodes.put(email, code);

        emailService.sendVerificationEmail(email, code);
    }

    @Override
    public void verifyCode(String email, String code) throws Exception {
        String expected = verificationCodes.get(email);
        if (expected == null || !expected.equals(code)) {
            throw new Exception("Invalid or expired verification code.");
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) throws Exception {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) throw new Exception("User not found");

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepo.save(user);
        verificationCodes.remove(email);
    }

}