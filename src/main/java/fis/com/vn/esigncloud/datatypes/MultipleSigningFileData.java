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
public class MultipleSigningFileData {
    
    private byte[] signingFileData;
    private String signingFileName;
    private String mimeType;
    private String xslTemplate;
    private String xmlDocument;
    private SignCloudMetaData signCloudMetaData;
    
    //
    private String hash;

    @JsonProperty("signingFileData")
    public byte[] getSigningFileData() {
        return signingFileData;
    }

    public void setSigningFileData(byte[] signingFileData) {
        this.signingFileData = signingFileData;
    }

    @JsonProperty("signingFileName")
    public String getSigningFileName() {
        return signingFileName;
    }

    public void setSigningFileName(String signingFileName) {
        this.signingFileName = signingFileName;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("xslTemplate")
    public String getXslTemplate() {
        return xslTemplate;
    }

    public void setXslTemplate(String xslTemplate) {
        this.xslTemplate = xslTemplate;
    }

    @JsonProperty("xmlDocument")
    public String getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    @JsonProperty("signCloudMetaData")
    public SignCloudMetaData getSignCloudMetaData() {
        return signCloudMetaData;
    }

    public void setSignCloudMetaData(SignCloudMetaData signCloudMetaData) {
        this.signCloudMetaData = signCloudMetaData;
    }

    @JsonProperty("hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
