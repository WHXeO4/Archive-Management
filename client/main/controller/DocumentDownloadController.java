package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;

import java.util.HashMap;
import java.util.Map;

public class DocumentDownloadController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Integer id = (Integer) params[0];
        String dst = (String) params[1];

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("destination", dst);

        Thread thread = getThread(ActionConfiguration.DOCUMENT_DOWNLOAD_ACTION, map);
        thread.start();
        thread.join();
        isSuccess(response);
        return response;
    }
}
