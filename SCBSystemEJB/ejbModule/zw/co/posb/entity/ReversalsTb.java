package zw.co.posb.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="reversals_tb")
@NamedQuery(name="ReversalsTb.findAll", query="SELECT r FROM ReversalsTb r")
public class ReversalsTb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private BigDecimal ptid;

	private Timestamp dtime;

	private String refno;

	public ReversalsTb() {
	}

	public BigDecimal getPtid() {
		return this.ptid;
	}

	public void setPtid(BigDecimal ptid) {
		this.ptid = ptid;
	}

	public Timestamp getDtime() {
		return this.dtime;
	}

	public void setDtime(Timestamp dtime) {
		this.dtime = dtime;
	}

	public String getRefno() {
		return this.refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

}