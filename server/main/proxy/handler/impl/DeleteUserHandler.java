package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

public class DeleteUserHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            Integer userId = (Integer) request.getData();
            Result result = UserController.delete(userId);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "删除用户处理异常: " + e.getMessage(), null);
        }
    }
}
