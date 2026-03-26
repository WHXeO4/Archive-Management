package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;
import main.entity.User;
import main.util.Md5Util;

public class UserEditController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Integer id = (Integer) params[0] ;
        String username = (String) params[1];
        String password = (String) params[2];
        String role = (String) params[3];

        User user = new User(id, username, Md5Util.md5Hex(password), role);
        Thread thread = getThread(ActionConfiguration.USER_EDIT_ACTION, user);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
