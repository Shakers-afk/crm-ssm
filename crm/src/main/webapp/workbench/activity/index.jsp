<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
    <link type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"
          rel="stylesheet"/>
    <link type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css" rel="stylesheet">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">
        $(function () {
            //页面加载完毕，执行页面分页查询
            pageList(1, 2);

            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            //获取使用者信息，添加时间组件，弹出添加模态窗口
            $("#addBtn").click(function () {
                $.ajax({
                    url: "workbench/activity/getUserList.do",
                    method: "get",
                    dateType: "json",
                    success: function (data) {
                        var html = "<option></option>";
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'>";
                            html += n.name;
                            html += "</option>";
                        })
                        $("#create-owner").html(html);

                        //将当前session作用域中的user作为默认选项（注：在js中使用el表达式要用""括起来）
                        var id = "${user.id}";
                        $("#create-owner").val(id);
                        //show：打开模态窗口；hide：关闭模态窗口
                        $("#createActivityModal").modal("show");

                    }
                })
            })

            //添加市场活动，并返回结果
            $("#saveBtn").click(function () {
                $.ajax({
                    url: "workbench/activity/save.do",
                    method: "post",
                    dataType: "json",
                    data: {
                        "owner": $.trim($("#create-owner").val()),
                        "name": $.trim($("#create-name").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-description").val())
                    },
                    success: function (data) {
                        //data:{success:true/false}
                        if (data.success) {
                            //添加成功后，刷新市场活动列表（局部刷新）
                            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#activityAddForm")[0].reset();
                            //关闭添加市场活动窗口
                            $("#createActivityModal").modal("hide");
                        } else {
                            alert("添加市场活动失败！");
                        }
                    }
                })
            })

            //查找市场活动
            $("#searchBtn").click(function () {
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-startDate").val($.trim($("#search-startDate").val()));
                $("#hidden-endDate").val($.trim($("#search-endDate").val()));
                pageList(1, 2);
            })

            //为复选框绑定单击事件：单选框全选/不全选
            $("#chkAllBox").click(function () {
                $("input[name=chkBox]").prop("checked", this.checked);
            })

            //单选框 name=chkBox 多选框 id=chkAllBox
            //对于动态生成的页面元素要使用回调函数绑定
            $("#activityBody").on("click", $("input[name=chkBox]"), function () {
                $("#chkAllBox").prop("checked", $("input[name=chkBox]").length == $("input[name=chkBox]:checked").length);
            })

            //批量删除市场活动
            $("#deleteBtn").click(function () {
                var $chkBox = $("input[name=chkBox]:checked");

                if ($chkBox.length==0){
                    alert("请选择删除选项");
                    return;
                }

                if(confirm("确定删除所选记录吗？")){
                    var parm = "";
                    for (var i = 0; i < $chkBox.length; i++) {
                        var id = $($chkBox[i]).val();
                        parm += ("id=" + id);
                        if (i < $chkBox.length - 1) {
                            parm += "&";
                        }
                    }

                    $.ajax({
                        url:"workbench/activity/delete.do",
                        method:"post",
                        data:parm,
                        dataType:"json",
                        success:function (data){
                            if (data.success){
                                alert("删除成功！");
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            }else {
                                alert("删除市场活动异常！！");
                            }
                        }
                    })
                }

            })

            //弹出修改市场活动模态窗口
            $("#editBtn").click(function (){
                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                var $chkBox = $("input[name=chkBox]:checked");
                if ($chkBox.length==0){
                    alert("请选择修改项");
                }else if($chkBox.length>1){
                    alert("请选择一个修改项");
                }else {
                    var id = $chkBox.val();

                    $.ajax({
                        url:"workbench/activity/getUserListAndActivity.do",
                        method:"post",
                        data:{
                            "id":id
                        },
                        dataType:"json",
                        success:function (data){
                            //data:{"users":[{},{}..],"activity":{}}
                            var html = "<option></option>";
                            $.each(data.userList, function (i, n) {
                                html += "<option value='" + n.id + "'>";
                                html += n.name;
                                html += "</option>";
                            })
                            $("#edit-owner").html(html);

                            $("#edit-id").val(data.activity.id);
                            $("#edit-name").val(data.activity.name);
                            $("#edit-owner").val(data.activity.owner);
                            $("#edit-startDate").val(data.activity.startDate);
                            $("#edit-endDate").val(data.activity.endDate);
                            $("#edit-cost").val(data.activity.cost);
                            $("#edit-description").val(data.activity.description);

                            $("#editActivityModal").modal("show");
                        }
                    })
                }
            })

            //修改市场活动
            $("#updateBtn").click(function (){
                $.ajax({
                    url: "workbench/activity/update.do",
                    method: "post",
                    dataType: "json",
                    data: {
                        "id":$.trim($("#edit-id").val()),
                        "owner": $.trim($("#edit-owner").val()),
                        "name": $.trim($("#edit-name").val()),
                        "startDate": $.trim($("#edit-startDate").val()),
                        "endDate": $.trim($("#edit-endDate").val()),
                        "cost": $.trim($("#edit-cost").val()),
                        "description": $.trim($("#edit-description").val())
                    },
                    success: function (data) {
                        //data:{success:true/false}
                        if (data.success) {
                            //修改成功后，刷新市场活动列表（局部刷新）
                            //第一个参数，维持当前页；
                            //第二个参数，维持当前个数
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            //关闭添加市场活动窗口
                            $("#editActivityModal").modal("hide");
                        } else {
                            alert("修改市场活动失败！");
                        }
                    }
                })
            })
        });

        function pageList(pageNo, pageSize) {
            //每次刷新时将全选框取消选择
            $("#chkAllBox").prop("checked",false);

            //参数设置为上次点击查询时的参数
            $("#search-name").val($("#hidden-name").val());
            $("#search-owner").val($("#hidden-owner").val());
            $("#search-startDate").val($("#hidden-startDate").val());
            $("#search-endDate").val($("#hidden-endDate").val());

            $.ajax({
                url: "workbench/activity/pageList.do",
                method: "get",
                dataType: "json",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "owner": $.trim($("#search-owner").val()),
                    "name": $.trim($("#search-name").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val()),
                },
                success: function (data) {
                    //data:{"total":10,"dataList":{"owner":....}}
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                        html += '<tr class="active">';
                        html += '"<td><input type="checkbox" name="chkBox" value="' + n.id + '"/></td>';
                        html += '"<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">' + n.name + '</a></td>';
                        html += '"<td>' + n.owner + '</td>';
                        html += '"<td>' + n.startDate + '</td>';
                        html += '"<td>' + n.endDate + '</td>';
                        html += '"</tr>';
                    })

                    $("#activityBody").html(html);

                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1

                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });

                }
            })
        }
    </script>
</head>
<body>
<!-- 隐藏域 -->
<input id="edit-id" type="hidden">
<input id="hidden-name" type="hidden"/>
<input id="hidden-owner" type="hidden"/>
<input id="hidden-startDate" type="hidden"/>
<input id="hidden-endDate" type="hidden"/>

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

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate" readonly>
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

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" readonly>
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" readonly>
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
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
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
                        <input class="form-control" type="text" id="search-startDate"/>
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
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input id="chkAllBox" type="checkbox"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--                <tr class="active">--%>
                <%--                    <td><input type="checkbox"/></td>--%>
                <%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
                <%--                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a>--%>
                <%--                    </td>--%>
                <%--                    <td>zhangsan</td>--%>
                <%--                    <td>2020-10-10</td>--%>
                <%--                    <td>2020-10-20</td>--%>
                <%--                </tr>--%>
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