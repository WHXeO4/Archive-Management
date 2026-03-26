package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;

public class UserDeleteController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Integer id = (Integer) params[0];

        Thread thread = getThread(ActionConfiguration.USER_DELETE_ACTION, id);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
