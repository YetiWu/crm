<%--
  Created by IntelliJ IDEA.
  User: yeti
  Date: 2021/10/10
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
        // 发送ajax请求来做删除操作
        $.ajax({
        url:"",
        data:{

        },
        dataType:"",
        type:"",
        success:function(){

        }

        })


        <!-- 修改市场活动的模态窗口 -->
        <div class="modal fade" id="editActivityModal" role="dialog">
                <div class="modal-dialog" role="document" style="width: 85%;">
                        <div class="modal-content">
                                <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">
                                                <span aria-hidden="true">×</span>
                                        </button>
                                        <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                                </div>
                                <div class="modal-body">

                                        <form class="form-horizontal" role="form">

                                                <div class="form-group">
                                                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                                                        <div class="col-sm-10" style="width: 300px;">
                                                                <select class="form-control" id="edit-marketActivityOwner">
                                                                        <option>zhangsan</option>
                                                                        <option>lisi</option>
                                                                        <option>wangwu</option>
                                                                </select>
                                                        </div>
                                                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                                                        <div class="col-sm-10" style="width: 300px;">
                                                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                                                        </div>
                                                </div>

                                                <div class="form-group">
                                                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                                                        <div class="col-sm-10" style="width: 300px;">
                                                                <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                                                        </div>
                                                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                                                        <div class="col-sm-10" style="width: 300px;">
                                                                <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                                                        </div>
                                                </div>

                                                <div class="form-group">
                                                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                                                        <div class="col-sm-10" style="width: 300px;">
                                                                <input type="text" class="form-control" id="edit-cost" value="5,000">
                                                        </div>
                                                </div>

                                                <div class="form-group">
                                                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                                                        <div class="col-sm-10" style="width: 81%;">
                                                                <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                                                        </div>
                                                </div>

                                        </form>

                                </div>
                                <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                        <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
                                </div>
                        </div>
                </div>
        </div>
</body>
</html>
