package com.digitalsignature.model;

import java.sql.Timestamp;

public class SignatureRecord {

    private long id;
    private String fileName;
    private String documentHash;
    private String signature;
    private Timestamp createdAt;

    public SignatureRecord() {
    }

    public SignatureRecord(
            long id,
            String fileName,
            String documentHash,
            String signature,
            Timestamp createdAt) {

        this.id = id;
        this.fileName = fileName;
        this.documentHash = documentHash;
        this.signature = signature;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}