package com.webrtc.boyj.model;

import com.webrtc.boyj.model.dao.UserDAO;
import com.webrtc.boyj.model.dto.User;

import java.util.List;

import io.reactivex.Single;

public class UserRepository {
    private static UserRepository userRepository = null;

    // TODO : Firebase remote DataSource 변수 추가

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            synchronized (UserRepository.class) {
                if (userRepository == null) {
                    userRepository = new UserRepository();
                }
            }
        }
        return userRepository;
    }

    public Single<List<User>> getUserList() {
        UserDAO userDAO = new UserDAO();
        return userDAO.readAll();
    }
}
