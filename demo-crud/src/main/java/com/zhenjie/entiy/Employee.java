package com.zhenjie.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_emp")
public class Employee {

	@Id
	@GeneratedValue
	@Column(name = "t_id")
	private Integer empId;

	@Column(name = "empName")
	private String empName;

	@Column(name = "t_gender")
	private String gender;

	@Column(name = "t_email")
	private String email;

	@OneToOne 
	@JoinColumn(name="t_did")//注释本表中指向另一个表的外键。  
	private Department department;

	public Employee() {
	}


	public Employee(Integer empId, String empName, String gender, String email, Department department) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.gender = gender;
		this.email = email;
		this.department = department;
	}


	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName == null ? null : empName.trim();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender == null ? null : gender.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}


	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", gender=" + gender + ", email=" + email
				+ ", department=" + department + "]";
	}

	

}