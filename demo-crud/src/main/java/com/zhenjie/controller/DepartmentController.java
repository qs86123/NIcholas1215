package com.zhenjie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenjie.entiy.Department;
import com.zhenjie.entiy.Msg;
import com.zhenjie.service.DepartmentService;

/**
 * 处理与部门与关的请求
 * @author Administrator
 *
 */
@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	/*
	 * 返回所有部门信息
	 */
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts() {
		//查出的所有部门信息
		List<Department> list=departmentService.getDepts();
		return Msg.success().add("depts", list);
	}
}
