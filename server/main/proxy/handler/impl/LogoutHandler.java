package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

public class LogoutHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            // 调用UserController中的logout方法
            Result result = UserController.logout();

            if (result.getCode() == 0) {
                // 成功
                return new Response(true, result.getMessage(), null);
            } else {
                // 失败
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "登出失败: " + e.getMessage(), null);
        }
    }
}
