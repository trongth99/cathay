<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>
<style>
.col-xs-12 {
	background: none;
}
</style>
<div class="content-wrapper">
	<section class="content-header">
		<h1>
			<spring:message code="dashboard" />
			(<span id='clock'></span>)
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"> <spring:message
						code="trang_chu" />
			</a></li>
			<li class="active"><spring:message code="dashboard" /></li>
		</ol>
	</section>

	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="info-box">
						<span class="info-box-icon bg-green"> <i
							class="fa fa-check-square-o"></i>
						</span>

						<div class="info-box-content">
							<span class="info-box-text"> <spring:message
									code="ekyc_thanh_cong" />
							</span> <span class="info-box-number"> <c:set var="tnew"
									scope="session" value="0" />
									 <c:forEach items="${thongKeTrangThais}" var="item" varStatus="status">
									<c:if test="${item.trangThaiEkyc eq 'success' }">
										<c:set var="tnew" scope="session" value="${item.tongSo }" />
									</c:if>
								</c:forEach> ${sumDaKy }
							</span>
						</div>
						<!-- /.info-box-content -->
					</div>
					<!-- /.info-box -->
				</div>
				<!-- /.col -->
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="info-box">
						<span class="info-box-icon bg-yellow"> <i class="fa fa-paper-plane-o"></i>
						</span>

						<div class="info-box-content">
							<span class="info-box-text"> <spring:message
									code="ekyc_that_bai" />
							</span> <span class="info-box-number"> <c:set var="reject"
									scope="session" value="0" /> <c:forEach
									items="${thongKeTrangThais}" var="item" varStatus="status">
									<c:if test="${item.trangThaiEkyc eq 'fail' }">
										<c:set var="reject" scope="session" value="${item.tongSo }" />
									</c:if>
								</c:forEach> ${sumDaGui }
							</span>
						</div>
						<!-- /.info-box-content -->
					</div>
					<!-- /.info-box -->
				</div>
				<%-- <div class="col-md-4 col-sm-6 col-xs-12">
					<div class="info-box">
						<span class="info-box-icon bg-yellow"> <i
							class="ion ion-ios-people-outline"></i>
						</span>

						<div class="info-box-content">
							<span class="info-box-text"> </span> <span
								class="info-box-number"> <c:set var="tongSo"
									scope="session" value="0" /> <c:forEach
									items="${thongKeTrangThais}" var="item" varStatus="status">
									<c:if test="${item.trangThaiEkyc eq 'success' }">
										<c:set var="tongSo" scope="session"
											value="${tongSo + item.tongSo }" />
									</c:if>
									<c:if test="${item.trangThaiEkyc eq 'fail' }">
										<c:set var="tongSo" scope="session"
											value="${tongSo + item.tongSo }" />
									</c:if>
								</c:forEach> ${tongSo }
							</span>
						</div>
						<!-- /.info-box-content -->
					</div>
					<!-- /.info-box -->
				</div> --%>
				<!-- /.col -->
			</div>
			<div class="box box-danger">
				<!-- <div class="box-header">
					<button type="button" class="btn btn-sm btn-primary" onclick="redirectdb('daily')">
						
					</button>
					<button type="button" class="btn btn-sm btn-primary" onclick="redirectdb('weekly')">
						
					</button>
					<button type="button" class="btn btn-sm btn-primary" onclick="redirectdb('monthly')">
						
					</button>
					<button type="button" class="btn btn-sm btn-primary" onclick="redirectdb('yearly')">
						
					</button>
				</div> -->
				<div style="clear: both;"></div>
				<div class="box-body table-responsive no-padding"></div>
				<div class="box-footer">
					<div class="row">
						<!-- <div class="col-md-1"></div> -->

						<div class="col-md-6">
							<div class="card">
								<div class="card-header">Line Chart</div>
								<div class="card-body">
									<p class="card-title"></p>
									<div class="canvas-wrapper">
										<canvas class="chart" id="linechart"></canvas>
									</div>
								</div>
							</div>
						</div>
						<!-- <div class="col-md-1"></div> -->
					<!-- </div>
					<div class="row"> -->
						<!-- <div class="col-md-1"></div> -->
						<div class="col-md-6">
							<div class="card">
								<div class="card-header">Bar Chart</div>

								<div class="card-body">
									<p class="card-title"></p>
									<div class="canvas-wrapper">
										<canvas height="160" width="328"
											style="width: 328px; height: 160px;" id="barchart"></canvas>
									</div>
								</div>
							</div>
						</div>
						<!-- <div class="col-md-1"></div> -->
					</div>
					
					<c:forEach items="${kyso}" var="item" varStatus="status">
						  <input type="hidden" id="kyso${status.index+1 }" value="${item }">
				     </c:forEach>
				     <c:forEach items="${kysoDaGui}" var="item" varStatus="status">
						  <input type="hidden" id="kysoDaGui${status.index+1 }" value="${item }">
				     </c:forEach>
				     
				     <c:forEach items="${kysoDay}" var="item" varStatus="status">
						  <input type="hidden" id="kysoDay${status.index+1 }" value="${item }">
				     </c:forEach>
				     <c:forEach items="${kysoDaGuiDay}" var="item" varStatus="status">
						  <input type="hidden" id="kysoDaGuiDay${status.index+1 }" value="${item }">
				     </c:forEach>
				</div>
			</div>
		</section>
	</form>
</div>

<link rel="stylesheet" href="${contextPath }/css/morris.css">
<script src="${contextPath }/js/raphael.min.js"></script>
<script src="${contextPath }/js/morris.min.js"></script>
<%-- <script src="${contextPath }/js/Chart.js"></script> --%>
<script src="${contextPath }/js/Chart.min.js"></script>


<script type="text/javascript">
	function redirectdb(type) {
		location.href = '${contextPath }?type=' + type;
	}
	function clock() {
		var timer = new Date();
		var month = timer.getUTCMonth() + 1;
		var day = timer.getUTCDate();
		var year = timer.getUTCFullYear();
		var hour = timer.getHours();
		var minute = timer.getMinutes();
		var second = timer.getSeconds();
		if (hour < 10) {
			hour = "0" + hour;
		}
		if (minute < 10) {
			minute = "0" + minute;
		}
		if (second < 10) {
			second = "0" + second;
		}
		document.getElementById("clock").innerHTML = day + "/" + month + "/"
				+ year + " " + hour + ":" + minute + ":" + second;
	}
	setInterval("clock()", 1000);
</script>


<script type="text/javascript">
	/* var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
	var pieChart = new Chart(pieChartCanvas);
	var PieData = [ {
	value : '3',
	color : '#00a65a',
	highlight : '#00a65a',
	label : '<spring:message code="ekyc_thanh_cong" />'
	}, {
	value : '4',
	color : '#f56954',
	highlight : '#f56954',
	label : '<spring:message code="ekyc_that_bai" />'
	}];
	// Create pie or douhnut chart
	// You can switch between pie and douhnut using the method below.
	pieChart.Doughnut(PieData);  */

	var chart1 = $('#linechart').get(0).getContext('2d');
	var myChart1 = new Chart(chart1, {
		type : 'line',
		data : {
			labels : [ '1st', '2nd', '3rd', '4th', '5th', '6th', '7th', '8th',
					'9th', '10th', '11th', '12th', '13th', '14th', '15th', '16th', '17th', '18th', '19th', '20th', '21th', '22th', '23th', '24th', '25th', '26th',  '27th', '28th', '29th', '30th', '31th' ],
			datasets : [
					{
						data : [ $("#kysoDay1").val(), $("#kysoDay2").val(), $("#kysoDay3").val(), $("#kysoDay4").val(), $("#kysoDay5").val(), $("#kysoDay6").val(), $("#kysoDay7").val(),
							$("#kysoDay8").val(), $("#kysoDay9").val(), $("#kysoDay10").val(), $("#kysoDay11").val(), $("#kysoDay12").val(),
							$("#kysoDay13").val(), $("#kysoDay14").val(), $("#kysoDay15").val(),  $("#kysoDay16").val(), $("#kysoDay17").val(), $("#kysoDay18").val(),
							$("#kysoDay19").val(), $("#kysoDay20").val(), $("#kysoDay21").val(), $("#kysoDay22").val(),$("#kysoDay23").val(), $("#kysoDay24").val(), $("#kysoDay25").val(), $("#kysoDay26").val(), $("#kysoDay27").val(),
							$("#kysoDay28").val(), $("#kysoDay29").val(), $("#kysoDay30").val(), $("#kysoDay31").val()],
						backgroundColor : "rgba(48, 164, 255, 0.2)",
						borderColor : "rgba(48, 164, 255, 0.8)",
						fill : true,
						borderWidth : 1,
						label : 'Ký thành công',
					},
					{
						data : [ $("#kysoDaGuiDay1").val(), $("#kysoDaGuiDay2").val(), $("#kysoDaGuiDay3").val(), $("#kysoDaGuiDay4").val(), $("#kysoDaGuiDay5").val(), $("#kysoDaGuiDay6").val(), $("#kysoDaGuiDay7").val(),
							$("#kysoDaGuiDay8").val(), $("#kysoDaGuiDay9").val(), $("#kysoDaGuiDay10").val(), $("#kysoDaGuiDay11").val(), $("#kysoDaGuiDay12").val(),
							$("#kysoDaGuiDay13").val(), $("#kysoDaGuiDay14").val(), $("#kysoDaGuiDay15").val(),  $("#kysoDaGuiDay16").val(), $("#kysoDaGuiDay17").val(), $("#kysoDaGuiDay18").val(),
							$("#kysoDaGuiDay19").val(), $("#kysoDaGuiDay20").val(), $("#kysoDaGuiDay21").val(), $("#kysoDaGuiDay22").val(),$("#kysoDaGuiDay23").val(), $("#kysoDaGuiDay24").val(), $("#kysoDaGuiDay25").val(), $("#kysoDaGuiDay26").val(), $("#kysoDaGuiDay27").val(),
							$("#kysoDaGuiDay28").val(), $("#kysoDaGuiDay29").val(), $("#kysoDaGuiDay30").val(), $("#kysoDaGuiDay31").val()],
						backgroundColor : "rgba(244, 67, 54, 0.5)",
						borderColor : "rgb(255, 99, 132)",
						fill : true,
						borderWidth : 1,
						label : 'Gửi thành công',
					} ]
		},
		options : {
			animation : {
				duration : 2000,
				easing : 'easeOutQuart',
			},
			plugins : {
				legend : {
					display : false,
					position : 'right',
				},
				title : {
					display : true,
					text : '',
					position : 'left',
				},
			},
		}
	});
	console.log("jdskhfdjskfh: "+${kyso})
	console.log("jdskhfdjskfh: "+${kysoDaGui})

	var kyso =[];
	
	   kyso.push(${kyso});
	   console.log(kyso);
	   for(var i=1;i<=kyso.length;i++)
	   {
		   
		   
	       
	   }
	    
	
	var chart2 = $('#barchart').get(0).getContext('2d');
    
	var myChart2 = new Chart(chart2, {
		type : 'bar',
		data : {
			labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug','Sep', 'Oct', 'Nov', 'Dec' ],
			datasets : [ {
				label : 'Ký thành công',
				backgroundColor : "rgba(54, 162, 235, 0.5)",
				borderColor : "rgb(54, 162, 235)",
				borderWidth : 1,
				data : [$("#kyso1").val(), $("#kyso2").val(), $("#kyso3").val(), $("#kyso4").val(), $("#kyso5").val(), $("#kyso6").val(), $("#kyso7").val(), $("#kyso8").val(), $("#kyso9").val(), $("#kyso10").val(), $("#kyso11").val(), $("#kyso12").val()],
			}, {
				label : 'Gửi thành công',
				backgroundColor : "rgba(244, 67, 54, 0.5)",
				borderColor : "rgb(255, 99, 132)",
				borderWidth : 1,
				data : [$("#kysoDaGui1").val(), $("#kysoDaGui2").val(), $("#kysoDaGui3").val(), $("#kysoDaGui4").val(), $("#kysoDaGui5").val(), $("#kysoDaGui6").val(), $("#kysoDaGui7").val(), $("#kysoDaGui8").val(), $("#kyso9").val(), $("#kysoDaGui10").val(), $("#kysoDaGui11").val(), $("#kysoDaGui12").val()],

			} ]
		},
		options : {
			animation : {
				duration : 2000,
				easing : 'easeOutQuart',
			},
			plugins : {
				legend : {
					display : true,
					position : 'top',
				},
				title : {
					display : true,
					text : '',
					position : 'left',
				},
			},
		}

	});
</script>

<%@include file="../layout/footer2.jsp"%>