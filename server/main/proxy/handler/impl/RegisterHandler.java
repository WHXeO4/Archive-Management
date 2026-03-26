package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Result;
import main.entity.User;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

public class RegisterHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            User userToRegister = (User) request.getData();
            String name = userToRegister.getName();
            String password = userToRegister.getPassword();
            String role = userToRegister.getRole();

            Result result = UserController.register(name, password, role);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            return new Response(false, "注册处理异常: " + e.getMessage(), null);
        }
    }
}
