<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/ZCommon_0.css?v=20211004-01">
<style type="text/css">
.btn-warning {
background: #00A950;
color: white;
}
.title {
color : red;
}
</style>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading" style="color: red">THÔNG TIN LƯU Ý</h3>
			<div class="row register-form">
			 	<div class="col-md-12">
					<div class="row">
						<div class="col-md-12">
							<p class="title">Chi phí khi giải ngân?<p/>
							<p>- Quý Khách thực hiện giải ngân vào tài khoản thanh toán tại SeABank miễn phí chuyển khoản. Trường hợp quý khách giải ngân vào TKTT tại ngân hàng khác thì các khoản chi phí chuyển khoản phát sinh liên quan sẽ được tính phí cho Bên Vay .<p/>
							
						</div>	
					</div>
					<div class="row">
						<div class="col-md-12" >
							<p class="title">Giải ngân với TKTT ngân hàng khác?<p/>
						</div>	
					</div>
						<div class="row">
						<div class="col-md-12">
							
							<p>- Trường hợp TK nhận tại ngân hàng không hoạt động qua mạng lưới chuyển tiền Napas 24/7 thì hệ thống PTF tự động chuyển Phương thức giải ngân là loại "Khác". <p/>
							
						</div>	
					</div>
					<div class="row">
						<div class="col-md-12">
							<p class="title">Sản phẩm vay hạn mức<p/>
							<p>- Với sản phẩm vay hạn mức thì quý khách mới có thể nhập thông tin số tiền giải ngân cho lần giải ngân đầu tiên. Thông tin số tiền giải ngân lần đầu phải nhỏ hơn hoặc bằng số tiền vay trên hợp đồng vay hạn mức. Trong trường hợp khách hàng chưa yêu cầu giải ngân thì Quý khách chọn “Chưa giải ngân”, hệ thống cập nhật thông tin số tiền giải ngân lần đầu trên hợp đồng là bằng 0.<p/>
							
						</div>	
					</div>
					<div class="row">
						<div class="col-md-12">
							
							<p class="title" style="font-style: oblique;">Lưu ý: Các thông tin khách hàng điền tại màn hình này sẽ được cập nhật lên mục yêu cầu giải ngân trên hợp đồng vay của Quý khách hàng tại PTF.<p/>
							
						</div>	
					</div>
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
			
