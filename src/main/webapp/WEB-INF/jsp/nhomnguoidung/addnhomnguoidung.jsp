<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@include file="../layout/js.jsp"%>

<link href="${contextPath}/plugins/nestable/jquery-nestable.css" rel="stylesheet" />
<script src="${contextPath}/plugins/nestable/jquery.nestable.js"></script>
<script src="${contextPath}/js/sortable-nestable.js"></script>

<script type="text/javascript">
    $(document).ready(
        function() {
            var userGroupPermissions = '${userGroupPermissions}';
            if (userGroupPermissions) {
                var userGroupPermissionsJson = JSON
                    .parse(userGroupPermissions);
                for (var i = 0; i < userGroupPermissionsJson.length; i++) {
                    $("#checkbox" + userGroupPermissionsJson[i].moduleId)
                        .prop('checked', true);
                }
            }

            $("#submitFormModal").validate({
                rules : {
                    "groupName" : {
                        required : true,
                    }
                },
                messages : {
                    "groupName" : {
                        required : '<spring:message code="nhap_vao_ten_nhom" />',
                    }
                },
                errorPlacement : function(error, element) {
                    $(element).parent().parent().append(error);
                }

            });
        });
</script>

<springForm:form method="POST" action="" id='submitFormModal' modelAttribute="userGroup">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="background: white;">
        <div class="modal-header">
            <h2>${name } - <spring:message code="nhom_nguoi_dung" /></h2>
        </div>
        <div class="modal-body">
            <div class="row clearfix">
                <div class="col-md-4">
                    <label class="form-label"><spring:message code="ten_nhom" /></label>
                    <div class="form-group form-float">
                        <div class="form-line">
                            <input type="text" class="form-control" value="<c:out value='${userGroup.groupName }'/>" name="groupName">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <label class="form-label"><spring:message code="mo_ta" /></label>
                    <div class="form-group form-float">
                        <div class="form-line">
                            <input type="text" class="form-control" value="<c:out value='${userGroup.description }'/>" name="description">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <label class="form-label"><spring:message code="trang_thai" /></label>
                    <div class="form-group form-float">
                        <div class="form-line">
                            <select class="form-control show-tick" name="status">
								<option value="" ${empty userGroup.status ? 'selected' : ''}><spring:message code="tat_ca" /></option>
								<option value="0" ${userGroup.status eq '0' ? 'selected': ''}><spring:message code="khong_hoat_dong" /></option>
								<option value="1" ${userGroup.status eq '1' ? 'selected': ''}><spring:message code="hoat_dong" /></option>
							</select>
                        </div>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="dd">
                        <ol class="dd-list">
                            <c:forEach items="${userModules}" var="item">
                                <c:if test="${item.parentId eq 0}">
                                    <li class="dd-item" data-id="${item.id }">
                                        <div class="dd-handle"><spring:message code="${item.name }" text="${item.name }"/></div>
                                        <ol class="dd-list">
                                            <c:forEach items="${userModules}" var="itemChild">
                                                <c:if test="${itemChild.parentId eq item.id}">
                                                    <li class="dd-item" data-id="${itemChild.id }">
                                                        <div class="dd-handle">
                                                            <input type="checkbox" class="filled-in chk-col-red" id="checkbox${itemChild.id }" value="${itemChild.id }" name="permissions" />
                                                            <label for="checkbox${itemChild.id }"><spring:message code="${itemChild.name }" text="${itemChild.name }"/></label>
                                                        </div>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ol>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ol>
                    </div>
                </div>
                <div class="col-md-12 mb-0 text-center">
                    <span style="color: red;font-size: 12px;" id="ajax-error"></span>
                </div>
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
                            class="fa fa-save"></i> <span><spring:message code="luu" /></span></button>
                    <button class="btn btn-danger btn-sm" data-dismiss="modal"><i
                            class="fa fa-times"></i> <span><spring:message code="dong" /></span></button>
                </div>
            </div>
        </div>
    </div>
</springForm:form>

<%@include file="../layout/footerAjax.jsp"%>