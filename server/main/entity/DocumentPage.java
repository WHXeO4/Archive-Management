package main.entity;

import java.io.Serializable;
import java.util.List;

public class DocumentPage implements Serializable {
    private Integer pageNumber;
    private Integer maxLine;
    private List<Document> documents;

    public DocumentPage() {
    }

    public DocumentPage(Integer pageNumber, Integer maxLine, List<Document> documents) {
        this.pageNumber = pageNumber;
        this.maxLine = maxLine;
        this.documents = documents;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(Integer maxLine) {
        this.maxLine = maxLine;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
