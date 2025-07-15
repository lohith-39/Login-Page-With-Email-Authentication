package com.app.User.Service;

import com.app.User.Model.Request;
import com.app.User.Model.UserDto;

public interface UserService {

    UserDto createUser(Request request);
    UserDto getUser(Integer id);

    String deleteUser(Integer id);

    boolean validateUser(String username, String password);

    void sendVerificationCode(String email) throws Exception;

    void verifyCode(String email, String code) throws Exception;

    void resetPassword(String email, String newPassword) throws Exception;
}