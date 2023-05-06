<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@include file="../../layout/js.jsp" %>


<springForm:form method="POST" action="" id='submitFormModal'>
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="modal-header">
                <h2>Sửa ngày sử dụng chữ ký số</h2>
            </div>
            <div class="modal-body">
                <div class="row clearfix">
                    <div class="col-md-6 mb-0">
                        <label>Ngày bắt đầu</label>
                        <input type="hidden" name="id" value="${quanLyChuKySo.id }"/>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control datepicker" value="<fmt:formatDate pattern = "dd/MM/yyyy" value = "${quanLyChuKySo.ngayBatDau }" />" id= 'ngayBatDau' name="ngayBatDau" autocomplete="off">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-0">
                        <label class="form-label">Ngày kết thúc</label>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control datepicker" value="<fmt:formatDate pattern = "dd/MM/yyyy" value = "${quanLyChuKySo.ngayKetThuc }" />" id='ngayKetThuc' name="ngayKetThuc" autocomplete="off"/>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-12 mb-0 text-right">
                        <button type="button" class="btn btn-primary btn-sm" id='submitbt'><i class="fa fa-save"></i> <span>Lưu</span></button>
                        <button class="btn btn-danger btn-sm" data-dismiss="modal"><i class="fa fa-times"></i> <span>Đóng</span></button>
                    </div>
                </div>
            </div>
        </div>
</springForm:form>
<script type="text/javascript">
    $(document).ready(function () {

    	$("#submitbt").click(function(){
			if(endAfterStart($("#ngayBatDau").val(), $("#ngayKetThuc").val())){
				$("#submitFormModal").submit();
			} else {
				toastr.error('Ngày bắt đầu sử dụng phải nhỏ hơn ngày kết thúc sử dụng');
			}
		});
        $('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
    });
</script>

<%@include file="../../layout/footerAjax.jsp" %>
