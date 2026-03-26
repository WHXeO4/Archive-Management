package main.proxy.handler.impl;

import main.controller.DocumentController;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

public class DocumentInfoHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            // 从Request中获取文档ID
            Integer documentId = (Integer) request.getData();

            // 调用Controller获取文档信息
            Result result = DocumentController.info(documentId);

            if (result.getCode() == 0) {
                // 成功
                return new Response(true, result.getMessage(), result.getData());
            } else {
                // 失败
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "获取文档信息失败: " + e.getMessage(), null);
        }
    }
}