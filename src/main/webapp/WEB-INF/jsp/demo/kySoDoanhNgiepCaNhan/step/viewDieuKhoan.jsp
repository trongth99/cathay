<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			
			<div class="row register-form">
				<div class="col-sm-12">
					
					<hr/>
					<iframe id="base64File" style="width: 100%; height: 900px; border: 0;" src="${LINK_ADMIN }/viewpdf?file=${LINK_ADMIN }/viewpdf/byte/${fileDieuKhoan}"></iframe>
					<hr/>
					
				</div>
				<input type="button" class="btnRegister" id="btnRegisters" onclick="close(this)" style="background-color: black;" value="Đóng"/>
			</div>
		</div>
	</div>
</div>
<script>
 $(document).ready(function(){
	$("#btnRegisters").click(function() {
		window.close();
	});
	
}); 
</script>

<%@include file="../layout/footer.jsp"%>
			
