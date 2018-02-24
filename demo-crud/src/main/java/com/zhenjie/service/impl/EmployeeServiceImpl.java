package com.zhenjie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zhenjie.entiy.Employee;
import com.zhenjie.jpa.EmployeeJPA;
import com.zhenjie.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements  EmployeeService {
	@Autowired
	EmployeeJPA employeeJPA;
	
	/**
	 * 查询所有员工
	 * @return
	 */
	@Override
	public List<Employee> getAll() {
		return employeeJPA.findAll();
	}
	
	/**
	 * 员工保存方法
	 * @param employee
	 */
	@Override
	public void saveEmp(Employee employee) {
		employeeJPA.save(employee);
	}
	/**
	 * 检验用户名是否可用
	 * @param empName
	 * @return true:代表可用 false不可用
	 */
	@Override
	public boolean checkUser(String empName) {
		Long l = employeeJPA.countByEmpName(empName);
		return l==0;
	}
	@Override
	public Employee getEmp(Integer id) {
		return employeeJPA.findOne(id);
	}
	
	/**
	 * 员工更新
	 * @param employee
	 */
	@Override
	public void updateEmp(Employee employee) {
		if(employee.getEmpId()!=null) {
		Employee emp = employeeJPA.findOne(employee.getEmpId());
		if(employee.getEmpName()!=null) {
			emp.setEmpName(employee.getEmpName());
		}
		if(employee.getEmail()!=null) {
			emp.setEmail(employee.getEmail());
		}
		if(employee.getGender()!=null) {
			emp.setGender(employee.getGender());
		}
		if(employee.getDepartment()!=null) {
			emp.setDepartment(employee.getDepartment());
		}
		employeeJPA.save(emp);
		}
	}
	
	/**
	 * 员工删除
	 * @param id
	 */
	@Override
	public void deleteEmp(Integer id) {
		employeeJPA.delete(id);
		
	}
	@Override
	public void deleteBatch(List<Integer> ids) {
		employeeJPA.deleteEmployeesByIds(ids.toArray(new Integer[ids.size()]));
		
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return employeeJPA.findAll(pageable);
	}

}
