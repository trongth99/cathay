<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../../layout/header2.jsp"%>
<%@include file="../../layout/js.jsp"%>
<script src="${contextPath }/js/select2.full.min.js"></script>
<style>
/*@import url('//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-theme.min.css');*/
.stepwizard-step p {
    margin-top: 0px;
    color:#666;
}
.stepwizard-row {
    display: table-row;
}
.stepwizard {
    display: table;
    width: 100%;
    position: relative;
}
.stepwizard-step button[disabled] {
    /*opacity: 1 !important;
    filter: alpha(opacity=100) !important;*/
}
.stepwizard .btn.disabled, .stepwizard .btn[disabled], .stepwizard fieldset[disabled] .btn {
    opacity:1 !important;
    color:#bbb;
}
.stepwizard-row:before {
    top: 14px;
    bottom: 0;
    position: absolute;
    content:" ";
    width: 100%;
    height: 1px;
    background-color: #ccc;
    z-index: 0;
}
.stepwizard-step {
    display: table-cell;
    text-align: center;
    position: relative;
}
.btn-circle {
    width: 30px;
    height: 30px;
    text-align: center;
    padding: 6px 0;
    font-size: 12px;
    line-height: 1.428571429;
    border-radius: 15px;
}
.setup-content {
display: none;
}
.select2-container--default .select2-selection--single, .select2-selection .select2-selection--single {
height: 30px; 
padding: 4px 12px;
}
.select2 {
    width: 100% !important;
    border-radius: 3px;
}
.select2-container .select2-selection--single {
height: 30px;
}
</style>
<div class="content-wrapper">
	<section class="content-header">
		<h1>
			Ký số tài liệu
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Ký số tài liệu</li>
		</ol>
	</section>

	<form id="submitForm" action="" method="get">
		<section class="content">
			<div class="container-fluid">
				<div class="row clearfix">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="card">
							<div class="container" style="margin-top: 20px;margin-bottom: 20px;">
								<%@include file="menuStep.jsp"%>
							    <%@include file="step/step1.jsp"%>
							    <%-- <%@include file="step/step2.jsp"%> --%>
							    <%@include file="step/step3.jsp"%>
						    </div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</form>
</div>
<script type="text/javascript">
		$(document).ready(function () {
		    var navListItems = $('div.setup-panel div.stepwizard-step a'),
		        allWells = $('.setup-content'),
		        allNextBtn = $('.nextBtn');
	
		    allWells.hide();
	
		    navListItems.click(function (e) {
		        e.preventDefault();
		        var $target = $($(this).attr('href')),
		            $item = $(this);
		        
				var curClick = $(this).text().trim();
		        navListItems.each(function(){
		        	if(parseInt($(this).text().trim()) > parseInt(curClick)) {
		        		$(this).attr("disabled", "disabled");
		        		$(this).addClass("disabled");
		        	}
		        })
		        if (!$item.hasClass('disabled')) {
		            navListItems.removeClass('btn-primary').addClass('btn-default');
		            $item.addClass('btn-primary');
		            $item.removeClass('btn-default');
		            allWells.hide();
		            $target.show();
		            $target.find('input[type="text"]:eq(0)').focus();
		        }
		    });
	
		    $('div.setup-panel div a.btn-primary').trigger('click');
		});
		var _validFileExtensions = [".jpg", ".jpeg", ".png"];    
		function ValidateSingleInput(oInput) {
		    if (oInput.type == "file") {
		        var sFileName = oInput.value;
		         if (sFileName.length > 0) {
		            var blnValid = false;
		            for (var j = 0; j < _validFileExtensions.length; j++) {
		                var sCurExtension = _validFileExtensions[j];
		                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		                    blnValid = true;
		                    break;
		                }
		            }
		             
		            if (!blnValid) {
		            	toastr.error("Chỉ chấp nhận file có định dạng " + _validFileExtensions.join(", "));
		                oInput.value = "";
		                return false;
		            }
		        }
		    }
		    return true;
		}
		function addEventFile(fileInput, idBase64, idNameShow) {
			$("#"+fileInput).change(function(){
				if(ValidateSingleInput(this)) {
					$("#"+idBase64).html("");
					var file = document.querySelector('#'+fileInput).files[0];
					getBase641(file, idBase64);
				}
				$("#"+idNameShow).html($("#"+fileInput).val());
				if($("#"+idNameShow).html()!="") {
					$("#"+idNameShow).show();
				}
			});
			$("#"+fileInput).click(function(){
				$("#"+fileInput).val("");
				$("#"+idNameShow).hide();
				$("#"+idBase64+"Img").attr("src", "");
				$("#"+idBase64+"Img").hide();
			})
		}
		function getBase641(file, idImg) {
			if (file) {
				if(file.type.match(/image.*/)) {
					console.log('An image has been loaded');
					var reader = new FileReader();
					reader.onload = function (readerEvent) {
						var image = new Image();
						image.onload = function (imageEvent) {
							var canvas = document.createElement('canvas'),
							max_size = 1000,// TODO : pull max size from a site config
							width = image.width,
							height = image.height;
							if (width > height) {
								if (width > max_size) {
									height *= max_size / width;
									width = max_size;
								}
							} else {
								if (height > max_size) {
									width *= max_size / height;
									height = max_size;
								}
							}
							canvas.width = width;
							canvas.height = height;
							canvas.getContext('2d').drawImage(image, 0, 0, width, height);
							var dataUrl = canvas.toDataURL('image/jpeg');
						
							$("#"+idImg).html( dataUrl.replace("data:image/jpeg;base64,", ""));
							$("#"+idImg+"Img").attr("src", dataUrl);
							$("#"+idImg+"Img").show();
						}
						image.src = readerEvent.target.result;
					}
					reader.readAsDataURL(file);
				}

			}
		}
		function nextStep(obj) {
			var curStep = $(obj).closest(".setup-content"),
            curStepBtn = curStep.attr("id"),
            nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
            curInputs = curStep.find("input[type='text'],input[type='url']"),
            isValid = true;
// 	        isValid = validate(curStepBtn);

	        if (isValid) {
	        	nextStepWizard.removeClass('disabled');
	        	nextStepWizard.removeAttr('disabled').trigger('click');
	        }
		}
		
		function prevStep(obj) {
			var curStep = $(obj).closest(".setup-content"),
            curStepBtn = curStep.attr("id"),
            nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().prev().children("a"),
            curInputs = curStep.find("input[type='text'],input[type='url']"),
            isValid = true;
// 	        isValid = validate(curStepBtn);

	        if (isValid) {
	        	nextStepWizard.removeClass('disabled');
	        	nextStepWizard.removeAttr('disabled').trigger('click');
	        }
		}

		function getBase64(file, idZen) {
		   var reader = new FileReader();
		   reader.readAsDataURL(file);
		   reader.onload = function () {
			   base64 = reader.result.replace(/^data:.+;base64,/, '');
			   $("#"+idZen).html(base64);
		   };
		   reader.onerror = function (error) {
		     	console.log('Error: ', error);
		   };
		}
		function convertbase64ToBlob() {
			var base64str = result.pdf;

			// decode base64 string, remove space for IE compatibility
			var binary = atob(base64str.replace(/\s/g, ''));
			var len = binary.length;
			var buffer = new ArrayBuffer(len);
			var view = new Uint8Array(buffer);
			for (var i = 0; i < len; i++) {
			    view[i] = binary.charCodeAt(i);
			}

			// create the blob object with content-type "application/pdf"               
			var blob = new Blob( [view], { type: "application/pdf" });
			var url = URL.createObjectURL(blob);
		}
	</script>
	
	<link rel="stylesheet" type="text/css" href="${contextPath }/css/xSigner.css">
	<script src='${contextPath }/js/xSigner.js'></script>
<%@include file="../../layout/footer2.jsp"%>