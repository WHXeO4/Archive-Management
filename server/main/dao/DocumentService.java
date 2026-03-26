package main.dao;

import main.entity.Document;

import java.util.List;

public interface DocumentService {
    Document findById(Integer id);
    String uploadFile(Document document, String url) throws IllegalAccessException;

    boolean downloadFile(String filename, String url, String dst);

    List<Document> getPage(String title, Integer page, Integer maxLine);

    String updateFile(Document document, String url) throws IllegalAccessException;
}
