package fis.com.vn.esigncloud.datatypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDetails {

    private String personalName;
    private String organization;
    private String organizationUnit;
    private String title;
    private String email;
    private String telephoneNumber;
    private String location;
    private String stateOrProvince;
    private String country;
    private String personalID;
    private String passportID;
    private String citizenID;
    private String taxID;
    private String budgetID;
    
    private byte[] applicationForm;
    private byte[] requestForm;
    private byte[] authorizeLetter;
    private byte[] photoIDCard;
    private byte[] photoFrontSideIDCard;
    private byte[] photoBackSideIDCard;
    private byte[] photoActivityDeclaration;
    private byte[] photoAuthorizeDelegate;

    @JsonProperty("applicationForm")
    public byte[] getApplicationForm() {
        return applicationForm;
    }

    public void setApplicationForm(byte[] applicationForm) {
        this.applicationForm = applicationForm;
    }

    @JsonProperty("authorizeLetter")
    public byte[] getAuthorizeLetter() {
        return authorizeLetter;
    }

    public void setAuthorizeLetter(byte[] authorizeLetter) {
        this.authorizeLetter = authorizeLetter;
    }

    @JsonProperty("taxID")
    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    @JsonProperty("budgetID")
    public String getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(String budgetID) {
        this.budgetID = budgetID;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("organization")
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @JsonProperty("organizationUnit")
    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    @JsonProperty("personalName")
    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    @JsonProperty("personalID")
    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    @JsonProperty("passportID")
    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    @JsonProperty("photoActivityDeclaration")
    public byte[] getPhotoActivityDeclaration() {
        return photoActivityDeclaration;
    }

    public void setPhotoActivityDeclaration(byte[] photoActivityDeclaration) {
        this.photoActivityDeclaration = photoActivityDeclaration;
    }

    @JsonProperty("photoAuthorizeDelegate")
    public byte[] getPhotoAuthorizeDelegate() {
        return photoAuthorizeDelegate;
    }

    public void setPhotoAuthorizeDelegate(byte[] photoAuthorizeDelegate) {
        this.photoAuthorizeDelegate = photoAuthorizeDelegate;
    }

    @JsonProperty("photoIDCard")
    public byte[] getPhotoIDCard() {
        return photoIDCard;
    }

    public void setPhotoIDCard(byte[] photoIDCard) {
        this.photoIDCard = photoIDCard;
    }

    @JsonProperty("requestForm")
    public byte[] getRequestForm() {
        return requestForm;
    }

    public void setRequestForm(byte[] requestForm) {
        this.requestForm = requestForm;
    }

    @JsonProperty("stateOrProvince")
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    @JsonProperty("telephoneNumber")
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("photoFrontSideIDCard")
    public byte[] getPhotoFrontSideIDCard() {
        return photoFrontSideIDCard;
    }

    public void setPhotoFrontSideIDCard(byte[] photoFrontSideIDCard) {
        this.photoFrontSideIDCard = photoFrontSideIDCard;
    }

    @JsonProperty("photoBackSideIDCard")
    public byte[] getPhotoBackSideIDCard() {
        return photoBackSideIDCard;
    }

    public void setPhotoBackSideIDCard(byte[] photoBackSideIDCard) {
        this.photoBackSideIDCard = photoBackSideIDCard;
    }

    @JsonProperty("citizenID")
    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }
}