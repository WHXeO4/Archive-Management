package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;

public class UserListController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Thread thread = getThread(ActionConfiguration.USER_LIST_ACTION, null);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
