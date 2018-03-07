package zw.co.posb.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name="dp_history")
@NamedQueries({
	@NamedQuery(name="DpHistory.findAllChqReturns", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 159 AND d.description IS NOT NULL"),
	@NamedQuery(name="DpHistory.findAllChqReturnsWithNullDescription", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 159 AND d.description IS NULL"),
	@NamedQuery(name="DpHistory.findAllCashDepositReversals", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 157 AND d.description IS NOT NULL"),
	@NamedQuery(name="DpHistory.findAllCashDepositReversalsNullDescription", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 157 AND d.description IS NULL"),
	@NamedQuery(name="DpHistory.findAllCashDepositsRectified", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 117 AND d.description IS NOT NULL"),
	@NamedQuery(name="DpHistory.findAllCashDepositsRectifiedNullDescription", query="SELECT d FROM DpHistory d WHERE d.acctNo = '100310015095' AND d.tranCode = 117 AND d.description IS NULL")
})
public class DpHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="acct_no")
	private String acctNo;

	@Column(name="acct_type")
	private String acctType;

	private BigDecimal amt;

	@Column(name="create_dt")
	private Timestamp createDt;

	private String description;

	@Column(name="effective_dt")
	private Timestamp effectiveDt;

	@Column(name="empl_id")
	private short emplId;

	@Column(name="exch_crncy_id")
	private short exchCrncyId;

	@Column(name="orig_branch_no")
	private short origBranchNo;

	@Column(name="posting_dt_tm")
	private Timestamp postingDtTm;

	@Id
	private BigDecimal ptid;

	@Column(name="tran_code")
	private short tranCode;

	@Column(name="value_dt")
	private Timestamp valueDt;

	public DpHistory() {
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getEffectiveDt() {
		return effectiveDt;
	}

	public void setEffectiveDt(Timestamp effectiveDt) {
		this.effectiveDt = effectiveDt;
	}

	public short getEmplId() {
		return emplId;
	}

	public void setEmplId(short emplId) {
		this.emplId = emplId;
	}

	public short getExchCrncyId() {
		return exchCrncyId;
	}

	public void setExchCrncyId(short exchCrncyId) {
		this.exchCrncyId = exchCrncyId;
	}

	public short getOrigBranchNo() {
		return origBranchNo;
	}

	public void setOrigBranchNo(short origBranchNo) {
		this.origBranchNo = origBranchNo;
	}

	public Timestamp getPostingDtTm() {
		return postingDtTm;
	}

	public void setPostingDtTm(Timestamp postingDtTm) {
		this.postingDtTm = postingDtTm;
	}

	public BigDecimal getPtid() {
		return ptid;
	}

	public void setPtid(BigDecimal ptid) {
		this.ptid = ptid;
	}

	public short getTranCode() {
		return tranCode;
	}

	public void setTranCode(short tranCode) {
		this.tranCode = tranCode;
	}

	public Timestamp getValueDt() {
		return valueDt;
	}

	public void setValueDt(Timestamp valueDt) {
		this.valueDt = valueDt;
	}

}