<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){

			// 将当前窗口变为顶层窗口
			if(window.top != window){
				window.top.location = window.location;
			}

			// 页面加载完毕后清除用户文本框中的内容
			$("#loginAct").val("");

			// 页面加载完毕后用户文本框自动获得焦点
			$("#loginAct").focus();

			// 为登录按钮绑定鼠标单击事件
			$("#submitBtn").click(function(){
				login();
			})

			// 为整个页面绑定键盘敲击事件
			$(window).keydown(function(event){
				if(event.keyCode == 13){
					login();
				}
			})
		})
		// 普通的自定义的函数，一定要写在$(function(){})的外面
		function login(){
			// 验证用户名和密码是否为空
			var loginAct = $("#loginAct").val().trim();
			var loginPwd = $("#loginPwd").val().trim();
			if(loginAct == "" || loginPwd == ""){
				$("#msg").html("账号密码不能为空");
				return false;
			}

			// 使用ajax来发起后台请求去验证用户名和密码是否存在
			$.ajax({
				url:"settings/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				dataType:"json",
				type:"post",
				success:function(resp){
					if(resp.success){
						window.location.href = "workbench/index.jsp";
					}else{
						$("#msg").html(resp.msg);
					}
				}
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form" id="">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>