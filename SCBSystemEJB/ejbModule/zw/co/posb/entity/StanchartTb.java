package zw.co.posb.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="stanchart_tb")
@NamedQuery(name="StanchartTb.findAll", query="SELECT s FROM StanchartTb s")
public class StanchartTb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="acct_no")
	private String acctNo;

	@Column(name="acct_name")
	private String acctName;

	public StanchartTb() {
	}

	public String getAcctNo() {
		return this.acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctName() {
		return this.acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

}