package com.zhenjie.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhenjie.entiy.Department;

public interface DepartmentJPA extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department>, Serializable  {
	
	

}
