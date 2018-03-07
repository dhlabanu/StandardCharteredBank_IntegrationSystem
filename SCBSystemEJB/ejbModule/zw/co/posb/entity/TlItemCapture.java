package zw.co.posb.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name="tl_item_capture")
@NamedQuery(name="TlItemCapture.findAll", query="SELECT t FROM TlItemCapture t WHERE t.acctNo='100310015095'")	
public class TlItemCapture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="acct_no")
	private String acctNo;

	@Column(name="acct_type")
	private String acctType;

	private BigDecimal amount;

	@Column(name="branch_no")
	private short branchNo;

	@Column(name="check_no")
	private int checkNo;

	@Column(name="check_type")
	private String checkType;

	@Column(name="clearing_type")
	private String clearingType;

	@Column(name="create_dt")
	private Timestamp createDt;

	@Column(name="crncy_id")
	private short crncyId;

	@Column(name="drawer_no")
	private short drawerNo;

	@Column(name="effective_dt")
	private Timestamp effectiveDt;

	@Column(name="empl_id")
	private short emplId;

	private Integer endpoint;

	@Column(name="equiv_amt")
	private BigDecimal equivAmt;

	@Column(name="exch_crncy_id")
	private short exchCrncyId;

	@Column(name="force_reason")
	private Byte forceReason;

	@Column(name="future_dt")
	private Timestamp futureDt;

	@Column(name="item_no")
	private short itemNo;

	@Column(name="item_type")
	private byte itemType;

	@Column(name="journal_ptid")
	private BigDecimal journalPtid;

	@Column(name="loc_cr_ptid")
	private BigDecimal locCrPtid;

	@Column(name="loc_dr_ptid")
	private BigDecimal locDrPtid;

	@Column(name="memo_ptid")
	private BigDecimal memoPtid;

	@Column(name="pb_updated")
	private String pbUpdated;

	@Id
	private BigDecimal ptid;

	@Column(name="routing_no")
	private String routingNo;

	@Column(name="sequence_no")
	private int sequenceNo;

	@Column(name="sub_sequence")
	private byte subSequence;

	@Column(name="super_empl_id")
	private short superEmplId;

	@Column(name="tran_code")
	private Short tranCode;

	@Column(name="tran_status")
	private byte tranStatus;

	public TlItemCapture() {
	}

	public String getAcctNo() {
		return this.acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctType() {
		return this.acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public short getBranchNo() {
		return this.branchNo;
	}

	public void setBranchNo(short branchNo) {
		this.branchNo = branchNo;
	}

	public int getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(int checkNo) {
		this.checkNo = checkNo;
	}

	public String getCheckType() {
		return this.checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getClearingType() {
		return this.clearingType;
	}

	public void setClearingType(String clearingType) {
		this.clearingType = clearingType;
	}

	public Timestamp getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public short getCrncyId() {
		return this.crncyId;
	}

	public void setCrncyId(short crncyId) {
		this.crncyId = crncyId;
	}

	public short getDrawerNo() {
		return this.drawerNo;
	}

	public void setDrawerNo(short drawerNo) {
		this.drawerNo = drawerNo;
	}

	public Timestamp getEffectiveDt() {
		return this.effectiveDt;
	}

	public void setEffectiveDt(Timestamp effectiveDt) {
		this.effectiveDt = effectiveDt;
	}

	public short getEmplId() {
		return this.emplId;
	}

	public void setEmplId(short emplId) {
		this.emplId = emplId;
	}

	public Integer getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(Integer endpoint) {
		this.endpoint = endpoint;
	}

	public BigDecimal getEquivAmt() {
		return this.equivAmt;
	}

	public void setEquivAmt(BigDecimal equivAmt) {
		this.equivAmt = equivAmt;
	}

	public short getExchCrncyId() {
		return this.exchCrncyId;
	}

	public void setExchCrncyId(short exchCrncyId) {
		this.exchCrncyId = exchCrncyId;
	}

	public Byte getForceReason() {
		return this.forceReason;
	}

	public void setForceReason(Byte forceReason) {
		this.forceReason = forceReason;
	}

	public Timestamp getFutureDt() {
		return this.futureDt;
	}

	public void setFutureDt(Timestamp futureDt) {
		this.futureDt = futureDt;
	}

	public short getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(short itemNo) {
		this.itemNo = itemNo;
	}

	public byte getItemType() {
		return this.itemType;
	}

	public void setItemType(byte itemType) {
		this.itemType = itemType;
	}

	public BigDecimal getJournalPtid() {
		return this.journalPtid;
	}

	public void setJournalPtid(BigDecimal journalPtid) {
		this.journalPtid = journalPtid;
	}

	public BigDecimal getLocCrPtid() {
		return this.locCrPtid;
	}

	public void setLocCrPtid(BigDecimal locCrPtid) {
		this.locCrPtid = locCrPtid;
	}

	public BigDecimal getLocDrPtid() {
		return this.locDrPtid;
	}

	public void setLocDrPtid(BigDecimal locDrPtid) {
		this.locDrPtid = locDrPtid;
	}

	public BigDecimal getMemoPtid() {
		return this.memoPtid;
	}

	public void setMemoPtid(BigDecimal memoPtid) {
		this.memoPtid = memoPtid;
	}

	public String getPbUpdated() {
		return this.pbUpdated;
	}

	public void setPbUpdated(String pbUpdated) {
		this.pbUpdated = pbUpdated;
	}

	public BigDecimal getPtid() {
		return this.ptid;
	}

	public void setPtid(BigDecimal ptid) {
		this.ptid = ptid;
	}

	public String getRoutingNo() {
		return this.routingNo;
	}

	public void setRoutingNo(String routingNo) {
		this.routingNo = routingNo;
	}

	public int getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public byte getSubSequence() {
		return this.subSequence;
	}

	public void setSubSequence(byte subSequence) {
		this.subSequence = subSequence;
	}

	public short getSuperEmplId() {
		return this.superEmplId;
	}

	public void setSuperEmplId(short superEmplId) {
		this.superEmplId = superEmplId;
	}

	public Short getTranCode() {
		return this.tranCode;
	}

	public void setTranCode(Short tranCode) {
		this.tranCode = tranCode;
	}

	public byte getTranStatus() {
		return this.tranStatus;
	}

	public void setTranStatus(byte tranStatus) {
		this.tranStatus = tranStatus;
	}

}