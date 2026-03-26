package main.controller;

import main.config.ActionConfiguration;
import main.entity.Document;
import main.entity.Response;

import java.util.HashMap;
import java.util.Map;

public class DocumentUploadController extends AbstractController {

    @Override
    public Response apply(Object... params) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        String title = (String) params[0];
        String description = (String) params[1];
        String url = (String) params[2];

        Document doc = new Document(title, description);

        map.put("document", doc);
        map.put("url", url);

        Thread thread = getThread(ActionConfiguration.DOCUMENT_UPLOAD_ACTION, map);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
