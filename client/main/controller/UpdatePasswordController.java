package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;
import main.entity.User;
import main.util.ThreadLocalUtil;

public class UpdatePasswordController extends AbstractController {

    @Override
    public Response apply(Object... params) throws InterruptedException {
        String password = (String) params[1];
        User cur = ThreadLocalUtil.get();
        Integer id = cur.getId();

        User u = new User(id, password);
        Thread thread = getThread(ActionConfiguration.USER_PASSWORD_UPDATE_ACTION, u);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
