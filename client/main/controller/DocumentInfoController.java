package main.controller;

import main.config.ActionConfiguration;
import main.entity.Response;

public class DocumentInfoController extends AbstractController {
    @Override
    public Response apply(Object... params) throws InterruptedException {
        Integer docId = (Integer) params[0];

        Thread thread = getThread(ActionConfiguration.DOCUMENT_INFO_ACTION, docId);
        thread.start();
        thread.join();

        isSuccess(response);
        return response;
    }
}
