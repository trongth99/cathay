<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<script type="text/javascript">
$().ready(function() {
	$("#submitForm").validate({
		rules: {
			"old-password": {
				required: true,
				minlength: 8
			},
			"new-password": {
				required: true,
				minlength: 8
			},
			"re-password": {
				equalTo: "#new-password",
				minlength: 8
				
			}
		},
		messages: {
			"old-password": {
				required: '<spring:message code="bat_buoc_nhap_mat_khau_cu" />',
				minlength: '<spring:message code="hay_nhap_it_nhat_8_ky_tu" />'
			},
			"new-password": {
				required: '<spring:message code="bat_buoc_nhap_mat_khau_moi" />',
				minlength: '<spring:message code="hay_nhap_it_nhat_8_ky_tu" />'
			},
			"re-password": {
				equalTo: '<spring:message code="hai_mat_khau_phai_giong_nhau" />',
				minlength: '<spring:message code="hay_nhap_it_nhat_8_ky_tu" />'
			}
		}, 
        errorPlacement: function(error, element) {
        	$(element).parent().parent().append(error);
         }
	});
});
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			<spring:message code="thay_doi_mat_khau" />
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active"><spring:message code="thay_doi_mat_khau" /></li>
		</ol>
	</section>

	<!-- Main content -->
	<springForm:form method="POST" action="" id='submitForm'>
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              </h3>
	
	              <div class="box-tools">
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<div class="clearfix">
						<div class="col-md-12 mb-0">
							<div class="form-group form-float">
								<div class="form-line">
									<label class="form-label"><spring:message code="mat_khau_cu" /> (<font style="color: red;">*</font>)</label>
									<input type="password" class="form-control" value="" name="old-password">
								</div>
							</div>
						</div>
						<div class="col-md-12 mb-0">
							<div class="form-group form-float">
								<div class="form-line">
									<label class="form-label"><spring:message code="mat_khau_moi" /> (<font style="color: red;">*</font>)</label>
									<input type="password" class="form-control" value="" id="new-password" name="new-password">
								</div>
							</div>
						</div>
						<div class="col-md-12 mb-0">
							<div class="form-group form-float">
								<div class="form-line">
									<label class="form-label"><spring:message code="xac_nhan_mat_khau_moi" /> (<font style="color: red;">*</font>)</label>
									<input type="password" class="form-control" value="" name="re-password">
								</div>
							</div>
						</div>
						<div class="col-md-12 mb-0" style="margin-bottom: 20px;">
							<a href="${contextPath}/" class="btn btn-default m-t-15 waves-effect"><spring:message code="quay_lai" /></a>
							<button type="submit" class="btn btn-primary m-t-15 waves-effect"><spring:message code="doi_mat_khau" /></button>
							<div style="clear: both;"></div>
						</div>
						<hr/>
					</div>
	            </div>
	            <!-- /.box-body -->
	          </div>
			
		</section>
	</springForm:form>
	<!-- /.content -->
</div>

<%@include file="../layout/footer2.jsp"%>