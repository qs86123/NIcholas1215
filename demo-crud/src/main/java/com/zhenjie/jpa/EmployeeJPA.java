package com.zhenjie.jpa;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhenjie.entiy.Employee;

public interface EmployeeJPA
		extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee>, Serializable {
	
	List<Employee> findByEmpName(String empName);
	Long countByEmpName(String empName);
	
	@Query(value = "delete from t_emp where t_id in (?1)", nativeQuery = true)
	@Modifying
	@Transactional 
	public void deleteEmployeesByIds(Integer[] ids);
}
