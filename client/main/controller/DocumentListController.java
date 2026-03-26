package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class DocumentListController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        String title = (String) params[0];
        Integer pageNumber = (Integer) params[1];
        Integer maxLine = (Integer) params[2];

        Map<String, Object> map = new HashMap<>();

        map.put("title", title);
        map.put("page", pageNumber);
        map.put("maxLine", maxLine);

        Thread thread = getThread(ActionConfiguration.DOCUMENT_LIST__ACTION, map);

        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
