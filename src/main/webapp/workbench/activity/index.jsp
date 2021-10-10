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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){

		// 时间控件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#addBtn").click(function(){

			// 模态窗口展示之前，过后台获取市场活动所有者的信息
			$.ajax({
				url:"workbench/activity/getUserList.do",
				dataType:"json",
				type:"get",
				success:function(data){

					var html = "";

					$.each(data,function(i,n){
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-owner").html(html);

					// 在js中使用el表达式，el表达式必须用字符串的形式（双引号引起来）
					var id = "${user.id}";
					$("#create-owner").val(id);
					$("#createActivityModal").modal("show");
				}

			})

		})

		$("#saveBtn").click(function(){

			// 发起ajax请求
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					"owner":$("#create-owner").val().trim(),
					"name":$("#create-name").val().trim(),
					"startDate":$("#create-startDate").val().trim(),
					"endDate":$("#create-endDate").val().trim(),
					"cost":$("#create-cost").val().trim(),
					"description":$("#create-description").val().trim(),
				},
				dataType:"json",
				type:"post",
				success:function(data){

					if(data.success){

						// 刷新表单信息
						// 清除模态窗口中的内容
						// jquery没有给我们提供reset函数，但是idea能够提示出来，所以这个是一个坑
						// 但是，js给我提供了该函数，所以我们需要将jquery对象转换成js对象，然后调用该函数即可
						$("#addActivityForm")[0].reset();

						/*
							$("#activityPage").bs_pagination('getOption', 'currentPage'):表示跳转到第几页，默认是当前页面

							$("#activityPage").bs_pagination('getOption', 'rowsPerPage')：表示每页的记录条数
						*/
						pageList(1
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						// 关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else{
						alert("添加失败");
					}

				}
			})
		})

		// 在页面加载的时候刷新市场信息列表
		pageList(1,3)

		// 给查询按钮绑定事件
		$("#searchBtn").click(function(){
			// 在查询之前，我们将查询条件保存在隐藏域中
			$("#hidden-name").val($("#search-name").val().trim());
			$("#hidden-owner").val($("#search-owner").val().trim());
			$("#hidden-startDate").val($("#search-startDate").val().trim());
			$("#hidden-endDate").val($("#search-endDate").val().trim());

			pageList(1
					,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})

		// 定义一个函数，用来刷新市场信息列表
		function pageList(pageNo,pageSize){

		    $("#checkAll").prop("checked",false);

			// 在查询之前将隐藏域保存的查询条件取出来
			$("#search-name").val($("#hidden-name").val().trim());
			$("#search-owner").val($("#hidden-owner").val().trim());
			$("#search-startDate").val($("#hidden-startDate").val().trim());
			$("#search-endDate").val($("#hidden-endDate").val().trim());

			// 发送ajax请求
			$.ajax({
				url:"workbench/activity/pageList.do",
				data:{
					// 页码
					"pageNo":pageNo,
					// 每页记录的条数
					"pageSize":pageSize,
					// 以下为查询条件
					"name":$("#search-name").val().trim(),
					"owner":$("#search-owner").val().trim(),
					"startDate":$("#search-startDate").val().trim(),
					"endDate":$("#search-endDate").val().trim()
				},
				dataType:"json",
				type:"get",
				success:function(data){
					// 市场信息
					var html = "";
					$.each(data.pageList,function (i,n){
						html += '<tr class="active">';
						html += '<td><input type="checkbox" name="commCheck" value="'+n.id+'"/></td>';
						html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
						html += '<td>'+n.owner+'</td>';
						html += '<td>'+n.startDate+'</td>';
						html += '<td>'+n.endDate+'</td>';
						html += '</tr>';
					})
					$("#activityBody").html(html);

					var totalPages = data.total%pageSize == 0?data.total/pageSize:parseInt(data.total/pageSize) + 1;
					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 5, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});
				}

			})
		}

		// 复选框的操作
		$("#checkAll").click(function(){

			$("input[name=commCheck]").prop("checked",this.checked);

		})

		// 动态生成的元素，是不能够以普通绑定事件的方式来进行操作的
		/*

			动态生成的元素，我们要以on方法的形式来触发事件
			语法：
				$(需要绑定元素的有效的外层元素).on(绑定的事件方式，需要绑定的元素jQuery对象，回调函数)

			注意：有效的意思是，它也不是动态生成的
		 */
		$("#activityBody").on("click",$("input[name=commCheck]"),function(){
			$("#checkAll").prop("checked",$("input[name=commCheck]").length == $("input[name=commCheck]:checked").length)
		})


        // 市场活动的删除操作
        $("#deleteBtn").click(function(){
            // 给用户的提示信息
			if(confirm("确定要删除本次选中的信息吗？")){
				// 拼接参数
				var param = "";
				// 判断复选框有几个被选中
				var $cc = $("input[name=commCheck]:checked");
				for(var i = 0; i < $cc.length ; i++){
					param += "id=" + $($cc[i]).val();
					if(i < $cc.length - 1){
						param += "&";
					}
				}

				// 发送ajax请求来做删除操作
				$.ajax({
					url:"workbench/activity/delete.do",
					data:param,
					dataType:"json",
					type:"post",
					success:function(data){
						if(data.success){

							// 删除成功，刷新列表
							pageList(1
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						}else{
							alert("删除市场活动信息失败！");
						}
					}

				})
			}
        })

		// 市场活动的修改操作
		$("#editBtn").click(function(){
			// 判断哪个复选框被选中了
			var $checked = $("input[name=commCheck]:checked");
			if($checked.length == 0){
				alert("请选择要修改的市场活动信息");
			}else if($checked.length > 1){
				alert("选择修改的市场活动信息不能超过一条");
			}else{
				var id = $checked.val();
				// 过后台，拿到之前创建市场活动的信息
				$.ajax({
					url:"workbench/activity/getUserListAndActicity.do",
					data:{
						"id":id
					},
					dataType:"json",
					type:"get",
					success:function(data){
						//json:["uList":{},{},{},"a":{}]
						// 拼接所有者的下拉框
						var html = "";
						$.each(data.uList,function(i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#edit-owner").html(html);

						// 拼接市场活动信息
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);
					}

				})

				// 打开修改模态窗口
				$("#editActivityModal").modal("show");
			}
		})

		// 给更新按钮绑定事件
		$("#updateBtn").click(function(){
			// 发起ajax请求
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":$("#edit-id").val().trim(),
					"owner":$("#edit-owner").val().trim(),
					"name":$("#edit-name").val().trim(),
					"startDate":$("#edit-startDate").val().trim(),
					"endDate":$("#edit-endDate").val().trim(),
					"cost":$("#edit-cost").val().trim(),
					"description":$("#edit-description").val().trim(),
				},
				dataType:"json",
				type:"post",
				success:function(data){

					if(data.success){

						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						// 关闭模态窗口
						$("#editActivityModal").modal("hide");

					}else{
						alert("修改市场活动失败");
					}

				}
			})
		})
	});
	
</script>
</head>
<body>

	<%--创建4个隐藏域，用来保存查询条件--%>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="addActivityForm">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn" ">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>