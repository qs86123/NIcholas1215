package com.zhenjie.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhenjie.Application;
import com.zhenjie.entiy.Department;
import com.zhenjie.entiy.Employee;
import com.zhenjie.jpa.DepartmentJPA;
import com.zhenjie.jpa.EmployeeJPA;
import com.zhenjie.service.EmployeeService;
/**
 *测试dao层的工作
 *推荐Spring的项目就可以用Spring的单元测试，可以自动注入我们要的组件
 *1、导入SpringTest模块
 *2、@ContextConfiguration()在括号内指定Sping 配置文件的位置
 *3、直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class JpaTest {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	EmployeeJPA emplyeeJAP;
	@Autowired
	DepartmentJPA departmentJAP;
	
	
	public void testCRUD() throws Exception {
	
		//插入部门数据
		//departmentJAP.save(new Department(null,"开发部"));
		//departmentJAP.save(new Department(null,"测试部"));
		////插入员工数据
		emplyeeJAP.save(new Employee(null, "振杰", "F", "qs86123@163.com", new Department(1)));
		System.out.println("插入成功");
		//批量插入员工
		/*for(int i = 0;i<1000;i++) {
			String uid = UUID.randomUUID().toString().substring(0, 5)+i;
			emplyeeJAP.save(new Employee(null,uid,"M",uid+"@zhenjie.com",2));
		}
		System.out.println("批量插入成功");
		List<Employee> list = emplyeeJAP.findAll();
		for(Employee emp:list) {
			System.out.println("ID:"+emp.getdId()+"==>Name: "+emp.getEmpName());
		}
		 */
	}
	
	public void selectEmp() {
		Employee employee = emplyeeJAP.findOne(1003);
		System.out.println(employee);
	}
	//@Test
	public void updateEmp() {
		Employee employee=new Employee(1003, "阿杰",null, null, new Department(2));
		employeeService.updateEmp(employee);
	}
	
	public void deleteEmp() {
		emplyeeJAP.delete(17);
	}
	
	public void checkUser() {
		boolean b = employeeService.checkUser("509b817");
		System.out.println(b);
	}
	
	public void deleteEmps() {
		List<Integer>ids =new ArrayList<>();
		ids.add(3);
		ids.add(4);
		employeeService.deleteBatch(ids);
	}
	
	public void test() {
		Pageable pageable =new PageRequest(0, 5);
		Page<Employee> pi = emplyeeJAP.findAll(pageable);
		System.out.println("当前页码:"+pi.getNumber());
		System.out.println("总页码:"+pi.getTotalPages());
		System.out.println("总记录数:"+pi.getTotalElements());
		
		System.out.println(pi.getNumberOfElements());
		System.out.println(pi.getSize());
		System.out.println("在页面需要连续显示的页码:");
		Pageable pageable2 = pi.nextPageable();
		System.out.println(pageable2.getPageNumber());
		System.out.println(pageable2.getPageSize());
		//获取员工数据
		List<Employee> list = pi.getContent();
		for(Employee emp:list) {
			System.out.println(emp);
		}
	}
	@Test
	public void rex() {
		String str="立马是";
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u4e00-\\u9fa5]{2,5}$)";
		boolean b = str.matches(regx);
		System.out.println(b);
	}
}
