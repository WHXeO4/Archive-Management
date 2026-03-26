package main.controller;

import main.dao.DocumentService;
import main.dao.impl.DocumentServiceImpl;
import main.entity.Document;
import main.entity.DocumentPage;
import main.entity.Result;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档相关接口
 */
public class DocumentController {
    static DocumentService documentService = new DocumentServiceImpl();

    /**
     * 文档信息查询接口
     * @param id 文档id
     * @return 文档信息
     */
    public static Result<Document> info(Integer id) {
        Document document = documentService.findById(id);
        if (document == null) {
            return Result.error("不存在的文件");
        }

        return Result.success("查询成功", document);
    }

    /**
     * 文档上传接口
     * @param document 文档信息
     * @param url 本地地址
     * @return 储存地址
     * @throws IllegalAccessException
     */
    public static Result<String> upload(Document document, String url) throws IllegalAccessException {
        String dst = documentService.uploadFile(document, url);
        if (dst == null) {
            return Result.error("上传失败，文件可能不存在");
        }

        return Result.success("上传成功", dst);
    }

    /**
     * 文档下载接口
     * @param id 文档id
     * @param dst 下载地址
     */
    public static Result download(Integer id, String dst) {
        Document document = documentService.findById(id);
        if (document==null) {
            return Result.error("文件不存在");
        }

        String url = document.getUrl();
        String filename = document.getTitle()+url.substring(url.lastIndexOf("."));

        boolean result = documentService.downloadFile(filename, url, dst);
        System.out.println("文件名: "+filename+" 文件存储地址:"+url+ " 目标下载地址:"+dst);
        if (! result) {
            return Result.error("下载失败，请重试");
        }

        return Result.success("下载成功");
    }

    /**
     * 文档列表接口
     * @param page 页数
     * @param maxLine 每页最大数量
     * @return 一页文档信息
     */
    public static Result<DocumentPage> list(String title, Integer page, Integer maxLine) {
        List<Document> documents = documentService.getPage(title, page, maxLine);
        DocumentPage documentPage = new DocumentPage(page, maxLine, documents);

        return Result.success("查询成功", documentPage);
    }

    /**
     * 文档更新接口
     * @param newDoc 新的文档信息
     * @param url 新的文档地址
     */
    public static Result updateFile(Document newDoc, String url) throws IllegalAccessException {
            String dst = documentService.updateFile(newDoc, url);

            if (dst == null) {
                return Result.error("更新失败，文件可能不存在");
            }

            return Result.success("更新成功", dst);
    }
}
