/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.com.vn.esigncloud.datatypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author VUDP
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipleSignedFileData {
    private byte[] signedFileData;
    private String mimeType;
    private String signedFileName;
    private String signedFileUUID;
    private String dmsMetaData;
    
    //
    private String signatureValue;
    
    @JsonProperty("signedFileData")
    public byte[] getSignedFileData() {
        return signedFileData;
    }

    public void setSignedFileData(byte[] signedFileData) {
        this.signedFileData = signedFileData;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("signedFileName")
    public String getSignedFileName() {
        return signedFileName;
    }

    public void setSignedFileName(String signedFileName) {
        this.signedFileName = signedFileName;
    }

    @JsonProperty("signedFileUUID")
    public String getSignedFileUUID() {
        return signedFileUUID;
    }

    public void setSignedFileUUID(String signedFileUUID) {
        this.signedFileUUID = signedFileUUID;
    }

    @JsonProperty("dmsMetaData")
    public String getDmsMetaData() {
        return dmsMetaData;
    }

    public void setDmsMetaData(String dmsMetaData) {
        this.dmsMetaData = dmsMetaData;
    }

    @JsonProperty("signatureValue")
    public String getSignatureValue() {
        return signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }
}
