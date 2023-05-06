<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@include file="../layout/js.jsp" %>


<springForm:form method="POST" action="" id='submitFormModal' modelAttribute="userInfo">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="modal-header">
                <h2><spring:message code="dat_lai_mat_khau" /></h2>
            </div>
            <div class="modal-body">
                <div class="row clearfix">
                	<div class="row">
	                    <div class="col-md-12 mb-0">
	                        <label class="form-label"><spring:message code="mat_khau" /> (*)</label>
	                        <div class="form-group form-float">
	                            <div class="form-line">
	                                <input type="password" class="form-control" value="" name="password"
	                                       autocomplete="off">
	                            </div>
	                        </div>
	                    </div>
                    </div>
                    <div class="col-md-12 mb-0 text-right">
                        <button type="submit" class="btn btn-primary btn-sm"><i
                            class="fa fa-save"></i> <span><spring:message code="luu" /></span></button>
                        <button class="btn btn-danger btn-sm" data-dismiss="modal"><i
                            class="fa fa-times"></i> <span><spring:message code="dong" /></span></button>
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
                password : {required: true, minlength: 8}
            },
            messages: {
                password: {
                    required: '<spring:message code="nhap_vao_mat_khau" />',
                    minlength: '<spring:message code="mat_khau_toi_thieu_tam" />'
                }
            }
        });
    });
</script>

<%@include file="../layout/footerAjax.jsp" %>
