package main.proxy.handler.impl;

import main.controller.DocumentController;
import main.entity.Document;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

import java.util.Map;

public class UploadHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {

            Map<String, Object> data = (Map<String, Object>) request.getData();
            Document docToUpload = (Document) data.get("document");
            String localUrl = (String) data.get("url");

            // 调用Controller的upload方法
            Result result = DocumentController.upload(docToUpload, localUrl);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "上传文件处理异常: " + e.getMessage(), null);
        }
    }
}
