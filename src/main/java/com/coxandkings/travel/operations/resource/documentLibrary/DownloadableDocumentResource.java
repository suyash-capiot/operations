package com.coxandkings.travel.operations.resource.documentLibrary;

import java.io.InputStream;

public class DownloadableDocumentResource {
    private String fileName;
    private String extension;
    private InputStream inputStream;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
