package com.app.User.Controller;

import com.app.User.Entity.User;
import com.app.User.Model.Request;
import com.app.User.Model.UserDto;
import com.app.User.Repository.UserRepo;
import com.app.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("")
    public ResponseEntity<UserDto> createUsers(@RequestBody Request request){
        UserDto response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id){
        UserDto response = userService.getUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String password = request.get("password");

        return userRepo.findByUsernameAndPassword(username, password)
                .map(user -> Map.of("message", "Login successful", "user", user))
                .orElseThrow(() -> new Exception("Invalid username or password"));
    }

    @PostMapping("/send-code")
    public Map<String, String> sendCode(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        userService.sendVerificationCode(email);
        return Map.of("message", "Verification code sent.");
    }

    @PostMapping("/verify-code")
    public Map<String, String> verifyCode(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        String code = request.get("code");
        userService.verifyCode(email, code);
        return Map.of("message", "Code verified.");
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        userService.resetPassword(email, newPassword);
        return Map.of("message", "Password updated successfully.");
    }

}