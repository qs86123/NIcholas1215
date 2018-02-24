package com.zhenjie.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_dept")
public class Department {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
    private Integer deptId;
	@Column(name = "name")
    private String deptName;

    
    public Department() {
	}

	public Department(Integer deptId) {
		super();
		this.deptId = deptId;
	}

	public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

	@Override
	public String toString() {
		return "Department [deptId=" + deptId + ", deptName=" + deptName + "]";
	}
    
}