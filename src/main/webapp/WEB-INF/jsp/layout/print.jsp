<%@ page contentType="text/html; charset=UTF-8"%>
<div id="printme" style="display: none;"></div>
<script type="text/javascript">
function exportExel(tab_text) {
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf("MSIE ");
	if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If Internet Explorer
	{
		txtArea1.document.open("txt/html", "replace");
		txtArea1.document.write(tab_text);
		txtArea1.document.close();
		txtArea1.focus();
		sa = txtArea1.document.execCommand("SaveAs", true, "download.xls");
	} else {
		//other browser not tested on IE 11
		//sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
		tab_text = "\ufeff"+"<style>td,th{border:1px solid black;padding:5px;}.borderless th{border:none;}</style>"+tab_text;
		try {
			var blob = new Blob([ tab_text ], {
				type : "application/vnd.ms-excel; charset=UTF-8"
			});
			window.URL = window.URL || window.webkitURL;
			link = window.URL.createObjectURL(blob);
			a = document.createElement("a");
			if (document.getElementById("caption") != null) {
				a.download = document.getElementById("caption").innerText;
			} else {
				a.download = 'download';
			}
			a.href = link;
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
		} catch (e) {
		}
	}
}
function printTable() {
	
	$("tbody td[data-x=\"0\"]").css("position", "inherit");
	$("#printme").append("<table style='width:100%; border:none;border-spacing: 0;border-collapse: collapse;'><tr><td style='border:none;'>Đơn vị: "+$("#donViId").val()+"</td><td style='border:none;text-align:center;'>"+$("#tenKeHoach").val()+"</td><td style='border:none;text-align:right;'>Nguồn: "+$("#nguonId").val()+"</td></tr></table>");
	
	printTableID("mytable");
	if($("#mytable2").attr("id")) {
		printTableID("mytable2");
	}
	if($("#mytable3").attr("id")) {
		printTableID("mytable3");
	}
	if($("#mytable4").attr("id")) {
		printTableID("mytable4");
	}
	
	var titleHeader = '';
	var w = window.open();
	w.document.write("<style>td,th{border:1px solid #CCC;padding:5px;}.borderless th{border:none;}</style>"+$("#printme").html());
	$("#printme").empty();
	w.document.close();
	w.setTimeout(function() {
		w.print(); //happens 5 secs later
	}, 1000);
}

function printTableID(tableId) {
	console.log(tableId)
	var rows = $("#"+tableId+" .jexcel > tbody > tr");
	var height = 0;
	var heightBreak = 1024;
	console.log(rows.length)
    for (var i = 0; i < rows.length; i ++) {
    	
    	if(i == 0) {
    		var table = $("<table style='width:100%; page-break-after: always;border-spacing: 0;border-collapse: collapse;' />");
    		table.append($("#"+tableId+" .jexcel thead").clone());
    		table.append("<tbody />");
    	}
    	height += $(rows[i]).height();	
    		table.find("tbody").append($(rows[i]).clone());
    	if(i ==  (rows.length-1)) {
    		table.append($("#"+tableId+" .jexcel tfoot").clone());
        	$("#printme").append(table);
        }
    }
}

$(document).ready(function() {
	$("#taiexcel").click(function(){
		var ex1 = "<table style='width:100%; border:none;border-spacing: 0;border-collapse: collapse;'><tr><td style='border:none;'>Đơn vị: "+$("#donViId").val()+"</td><td style='border:none;text-align:center;'>"+$("#tenKeHoach").val()+"</td><td style='border:none;text-align:right;'>Nguồn: "+$("#nguonId").val()+"</td></tr></table>";
		var expStr = ex1+$("#mytable .jexcel")[0].outerHTML;
		if($("#mytable2").attr("id")) {
			expStr += $("#mytable2 .jexcel")[0].outerHTML;
		}
		if($("#mytable3").attr("id")) {
			expStr += $("#mytable3 .jexcel")[0].outerHTML;
		}
		if($("#mytable4").attr("id")) {
			expStr += $("#mytable4 .jexcel")[0].outerHTML;
		}
		exportExel(expStr);
	});
});
</script>