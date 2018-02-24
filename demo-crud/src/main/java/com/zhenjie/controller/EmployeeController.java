package com.zhenjie.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenjie.entiy.Employee;
import com.zhenjie.entiy.Msg;
import com.zhenjie.service.EmployeeService;

/**
 * 处理员工CRUD请求
 * 
 * @author Administrator
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	
	/**
	 * 单个批量二合一
	 * 批量删除：1-2-3
	 * 单个删除：1
	 * @param id
	 * @return
	 */
	
	@RequestMapping(value = {"/","/index"})
	public String index(){
		return "index";		
	}
	
	@ResponseBody
	@RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("ids")String ids) {
		//批量删除
		if(ids.contains("-")) {
			List<Integer> del_ids=new ArrayList<>();
			String[] str_ids = ids.split("-");
			for(String sid:str_ids) {
				del_ids.add(Integer.parseInt(sid));
			}
			employeeService.deleteBatch(del_ids);
		}else {
			//单个删除
			employeeService.deleteEmp(Integer.parseInt(ids));
		}
		return Msg.success();
	}

	/**
	 * 如果直接发送ajax=PUT形式的请求 封装的数据为 Employee [empId=1003, empName=null, gender=null,
	 * email=null, dId=null, department=null] 问题： 请求体中有数据，但是Employee 对象封装不上； update
	 * tbl_emp where emp_id =1003; 原因： Tomcat, 1、将请求体中的数据，封装一个map.
	 * 2、request.getParameter("empName")就会从这个map中取值。 3、SpringMVC封装POJO对象的时候。
	 * 会把POJO中每一个属性的值，request.getParamter("email"); AJAX发送PUT请求引发的血案！
	 * PUT请求，请求体中的数据，拿不出！ 原因是Tomcat一看是PUT请求就不会封装请求体中的数据为map，只有POAT形式的才封装为map
	 * 要能支持直接发送PUT之类的请求还要封装请求体的数据 1、需配过滤器HttpPutFormContentFilter
	 * 2、他的作用，将请求体中的数据解析包装成一个map。 3、request.getParameter()被重写，就会从自己封装的map中取数据 员工更新方法
	 * 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		System.out.println(employee);
		employeeService.updateEmp(employee);
		return Msg.success();
	}

	/**
	 * 根据id查询员工
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}

	/**
	 * 检查用户名是否可用
	 * 
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkuser")
	public Msg checkuser(@RequestParam("empName") String empName) {
		// 服务端也可先再次校验前端数据
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u4e00-\\u9fa5]{2,5}$)";
		if (!empName.matches(regx)) {
			return Msg.fail().add("va_msg", "用户名可以是6-16位英文和数字的组合或者2-5位中文");
		}
		// 数据库用户名重复校验
		boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}

	/**
	 * 员工保存 1、支持JSR303校验 2、导入Hibernate-Validator
	 * 
	 * @return
	 */
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			// 校验失败返回校验 失败信息
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名" + fieldError.getField());
				System.out.println("错误信息：" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/**
	 * 导入Jackson包
	 * 
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "0") Integer pn) {
		Pageable pageable =new PageRequest(pn, 5);
		Page<Employee> page =employeeService.findAll(pageable);
		// 所有PageInfo包装查询后的结果，只需要将PageInfo交给页面即可
		// PageInfo封装了详细的分页信息，包括有我们查出来的数据,传入连续显示的页数

		return Msg.success().add("pageInfo", page);
	}

	/**
	 * 查询员工数据(分页查询)
	 * 
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "0") Integer pn, Model model) {
		// 引入PageHelper 分页插件
		// 在查询之前只想要调用,传入页面以及每页的大小
		Pageable pageable =new PageRequest(pn, 5);
		// startPage后面紧跟的这个查询就是一个分页查询
		Page<Employee> page =employeeService.findAll(pageable);
		// 所有PageInfo包装查询后的结果，只需要将PageInfo交给页面即可
		// PageInfo封装了详细的分页信息，包括有我们查出来的数据,传入连续显示的页数
		model.addAttribute("pageInfo", page);
		return "list";
	}
}
