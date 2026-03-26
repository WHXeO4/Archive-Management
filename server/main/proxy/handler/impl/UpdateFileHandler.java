package main.proxy.handler.impl;

import main.controller.DocumentController;
import main.entity.Document;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

import java.util.Map;

public class UpdateFileHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            Map<String, Object> data = (Map<String, Object>) request.getData();
            Document doc = (Document) data.get("document");
            String url = (String) data.get("url");

            Result result = DocumentController.updateFile(doc, url);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "更新文档处理异常: " + e.getMessage(), null);
        }
    }
}
