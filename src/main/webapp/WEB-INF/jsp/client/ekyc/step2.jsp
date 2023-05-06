<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<form action="" class="form" id="forms" method="post">
	<%@include file="menu.jsp"%>
	<div class="step-forms step-forms-active">
		<div class="group-inputs">
			<label for="phone">Facebook</label> <input type="text" name="phone" id="phone" />
		</div>
		<div class="group-inputs">
			<label for="email">Twitter</label> <input type="text" name="email" id="email" />
		</div>
		<div class="group-inputs">
			<label for="email">Linkedin</label> <input type="text" name="email" id="email" />
		</div>
		<div class="group-inputs">
			<label for="email">Dribbble</label> <input type="text" name="email" id="email" />
		</div>
		<div class="btns-group">
			<a href="#" class="btn btn-prev">Previous</a> <a href="#" class="btn btn-next">Next</a>
		</div>
	</div>
</form>
<%@include file="../layout/footer.jsp"%>