package fis.com.vn.esigncloud.datatypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignCloudResp {
    private int responseCode;
    private String responseMessage;
    private String billCode;
    private Date timestamp;
    private int logInstance;
    private String notificationMessage;
    private int remainingCounter;
    private byte[] signedFileData;
    private String signedFileName;
    private String authorizeCredential;
    private String signedFileUUID;
    private String mimeType;
    private String certificateDN;
    private String certificateSerialNumber;
    private String certificateThumbprint;
    private Date validFrom;
    private Date validTo;
    private String issuerDN;
    private String uploadedFileUUID;
    private String downloadedFileUUID;
    private byte[] downloadedFileData;
    private String signatureValue;
    private int authorizeMethod;
    private String notificationSubject;
    private String dmsMetaData;
    private String csr;
    private String certificate;
    private int certificateStateID;
    private List<MultipleSignedFileData> multipleSignedFileData;
    

    public SignCloudResp(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
    

    public SignCloudResp() {
        
    }

    @JsonProperty("billCode")
    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    @JsonProperty("notificationMessage")
    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    @JsonProperty("remainingCounter")
    public int getRemainingCounter() {
        return remainingCounter;
    }

    public void setRemainingCounter(int remainingCounter) {
        this.remainingCounter = remainingCounter;
    }

    @JsonProperty("responseCode")
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("responseMessage")
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @JsonProperty("signedFileData")
    public byte[] getSignedFileData() {
        return signedFileData;
    }

    public void setSignedFileData(byte[] signedFileData) {
        this.signedFileData = signedFileData;
    }

    @JsonProperty("authorizeCredential")
    public String getAuthorizeCredential() {
        return authorizeCredential;
    }

    public void setAuthorizeCredential(String authorizeCredential) {
        this.authorizeCredential = authorizeCredential;
    }

    @JsonProperty("certificateDN")
    public String getCertificateDN() {
        return certificateDN;
    }

    public void setCertificateDN(String certificateDN) {
        this.certificateDN = certificateDN;
    }

    @JsonProperty("certificateSerialNumber")
    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    @JsonProperty("certificateThumbprint")
    public String getCertificateThumbprint() {
        return certificateThumbprint;
    }

    public void setCertificateThumbprint(String certificateThumbprint) {
        this.certificateThumbprint = certificateThumbprint;
    }

    @JsonProperty("issuerDN")
    public String getIssuerDN() {
        return issuerDN;
    }

    public void setIssuerDN(String issuerDN) {
        this.issuerDN = issuerDN;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("signedFileUUID")
    public String getSignedFileUUID() {
        return signedFileUUID;
    }

    public void setSignedFileUUID(String signedFileUUID) {
        this.signedFileUUID = signedFileUUID;
    }

    @JsonProperty("validFrom")
    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    @JsonProperty("validTo")
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @JsonProperty("downloadedFileData")
    public byte[] getDownloadedFileData() {
        return downloadedFileData;
    }

    public void setDownloadedFileData(byte[] downloadedFileData) {
        this.downloadedFileData = downloadedFileData;
    }

    @JsonProperty("downloadedFileUUID")
    public String getDownloadedFileUUID() {
        return downloadedFileUUID;
    }

    public void setDownloadedFileUUID(String downloadedFileUUID) {
        this.downloadedFileUUID = downloadedFileUUID;
    }

    @JsonProperty("signatureValue")
    public String getSignatureValue() {
        return signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }

    @JsonProperty("uploadedFileUUID")
    public String getUploadedFileUUID() {
        return uploadedFileUUID;
    }

    public void setUploadedFileUUID(String uploadedFileUUID) {
        this.uploadedFileUUID = uploadedFileUUID;
    }

    @JsonProperty("authorizeMethod")
    public int getAuthorizeMethod() {
        return authorizeMethod;
    }

    public void setAuthorizeMethod(int authorizeMethod) {
        this.authorizeMethod = authorizeMethod;
    }

    @JsonProperty("notificationSubject")
    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("logInstance")
    public int getLogInstance() {
        return logInstance;
    }

    public void setLogInstance(int logInstance) {
        this.logInstance = logInstance;
    }

    @JsonProperty("dmsMetaData")
    public String getDmsMetaData() {
        return dmsMetaData;
    }

    public void setDmsMetaData(String dmsMetaData) {
        this.dmsMetaData = dmsMetaData;
    }

    @JsonProperty("csr")
    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    @JsonProperty("certificate")
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @JsonProperty("multipleSignedFileData")
    public List<MultipleSignedFileData> getMultipleSignedFileData() {
        return multipleSignedFileData;
    }

    public void setMultipleSignedFileData(List<MultipleSignedFileData> multipleSignedFileData) {
        this.multipleSignedFileData = multipleSignedFileData;
    }

    @JsonProperty("signedFileName")
    public String getSignedFileName() {
        return signedFileName;
    }

    public void setSignedFileName(String signedFileName) {
        this.signedFileName = signedFileName;
    }

    @JsonProperty("certificateStateID")
    public int getCertificateStateID() {
        return certificateStateID;
    }

    public void setCertificateStateID(int certificateStateID) {
        this.certificateStateID = certificateStateID;
    }
}