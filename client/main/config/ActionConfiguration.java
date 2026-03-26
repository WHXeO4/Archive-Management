package main.config;

import com.mysql.cj.protocol.x.Notice;

public class ActionConfiguration {
    // 用户相关行为
    public final static String USER_REGISTER_ACTION = "register";
    public final static String USER_LOGIN_ACTION = "login";
    public final static String USER_LOGOUT_ACTION = "logout";
    public final static String USER_PASSWORD_UPDATE_ACTION = "updatePassword";
    public final static String USER_DELETE_ACTION = "delete";
    public final static String USER_SEARCH_ACTION = "search";
    public final static String USER_LIST_ACTION = "userList";
    public final static String USER_EDIT_ACTION = "edit";

    // 档案相关行为
    public final static String DOCUMENT_INFO_ACTION = "info";
    public final static String DOCUMENT_UPLOAD_ACTION = "upload";
    public final static String DOCUMENT_DOWNLOAD_ACTION = "download";
    public final static String DOCUMENT_LIST__ACTION = "list";
    public final static String DOCUMENT_UPDATE_ACTION = "updateFile";
}
