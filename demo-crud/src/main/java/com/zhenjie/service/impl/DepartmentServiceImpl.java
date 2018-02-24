package com.zhenjie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhenjie.entiy.Department;
import com.zhenjie.jpa.DepartmentJPA;
import com.zhenjie.service.DepartmentService;
@Service
public class DepartmentServiceImpl implements DepartmentService{
	@Autowired
	DepartmentJPA DepartmentJPA;
	
	@Override
	public List<Department> getDepts() {
		return DepartmentJPA.findAll();
	}

}
