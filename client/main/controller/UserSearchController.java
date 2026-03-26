package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;
import main.entity.User;
import main.util.ThreadLocalUtil;

public class UserSearchController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        User user = ThreadLocalUtil.get();
        Integer id = user.getId();

        Thread thread = getThread(ActionConfiguration.USER_SEARCH_ACTION, id);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
