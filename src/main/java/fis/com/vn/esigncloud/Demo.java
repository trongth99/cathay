/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.com.vn.esigncloud;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import fis.com.vn.esigncloud.datatypes.SignCloudMetaData;

/**
 *
 * @author ADMIN
 */
public class Demo {

    private static String agreementUUID = "202103220930";

    // Customer information (for example)
    private static String personalID = "0123456789";
    private static String citizenID = "079090008523"; // if available
    private static String personalName = "NGUYỄN VĂN B";
    private static String location = "Quận 1";
    private static String stateProvince = "TP Hồ Chí Minh";
    private static String country = "VN";
//    private static String authorizationEmail = "phuongvu_0203@yahoo.com";
//    private static String authorizationMobileNo = "84378932887";
    private static String authorizationEmail = "quynpp1986@gmail.com";
    private static String authorizationMobileNo = "0902490772";  
    
    final public static String FILE_PERSONAL_FRONTSIDE_DOC = "can-cuoc-cong-dan-cmnd.jpg";
    final public static String FILE_PERSONAL_BACKSIDE_DOC = "can-cuoc-cong-dan-cmnd.jpg";

    public static String notificationTemplate = "[FPT-CA] Ma xac thuc (OTP) cua Quy khach la {AuthorizeCode}. Vui long dien ma so nay de ky Hop dong Dien Tu va khong cung cap OTP cho bat ky ai";
    public static String notificationSubject = "[FPT-CA] Ma xac thuc (OTP)";

    final public static String FILE_DIRECTORY = "/image/kyso/";

    final public static String FILE_PDF_01 = "string-to-pdf.pdf";
    final public static String FILE_PDF_SIGNED_01 = "sample.signed.pdf";

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        printUsage();
        int resultCode = 0;

        byte[] fileData01;
        String mimeType01;
        SignCloudMetaData signCloudMetaDataForItem01;
        HashMap<String, String> singletonSigningForItem01;

        eSignCall service = new eSignCall();
        do {
            System.out.print("Enter the function: ");
            resultCode = Integer.parseInt(reader.readLine());
            switch (resultCode) {
                case 1:
                    byte[] frontSideOfIDDocument = IOUtils.toByteArray(new FileInputStream("C:\\Users\\admin\\Downloads\\FPTeSignKBank.20210628\\FPTeSignKBank\\file\\" + FILE_PERSONAL_FRONTSIDE_DOC));
                    byte[] backSideOfIDDocument = IOUtils.toByteArray(new FileInputStream("C:\\Users\\admin\\Downloads\\FPTeSignKBank.20210628\\FPTeSignKBank\\file\\" + FILE_PERSONAL_BACKSIDE_DOC));
                    service.prepareCertificateForSignCloud(
                            agreementUUID,
                            personalName,
                            personalID,
                            citizenID,
                            location,
                            stateProvince,
                            country,
                            frontSideOfIDDocument,
                            backSideOfIDDocument,
                            authorizationEmail,
                            authorizationMobileNo, null);
                    break;
                case 2:
                    // prepare file 01
                    fileData01 = IOUtils.toByteArray(new FileInputStream(FILE_DIRECTORY + FILE_PDF_01));
                    mimeType01 = ESignCloudConstant.MIMETYPE_PDF;

                    signCloudMetaDataForItem01 = new SignCloudMetaData();
                    // -- SingletonSigning (Signature properties for customer)        
                    singletonSigningForItem01 = new HashMap<String, String>();
                    singletonSigningForItem01.put("COUNTERSIGNENABLED", "False");
                    singletonSigningForItem01.put("PAGENO", "3");
                    singletonSigningForItem01.put("POSITIONIDENTIFIER", "Họ tên và chữ ký của khách hàng");
                    singletonSigningForItem01.put("RECTANGLEOFFSET", "0,-60");
                    singletonSigningForItem01.put("RECTANGLESIZE", "200,50");
                    singletonSigningForItem01.put("VISIBLESIGNATURE", "True");
                    singletonSigningForItem01.put("VISUALSTATUS", "False");
                    singletonSigningForItem01.put("IMAGEANDTEXT", "False");
                    singletonSigningForItem01.put("TEXTDIRECTION", "LEFTTORIGHT");
                    singletonSigningForItem01.put("SHOWSIGNERINFO", "True");
                    singletonSigningForItem01.put("SIGNERINFOPREFIX", "Ký bởi:");
                    singletonSigningForItem01.put("SHOWDATETIME", "True");
                    singletonSigningForItem01.put("DATETIMEPREFIX", "Ký ngày:");
                    singletonSigningForItem01.put("SHOWREASON", "True");
                    singletonSigningForItem01.put("SIGNREASONPREFIX", "Lý do:");
                    singletonSigningForItem01.put("SIGNREASON", "Tôi đồng ý");
                    singletonSigningForItem01.put("SHOWLOCATION", "True");
//                    singletonSigningForItem01.put("LOCATION", "Hà Nội");
//                    singletonSigningForItem01.put("LOCATIONPREFIX", "Nơi ký:");
                    singletonSigningForItem01.put("TEXTCOLOR", "black");

                    signCloudMetaDataForItem01.setSingletonSigning(singletonSigningForItem01);
                    service.prepareFileForSignCloud(
                            agreementUUID,
                            //ESignCloudConstant.AUTHORISATION_METHOD_SMS,
                            ESignCloudConstant.AUTHORISATION_METHOD_EMAIL,
                            null,
                            notificationTemplate,
                            notificationSubject,
                            fileData01,
                            FILE_PDF_01,
                            mimeType01,
                            signCloudMetaDataForItem01);
                    break;
                case 3:
                    System.out.print("OTP:");
                    String otp = reader.readLine();
                    System.out.print("BillCode:"); // billCode response in prepareFileForSignCloud
                    String billCode = reader.readLine();
                    service.authorizeSingletonSigningForSignCloud(agreementUUID, otp, billCode);
                    break;
                case 4:
                    service.regenerateAuthorizationCodeForSignCloud(
                            agreementUUID,
                            notificationTemplate,
                            notificationSubject,
                            ESignCloudConstant.AUTHORISATION_METHOD_SMS);
                    break;
                default:
                    break;
            }
        } while (resultCode != 0);
    }

    private static void printUsage() {
        System.out.println("Welcome to eSignCloud Service");
        System.out.println("There are functions we support");
        System.out.println("1. prepareCertificateForSignCloud");
        System.out.println("2. prepareFilesForSignCloud");
        System.out.println("3. authorizeCounterSigningForSignCloud");
        System.out.println("4. regenerateAuthorizationCodeForSignCloud");
        System.out.println("5. Quit");
    }
}
