package com.tech_fusion.model.gramsevak;

// ============================================================
// DOCUMENT MODEL
// ============================================================
//
// Simple data holder for one document verification request.
//
// Right now this is filled with temporary/sample data inside
// DocumentsManage.java.
//
// Later, when the backend/API is ready, this exact same class
// can be filled from the server response instead — nothing in
// DocumentView.java needs to change, because it only reads from
// a Document object, it never creates the data itself.
// ============================================================

public class Document {

    private final String id;
    private final String applicantName;
    private final String documentType;
    private final String dateSubmitted;
    private final String status;

    private final String mobileNumber;
    private final String address;
    private final String village;
    private final String maskedIdNumber;

    private final String documentName;
    private final String fileName;
    private final String uploadedDate;
    private final String fileType;
    private final String verificationStatus;

    public Document(
            String id,
            String applicantName,
            String documentType,
            String dateSubmitted,
            String status,
            String mobileNumber,
            String address,
            String village,
            String maskedIdNumber,
            String documentName,
            String fileName,
            String uploadedDate,
            String fileType,
            String verificationStatus) {

        this.id = id;
        this.applicantName = applicantName;
        this.documentType = documentType;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.village = village;
        this.maskedIdNumber = maskedIdNumber;
        this.documentName = documentName;
        this.fileName = fileName;
        this.uploadedDate = uploadedDate;
        this.fileType = fileType;
        this.verificationStatus = verificationStatus;
    }

    public String getId() { return id; }
    public String getApplicantName() { return applicantName; }
    public String getDocumentType() { return documentType; }
    public String getDateSubmitted() { return dateSubmitted; }
    public String getStatus() { return status; }
    public String getMobileNumber() { return mobileNumber; }
    public String getAddress() { return address; }
    public String getVillage() { return village; }
    public String getMaskedIdNumber() { return maskedIdNumber; }
    public String getDocumentName() { return documentName; }
    public String getFileName() { return fileName; }
    public String getUploadedDate() { return uploadedDate; }
    public String getFileType() { return fileType; }
    public String getVerificationStatus() { return verificationStatus; }
}
