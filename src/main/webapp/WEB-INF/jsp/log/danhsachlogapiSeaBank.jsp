<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.12/ace.js" type="text/javascript" charset="utf-8"></script>

<div class="content-wrapper">
	<section class="content-header">
		<h1>
			<spring:message code="bao_cao_log_request" />
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active"><spring:message code="bao_cao_log_request" /></li>
		</ol>
	</section>
	
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              		<button class="btn  btn-primary btn-xs" type="button" id="download" >
	                         <i class="fa fa-download" aria-hidden="true"></i>
	                         <span>Tải danh sách</span>
	                     </button>
	              </h3>
	
	              <div class="box-tools" style="position: relative;text-align: right;">
	                <div class="form-inline input-group-sm" style="width: 100%;">
						 <select class="form-control form-control-sm" name="loaiApi" data-toggle="tooltip" title="Loại api">
							<option value="" ${empty loaiApi ? 'selected' : ''}><spring:message code="tat_ca" /></option>
							<option value="api lấy thông tin từ los" ${loaiApi eq 'api lấy thông tin từ los' ? 'selected': ''}>api lấy thông tin từ los</option>
							<option value="api mở tktt seabank" ${loaiApi eq 'api mở tktt seabank' ? 'selected': ''}>api mở tktt seabank</option>
							<option value="api close acc" ${loaiApi eq 'api close acc' ? 'selected': ''}>api close acc</option>
							<option value="api check Bankaccount" ${loaiApi eq 'api check Bankaccount' ? 'selected': ''}>api check Bankaccount</option>
							<option value="api update thông tin los" ${loaiApi eq 'api update thông tin los' ? 'selected': ''}>api update thông tin los</option>
							<option value="api send-contracts" ${loaiApi eq 'api send-contracts' ? 'selected': ''}>api send-contracts</option>
							<option value="api lấy thông tin từ los" ${loaiApi eq 'api lấy thông tin từ los' ? 'selected': ''}>api lấy thông tin từ los</option>
							<option value="send sms" ${loaiApi eq 'send sms' ? 'selected': ''}>send sms</option>
						</select> 
						<input class="form-control form-control-sm" type="number" value="<c:out value="${status}"/>" name="status" placeholder="<spring:message code="trang_thai" />" data-toggle="tooltip" title="<spring:message code="trang_thai" />"/> 
						<input class="form-control form-control-sm" type="text" value="<c:out value="${uri}"/>" name="uri" placeholder="Uri" data-toggle="tooltip" title="Uri"/> 
						<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromDate}"/>" name="fromDate" placeholder="Từ ngày" data-toggle="tooltip" title="Từ ngày" autocomplete="off"/>
						<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDate}"/>" name="toDate" placeholder="Đến ngày" data-toggle="tooltip" title="Đến ngày" autocomplete="off"/>
	                   
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${soHopDong}"/>" name="soHopDong" placeholder="Số hợp đồng" data-toggle="tooltip" title="Số hợp đồng" autocomplete="off"/>
	                   	<input class="form-control form-control-sm" type="text" value="<c:out value="${id_los}"/>" name="id_los" placeholder="Los id" data-toggle="tooltip" title="Los id"/> 
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${soCmt}"/>" name="soCmt" placeholder="Số chứng minh thư" data-toggle="tooltip" title="Số chứng minh thư" autocomplete="off"/>
	                   
	                    <button type="button" class="btn btn-sm btn-primary" id="search">
							<i class="fa fa-search"></i>
						</button>
	                </div>
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<c:if test="${not empty logApiSeaBanks }">
		            	<table class="table table-bordered table-sm table-striped">
							<thead>
								<tr>
									<th style="max-width: 40px;text-align: center;">#</th>
									<th>Los ID</th>
									<th>Số cmt</th>
									<th>Số hợp đồng</th>
									<th>Username</th>
									<th>Uri</th>
									<th>Trạng thái</th>
									<th>Thời gian xử lý(ms)</th>
									<th style="width: 180px;">Thời gian</th>
									<th>Phương thức</th>
									<th>Mô tả</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${logApiSeaBanks}" var="item" varStatus="status">
									<tr>
										<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
										<td><c:out value="${item.idLos }"/></td>
										<td><c:out value="${item.idCard }"/></td>
										<td><c:out value="${item.idContract }"/></td>
										<td><c:out value="${item.username }"/></td>
										<td style="padding-left: 5px;"><a href="javascript:void(0)"
										 onclick="loadEdit('${contextPath}/danh-sach-log-api-seabank/xem2?code=<c:out value="${item.code }"/>&id=${item.id}&logId=<c:out value="${item.logId}"/>&time=<fmt:formatDate pattern = "yyyy-MM-dd" value = "${item.date }" />')" data-toggle="modal" data-target="#largeModal" class="text-info"><c:out value="${item.uri }"/></a></td>
								         
								        <td> 
											<c:if test="${item.status  eq 200  }">
												<b style="color: blue;">${item.status}</b>
											</c:if>
											<c:if test="${ item.status  eq 201 }">
												<b style="color: blue;">${item.status}</b>
											</c:if>
											<c:if test="${item.status  eq 0  }">
												<b style="color: blue;">${item.status}</b>
											</c:if>
											
											<c:if test="${item.status  ne 200 && item.status  ne 201 && item.status ne 0}">
												<b style="color: red;">${item.status}</b>
											</c:if>
											
										</td> 
										<td><fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${item.timeHandling }" /></td>
										<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.date }" /></td> 
										<td><c:out value="${item.method }"/></td>
										<td><c:out value="${item.mota }"/></td>
										<td class="text-center">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-log-api-seabank/xem2?id=${item.id}&logId=<c:out value="${item.logId}"/>&time=<fmt:formatDate pattern = "yyyy-MM-dd" value = "${item.date }" />')" data-toggle="modal" data-target="#largeModal" class="text-info">
												<i class="fa fa-eye"></i>
											</a>
											
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<%@include file="../layout/paginate.jsp"%>
		            </c:if>
	            </div>
	            <!-- /.box-body -->
	          </div>
			
		</section>
	</form>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$("#download").click(function(){
			$("#submitForm").attr("action", "${contextPath}/danh-sach-log-api-seabank/export");
			$("#submitForm").submit();
		}); 
		$("#search").click(function(){
			$("#submitForm").attr("action", "${contextPath}/danh-sach-log-api-seabank");
			$("#submitForm").submit();
		});
		$('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
	});
</script>	

<%@include file="../layout/footer2.jsp"%>