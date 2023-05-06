<%@ page contentType="text/html; charset=UTF-8"%>
<input type="hidden" value="${success}" id="success"/>
<input type="hidden" value="${error}" id="error"/>
<script type="text/javascript">
$(document).ready(function(){
	$('ul.pagination').on('click', 'a', function () {
        // Create paging action
        $('#action').val('paging');
        var value = $(this).attr('data-value');
        var $page = $('#page');
        
        switch (value) {
            case 'next':
                $page.val(parseInt($page.val()) + 1);
                break;
            case 'end':
                $page.val('${totalPage}');
                break;
            case 'prev':
                if((parseInt($page.val()) - 1) >= 1)
                    $page.val(parseInt($page.val()) - 1);
                break;
            case 'begin':
                $page.val(1);
                break;
            default:
                $page.val(value);
        }
        $("#submitForm").attr("method", "GET");
        $("#submitForm").submit();
	});
	if($("#success").val() != "") {
		toastr.info($("#success").val());
    }
    if($("#error").val() != "") {
    	toastr.error($("#error").val());
    }
});
function loadAdd(urlAdd) {
	loadUrl(urlAdd);
}
function loadEdit(urlEdit) {
	loadUrl(urlEdit);
}
function loadUrl(url) {
	resetInfo();
	$.ajax({
		url : url,
		success : function(result) {
			$("#modal-content").html(result);
		}
	});
}
function resetInfo() {
	$("#error").val("");
	$("#success").val("");
	showWait();
}
function showWait() {
    $('#modal-content').html('<div  style="padding:10px;" class="d-flex justify-content-center"><div class="spinner-border text-primary" role="status"> <span class="sr-only">Loading...</span> </div></div>');

}

function processTable(data, idField, foreignKey, rootLevel) {
    let hash = {};

    for (let i = 0; i < data.length; i++) {
        let item = data[i];
        let id = item[idField];
        let parentId = item[foreignKey];

        hash[id] = hash[id] || [];
        hash[parentId] = hash[parentId] || [];

        item.items = hash[id];
        item.expanded = true;
        hash[parentId].push(item);
    }

    return hash[rootLevel];
}

function list_to_tree(list) {
        var map = {}, node, roots = [], i;
        for (i = 0; i < list.length; i += 1) {
            map[list[i].id] = i; // initialize the map
            list[i].items = []; // initialize the children
        }
        for (i = 0; i < list.length; i += 1) {
            node = list[i];
            if (node.parentId !== 0) {
                // if you have dangling branches check that map[node.parentId] exists
                list[map[node.parentId]].items.push(node);
            } else {
                roots.push(node);
            }
        }
        return roots;
    }


function alertSC(msg) {
	$.growl.notice({
        title: "Thông báo",
        message: msg
    });
}
function alertER(msg) {
	$.growl.error({
        title: "Thông báo",
        message: msg
    });
}
function deleteRC(url) {
	alertRC(url, '<spring:message code="ban_co_chac_muon_xoa" />');
}
function change(url) {
	alertRC(url, '<spring:message code="ban_co_chac_muon_thay_doi" />');
}
function nextUrl(url) {
	location.href=url;
}
function alertRC(url, title) {
	swal({
		title: title,
        text: "",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: '<spring:message code="dong_y" />',
        cancelButtonText: '<spring:message code="thoat" />',
        closeOnConfirm: false
    }, function () {
    	location.href=url;
    });
}
function alertRCDl(url, title) {
	swal({
		title: title,
        text: "",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: '<spring:message code="dong_y" />',
        cancelButtonText: '<spring:message code="thoat" />',
        closeOnConfirm: true
    }, function () {
    	location.href=url;
    });
}
function goBack() {
  	window.history.back();
}
function getBase64(file, idZen) {
   var reader = new FileReader();
   reader.readAsDataURL(file);
   reader.onload = function () {
	   base64 = reader.result.replace(/^data:.+;base64,/, '');
	   $("#"+idZen).html(base64);
	   $("#"+idZen+"Img").attr("src", "data:image/jpeg;base64,"+base64);
	   $("#"+idZen+"Img").show();
   };
   reader.onerror = function (error) {
     	console.log('Error: ', error);
   };
}
function addEventFile(fileInput) {
	$("#"+fileInput).change(function(){
		if(ValidateSingleInput(this)) {
			var file = document.querySelector('#'+fileInput).files[0];
			getBase64(file, fileInput);
		}
	});
}
var _validFileExtensions = [".jpg", ".pdf", ".jpeg", ".png"];    
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
</script>

<style type="text/css">
/* Style the Image Used to Trigger the Modal */
#myModalImg #myImg {
	border-radius: 5px;
	cursor: pointer;
	transition: 0.3s;
}

#myModalImg #myImg:hover {
	opacity: 0.7;
}

/* The Modal (background) */
#myModalImg.modal {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 9999; /* Sit on top */
	padding-top: 100px; /* Location of the box */
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.9); /* Black w/ opacity */
}

/* Modal Content (Image) */
#myModalImg .modal-content {
	margin: auto;
	display: block;
	width: 80%;
	max-width: 700px;
}

/* Caption of Modal Image (Image Text) - Same Width as the Image */
#myModalImg #caption {
	margin: auto;
	display: block;
	width: 80%;
	max-width: 700px;
	text-align: center;
	color: #ccc;
	padding: 10px 0;
	height: 150px;
}

/* Add Animation - Zoom in the Modal */
#myModalImg .modal-content, #myModalImg #caption {
	animation-name: zoom;
	animation-duration: 0.6s;
}

@keyframes zoom {
	from {transform: scale(0)
}

to {
	transform: scale(1)
}

}

/* The Close Button */
#myModalImg .close {
	position: absolute;
	top: 15px;
	right: 35px;
	color: #f1f1f1;
	font-size: 40px;
	font-weight: bold;
	transition: 0.3s;
}

#myModalImg .close:hover, #myModalImg .close:focus {
	color: #bbb;
	text-decoration: none;
	cursor: pointer;
}

/* 100% Image Width on Smaller Screens */
@media only screen and (max-width: 700px) {
	.modal-content {
		width: 100%;
	}
}
.select2, .bootstrap-tagsinput{
width: 100%;
}
.bootstrap-tagsinput{
border-radius: 0 !important;
background-color: #fff;
box-shadow: none !important;
border-color: #d2d6de !important;
}
.bg-aqua, .callout.callout-info, .alert-info, .label-info, .modal-info .modal-body {
    background-color: #367fa9 !important;
}

</style>
<script type="text/javascript">
	$(document).ready(function(){
		var modal = document.getElementById("myModalImg");

		// Get the image and insert it inside the modal - use its "alt" text as a caption
		var img = document.getElementsByClassName("img-thumbnail");
		var modalImg = document.getElementById("img01");
		var captionText = document.getElementById("caption");
		for (var i = 0; i < img.length; i++) {
			img[i].onclick = function() {
				modal.style.display = "block";
				modalImg.src = this.src;
				var title = this.getAttribute("title");
				if(title == 'facebook') {
					captionText.innerHTML = "<a href='"+this.alt+"' target='_blank'>Facebook</a>";
				} else {
					captionText.innerHTML = "";
				}
			}
		}
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];

		// When the user clicks on <span> (x), close the modal
		span.onclick = function() {
			modal.style.display = "none";
		}
		modal.onclick = function() {
			modal.style.display = "none";
		}
	})
</script>
<div id="myModalImg" class="modal">
	<span class="close">&times;</span> <img class="modal-content" id="img01">
	<div id="caption"></div>
</div>