package main.proxy.handler.impl;

import main.controller.UserController;
import main.entity.Request;
import main.entity.Response;
import main.entity.Result;
import main.proxy.handler.CommandHandler;

public class UserListHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        Result result = UserController.list();
        if (result.getCode()==0) {
            return new Response(true, result.getMessage(), result.getData());
        } else {
            return new Response(false, result.getMessage(), null);
        }
    }
}
