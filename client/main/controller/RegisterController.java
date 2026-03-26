package main.controller;

import main.config.ActionConfiguration;
import main.entity.User;
import main.entity.Response;

public class RegisterController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        String username = (String) params[0];
        String password = (String) params[1];
        String role = (String) params[2];

        User user = new User(username, password, role);
        Thread thread = getThread(ActionConfiguration.USER_REGISTER_ACTION, user);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
