<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Nhập số chứng minh thư</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step1" style="width: 100%;" method="post" id="submitForm">
					<div class="col-md-12">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Nhập số chứng minh thư" id="soCmt" name="soCmt" re/>
						</div>
						<div class="form-group">
							<input type="password" class="form-control" placeholder="Mật khẩu" id="matKhau" name="matKhau"/>
						</div>
						<input type="submit" class="btnRegister" id="btnRegister" value="Đăng nhập" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("#submitForm").validate({
        rules: {
        	soCmt: {required: true},
        	matKhau: {required: true},
        },
        messages: {
        	soCmt: {
                required: "Nhập số chứng minh thư",
            },
            matKhau: {
                required: "Nhập mật khẩu",
            }
        }
    });
});
</script>
<%@include file="../layout/footer.jsp"%>
			
