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
function numberWithCommas(x) {
	return tonumber(x).toLocaleString("en-US");
//     return tonumber(x).replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}
function tonumber(str) {
	str = str.replace(/[^0-9]/g, "");
	if(str != "")
		return parseInt(str.replace(/[^0-9]/g, ""));
	else
		return "";
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
	alertRC(url, "Bạn có chắc muốn xóa?");
}
function change(url) {
	alertRC(url, "Bạn có chắc muốn thay đổi?");
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
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Thoát",
        closeOnConfirm: false
    }, function () {
    	location.href=url;
    });
}
function goBack() {
  	window.history.back();
}
function getBase64(file, idImg) {
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
	function addEventFile(fileInput) {
		$("#"+fileInput).change(function(){
			if(ValidateSingleInput(this)) {
				var file = document.querySelector('#'+fileInput).files[0];
				getBase64(file, fileInput);
			}
		});
	}
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
	            	toastr.error("Accepted file: " + _validFileExtensions.join(", "));
	                oInput.value = "";
	                return false;
	            }
	        }
	    }
	    return true;
	}
</script>
<style>
/* Absolute Center Spinner */
.loading {
  position: fixed;
  z-index: 999;
  height: 2em;
  width: 2em;
  overflow: show;
  margin: auto;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
.register .register-form {
padding-top: 30px;
}
/* Transparent Overlay */
.loading:before {
  content: '';
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
    background: radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0, .8));

  background: -webkit-radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0,.8));
}

/* :not(:required) hides these rules from IE9 and below */
.loading:not(:required) {
  /* hide "loading..." text */
  font: 0/0 a;
  color: transparent;
  text-shadow: none;
  background-color: transparent;
  border: 0;
}

.loading:not(:required):after {
  content: '';
  display: block;
  font-size: 10px;
  width: 1em;
  height: 1em;
  margin-top: -0.5em;
  -webkit-animation: spinner 150ms infinite linear;
  -moz-animation: spinner 150ms infinite linear;
  -ms-animation: spinner 150ms infinite linear;
  -o-animation: spinner 150ms infinite linear;
  animation: spinner 150ms infinite linear;
  border-radius: 0.5em;
  -webkit-box-shadow: rgba(255,255,255, 0.75) 1.5em 0 0 0, rgba(255,255,255, 0.75) 1.1em 1.1em 0 0, rgba(255,255,255, 0.75) 0 1.5em 0 0, rgba(255,255,255, 0.75) -1.1em 1.1em 0 0, rgba(255,255,255, 0.75) -1.5em 0 0 0, rgba(255,255,255, 0.75) -1.1em -1.1em 0 0, rgba(255,255,255, 0.75) 0 -1.5em 0 0, rgba(255,255,255, 0.75) 1.1em -1.1em 0 0;
box-shadow: rgba(255,255,255, 0.75) 1.5em 0 0 0, rgba(255,255,255, 0.75) 1.1em 1.1em 0 0, rgba(255,255,255, 0.75) 0 1.5em 0 0, rgba(255,255,255, 0.75) -1.1em 1.1em 0 0, rgba(255,255,255, 0.75) -1.5em 0 0 0, rgba(255,255,255, 0.75) -1.1em -1.1em 0 0, rgba(255,255,255, 0.75) 0 -1.5em 0 0, rgba(255,255,255, 0.75) 1.1em -1.1em 0 0;
}

/* Animation */

@-webkit-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-moz-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-o-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
</style>
<div class="loading" style="display: none;">Loading&#8230;</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#btnRegister").click(function(){
		if($("#submitForm").valid()) {
			$(".loading").show();
		}
	});
});
function createVideoBack(idVideo, idAction, idImg, idCanvas, idFrame) {
	navigator.mediaDevices.getUserMedia({video: { facingMode: 'environment' }}).then(function(stream) {
		window.stream = stream; 
		document.getElementById(idVideo).srcObject = stream;
	})
	.catch(function(err) {
		console.log('navigator.MediaDevices.getUserMedia error: ', error.message, error.name);
	});
	$("#"+idAction).click(function(){
		$("#videoTakePhotoFront").appendTo("#"+idFrame);
		$("#canvasTakePhotoFront").appendTo("#"+idFrame);
		if(document.getElementById(idVideo).paused){
			document.getElementById(idVideo).play();	
			$("#"+idVideo).show();
			$("#"+idImg).hide();
		} else {
			document.getElementById(idVideo).pause();
			$("#"+idVideo).hide();
			$("#"+idImg).show();
			process(idVideo, idImg, idCanvas);	
		}
	});
}
function process(idVideo, idImg, idCanvas) {
	try {
		document.getElementById(idCanvas).width = document.getElementById(idVideo).videoWidth;
		document.getElementById(idCanvas).height = document.getElementById(idVideo).videoHeight;
		document.getElementById(idCanvas).getContext('2d').drawImage(document.getElementById(idVideo), 0, 0, document.getElementById(idCanvas).width, document.getElementById(idCanvas).height);
		
		var type = "image/jpeg"
		var data = document.getElementById(idCanvas).toDataURL(type);
		$("#"+idImg).attr("src", data);
		data = data.replace('data:' + type + ';base64,', ''); 
	} catch (err) {
		console.log(err);
	}
}
function createVideoBack1(idVideo, idAction, idImg, idCanvas, idFrame) {
	var constraints = { 
		    video: {
		    	facingMode: 'environment',
		        width: { ideal: 1280 },
		        height: { ideal: 1024 } 
		    } 
		};
	navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
		window.stream = stream; 
		document.getElementById(idVideo).srcObject = stream;
	})
	.catch(function(err) {
		console.log('navigator.MediaDevices.getUserMedia error: ', error.message, error.name);
	});
	$("#"+idAction).click(function(){
		$("#videoTakePhotoFront").appendTo("#"+idFrame);
		$("#canvasTakePhotoFront").appendTo("#"+idFrame);
		if(document.getElementById(idVideo).paused){
			document.getElementById(idVideo).play();	
			$("#"+idVideo).show();
			$("#"+idImg).hide();
		} else {
			document.getElementById(idVideo).pause();
			$("#"+idVideo).hide();
			$("#"+idImg).show();
			process1(idVideo, idImg, idCanvas);	
		}
	});
}
function process1(idVideo, idImg, idCanvas) {
	try {
		document.getElementById(idCanvas).width = document.getElementById(idVideo).videoWidth;
		document.getElementById(idCanvas).height = document.getElementById(idVideo).videoHeight;
		document.getElementById(idCanvas).getContext('2d').drawImage(document.getElementById(idVideo), 0, 0, document.getElementById(idCanvas).width, document.getElementById(idCanvas).height);
		
		var type = "image/jpeg"
		var data = document.getElementById(idCanvas).toDataURL(type);
		$("#"+idImg).attr("src", data);
		data = data.replace('data:' + type + ';base64,', ''); 
	} catch (err) {
		console.log(err);
	}
}
window.mobileCheck = function() {
  let check = false;
  (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
  return check;
};
</script>