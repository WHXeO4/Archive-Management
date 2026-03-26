package main.proxy.handler.impl;

import main.controller.DocumentController;
import main.entity.Result;
import main.entity.Request;
import main.entity.Response;
import main.proxy.handler.CommandHandler;

import java.util.Map;

public class DownloadHandler implements CommandHandler {
    @Override
    public Response execute(Request request) {
        try {
            Map<String, Object> data = (Map<String, Object>) request.getData();
            Integer id = (Integer) data.get("id");
            String dst = (String) data.get("destination");

            System.out.println("文件id："+id+" 目标地址："+dst);

            Result result = DocumentController.download(id, dst);

            if (result.getCode() == 0) {
                return new Response(true, result.getMessage(), null);
            } else {
                return new Response(false, result.getMessage(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "下载文件处理异常: " + e.getMessage(), null);
        }
    }
}
