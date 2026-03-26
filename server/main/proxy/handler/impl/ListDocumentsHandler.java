package main.proxy.handler.impl;

import main.controller.DocumentController;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class ListDocumentsHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            Map<String, Object> pageInfo = (Map<String, Object>) request.getData();
            int page = (Integer) pageInfo.get("page");
            int maxLine = (Integer) pageInfo.get("maxLine");
            String title = (String) pageInfo.get("title");

            Result result = DocumentController.list(title, page, maxLine);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), result.getData());
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "列出文档处理异常: " + e.getMessage(), null);
        }
    }
}
