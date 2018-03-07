package zw.co.posb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ad_gb_branch")
@NamedQuery(name = "AdGbBranch.findAll", query = "SELECT a FROM AdGbBranch a")
public class AdGbBranch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "branch_no")
	private Short branchNo;

	@Column(name = "name_1")
	private String name1;
	
	public AdGbBranch() {
	}

	public Short getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(Short branchNo) {
		this.branchNo = branchNo;
	}

	public String getName1() {
		return this.name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

}