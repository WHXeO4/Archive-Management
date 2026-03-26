package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Result;
import main.entity.User;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

public class UpdatePasswordHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            User user = (User) request.getData();
            Result result = UserController.updatePassword(user.getId(), user.getPassword());

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "修改密码处理异常: " + e.getMessage(), null);
        }
    }
}
