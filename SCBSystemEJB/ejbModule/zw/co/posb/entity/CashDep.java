package zw.co.posb.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CashDep {
	
	private Short ptid;
	private String acct_no;
	private String description;
	private short branch_no;
	private String name_1;
	private String tl_tran_code;
	private BigDecimal cash_in;
	private Date create_dt;
	private Date effective_dt;
	private Date posting_dt_tm;
	
	public Short getPtid() {
		return ptid;
	}
	public void setPtid(Short ptid) {
		this.ptid = ptid;
	}
	public String getAcct_no() {
		return acct_no;
	}
	public void setAcct_no(String acct_no) {
		this.acct_no = acct_no;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public short getBranch_no() {
		return branch_no;
	}
	public void setBranch_no(short branch_no) {
		this.branch_no = branch_no;
	}
	public String getName_1() {
		return name_1;
	}
	public void setName_1(String name_1) {
		this.name_1 = name_1;
	}
	public String getTl_tran_code() {
		return tl_tran_code;
	}
	public void setTl_tran_code(String tl_tran_code) {
		this.tl_tran_code = tl_tran_code;
	}
	public BigDecimal getCash_in() {
		return cash_in;
	}
	public void setCash_in(BigDecimal cash_in) {
		this.cash_in = cash_in;
	}
	public Date getCreate_dt() {
		return create_dt;
	}
	public void setCreate_dt(Date create_dt) {
		this.create_dt = create_dt;
	}
	public Date getEffective_dt() {
		return effective_dt;
	}
	public void setEffective_dt(Date effective_dt) {
		this.effective_dt = effective_dt;
	}
	public Date getPosting_dt_tm() {
		return posting_dt_tm;
	}
	public void setPosting_dt_tm(Date posting_dt_tm) {
		this.posting_dt_tm = posting_dt_tm;
	}
}
