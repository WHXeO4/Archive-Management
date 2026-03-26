package main.dao.impl;

import main.config.JDBCConfiguration;
import main.dao.UserService;
import main.entity.Result;
import main.entity.User;
import main.util.JDBCUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    /**
     * 添加用户
     * @param user 用户信息
     * @throws IllegalAccessException
     */
    @Override
    public void addUser(User user) throws IllegalAccessException {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = user.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.equals("id")) continue;

            Object value = field.get(user);
            params.put(fieldName, value);
        }

        JDBCUtil.insert(params, JDBCConfiguration.USER_TABLE);
    }

    /**
     * 更新密码
     * @param id 要更新的用户id
     * @param password 新密码
     */
    @Override
    public void updatePassword(Integer id, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("password", password);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        JDBCUtil.update(params, conditions, JDBCConfiguration.USER_TABLE);
    }

    /**
     * 更新用户
     * @param user 新的用户信息
     * @throws IllegalAccessException
     */
    @Override
    public void updateUser(User user) throws IllegalAccessException {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = user.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.equals("id")) continue;
            Object value = field.get(user);
            params.put(fieldName, value);
        }

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", user.getId());
        JDBCUtil.update(params, conditions, JDBCConfiguration.USER_TABLE);
    }

    /**
     * 删除用户
     * @param id 目标用户id
     */
    @Override
    public void deleteUser(Integer id) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        JDBCUtil.delete(JDBCConfiguration.USER_TABLE, conditions);
    }

    /**
     * 根据用户名查询用户
     * @param name 用户名
     * @return 用户信息
     */
    @Override
    public User findByName(String name) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", name);
        Result<List<User>> result = JDBCUtil.select(User.class, JDBCConfiguration.USER_TABLE, conditions, null);
//        System.out.println(result.getData());
        if (result.getData().isEmpty()) return null;
        else return result.getData().get(0);
    }

    /**
     * 根据id查询用户
     * @param id 目标id
     * @return 用户信息
     */
    @Override
    public User findById(Integer id) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);
        Result<List<User>> result = JDBCUtil.select(User.class, JDBCConfiguration.USER_TABLE, conditions, null);
        if (result.getData().isEmpty()) return null;
        else return result.getData().get(0);
    }

    /**
     * 用户列表
     * @return 用户列表
     */
    @Override
    public List<User> list() {
        return JDBCUtil.select(User.class, JDBCConfiguration.USER_TABLE, null, null).getData();
    }
}
