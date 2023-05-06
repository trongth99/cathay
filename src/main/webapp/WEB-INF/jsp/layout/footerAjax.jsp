<script src="${contextPath }/js/select2.full.min.js"></script>
<script src="${contextPath }/js/bootstrap-tagsinput.min.js"></script>
<link rel="stylesheet" href="${contextPath }/css/bootstrap-tagsinput.css">
<script type="text/javascript">

$(function () {
	var query = window.location.search.substring(1);
	$("#submitFormModal").append('<input type="hidden" name="paramsQuery" id="paramsQuery" value="'+query+'"/>');
    $.AdminBSB.input.activate();
    $.AdminBSB.select.activate();
    
    $('.yearpicker').datepicker({
        format: "yyyy",
        viewMode: "years", 
        minViewMode: "years",
        autoclose: true
    });
});
</script>