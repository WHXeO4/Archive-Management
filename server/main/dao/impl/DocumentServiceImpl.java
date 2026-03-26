package main.dao.impl;

import main.config.JDBCConfiguration;
import main.dao.DocumentService;
import main.entity.Document;
import main.entity.Result;
import main.util.FileUtil;
import main.util.JDBCUtil;

import javax.print.Doc;
import java.io.File;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class DocumentServiceImpl implements DocumentService {

    /**
     * 根据id获取文档
     * @param id 目标文档id
     * @return 目标文档
     */
    @Override
    public Document findById(Integer id) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);

        Result<List<Document>> result = JDBCUtil.select(Document.class, "document", conditions, null);

        if (result.getData().isEmpty()) return null;

        return result.getData().get(0);
    }

    /**
     * 文档上传
     * @param document 文档信息
     * @param url 文档本地地址
     * @return 文档存储地址
     */
    @Override
    public String uploadFile(Document document, String url) throws IllegalAccessException {
        String result = FileUtil.upload(new File(url));

        if (result==null) return null;

        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        document.setUrl(result);

        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = document.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.equals("id")) continue;
            Object value = field.get(document);

            params.put(fieldName, value);
        }
        JDBCUtil.insert(params, "document");

        return result;
    }

    /**
     * 文档下载
     * @param filename 文件名
     * @param url 文档存储地址
     * @param dst 文档下载地址
     * @return 下载是否成功
     */
    @Override
    public boolean downloadFile(String filename, String url, String dst) {
        return FileUtil.download(url, dst, filename);
    }

    /**
     * 获取分页
     * @param time 上传时间下限
     * @param title 标题前缀
     * @param page 页数
     * @param maxLine 每页最大数量
     * @return 文档信息的列表
     */
    @Override
    public List<Document> getPage(String title, Integer page, Integer maxLine) {
        Map<String, String> likeConditions = null;
        if (title!=null && !title.isEmpty()) {
            likeConditions = new HashMap<>();
            likeConditions.put("title", '\"'+title+"%\""); // 正则匹配以 *title 开头的数据
        }

        Result<List<Document>> documents = JDBCUtil.select(Document.class, JDBCConfiguration.DOCUMENT_TABLE, null, likeConditions);

        if (documents.getData().size()>=page*maxLine) {
            return new java.util.ArrayList<>(documents.getData().subList((page - 1) * maxLine, page * maxLine));
        } else if (documents.getData().size()>=(page - 1) * maxLine){
            return new java.util.ArrayList<>(documents.getData().subList(((page - 1) * maxLine), documents.getData().size()));
        } else {
            return null;
        }
    }

    /**
     * 文档更新
     * @param document 新的文档信息
     * @param url 新的文档地址（若无则默认填写与旧的地址相同）
     * @return 新的文档存储地址
     * @throws IllegalAccessException
     */
    @Override
    public String updateFile(Document document, String url) throws IllegalAccessException {
        String result = null;
        if (url!=null && !url.isEmpty()) {
            result = FileUtil.upload(new File(url));
        }

        Document doc = findById(document.getId());

        if (! FileUtil.delete(doc.getUrl())) return null;

        if (result!=null) doc.setUrl(result);
        doc.setUpdateTime(LocalDateTime.now());
        doc.setTitle(document.getTitle());
        doc.setDescription(document.getDescription());

        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = doc.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.equals("id")) continue;
            Object value = field.get(doc);

            params.put(fieldName, value);
        }

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", document.getId());

        JDBCUtil.update(params, conditions, JDBCConfiguration.DOCUMENT_TABLE);

        return result;
    }
}
