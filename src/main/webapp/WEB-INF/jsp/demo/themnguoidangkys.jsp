<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@include file="../layout/js.jsp" %>


<springForm:form method="POST" action="" id='submitFormModal' modelAttribute="demoUser" enctype="multipart/form-data">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="modal-header">
                <h2>${name} khách hàng</h2>
            </div>
            <div class="modal-body">
                <div class="row clearfix">
                    <div class="col-md-6 mb-0">
                        <label>Mã công ty (*)</label>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control" value="${demoUser.maCongTy }"
                                       name="maCongTy" autocomplete="nofill">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-0">
                        <label>Họ và tên (*)</label>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control" value="${demoUser.hoVaTen }"
                                       name="hoVaTen" autocomplete="nofill">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-0">
                        <label>Họ và tên</label>
                        <div class="form-group form-float">
                            <div class="form-line">
                                <input type="text" class="form-control" value="${demoUser.hoVaTen }"
                                       name="hoVaTen" autocomplete="nofill">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 mb-0 text-right">
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
            	maCongTy: {required: true}
            },
            messages: {
            	hoVaTen: {
                    required: "Nhập tên người dùng",
                },
                maCongTy: {
                    required: "Nhập mã công ty"
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
