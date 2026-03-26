package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;
import main.util.ThreadLocalUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends AbstractController {

    @Override
    public Response apply(Object... params) throws InterruptedException {
        String username = (String) params[0];
        String password = (String) params[1];

        Map<String, Object> map = new HashMap<>();
        map.put("name", username);
        map.put("password", password);

        Thread thread = getThread(ActionConfiguration.USER_LOGIN_ACTION, map);
        thread.start();
        thread.join();

        if (isSuccess(response)) ThreadLocalUtil.set(response.getData());

        return response;
    }
}
