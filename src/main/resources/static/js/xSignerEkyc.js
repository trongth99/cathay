var extId = "knldjmfmopnpolahpmmgbagdohdnhkik";
var selectedCert;
var selectedCertThumbprint;
var fileContent;
var signedContent;
function createCertList() {
	chrome.runtime.sendMessage(extId, {type: "cert"},
		function(response) {
	   		var certTable = document.getElementById("certTable");
			while(certTable.rows.length > 0) {
				certTable.deleteRow(0);
			}
	   		for (var i = 0; i < response.certificates.length; i++) {
	   		    var cert = response.certificates[i];
	   		    var certHTML = 
	       		"           <td align=\"center\"><input type=\"radio\" value=\"" + i + "\" name=\"selectCert\">" +
	   			"			<input type=\"hidden\" id=\"cert_" + i + "\" value=\"" + cert.cert + "\"/>" +
	   			"			<input type=\"hidden\" id=\"thumbprint_" + i + "\" value=\"" + cert.thumbprint + "\"/>" +		   			
	   			"			</td>\n" + 
	       		"           <td>" + cert.mst + "</td>\n" +
	       		"           <td>" + cert.subject + "</td>\n" +
	       		"           <td>" + cert.issuer + "</td>\n" +
	       		"           <td align=\"center\">" + cert.validFrom + "</td>\n" +
	       		"           <td align=\"center\">" + cert.validTo + "</td>\n";
	   			var row = certTable.insertRow(-1);
	   			row.innerHTML = certHTML;
	   		}
	   		document.getElementsByName("selectCert")[0].checked = true;
	});
}
function slectCert(){
	closeCertListDialog();
	goiHamalert();
	var certs = document.getElementsByName('selectCert');
	for(var i = 0; i < certs.length; i++){
	    if(certs[i].checked){
	        selectedCert = document.getElementById("cert_" + i).value;
	        document.getElementById("base64Cert").value = selectedCert;
	        selectedCertThumbprint = document.getElementById("thumbprint_" + i).value;
			return true;
	    }
	}
	return false;
}
function selectFileToSign() {
	 const file = document.getElementById('taiFile').files[0];
	 const reader = new FileReader();
	
	 reader.addEventListener("load", function () {
	 	fileContent = reader.result.replace(/^data:.+;base64,/, '');
	 }, false);
	
	 if (file) {
	   reader.readAsDataURL(file);
	 }
}
function sign(obj) {
	var signMsg = {
		type: "sign",
		content: fileContent,
		thumbprint: selectedCertThumbprint
	}
	chrome.runtime.sendMessage(extId, signMsg,
			function(response) {
				signedContent = response.signedContent;
				luuNoiDungKy(signedContent, obj);
			});
} 

function showCertListDialog() {
	createCertList();
	var modal = document.querySelector(".modal_ext");
    modal.classList.toggle("show-modal");
}
function closeCertListDialog() {
	var modal = document.querySelector(".modal_ext");
    modal.classList.toggle("show-modal");
}
document.addEventListener("DOMContentLoaded", function() {
	const modal_div = document.createElement('div');
	modal_div.setAttribute('class', 'modal_ext');
	modal_div.setAttribute('id', 'modal_div');
	
	const modal_content = document.createElement('div');
	modal_content.setAttribute('class', 'modal-content_ext');
	modal_content.innerHTML = 
		"	  <h2>Danh sách chứng thư số</h2>\r\n" +  
		"       <table class=\"xSignerTable\" cellpadding=\"5\" cellspacing=\"0\" style=\"text-align: left;\">\r\n" + 
		"        <thead>\r\n" + 
		"         <tr style=\"text-align: center;\">\r\n" + 
		"           <th rowspan=\"2\">Chọn</th>\r\n" + 
		"           <th rowspan=\"2\">Mã số thuế</th>\r\n" + 
		"           <th rowspan=\"2\">Chủ thể</th>\r\n" + 
		"           <th rowspan=\"2\">Đơn vị CA</th>\r\n" + 
		"           <th colspan=\"2\">Ngày hiệu lực</th>\r\n" + 
		"         </tr>\r\n" + 
		"         <tr>\r\n" + 
		"           <th style=\"text-align: center;\">Từ ngày</th>\r\n" + 
		"           <th style=\"text-align: center;\">Đến ngày</th>\r\n" + 
		"         </tr>\r\n" + 
		"        </thead>\r\n" + 
		"        <tbody id=\"certTable\">\r\n" + 
		"        </tbody>\r\n" + 
		"       </table>\r\n" +
		"	   <br/>\r\n" + 
		"	   <input type=\"button\" class='btn btn-primary btn-sm' onclick=\"slectCert();\" value=\"Chọn\"/>\r\n" + 
		"      &nbsp;" +
		"	   <input onClick=\"closeCertListDialog()\" class='btn btn-default btn-sm' type=\"button\" value=\"Bỏ qua\"/>";		
	modal_div.appendChild(modal_content);
	
	document.body.appendChild(modal_div);
});
