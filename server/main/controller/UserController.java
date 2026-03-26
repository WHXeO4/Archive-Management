package main.controller;

import main.dao.UserService;
import main.dao.impl.UserServiceImpl;
import main.entity.Result;
import main.entity.User;
import main.util.Md5Util;
import main.validation.UserValidator;

import java.util.*;

/**
 * 用户数据处理
 * 用户数据的增删改查
 */
public  class UserController{
    /**
     * 用户数据库通信的实例
     */
    static UserService userService = new UserServiceImpl();

    /**
     * 用户注册接口
     * @param name 用户名
     * @param password 密码
     * @param role 用户身份
     */
    public static Result register(String name, String password, String role) throws IllegalAccessException {
        User u = userService.findByName(name);
        if (u==null) {
            if (! UserValidator.usernameValidator(name)) return Result.error("用户名应为5~16字符");
            if (! UserValidator.passwordValidator(password)) return Result.error("密码应为5~16位数字或字母");
            userService.addUser(new User(name, Md5Util.md5Hex(password), role));
        } else {
            return Result.error("用户名已存在");
        }

        return Result.success("注册成功");
    }

    /**
     * 用户登录接口
     * @param name 输入的用户名
     * @param password 输入的密码
     * @return 用户对象
     */

    public static Result<User> login(String name, String password) {
        User u = userService.findByName(name);
        System.out.println(Md5Util.md5Hex(password) + "  " +u.getPassword());
        if (u==null) {
            return Result.error("用户名不存在");
        } else {
//            System.out.println(Md5Util.md5Hex(password) + "  " +u.getPassword());
            if (Md5Util.matches(password, u.getPassword())) {
                return Result.success("登录成功", u);
            } else {
                return Result.error("密码错误");
            }
        }
    }

    /**
     * 更新密码接口
     * @param id 用户id
     * @param password 新密码
     */
    public static Result updatePassword(Integer id, String password) {
        User u = userService.findById(id);
        System.out.println(Md5Util.md5Hex(password) + "  " +u.getPassword());

        if (! UserValidator.passwordValidator(password)) {
            return Result.error("密码应为5~16位数字或字母");
        }

        if (Md5Util.matches(password, u.getPassword())) {
            return Result.error("新密码不能与原密码相同");
        }

        userService.updatePassword(id, Md5Util.md5Hex(password));

        return Result.success("密码修改成功，请重新登录");
    }

    /**
     * 删除用户接口
     * @param id 目标id
     */
    public static Result delete(Integer id) {

        userService.deleteUser(id);

        return Result.success("删除成功");
    }

    /**
     * 用户信息查询接口
     * @param id 目标用户id
     * @return 目标用户
     */
    public static Result<User> search(Integer id) {

        User user = userService.findById(id);

        return Result.success("查询成功", user);
    }

    /**
     * 用户列表接口
     * @return 用户列表
     */
    public static Result<List<User>> list() {
        List<User> userList = userService.list();

        return Result.success("查询成功", userList);
    }

    /**
     * 用户编辑接口
     * @param user 新的用户信息
     */
    public static Result edit(User user) throws IllegalAccessException {

        userService.updateUser(user);

        return Result.success("修改成功");
    }

    /**
     * 用户登出接口
     */
    public static Result logout() {
        return Result.success("登出成功");
    }
}