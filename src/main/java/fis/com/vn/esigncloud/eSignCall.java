/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.com.vn.esigncloud;

import static fis.com.vn.esigncloud.Demo.FILE_DIRECTORY;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fis.com.vn.esigncloud.datatypes.AgreementDetails;
import fis.com.vn.esigncloud.datatypes.CredentialData;
import fis.com.vn.esigncloud.datatypes.MultipleSignedFileData;
import fis.com.vn.esigncloud.datatypes.MultipleSigningFileData;
import fis.com.vn.esigncloud.datatypes.SignCloudMetaData;
import fis.com.vn.esigncloud.datatypes.SignCloudReq;
import fis.com.vn.esigncloud.datatypes.SignCloudResp;

/**
 *
 * @author ADMIN
 */
public class eSignCall {

    private static final String USER_AGENT = "Mozilla/5.0";

    final public static String URL = "https://esigncloud.fis.com.vn:10443/eSignCloud/restapi/";
    final public static String relyingParty = "PTF";
    final public static String relyingPartyUser = "ptfesign";
    final public static String relyingPartyPassword = "8yEHHL5B";
    final public static String relyingPartySignature = "O4nXPG12w7/+fQjYIB4z3RxHECh1wNTyZevmn+MakWHYdnNNAXZbQZi8d8NSE01+LSBq7e0axv/FzsdCre0ve4oTKBhPMSJAWhWBaVxqlRqVPWsz3ZIncHdbGteMjR8530+ClOkfNLd7oZ4B1WWOOCK1sl5/clvY2FaDKPcPIB+8dI66G0wB4WE/dXt6w/VHZrBUWuh0Oa8Lny/jgIvcnoi3uIu1y+ngZjsn49lg9FNrlJkDbLGEIDQYhydUFcNs6AUa3sb5yIbDGXgZQ7Qeurkv6FLvLJHXF8z6bfRyBzICmd7Ho/SQ8TPtVjKiMNfy7Q82X92qEzCSQ740+HmP9g==";
    final public static String relyingPartyKeyStore = "/image/kyso/PTF.p12";
    final public static String relyingPartyKeyStorePassword = "taX7sfvr";
    
    public static String CERTIFICATE_ROOT_CA = "MIID6TCCAtGgAwIBAgIQVBBSesIo0n5SXiud4r01PzANBgkqhkiG9w0BAQUFADB+MR0wGwYDVQQDDBRNSUMgTmF0aW9uYWwgUm9vdCBDQTEbMBkGA1UECwwSTmF0aW9uYWwgQ0EgQ2VudGVyMTMwMQYDVQQKDCpNaW5pc3RyeSBvZiBJbmZvcm1hdGlvbiBhbmQgQ29tbXVuaWNhdGlvbnMxCzAJBgNVBAYTAlZOMB4XDTE5MDYwNDA4MjAwOFoXDTI5MDYwNDA4MjAwOFowfjEdMBsGA1UEAwwUTUlDIE5hdGlvbmFsIFJvb3QgQ0ExGzAZBgNVBAsMEk5hdGlvbmFsIENBIENlbnRlcjEzMDEGA1UECgwqTWluaXN0cnkgb2YgSW5mb3JtYXRpb24gYW5kIENvbW11bmljYXRpb25zMQswCQYDVQQGEwJWTjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJr1tm6iDDtvs0HtFLksjBiCMAdu0XNgeQ1s7QT2tYvLM4S6FDtQryZ+HGEh9pI04IQ0bJ7DNM1F6N583mPxpcFgG0a5QXpyuJMcDOQ0+ih+KH2mgzmEeFN9HrL6HA0h/x7p7kyAprKRsaNdclNq8lVcxaJqBy2DRFptjhGErntbZQKP80vqiKwLIHi+xOddpI1mEGnB4D9NItbQz+1vKLHCtB20ywsJ30GMcu162T+PSM2PpK9u+U25ZrcfLa2EmBW0tiMmZuQl4PTyGmoPmup8K6THrt57XHHgRoA2svyDOWUuMCVABE5K31IHN3oWEOmJViry/lae+PYy7KV00y0CAwEAAaNjMGEwHQYDVR0OBBYEFO2qtcbfOrjXTp38PVx+RGsW6/wgMA8GA1UdEwEB/wQFMAMBAf8wHwYDVR0jBBgwFoAU7aq1xt86uNdOnfw9XH5Eaxbr/CAwDgYDVR0PAQH/BAQDAgGGMA0GCSqGSIb3DQEBBQUAA4IBAQCa3YjUBCFs9oTSovWxxY1Gd6hYMkUrPFeDX45K6LfIEMN8iotisF+Ss+zHe+rWF5mDA53547x3wdkJFxAEmTHwu5OXZbWfbtXQPu4b0CBFt53XamAyAv4MUqzFpgzCNj8dMD4WHHqlXd1++YcpN5+QAMW6ARqfgnYLItGtzm2tF9WmV51I6Zfbo4tfr9JY/9UlrgfjfTgnxZvXknQIwz9D7xgND9gUhPPkn6J/jW3H9r57ZxknoLty3MJOu3cwOljoEOhWWleN/iGrw7VIJc5U5BD3hsYHUITl0aSsJ5+7ikBDKs2EGTCduv97T4nlWOhV/JST6m8DynwYbChgwylt";
    public static String CERTIFICATE_SUB_CA = "MIID2TCCAsGgAwIBAgIQVBA4gCBbbRpw4CyQ2OpvgzANBgkqhkiG9w0BAQsFADB+MR0wGwYDVQQDDBRNSUMgTmF0aW9uYWwgUm9vdCBDQTEbMBkGA1UECwwSTmF0aW9uYWwgQ0EgQ2VudGVyMTMwMQYDVQQKDCpNaW5pc3RyeSBvZiBJbmZvcm1hdGlvbiBhbmQgQ29tbXVuaWNhdGlvbnMxCzAJBgNVBAYTAlZOMB4XDTIwMDUwMjEzMjczOFoXDTI1MDUwMjEzMjczOFowbjEkMCIGA1UEAwwbRlBUIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MR8wHQYDVQQLDBZGUFQgSW5mb3JtYXRpb24gU3lzdGVtMRgwFgYDVQQKDA9GUFQgQ29ycG9yYXRpb24xCzAJBgNVBAYTAlZOMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzt8oRMosASZg3wJCEqkYxKWB6StjEhqBC6K5RB5Qan63fMPr5bfz3Z30hoYCDjT47NMVbo1Dv85w+Lwqe/NJ19ELB3A6j6x68EbwAv8RkQGusI1TGZ1yFIbTdbXg0AeJm2o+Wq7I1ifc4ZaVZ2+r6sXCXFl7EbIEffRiwA+Rs3qpDamwLfslbTtE8iWcuEy4D7ssb3jz7oKv+S/njG9l/qPZFqqF42pTILflOoghJqFHdTsN01vTPiI0aNvjgAlXM+KAH4vH4K6mJ7Yusxy3/TGSAj0NuSOTuS+wAbAqKQAqWgOxlWV6Da3IQpLDRFtGkfwU+pWgvRn+b1uG1ofjQIDAQABo2MwYTAdBgNVHQ4EFgQUGcwKzXuZL1PAJvoV6V05gkJUfA8wDwYDVR0TAQH/BAUwAwEB/zAfBgNVHSMEGDAWgBTtqrXG3zq4106d/D1cfkRrFuv8IDAOBgNVHQ8BAf8EBAMCAYYwDQYJKoZIhvcNAQELBQADggEBABUyFff2NKZrGDeA/G9CN/wgH1fOAxkz7yRWxDg05qebOejXKf1wvsurnlQeWMi2hl8ppNM/e8rpm5KIr9UZdvteF5K89NUtA5HjRH5TE5EV+HpAJ7BYQp7GYDBpxCmCAQSsWROAWNVPwqpY6aW1q7Jn1GU751P3sWtKJPJKx2CGKfpUEUPjeIShtuSyO1xfVuxDtfNPPzo43CZMwIEPXJk6lt41vciQsZz4D9FsfbzQKLTg/Bt4AJuLDwNgSJpHOB36fAfTVwXmWrGGtios0NXw6JrTP8HXeXOlzmJBnNQIYeJ8LWOjA30PHPd/Dkzce2X4UtTWY9JVM3cycJ9XGX8=";
    
    final public static String CERTIFICATEPROFILE = "F2PSA21D";

    final public static String FUNCTION_PREPARECERTIFICATEFORSIGNCLOUD = "prepareCertificateForSignCloud";
    final public static String FUNCTION_REGENERATEAUTHORIZATIONCODEFORSIGNCLOUD = "regenerateAuthorizationCodeForSignCloud";
    final public static String FUNCTION_PREPAREFILEFORSIGNCLOUD = "prepareFileForSignCloud";
    final public static String FUNCTION_AUTHORIZESINGLETONSIGNINGFORSIGNCLOUD = "authorizeSingletonSigningForSignCloud";
    final public static String FUNCTION_AUTHORIZECOUNTERSIGNINGFORSIGNCLOUD = "authorizeCounterSigningForSignCloud";
    final public static String FUNCTION_GETCERTIFICATEDETAILFORSIGNCLOUD = "getCertificateDetailForSignCloud";
    final public static String FUNCTION_PREPAREHASHSIGNINGFORSIGNCLOUD = "prepareHashSigningForSignCloud";
    
    
    public String prepareMultipleFilesForSignCloud(
            String agreementUUID,
            int authorizeMethod,
            String authorizeCode,
            String notificationTemplate,
            String notificationSubject,
            List<MultipleSigningFileData> listOfSigningFileData) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setAuthorizeMethod(ESignCloudConstant.AUTHORISATION_METHOD_SMS);
        signCloudReq.setMessagingMode(ESignCloudConstant.ASYNCHRONOUS_CLIENTSERVER);
        signCloudReq.setNotificationTemplate(notificationTemplate);
        signCloudReq.setNotificationSubject(notificationSubject);
        signCloudReq.setMultipleSigningFileData(listOfSigningFileData);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_PREPAREFILEFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());
        System.out.println("AuthorizeCredential: " + signCloudResp.getAuthorizeCredential());
        return jsonResponse;
    }
    
    public String getCertificateDetailForSignCloud(
            String agreementUUID) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);
        
        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_GETCERTIFICATEDETAILFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);
        if (signCloudResp.getResponseCode() == 0) {
            System.out.println("Message: " + signCloudResp.getResponseMessage());
            return signCloudResp.getCertificate();
        } else {
            throw new Exception("Error while calling getCertificateDetailForSignCloud");
        }
    }
    
    public String prepareCertificateForSignCloud(
            String agreementUUID,
            String personalName,
            String personalID,
            String citizenID,
            String location,
            String stateProvince,
            String country,
            byte[] frontSideOfIDDocument,
            byte[] backSideOfIDDocument,
            String authorizationEmail,
            String authorizationMobileNo,
            byte[] requestForm
            ) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setEmail(authorizationEmail);
        signCloudReq.setMobileNo(authorizationMobileNo);

        signCloudReq.setCertificateProfile(CERTIFICATEPROFILE);

        AgreementDetails agreementDetails = new AgreementDetails();
        agreementDetails.setPersonalName(personalName);
        agreementDetails.setPersonalID(personalID);
        agreementDetails.setCitizenID(citizenID); // at least personalID or citizenID required
        agreementDetails.setLocation(location);
        agreementDetails.setStateOrProvince(stateProvince);
        agreementDetails.setCountry(country);

        // CMND
        agreementDetails.setPhotoFrontSideIDCard(frontSideOfIDDocument);
        agreementDetails.setPhotoBackSideIDCard(backSideOfIDDocument);
        
        //giay dang ky chung thu so
        agreementDetails.setRequestForm(requestForm);

        signCloudReq.setAgreementDetails(agreementDetails);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);

        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_PREPARECERTIFICATEFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        
        return jsonResponse;
    }
    public String authorizeCounterSigningForSignCloud(
            String agreementUUID,
            String authorizeCode,
            String billCode) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setMessagingMode(ESignCloudConstant.SYNCHRONOUS);
        signCloudReq.setBillCode(billCode);
        signCloudReq.setAuthorizeCode(authorizeCode);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_AUTHORIZECOUNTERSIGNINGFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());

//        if (signCloudResp.getResponseCode() == 0) {
//            if (signCloudResp.getSignedFileData() != null) {
//                String file = FILE_DIRECTORY + ".signed." + signCloudResp.getSignedFileName();
//                System.out.println("Saved in " + file);
//                System.out.println("MimeType: " + signCloudResp.getMimeType());
//                FileOutputStream fos = new FileOutputStream(file);
//                IOUtils.write(signCloudResp.getSignedFileData(), fos);
//                fos.close();
//            } else {
//                System.out.println(signCloudResp.getSignedFileUUID());
//                System.out.println(signCloudResp.getDmsMetaData());
//            }
//            
//            // for multiple files
//            if (signCloudResp.getMultipleSignedFileData() != null) {
//                if (!signCloudResp.getMultipleSignedFileData().isEmpty()) {
//                    for (int i = 0; i < signCloudResp.getMultipleSignedFileData().size(); i++) {
//                        MultipleSignedFileData multipleSignedFileData = signCloudResp.getMultipleSignedFileData().get(i);
//                        if (multipleSignedFileData.getSignedFileData() != null) {
//                            String file = FILE_DIRECTORY + ".signed." +  multipleSignedFileData.getSignedFileName();
//                            System.out.println("Saved in " + file);
//                            System.out.println("MimeType: " + multipleSignedFileData.getMimeType());
//                            IOUtils.write(multipleSignedFileData.getSignedFileData(), new FileOutputStream(file));
//                        } else {
//                            System.out.println(multipleSignedFileData.getSignedFileUUID());
//                            System.out.println(multipleSignedFileData.getDmsMetaData());
//                        }
//                    }
//                }
//            }
//        }
        return jsonResponse;
    }
    public SignCloudResp regenerateAuthorizationCodeForSignCloud(
            String agreementUUID,
            String notificationTemplate,
            String notificationSubject,
            int authorizeMethod) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setAuthorizeMethod(authorizeMethod);

        signCloudReq.setNotificationTemplate(notificationTemplate);
        signCloudReq.setNotificationSubject(notificationSubject);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);

        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_REGENERATEAUTHORIZATIONCODEFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode()); // lưu ý, billCode này không dùng cho hàm authorize
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("AuthorizeCredential: " + signCloudResp.getAuthorizeCredential());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());
        
        return signCloudResp;
    }

    public String prepareFileForSignCloud(
            String agreementUUID,
            int authorizeMethod,
            String authorizeCode,
            String notificationTemplate,
            String notificationSubject,
            byte[] sigingFileData,
            String signingFileName,
            String mimeType,
            SignCloudMetaData signCloudMetaData) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        //signCloudReq.setAuthorizeMethod(ESignCloudConstant.AUTHORISATION_METHOD_SMS);
        signCloudReq.setAuthorizeMethod(authorizeMethod);
        signCloudReq.setMessagingMode(ESignCloudConstant.ASYNCHRONOUS_CLIENTSERVER);

        signCloudReq.setNotificationTemplate(notificationTemplate);
        signCloudReq.setNotificationSubject(notificationSubject);

        signCloudReq.setSignCloudMetaData(signCloudMetaData);
        signCloudReq.setSigningFileData(sigingFileData);
        signCloudReq.setSigningFileName(signingFileName);
        signCloudReq.setMimeType(mimeType);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_PREPAREFILEFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());
        System.out.println("AuthorizeCredential: " + signCloudResp.getAuthorizeCredential());
        return jsonResponse;
    }
    
    public String prepareFileForSignCloudForOrganization(
            String agreementUUID,
            int authorizeMethod,
            String authorizeCode,
            int messageingMode,
            String notificationTemplate,
            String notificationSubject,
            byte[] sigingFileData,
            String signingFileName,
            String mimeType,
            SignCloudMetaData signCloudMetaData) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        //signCloudReq.setAuthorizeMethod(ESignCloudConstant.AUTHORISATION_METHOD_SMS);
        signCloudReq.setAuthorizeMethod(authorizeMethod);
        //signCloudReq.setMessagingMode(ESignCloudConstant.ASYNCHRONOUS_CLIENTSERVER);
        signCloudReq.setMessagingMode(messageingMode);
        signCloudReq.setAuthorizeCode(authorizeCode);

        signCloudReq.setNotificationTemplate(notificationTemplate);
        signCloudReq.setNotificationSubject(notificationSubject);

        signCloudReq.setSignCloudMetaData(signCloudMetaData);
        signCloudReq.setSigningFileData(sigingFileData);
        signCloudReq.setSigningFileName(signingFileName);
        signCloudReq.setMimeType(mimeType);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_PREPAREFILEFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());
        System.out.println("AuthorizeCredential: " + signCloudResp.getAuthorizeCredential());
        return jsonResponse;
    }

    public String authorizeSingletonSigningForSignCloud(
            String agreementUUID,
            String authorizeCode,
            String billCode) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setMessagingMode(ESignCloudConstant.SYNCHRONOUS);
        signCloudReq.setBillCode(billCode);
        signCloudReq.setAuthorizeCode(authorizeCode);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);

        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_AUTHORIZESINGLETONSIGNINGFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());

        if (signCloudResp.getResponseCode() == 0) {
            // for single file
            if (signCloudResp.getSignedFileData() != null) {
                String file = FILE_DIRECTORY + ".signed." + signCloudResp.getSignedFileName();
                System.out.println("Saved in " + file);
                System.out.println("MimeType: " + signCloudResp.getMimeType());
                FileOutputStream file2 = new FileOutputStream(file);
                IOUtils.write(signCloudResp.getSignedFileData(), file2);
                file2.close();
            } else {
                System.out.println(signCloudResp.getSignedFileUUID());
                System.out.println(signCloudResp.getDmsMetaData());
            }

            // for multiple files
            if (signCloudResp.getMultipleSignedFileData() != null) {
                if (!signCloudResp.getMultipleSignedFileData().isEmpty()) {
                    for (int i = 0; i < signCloudResp.getMultipleSignedFileData().size(); i++) {
                        MultipleSignedFileData multipleSignedFileData = signCloudResp.getMultipleSignedFileData().get(i);
                        if (multipleSignedFileData.getSignedFileData() != null) {
                            String file = FILE_DIRECTORY + multipleSignedFileData.getSignedFileName() + "." + i + ".signed.pdf";
                            System.out.println("Saved in " + file);
                            System.out.println("MimeType: " + multipleSignedFileData.getMimeType());
                            IOUtils.write(multipleSignedFileData.getSignedFileData(), new FileOutputStream(file));
                        } else {
                            System.out.println(multipleSignedFileData.getSignedFileUUID());
                            System.out.println(multipleSignedFileData.getDmsMetaData());
                        }
                    }
                }
            }
        }
        return jsonResponse;
    }
    public String authorizeSingletonSigningForSignCloudNotSaveFile(
            String agreementUUID,
            String authorizeCode,
            String billCode) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setMessagingMode(ESignCloudConstant.SYNCHRONOUS);
        signCloudReq.setBillCode(billCode);
        signCloudReq.setAuthorizeCode(authorizeCode);

        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);

        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_AUTHORIZESINGLETONSIGNINGFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        System.out.println("Code: " + signCloudResp.getResponseCode());
        System.out.println("Message: " + signCloudResp.getResponseMessage());
        System.out.println("BillCode: " + signCloudResp.getBillCode());
        System.out.println("NotificationMessage: " + signCloudResp.getNotificationMessage());
        System.out.println("NotificationSubject: " + signCloudResp.getNotificationSubject());
        System.out.println("RemainingCounter: " + signCloudResp.getRemainingCounter());

        return jsonResponse;
    }
    private String sendPost(String function, String payload) throws Exception {
//        System.out.println("Request: " + payload);
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        String endpointUrl = URL + function;
        System.out.println("Send POST request to " + endpointUrl);
        URL obj = new URL(endpointUrl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(payload.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        if (responseCode != 200) {
            throw new Exception("Error while calling eSignCloud server");
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println("Response: " + response.toString());
        return response.toString();
    }
    public SignCloudResp prepareHashSigningForSignCloud(
            String agreementUUID,
            int authorizeMethod,
            String authorizeCode,
            List<String> hashList,
            String hashAlgorithm,
            String mimeType,
            boolean certRequired) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = Utils.getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);
        
        SignCloudReq signCloudReq = new SignCloudReq();
        signCloudReq.setRelyingParty(relyingParty);
        signCloudReq.setAgreementUUID(agreementUUID);
        signCloudReq.setAuthorizeMethod(authorizeMethod);
        signCloudReq.setMessagingMode(ESignCloudConstant.SYNCHRONOUS);
        signCloudReq.setAuthorizeCode(authorizeCode);
        signCloudReq.setCertificateRequired(true);
        
        ArrayList<MultipleSigningFileData> multipleSigningFileDatas = new ArrayList<MultipleSigningFileData>();
        for (int i = 0; i < hashList.size(); i++) {
            MultipleSigningFileData multipleSigningFileData = new MultipleSigningFileData();
            multipleSigningFileData.setHash(DatatypeConverter.printHexBinary(DatatypeConverter.parseBase64Binary(hashList.get(i))));
            multipleSigningFileData.setMimeType(ESignCloudConstant.MIMETYPE_SHA256);
            multipleSigningFileData.setSigningFileName("Hash_Name_" + System.currentTimeMillis());
            multipleSigningFileDatas.add(multipleSigningFileData);
        }
        signCloudReq.setMultipleSigningFileData(multipleSigningFileDatas);
        signCloudReq.setCertificateRequired(certRequired);
        
        CredentialData credentialData = new CredentialData();
        credentialData.setUsername(relyingPartyUser);
        credentialData.setPassword(relyingPartyPassword);
        credentialData.setTimestamp(timestamp);
        credentialData.setSignature(relyingPartySignature);
        credentialData.setPkcs1Signature(pkcs1Signature);
        signCloudReq.setCredentialData(credentialData);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = sendPost(FUNCTION_PREPAREHASHSIGNINGFORSIGNCLOUD, objectMapper.writeValueAsString(signCloudReq));
        SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

        if (signCloudResp.getResponseCode() == 0 || signCloudResp.getResponseCode() == 1018 || signCloudResp.getResponseCode() == 1007) {
            System.out.println("Code: " + signCloudResp.getResponseCode());
            System.out.println("Message: " + signCloudResp.getResponseMessage());
            System.out.println("OTP: " + signCloudResp.getAuthorizeCredential());
            System.out.println("BillCode: " + signCloudResp.getBillCode());
            return signCloudResp;
        } else {
            throw new Exception("Error while calling prepareHashSigningForSignCloud");
        }
    }
}
