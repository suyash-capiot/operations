package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "Media")
public class Media {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "fileCategory")
    private String fileCategory;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "filePath")
    private File filePath;

    @Column(name = "fileUrl")
    private String fileUrl;

    @Column(name = "fileDescription")
    private String fileDescription;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }
}
