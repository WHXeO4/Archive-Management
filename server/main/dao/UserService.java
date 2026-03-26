package main.dao;

import main.entity.User;

import java.util.List;

public interface UserService {
    void addUser(User user) throws IllegalAccessException;
    void updatePassword(Integer id, String password);
    void updateUser(User user) throws IllegalAccessException;
    void deleteUser(Integer id);
    User findByName(String name);
    User findById(Integer id);

    List<User> list();
}
