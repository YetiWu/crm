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

        // 时间控件
        $(".time").datetimepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
        });

        // taglib
        <c:forEach items="" var="">
                <option></option>
        </c:forEach>
</body>
</html>
