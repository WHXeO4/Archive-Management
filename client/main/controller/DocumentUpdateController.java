package main.controller;

import main.config.ActionConfiguration;
import main.entity.Document;
import main.entity.Response;

import java.util.HashMap;
import java.util.Map;

public class DocumentUpdateController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Integer id = (Integer) params[0];
        String title = (String) params[1];
        String description = (String) params[2];
        String newUrl = (String) params[3];

        Map<String, Object> map = new HashMap<>();

        Document newDoc = new Document();
        newDoc.setDescription(description);
        newDoc.setTitle(title);
        newDoc.setId(id);
        map.put("document", newDoc);
        map.put("url", newUrl);

        Thread thread = getThread(ActionConfiguration.DOCUMENT_UPDATE_ACTION, map);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
