<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@include file="../layout/js.jsp" %>


<springForm:form method="POST" action="" id='submitFormModal' modelAttribute="thongTinNguoiDungTimKiem" enctype="multipart/form-data">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="modal-header">
                <h2>${name} khách hàng</h2>
            </div>
            <div class="modal-body">
                <div class="row clearfix">
                    <div class="col-md-6 mb-0">
                        <label>Họ và tên (*)</label>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control" value="${demoUser.hoVaTen }"
                                       name="hoVaTen" autocomplete="nofill">
                            </div>
                        </div>
                    </div>
                    <c:if test="${empty edit }">
	                    <div class="col-md-6 mb-0">
	                        <label class="form-label">Ảnh mẫu (*)</label>
	                        <div class="form-group form-float">
	                            <div class="form-line">
	                                <div class="custom-file">
				                        <input type="file" class="custom-file-input" id="exampleInputFile" name="file" accept=".jpg,.png,.gif,.jpeg">
				                        <label class="custom-file-label" for="exampleInputFile">Choose file</label>
				                    </div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-md-6 mb-0">
	                        <label class="form-label">Ảnh mặt trước giấy tờ</label>
	                        <div class="form-group form-float">
	                            <div class="form-line">
	                                <div class="custom-file">
				                        <input type="file" class="custom-file-input" id="anhMatTruoc" name="anhMatTruoc" accept=".jpg,.png,.gif,.jpeg">
				                        <label class="custom-file-label" for="anhMatTruoc">Choose file</label>
				                    </div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="col-md-6 mb-0">
	                        <label class="form-label">Ảnh mặt sau giấy tờ</label>
	                        <div class="form-group form-float">
	                            <div class="form-line">
	                                <div class="custom-file">
				                        <input type="file" class="custom-file-input" id="anhMatSau" name="anhMatSau" accept=".jpg,.png,.gif,.jpeg">
				                        <label class="custom-file-label" for="anhMatSau">Choose file</label>
				                    </div>
	                            </div>
	                        </div>
	                    </div>
                    </c:if>
                    <div class="col-md-12 mb-0 text-right">
                        <div id="ajax-load" class="preloader pl-size-xs hidden">
                            <div class="spinner-layer pl-red-grey">
                                <div class="circle-clipper left">
                                    <div class="circle"></div>
                                </div>
                                <div class="circle-clipper right">
                                    <div class="circle"></div>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm"><i
                            class="fa fa-save"></i> <span>Lưu</span></button>
                        <button class="btn btn-danger btn-sm" data-dismiss="modal"><i
                            class="fa fa-times"></i> <span>Đóng</span></button>
                    </div>
                </div>
            </div>
        </div>
</springForm:form>
<script type="text/javascript">
    $(document).ready(function () {

        $.validator.addMethod("valueNotEquals", function (value, element, arg) {
            return arg !== value;
        }, "Value must not equal arg.");

        $("#submitFormModal").validate({
            ignore: function (index, el) {
                let $el = $(el);
                if ($el.hasClass('always-validate')) {
                    return false;
                }
                return $el.is(':hidden');
            },
            rules: {
            	hoVaTen: {required: true},
            	file: {required: true}
            },
            messages: {
            	hoVaTen: {
                    required: "Nhập tên người dùng",
                },
                file: {
                    required: "Tải ảnh mẫu"
                }
            }
        });
        $(".custom-file-input").change(function(){
        	if($(this).val() != "")
        		$(this).parent().find(".custom-file-label").html($(this).val());
        	else
        		$(this).parent().find(".custom-file-label").html("Choose file");
        });
    });
</script>

<%@include file="../layout/footerAjax.jsp" %>
