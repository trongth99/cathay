package fis.com.vn.esigncloud.datatypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignCloudReq {

    private String relyingParty;
    private String relyingPartyBillCode;
    private String agreementUUID;
    private String sharedAgreementUUID;
    private String sharedRelyingParty;
    private String mobileNo;
    private String email;
    private String certificateProfile;
    private AgreementDetails agreementDetails;
    private CredentialData credentialData;
    private String signingFileUUID;
    private byte[] signingFileData;
    private String signingFileName;
    private String mimeType;
    private String notificationTemplate;
    private String notificationSubject;
    private boolean timestampEnabled;
    private boolean ltvEnabled;
    private String language;
    private String authorizeCode;
    private boolean postbackEnabled;
    private boolean noPadding;
    private int authorizeMethod;
    private byte[] uploadingFileData;
    private String downloadingFileUUID;
    private String currentPasscode;
    private String newPasscode;
    private String hash;
    private String hashAlgorithm;
    private String encryption;
    private String billCode;
    private SignCloudMetaData signCloudMetaData;
    private int messagingMode;
    private int sharedMode;
    private String xslTemplateUUID;
    private String xslTemplate;
    private String xmlDocument;
    private boolean p2pEnabled;
    private boolean csrRequired;
    private boolean certificateRequired;
    private boolean keepOldKeysEnabled;
    private boolean revokeOldCertificateEnabled;
    private String certificate;
    private List<MultipleSigningFileData> multipleSigningFileData;
    private Date timestamp;
    
    

    @JsonProperty("agreementDetails")
    public AgreementDetails getAgreementDetails() {
        return agreementDetails;
    }

    public void setAgreementDetails(AgreementDetails agreementDetails) {
        this.agreementDetails = agreementDetails;
    }

    @JsonProperty("agreementUUID")
    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    @JsonProperty("certificateProfile")
    public String getCertificateProfile() {
        return certificateProfile;
    }

    public void setCertificateProfile(String certificateProfile) {
        this.certificateProfile = certificateProfile;
    }

    @JsonProperty("credentialData")
    public CredentialData getCredentialData() {
        return credentialData;
    }

    public void setCredentialData(CredentialData credentialData) {
        this.credentialData = credentialData;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("mobileNo")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @JsonProperty("relyingParty")
    public String getRelyingParty() {
        return relyingParty;
    }

    public void setRelyingParty(String relyingParty) {
        this.relyingParty = relyingParty;
    }

    @JsonProperty("authorizeCode")
    public String getAuthorizeCode() {
        return authorizeCode;
    }

    public void setAuthorizeCode(String authorizeCode) {
        this.authorizeCode = authorizeCode;
    }

    @JsonProperty("authorizeMethod")
    public int getAuthorizeMethod() {
        return authorizeMethod;
    }

    public void setAuthorizeMethod(int authorizeMethod) {
        this.authorizeMethod = authorizeMethod;
    }

    @JsonProperty("signingFileData")
    public byte[] getSigningFileData() {
        return signingFileData;
    }

    public void setSigningFileData(byte[] signingFileData) {
        this.signingFileData = signingFileData;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("notificationTemplate")
    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    @JsonProperty("postbackEnabled")
    public boolean isPostbackEnabled() {
        return postbackEnabled;
    }

    public void setPostbackEnabled(boolean postbackEnabled) {
        this.postbackEnabled = postbackEnabled;
    }

    @JsonProperty("signingFileUUID")
    public String getSigningFileUUID() {
        return signingFileUUID;
    }

    public void setSigningFileUUID(String signingFileUUID) {
        this.signingFileUUID = signingFileUUID;
    }

    @JsonProperty("sharedMode")
    public int getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(int sharedMode) {
        this.sharedMode = sharedMode;
    }

    @JsonProperty("timestampEnabled")
    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }
    @JsonProperty("timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public void setTimestampEnabled(boolean timestampEnabled) {
        this.timestampEnabled = timestampEnabled;
    }

    @JsonProperty("currentPasscode")
    public String getCurrentPasscode() {
        return currentPasscode;
    }

    public void setCurrentPasscode(String currentPasscode) {
        this.currentPasscode = currentPasscode;
    }

    @JsonProperty("downloadingFileUUID")
    public String getDownloadingFileUUID() {
        return downloadingFileUUID;
    }

    public void setDownloadingFileUUID(String downloadingFileUUID) {
        this.downloadingFileUUID = downloadingFileUUID;
    }

    @JsonProperty("encryption")
    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    @JsonProperty("hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonProperty("hashAlgorithm")
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @JsonProperty("newPasscode")
    public String getNewPasscode() {
        return newPasscode;
    }

    public void setNewPasscode(String newPasscode) {
        this.newPasscode = newPasscode;
    }

    @JsonProperty("uploadingFileData")
    public byte[] getUploadingFileData() {
        return uploadingFileData;
    }

    public void setUploadingFileData(byte[] uploadingFileData) {
        this.uploadingFileData = uploadingFileData;
    }

    @JsonProperty("notificationSubject")
    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    @JsonProperty("billCode")
    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    @JsonProperty("signCloudMetaData")
    public SignCloudMetaData getSignCloudMetaData() {
        return signCloudMetaData;
    }

    public void setSignCloudMetaData(SignCloudMetaData signCloudMetaData) {
        this.signCloudMetaData = signCloudMetaData;
    }

    @JsonProperty("messagingMode")
    public int getMessagingMode() {
        return messagingMode;
    }

    public void setMessagingMode(int messagingMode) {
        this.messagingMode = messagingMode;
    }

    @JsonProperty("xmlDocument")
    public String getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    @JsonProperty("xslTemplate")
    public String getXslTemplate() {
        return xslTemplate;
    }

    public void setXslTemplate(String xslTemplate) {
        this.xslTemplate = xslTemplate;
    }

    @JsonProperty("xslTemplateUUID")
    public String getXslTemplateUUID() {
        return xslTemplateUUID;
    }

    public void setXslTemplateUUID(String xslTemplateUUID) {
        this.xslTemplateUUID = xslTemplateUUID;
    }

    @JsonProperty("relyingPartyBillCode")
    public String getRelyingPartyBillCode() {
        return relyingPartyBillCode;
    }

    public void setRelyingPartyBillCode(String relyingPartyBillCode) {
        this.relyingPartyBillCode = relyingPartyBillCode;
    }

    @JsonProperty("signingFileName")
    public String getSigningFileName() {
        return signingFileName;
    }

    public void setSigningFileName(String signingFileName) {
        this.signingFileName = signingFileName;
    }

    @JsonProperty("p2pEnabled")
    public boolean isP2pEnabled() {
        return p2pEnabled;
    }

    public void setP2pEnabled(boolean p2pEnabled) {
        this.p2pEnabled = p2pEnabled;
    }

    @JsonProperty("csrRequired")
    public boolean isCsrRequired() {
        return csrRequired;
    }

    public void setCsrRequired(boolean csrRequired) {
        this.csrRequired = csrRequired;
    }

    @JsonProperty("certificate")
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @JsonProperty("multipleSigningFileData")
    public List<MultipleSigningFileData> getMultipleSigningFileData() {
        return multipleSigningFileData;
    }

    public void setMultipleSigningFileData(List<MultipleSigningFileData> multipleSigningFileData) {
        this.multipleSigningFileData = multipleSigningFileData;
    }

    @JsonProperty("ltvEnabled")
    public boolean isLtvEnabled() {
        return ltvEnabled;
    }

    public void setLtvEnabled(boolean ltvEnabled) {
        this.ltvEnabled = ltvEnabled;
    }

    @JsonProperty("sharedAgreementUUID")
    public String getSharedAgreementUUID() {
        return sharedAgreementUUID;
    }

    public void setSharedAgreementUUID(String sharedAgreementUUID) {
        this.sharedAgreementUUID = sharedAgreementUUID;
    }

    @JsonProperty("sharedRelyingParty")
    public String getSharedRelyingParty() {
        return sharedRelyingParty;
    }

    public void setSharedRelyingParty(String sharedRelyingParty) {
        this.sharedRelyingParty = sharedRelyingParty;
    }

    @JsonProperty("certificateRequired")
    public boolean isCertificateRequired() {
        return certificateRequired;
    }

    public void setCertificateRequired(boolean certificateRequired) {
        this.certificateRequired = certificateRequired;
    }

    @JsonProperty("keepOldKeysEnabled")
    public boolean isKeepOldKeysEnabled() {
        return keepOldKeysEnabled;
    }

    public void setKeepOldKeysEnabled(boolean keepOldKeysEnabled) {
        this.keepOldKeysEnabled = keepOldKeysEnabled;
    }

    @JsonProperty("revokeOldCertificateEnabled")
    public boolean isRevokeOldCertificateEnabled() {
        return revokeOldCertificateEnabled;
    }

    public void setRevokeOldCertificateEnabled(boolean revokeOldCertificateEnabled) {
        this.revokeOldCertificateEnabled = revokeOldCertificateEnabled;
    }

    @JsonProperty("noPadding")
    public boolean isNoPadding() {
        return noPadding;
    }

    public void setNoPadding(boolean noPadding) {
        this.noPadding = noPadding;
    }
}
