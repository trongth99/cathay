<%@ page contentType="text/html; charset=UTF-8"%>
<style>
body {
-webkit-touch-callout: none;
-webkit-user-select: none;
-khtml-user-select: none;
-moz-user-select: none;
-ms-user-select: none;
user-select: none;
}
</style>
<script type="text/javascript">
//<=!=[=C=D=A=T=A=[
document.onkeypress = function(event) {
event = (event || window.event);
if (event.keyCode === 123) {
//alert('No F-12');
return false;
}
};
document.onmousedown = function(event) {
event = (event || window.event);
if (event.keyCode === 123) {
//alert('No F-keys');
return false;
}
};
document.onkeydown = function(event) {
event = (event || window.event);
if (event.keyCode === 123) {
//alert('No F-keys');
return false;
}
};
function contentprotector() {
return false;
}
function mousehandler(e) {
var myevent = (isNS) ? e : event;
var eventbutton = (isNS) ? myevent.which : myevent.button;
if ((eventbutton === 2) || (eventbutton === 3))
return false;
}
document.oncontextmenu = contentprotector;
document.onmouseup = contentprotector;
var isCtrl = false;
window.onkeyup = function(e)
{
if (e.which === 17)
isCtrl = false;
}
window.onkeydown = function(e)
{
if (e.which === 17)
isCtrl = true;
if (((e.which === 85) || (e.which === 65) || (e.which === 88) || (e.which === 67) || (e.which === 86) || (e.which === 83)) && isCtrl === true)
{
return false;
}
}
isCtrl = false;
document.ondragstart = contentprotector;
//]=]=> </script>