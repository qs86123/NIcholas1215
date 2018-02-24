//1、页面加载完成以后，直接ajax请求，要到分页信息
$(function () {
	//开始去首页
	to_page(0);
	//新增员工
	addEmp();
	//提交新增员工信息
	add_btn();
	//员工更新
	updateEmp();
	//提交员工修改信息
	update_btn();
	//员工删除
	deleteEmp();
	//复选框功能
	checkAll_cbx();
	checkItem_cbx();
	//删除复选框选中的员工
	deleteEmps();
});


function to_page(pn) {
	$.ajax({
		url: "emps",
		data: "pn=" + pn,
		type: "GET",
		success: function (result) {
			//console.log(result);
			//1、解析员工并显示员工数据
			build_emps_table(result);
			//2、解析并显示分页信息
			build_page_info(result);
			//3、解析显示分页条数据
			build_page_nav(result);
		},
	});
}

//解析员工并显示员工数据
function build_emps_table(result) {
	//清空table表格
	$("#emps_table tbody").empty();
	var emps = result.extend.pageInfo.content;
	$.each(emps, function (index, item) {
		var checkBoxTd=$("<td><input type='checkbox' class='check_item' /></td>");
		var empIdTd = $("<td></td>").append(item.empId);
		var empNameTd = $("<td></td>").append(item.empName);
		var gender = item.gender == 'M' ? "男" : "女";
		//append方法执行完成以后还是返回原来的元素
		var genderTd = $("<td></td>").append(gender);
		var emailTd = $("<td></td>").append(item.email);
		var deptTd = $("<td></td>").append(item.department.deptName);
		var editBtn = $("<button></button").addClass("btn btn-primary btn-sm edit_btn")
			.append($("<span></span>").addClass("glyphicon glyphicon-pencil"))
			.append("编辑");
		//为编辑按钮添加一个自定义的属性，来表示当前员工id
		editBtn.attr("edit-id",item.empId);
		var delBtn = $("<button></button").addClass("btn btn-danger btn-sm delete_btn")
			.append($("<span></span>").addClass("glyphicon glyphicon-trash"))
			.append("删除");
		//为删除按钮添加一个自定义的属性，来表示当前员工id	
		delBtn.attr("delete-id",item.empId);
		var btnTd = $("<td></td>").append(editBtn).append(delBtn);
		$("<tr></tr>").append(checkBoxTd)
			.append(empIdTd)
			.append(empNameTd)
			.append(genderTd)
			.append(emailTd)
			.append(deptTd)
			.append(btnTd)
			.appendTo("#emps_table tbody");
	});
}

//解析显示分页信息
function build_page_info(result) {
	$("#page_info_area").empty();
	$("#page_info_area").append("当前第"
		+ (result.extend.pageInfo.number+1) + "页，总"
		+ result.extend.pageInfo.totalPages + "页，总"
		+ result.extend.pageInfo.totalElements + "条记录")
}

//解析显示分页条,点击能去跳转到指定页面
function build_page_nav(result) {
	$("#page_nav_area").empty();
	var ul = $("<ul></ul>").addClass("pagination");
	//构建元素
	var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href", "#"));
	var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;").attr("href", "#"));
	if (result.extend.pageInfo.last == false) {
		firstPageLi.addClass("disabled");
		prePageLi.addClass("disabled");
	} else {
		//为元素添加点击翻页的事件
		firstPageLi.click(function () {
			to_page(0);
		});
		prePageLi.click(function () {
			to_page(result.extend.pageInfo.number - 1);
		});
	}
	var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;").attr("href", "#"));
	var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href", "#"));
	if (result.extend.pageInfo.first == false) {
		nextPageLi.addClass("disabled");
		lastPageLi.addClass("disabled");
	} else {
		nextPageLi.click(function () {
			to_page(result.extend.pageInfo.number + 1);
		});
		lastPageLi.click(function () {
			to_page(result.extend.pageInfo.totalPages);
		});
	}
	//添加首页和上一页
	ul.append(firstPageLi).append(prePageLi);
	$.each(result.extend.pageInfo.numberOfElements, function (index, item) {
		var numLi = $("<li></li>").append($("<a></a>").append(item).attr("href", "#"));
		if (result.extend.pageInfo.number == item) {
			numLi.addClass("active");
		}
		numLi.click(function () {
			to_page(item);
		});
		//添加遍历的数字页
		ul.append(numLi);
	});
	//添加下一页和末页
	ul.append(nextPageLi).append(lastPageLi);
	//把ul加入到nav
	var navEle = $("<nav></nav>").append(ul);
	navEle.appendTo("#page_nav_area");
}

function addEmp() {
	//点击新增按键弹出模态框
	$("#emp_add_modal_btn").click(function () {
		//清除表单数据（重置）
		$("#empAddModal form")[0].reset();
		$("#empAddModal input").parent().removeClass("has-success has-error");
		$("#empAddModal input").next("span").text("");
		//发送ajax请求，查出部门信息，显示在下拉列表中
		getDePts("#dept_add_select");
		$("#empAddModal").modal({
			backdrop: "static"
		});
	});
}
//查出所有部门信息并显示在下拉列表中
function getDePts(ele) {
	$(ele).empty();
	$.ajax({
		url: "depts",
		type: "GET",
		success: function (result) {
			//console.log(result);
			$.each(result.extend.depts, function () {
				var optionEle = $("<option></option>").append(this.deptName).attr("value", this.deptId);
				optionEle.appendTo(ele);
			});
		},
	});
}
//保存员工方法
function add_btn() {
	$("#empName_add_input").change(function () {
		validate_form_name("#empName_add_input");
	});
	$("#email_add_input").change(function () {
		validate_form_email("#email_add_input");
	});

	$("#emp_save_btn").click(function () {
		//模态框中填写的表单数据提交给服务器进行保存
		//1、先对要提交的表单交给服务器进行校验
		 if(!validate_form_name("#empName_add_input")||!validate_form_email("#email_add_input")){
		 	return false;
		 }
		if($(this).attr("ajax-va")=="error"){
			return false;
		 }
		//2、发送ajax请求保存员工
		$.ajax({
			url: "emp",
			type: "POST",
			data: $("#empAddModal form").serialize(),
			success: function (result) {
				//alert(result.msg);
				if(result.code==100){
					//员工保存成功
					//1、关闭模态框
					$("#empAddModal").modal('hide');
					//2、来到最后一页，显示刚才保存的数据
					//发送ajax请求显示最后一页数据即可
	
					to_page(99999);
				}else{
					//显示失败信息
					if(undefind!=result.extend.errorFields.email){
						//显示邮箱信息
						show_validate_msg("#email_add_input", "error", result.extend.errorFields.email);
					}
					if(undefind!=result.extend.errorFields.empName){
						//显示员工名字错误信息
						show_validate_msg("#empName_add_input", "error", result.extend.errorFields.empName);
					}
				}
			}
		});
	});
}

//校验表单数据方法
function validate_form_name(ele) {
	//1、拿到表单的数据，使用正则表达式
	var empName = $(ele).val();
	var regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u4e00-\u9fa5]{2,5}$)/;
	if (!regName.test(empName)) {
		//alert("用户名可以是2-5位中文或者6-16位英文和数字的组合");
		show_validate_msg(ele, "error", "用户名可以是2-5位中文或者6-16位英文和数字的组合");
		return false;
	} else {
		checkuSer(empName);
	};
	show_validate_msg(ele, "success", "");
	return true;
}
function validate_form_email(ele) {
	//2、校验邮箱
	var email = $(ele).val();
	var regemail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
	if (!regemail.test(email)) {
		//alert("邮箱格式不正确");
		show_validate_msg(ele, "error", "邮箱格式不正确");
		return false;
	} else {
		show_validate_msg(ele, "success", "");
	};
	return true;
}
//校验的方法
function show_validate_msg(ele, status, msg) {
	//清除当前元素校验状态
	$(ele).parent().removeClass("has-success has-error");
	$(ele).next("span").text("");
	if ("success==status") {
		$(ele).parent().addClass("has-success");
		$(ele).next("span").text(msg);
	}
	if ("error" == status) {
		$(ele).parent().addClass("has-error");
		$(ele).next("span").text(msg);
	}
}


function checkuSer(empName){
	$.ajax({
		url:"checkuser",
		data:"empName="+empName,
		type:"POST",
		success:function(result){
			if(result.code==100){
				show_validate_msg("#empName_add_input","success","用户名可用");
				$("#emp_save_btn").attr("ajax-va","success");
			}else{
				show_validate_msg("#empName_add_input","error",result.extend.va_msg);
				$("#emp_save_btn").attr("ajax-va","error");
				return false;
			};
		},
	});
}
//清空表单样式及内容方法
function reset_form(ele){
	$(ele)[0].reset();
	$(ele).removeClass("has-success has-error");
	$(ele).find(".help-block").text("");
}


function updateEmp(){
	//注意：我们是按钮创建之前就绑定了click，所以会绑不上
	//可以在创建按键的时候绑定或新版jquery的on代替
	$(document).on("click",".edit_btn",function(){
		//alert("edit");
		//1、查出员工信息及部门列表，并显示及部门列表
		getDePts("#dept_update_select");
		getEmp($(this).attr("edit-id"));
		//2、把员工的id传递给模态框的更新按钮
		$("#emp_update_btn").attr("edit-id",$(this).attr("edit-id"));
		$("#empUpdateModal").modal({
			backdrop: "static"
		});
	});
}

function getEmp(id){
	$.ajax({
		url:"emp/"+id,
		//data:"id="+id,
		type:"GET",
		success:function(result){
			var empData=result.extend.emp;
			$("#empName_update_static").text(empData.empName);
			$("#email_update_input").val(empData.email);
			$("#empUpdateModal input[name=gender]").val([empData.gender]);
			$("#empUpdateModal select").val([empData.dId]);
		},
	});
}

function update_btn(){
	$("#email_update_input").change(function () {
		validate_form_email("#email_update_input");
	});
	$("#emp_update_btn").click(function(){
		//验证邮箱是否合法
		if(!validate_form_email("#email_update_input")){
			return false;
		}
	//2、发送ajax请求更新员工
	$.ajax({
		url: "emp/"+$(this).attr("edit-id"),
		type: "PUT",
		data: $("#empUpdateModal form").serialize(),
		success:function(result){
			//alert(result.msg);
			//1、关闭模态框
			$("#empUpdateModal").modal('hide');
			//2、回到第一页页面
			to_page(1);
		},
	});	
	});
}

function deleteEmp(){
	$(document).on("click",".delete_btn",function(){
		//alert("delete");
		var empName= $(this).parents("tr").find("td:eq(2)").text();
		var empId=$(this).attr("delete-id");
		if(confirm("确认删除【"+empName+"】吗？")){
			//确认发送ajax请求删除
			$.ajax({
				url:"emp/"+empId,
				type:"DELETE",
				success:function(result){
					alert(result.msg);
					to_page(1);
				},
			});
		}
	});
}

//全选全不选功能
function checkAll_cbx(){
	//attr获取checked是undefined;
	//我们这些dom原生的属性，attr获取自定义的值
	//prop修改和读取dom原生的值
	$("#check_all").click(function(){
		$(this).prop("checked");
		$(".check_item").prop("checked",$(this).prop("checked"));
	});
}

function checkItem_cbx(){
	$(document).on("click",".check_item",function(){
		//判断当前选择中的元素是否全选
	var flag=$(".check_item:checked").length==$(".check_item").length;
		$("#check_all").prop("checked",flag);
	});
}

function deleteEmps(){
	$("#emp_delete_all_btn").click(function(){
		var empNames="";
		var del_ids="";
		$.each($(".check_item:checked"),function(){
			empNames+=$(this).parents("tr").find("td:eq(2)").text()+",";
			del_ids+=$(this).parents("tr").find("td:eq(1)").text()+"-";
			 
		});
		//去除empNames多余的,及del_ids多余的-
		empNames=empNames.substring(0,empNames.length-1);
		del_ids=del_ids.substring(0,del_ids.length-1);
		if(confirm("确认删除【"+empNames+"】吗？")){
			//发送ajax请求
			$.ajax({
				url:"emp/"+del_ids,
				type:"DELETE",
				success:function(result){
					alert(result.msg);
					to_page(1);
				},
			});
		}
	});
}