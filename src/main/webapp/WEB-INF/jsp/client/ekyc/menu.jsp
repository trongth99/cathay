<%@ page contentType="text/html; charset=UTF-8"%>
<h2 style="text-align: center;">Demo eKYC</h2>
<div class="progressbar">
	<div class="progress" id="progress"></div>
	<div class="progress-step progress-step-active" data-title=""></div>
	<div class="progress-step" data-title=""></div>
	<div class="progress-step" data-title=""></div>
	<div class="progress-step" data-title=""></div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var step = '${step}';
	$(".progress-step").each(function(i){
		if(step > i) $(this).addClass("progress-step-active");
	});
});
</script>