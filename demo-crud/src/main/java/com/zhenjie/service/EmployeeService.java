package com.zhenjie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zhenjie.entiy.Employee;

public interface EmployeeService {
	
	
	/**
	 * 分页查询
	 *@author zhenjie_qin
	 * @param pageable
	 * @return
	 */
	public Page<Employee> findAll(Pageable pageable);
	
	/**
	 * 查询所有员工
	 * @return
	 */
	public List<Employee> getAll();
	
	
	/**
	 * 员工保存方法
	 * @param employee
	 */
	public void saveEmp(Employee employee) ;
	/**
	 * 检验用户名是否可用
	 * @param empName
	 * @return true:代表可用 false不可用
	 */
	public boolean checkUser(String empName) ;

	public Employee getEmp(Integer id) ;
	
	/**
	 * 员工更新
	 * @param employee
	 */
	public void updateEmp(Employee employee) ;
	
	/**
	 * 员工删除
	 * @param id
	 */
	public void deleteEmp(Integer id) ;

	public void deleteBatch(List<Integer> ids) ;

}
