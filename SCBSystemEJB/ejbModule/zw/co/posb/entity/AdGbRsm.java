package zw.co.posb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="ad_gb_rsm")
@NamedQuery(name="AdGbRsm.findAll", query="SELECT a FROM AdGbRsm a")
public class AdGbRsm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="employee_id")
	private short employeeId;

	@Column(name="user_name")
	private String userName;
	
	private String name;

	public AdGbRsm() {
	}

	public short getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(short employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}