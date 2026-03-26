package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Result;
import main.entity.User;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

import java.util.Map;

public class LoginHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            Map<String, Object> map = (Map<String, Object>) request.getData();
            String name = (String) map.get("name");
            String password = (String) map.get("password");
            Result result = UserController.login(name, password);

            if (result.getCode() == 0) {
                // 登录成功，将用户信息返回
                return new Response(true, result.getMessage(), result.getData());
            } else {
                // 登录失败
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "登录处理异常: " + e.getMessage(), null);
        }
    }
}
