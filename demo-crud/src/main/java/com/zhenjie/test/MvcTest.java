package com.zhenjie.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.zhenjie.Application;
import com.zhenjie.entiy.Department;
import com.zhenjie.entiy.Employee;
import com.zhenjie.jpa.DepartmentJPA;
import com.zhenjie.jpa.EmployeeJPA;

/**
 * 使用Spring测试模块提供的测试请求功能，测试curd请求的正确性
 * Spring测试的时候，需要servlet3.0的支持
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes=Application.class)
public class MvcTest {
	//传入Springmvc的IOC
	@Autowired
	WebApplicationContext context;
	@Autowired
	DepartmentJPA departmentJPA;
	@Autowired
	EmployeeJPA employeeJPA;
	//虚拟mvc请求，获取到处理结果
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	//@Test
	public void testPage() throws Exception {
		//模拟请求拿到返回值
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "1"))
			.andReturn();
		
		//请求成功以后，请求域中会有pageInfo;我们可以取出pageInfo 进行验证
		MockHttpServletRequest request = result.getRequest();
		Page<Employee> page = (Page<Employee>) request.getAttribute("pageInfo");
		System.out.println("当前页码:"+page.getNumber());
		System.out.println("总页码:"+page.getTotalPages());
		System.out.println("总记录数:"+page.getTotalElements());
	
		//获取员工数据
		List<Employee> list = page.getContent();
		for(Employee emp:list) {
			System.out.println(emp);
		}
	}
	
	
	@Test
	public void testPage1() throws Exception {
		//模拟请求拿到返回值
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/list"))
			.andReturn();
		MockHttpServletRequest request = result.getRequest();
		
		}
	
	public void testgetDepts() {
		List<Department> list = departmentJPA.findAll();
		for(Department dept:list) {
			System.out.println(dept.getDeptName()+"==>"+dept.getDeptId());
		}
	}
	
	public void testaddEmp() {
		Employee emp=new Employee(null, "xiaoxiao", "F", "xiaoxiao@123.com", null);
		employeeJPA.save(emp);
	}
	
	public void testEmpCheckName() {
			String empName="xiaoxiao";
			/*long l = employeeJPA.CountByEmpName(empName);
			System.out.println(l);*/
	}
	
	public void testRegx() {
		String empName="晓1吗";
		String regx="(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u4e00-\\u9fa5]{2,5}$)";
		boolean b = empName.matches(regx);
		System.out.println(b);
	}
}

