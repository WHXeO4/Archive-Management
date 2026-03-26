package main.proxy.handler;

import main.entity.Request;
import main.entity.Response;

public interface CommandHandler {
    /**
     * 处理前端传来的 request 对象
     * @param request 前端发送的请求
     * @return 响应数据
     */
    Response execute(Request request);
}
