package fis.com.vn.callapi.entities;

import com.google.gson.annotations.Expose;

import lombok.Data;
import lombok.ToString;

@Data
public class PramMoTaiKhoan {
	@Expose  String frontPortraitImage;
	@Expose  String otherPortraitImages;
	@Expose  String otherPortraitImages1;
	@Expose  String frontIdentityImage;
	@Expose  String backIdentityImages;
	@Expose String eKycRate;
	@Expose String fullName;
	@Expose String dob;
	@Expose String birtPlace;
	@Expose String gender;
	@Expose String identityType;
	@Expose String identityNumber;
	@Expose String issuedDate;
	@Expose String issuedBy;
	@Expose String countryCode;
	@Expose String phoneNumber;
	@Expose String email;
	@Expose String income;
	@Expose String taxCode;
	@Expose String industryGroup;
	@Expose String job;
	@Expose String position;
	@Expose String maritalStatus;
	@Expose String permanentAddress;
	@Expose String address;
	@Expose String district;
	@Expose String province;
	@Expose String resident;
	@Expose String otherOwner;
	@Expose String purpose;
	@Expose String legalAgreement;
	@Expose String fatcaInformation;
	@Expose String preferLocation;
	@Expose String productCode;
	@Expose String accuracyMethod;
	@Expose String referral;
	@Expose String confirmTermSea;
	@Expose String agent;
	@Expose String registerEbankUsername;
	@Expose String expiryDate;
}
