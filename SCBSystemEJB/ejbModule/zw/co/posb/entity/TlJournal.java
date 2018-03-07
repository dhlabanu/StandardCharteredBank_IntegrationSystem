package zw.co.posb.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "TL_JOURNAL")
@NamedQueries({
		//@NamedQuery(name = "TlJournal.findAll", query = "SELECT t FROM TlJournal t WHERE t.acctNo='100310015095' AND t.reversal = 0 AND t.description IS NOT NULL"),
		@NamedQuery(name = "TlJournal.findAll", query = "SELECT t FROM TlJournal t WHERE t.acctNo='100310015095' AND t.description IS NOT NULL"),
		@NamedQuery(name = "TlJournal.findAllNULLDesc", query = "SELECT t FROM TlJournal t WHERE t.acctNo='100310015095' AND t.reversal = 0 AND t.description IS NULL"),
		@NamedQuery(name = "TlJournal.findAllReveredCashDeposits", query = "SELECT t FROM TlJournal t WHERE t.acctNo='100310015095' AND t.description IS NOT NULL AND t.reversal = 2") })
public class TlJournal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "acct_no")
	private String acctNo;

	@Column(name = "acct_type")
	private String acctType;

	@Column(name = "actual_rate")
	private BigDecimal actualRate;

	@Column(name = "actual_rate2")
	private BigDecimal actualRate2;

	@Column(name = "batch_id")
	private Byte batchId;

	@Column(name = "batch_status")
	private byte batchStatus;

	@Column(name = "blocked_check")
	private String blockedCheck;

	@Column(name = "branch_no")
	private short branchNo;

	@Column(name = "buy_receipt_no")
	private BigDecimal buyReceiptNo;

	@Column(name = "calc_data_1")
	private String calcData1;

	@Column(name = "calc_data_2")
	private String calcData2;

	@Column(name = "calc_tax_amt")
	private BigDecimal calcTaxAmt;

	@Column(name = "cash_in")
	private BigDecimal cashIn;

	@Column(name = "cash_out")
	private BigDecimal cashOut;

	@Column(name = "cashin_reason")
	private Integer cashinReason;

	@Column(name = "cashout_reason")
	private Integer cashoutReason;

	@Column(name = "cc_amt")
	private BigDecimal ccAmt;

	@Column(name = "check_no")
	private BigDecimal checkNo;

	@Column(name = "chks_as_cash")
	private BigDecimal chksAsCash;

	@Column(name = "chkscash_reason")
	private Integer chkscashReason;

	@Column(name = "clearing_acct_no")
	private String clearingAcctNo;

	@Column(name = "clearing_acct_type")
	private String clearingAcctType;

	@Column(name = "close_amt")
	private BigDecimal closeAmt;

	@Column(name = "corres_branch")
	private Integer corresBranch;

	@Column(name = "create_dt")
	private Timestamp createDt;

	@Column(name = "crncy_id")
	private short crncyId;

	private String description;

	@Column(name = "drawer_no")
	private short drawerNo;

	@Column(name = "effective_dt")
	private Timestamp effectiveDt;

	@Column(name = "empl_id")
	private short emplId;

	private Integer endpoint;

	@Column(name = "equiv_amt")
	private BigDecimal equivAmt;

	@Column(name = "equiv_cashin")
	private BigDecimal equivCashin;

	@Column(name = "equiv_cashout")
	private BigDecimal equivCashout;

	@Column(name = "equiv_chksascash")
	private BigDecimal equivChksascash;

	@Column(name = "equiv_fcccamt")
	private BigDecimal equivFcccamt;

	@Column(name = "equiv_onuschks")
	private BigDecimal equivOnuschks;

	@Column(name = "equiv_transitchks")
	private BigDecimal equivTransitchks;

	@Column(name = "exch_crncy_id")
	private short exchCrncyId;

	@Column(name = "exch_r_hist_ptid")
	private Integer exchRHistPtid;

	@Column(name = "exch_r_hist_ptid2")
	private Integer exchRHistPtid2;

	@Column(name = "exch_rate")
	private BigDecimal exchRate;

	@Column(name = "exch_rate_no")
	private Byte exchRateNo;

	@Column(name = "exch_rate_no2")
	private Byte exchRateNo2;

	@Column(name = "exch_rate2")
	private BigDecimal exchRate2;

	@Column(name = "external_id")
	private Integer externalId;

	@Column(name = "fc_cc_amt")
	private BigDecimal fcCcAmt;

	@Column(name = "force_reason")
	private Byte forceReason;

	private String future;

	@Column(name = "grant_deny")
	private String grantDeny;

	@Column(name = "history_ptid")
	private BigDecimal historyPtid;

	@Column(name = "int_amt")
	private BigDecimal intAmt;

	@Column(name = "item_count")
	private short itemCount;

	@Column(name = "loc_cr_ptid")
	private BigDecimal locCrPtid;

	@Column(name = "loc_dr_ptid")
	private BigDecimal locDrPtid;

	@Column(name = "Memo_Bal_Upd")
	private String memo_Bal_Upd;

	@Column(name = "memo_float")
	private BigDecimal memoFloat;

	@Column(name = "memo_post_amt")
	private BigDecimal memoPostAmt;

	@Column(name = "memo_ptid")
	private BigDecimal memoPtid;

	@Column(name = "net_amt")
	private BigDecimal netAmt;

	@Column(name = "net_amt2")
	private BigDecimal netAmt2;

	@Column(name = "netamt_reason")
	private Integer netamtReason;

	@Column(name = "netamt2_reason")
	private Integer netamt2Reason;

	@Column(name = "on_us_chks")
	private BigDecimal onUsChks;

	@Column(name = "onuschks_reason")
	private Integer onuschksReason;

	@Column(name = "pb_updated")
	private String pbUpdated;

	@Column(name = "penalty_amt")
	private BigDecimal penaltyAmt;

	@Column(name = "pin_entered")
	private String pinEntered;

	@Column(name = "pkg_adj_1")
	private BigDecimal pkgAdj1;

	@Column(name = "pkg_adj_2")
	private BigDecimal pkgAdj2;

	@Column(name = "pod_status")
	private byte podStatus;

	@Column(name = "posting_dt_tm")
	private Timestamp postingDtTm;

	@Id
	private BigDecimal ptid;

	@Column(name = "rate_sheet_type_1")
	private String rateSheetType1;

	@Column(name = "rate_sheet_type_2")
	private String rateSheetType2;

	@Column(name = "rate_type")
	private String rateType;

	@Column(name = "rate_type2")
	private String rateType2;

	private String reference;

	@Column(name = "reference_no")
	private String referenceNo;

	@Column(name = "rem_tran_ptid")
	private BigDecimal remTranPtid;

	@Column(name = "rev_super_empl_id")
	private Short revSuperEmplId;

	private byte reversal;

	@Column(name = "rim_adj_1")
	private BigDecimal rimAdj1;

	@Column(name = "rim_adj_2")
	private BigDecimal rimAdj2;

	@Column(name = "rim_no")
	private int rimNo;

	@Column(name = "rim_ptid")
	private BigDecimal rimPtid;

	@Column(name = "sell_receipt_no")
	private BigDecimal sellReceiptNo;

	@Column(name = "sequence_no")
	private short sequenceNo;

	@Column(name = "sub_sequence")
	private byte subSequence;

	@Column(name = "super_empl_id")
	private Short superEmplId;

	@Column(name = "suspect_acct")
	private String suspectAcct;

	@Column(name = "tax_super_empl_id")
	private Short taxSuperEmplId;

	@Column(name = "tfr_acct_no")
	private String tfrAcctNo;

	@Column(name = "tfr_acct_type")
	private String tfrAcctType;

	@Column(name = "tfr_chk_no")
	private Integer tfrChkNo;

	@Column(name = "tfr_empl_id")
	private Short tfrEmplId;

	@Column(name = "tfr_pb_updated")
	private String tfrPbUpdated;

	@Column(name = "tl_cash_tfr_ptid")
	private BigDecimal tlCashTfrPtid;

	@Column(name = "tl_tran_code")
	private String tlTranCode;

	@Column(name = "tran_amt")
	private BigDecimal tranAmt;

	@Column(name = "tran_status")
	private byte tranStatus;

	@Column(name = "tranchks_reason")
	private Integer tranchksReason;

	@Column(name = "transit_chks")
	private BigDecimal transitChks;

	@Column(name = "usd_equiv_amt")
	private BigDecimal usdEquivAmt;

	@Column(name = "usd_equiv_cashin")
	private BigDecimal usdEquivCashin;

	@Column(name = "usd_equiv_cashout")
	private BigDecimal usdEquivCashout;

	@Column(name = "usd_equiv_chkscash")
	private BigDecimal usdEquivChkscash;

	@Column(name = "usd_equiv_onuschks")
	private BigDecimal usdEquivOnuschks;

	@Column(name = "usd_equiv_tranchks")
	private BigDecimal usdEquivTranchks;

	@Column(name = "utility_id")
	private Short utilityId;

	@Column(name = "VALUE_DT")
	private Timestamp valueDt;

	public TlJournal() {
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

	public BigDecimal getActualRate() {
		return this.actualRate;
	}

	public void setActualRate(BigDecimal actualRate) {
		this.actualRate = actualRate;
	}

	public BigDecimal getActualRate2() {
		return this.actualRate2;
	}

	public void setActualRate2(BigDecimal actualRate2) {
		this.actualRate2 = actualRate2;
	}

	public Byte getBatchId() {
		return this.batchId;
	}

	public void setBatchId(Byte batchId) {
		this.batchId = batchId;
	}

	public byte getBatchStatus() {
		return this.batchStatus;
	}

	public void setBatchStatus(byte batchStatus) {
		this.batchStatus = batchStatus;
	}

	public String getBlockedCheck() {
		return this.blockedCheck;
	}

	public void setBlockedCheck(String blockedCheck) {
		this.blockedCheck = blockedCheck;
	}

	public short getBranchNo() {
		return this.branchNo;
	}

	public void setBranchNo(short branchNo) {
		this.branchNo = branchNo;
	}

	public BigDecimal getBuyReceiptNo() {
		return this.buyReceiptNo;
	}

	public void setBuyReceiptNo(BigDecimal buyReceiptNo) {
		this.buyReceiptNo = buyReceiptNo;
	}

	public String getCalcData1() {
		return this.calcData1;
	}

	public void setCalcData1(String calcData1) {
		this.calcData1 = calcData1;
	}

	public String getCalcData2() {
		return this.calcData2;
	}

	public void setCalcData2(String calcData2) {
		this.calcData2 = calcData2;
	}

	public BigDecimal getCalcTaxAmt() {
		return this.calcTaxAmt;
	}

	public void setCalcTaxAmt(BigDecimal calcTaxAmt) {
		this.calcTaxAmt = calcTaxAmt;
	}

	public BigDecimal getCashIn() {
		return this.cashIn;
	}

	public void setCashIn(BigDecimal cashIn) {
		this.cashIn = cashIn;
	}

	public BigDecimal getCashOut() {
		return this.cashOut;
	}

	public void setCashOut(BigDecimal cashOut) {
		this.cashOut = cashOut;
	}

	public Integer getCashinReason() {
		return this.cashinReason;
	}

	public void setCashinReason(Integer cashinReason) {
		this.cashinReason = cashinReason;
	}

	public Integer getCashoutReason() {
		return this.cashoutReason;
	}

	public void setCashoutReason(Integer cashoutReason) {
		this.cashoutReason = cashoutReason;
	}

	public BigDecimal getCcAmt() {
		return this.ccAmt;
	}

	public void setCcAmt(BigDecimal ccAmt) {
		this.ccAmt = ccAmt;
	}

	public BigDecimal getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(BigDecimal checkNo) {
		this.checkNo = checkNo;
	}

	public BigDecimal getChksAsCash() {
		return this.chksAsCash;
	}

	public void setChksAsCash(BigDecimal chksAsCash) {
		this.chksAsCash = chksAsCash;
	}

	public Integer getChkscashReason() {
		return this.chkscashReason;
	}

	public void setChkscashReason(Integer chkscashReason) {
		this.chkscashReason = chkscashReason;
	}

	public String getClearingAcctNo() {
		return this.clearingAcctNo;
	}

	public void setClearingAcctNo(String clearingAcctNo) {
		this.clearingAcctNo = clearingAcctNo;
	}

	public String getClearingAcctType() {
		return this.clearingAcctType;
	}

	public void setClearingAcctType(String clearingAcctType) {
		this.clearingAcctType = clearingAcctType;
	}

	public BigDecimal getCloseAmt() {
		return this.closeAmt;
	}

	public void setCloseAmt(BigDecimal closeAmt) {
		this.closeAmt = closeAmt;
	}

	public Integer getCorresBranch() {
		return this.corresBranch;
	}

	public void setCorresBranch(Integer corresBranch) {
		this.corresBranch = corresBranch;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public BigDecimal getEquivCashin() {
		return this.equivCashin;
	}

	public void setEquivCashin(BigDecimal equivCashin) {
		this.equivCashin = equivCashin;
	}

	public BigDecimal getEquivCashout() {
		return this.equivCashout;
	}

	public void setEquivCashout(BigDecimal equivCashout) {
		this.equivCashout = equivCashout;
	}

	public BigDecimal getEquivChksascash() {
		return this.equivChksascash;
	}

	public void setEquivChksascash(BigDecimal equivChksascash) {
		this.equivChksascash = equivChksascash;
	}

	public BigDecimal getEquivFcccamt() {
		return this.equivFcccamt;
	}

	public void setEquivFcccamt(BigDecimal equivFcccamt) {
		this.equivFcccamt = equivFcccamt;
	}

	public BigDecimal getEquivOnuschks() {
		return this.equivOnuschks;
	}

	public void setEquivOnuschks(BigDecimal equivOnuschks) {
		this.equivOnuschks = equivOnuschks;
	}

	public BigDecimal getEquivTransitchks() {
		return this.equivTransitchks;
	}

	public void setEquivTransitchks(BigDecimal equivTransitchks) {
		this.equivTransitchks = equivTransitchks;
	}

	public short getExchCrncyId() {
		return this.exchCrncyId;
	}

	public void setExchCrncyId(short exchCrncyId) {
		this.exchCrncyId = exchCrncyId;
	}

	public Integer getExchRHistPtid() {
		return this.exchRHistPtid;
	}

	public void setExchRHistPtid(Integer exchRHistPtid) {
		this.exchRHistPtid = exchRHistPtid;
	}

	public Integer getExchRHistPtid2() {
		return this.exchRHistPtid2;
	}

	public void setExchRHistPtid2(Integer exchRHistPtid2) {
		this.exchRHistPtid2 = exchRHistPtid2;
	}

	public BigDecimal getExchRate() {
		return this.exchRate;
	}

	public void setExchRate(BigDecimal exchRate) {
		this.exchRate = exchRate;
	}

	public Byte getExchRateNo() {
		return this.exchRateNo;
	}

	public void setExchRateNo(Byte exchRateNo) {
		this.exchRateNo = exchRateNo;
	}

	public Byte getExchRateNo2() {
		return this.exchRateNo2;
	}

	public void setExchRateNo2(Byte exchRateNo2) {
		this.exchRateNo2 = exchRateNo2;
	}

	public BigDecimal getExchRate2() {
		return this.exchRate2;
	}

	public void setExchRate2(BigDecimal exchRate2) {
		this.exchRate2 = exchRate2;
	}

	public Integer getExternalId() {
		return this.externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	public BigDecimal getFcCcAmt() {
		return this.fcCcAmt;
	}

	public void setFcCcAmt(BigDecimal fcCcAmt) {
		this.fcCcAmt = fcCcAmt;
	}

	public Byte getForceReason() {
		return this.forceReason;
	}

	public void setForceReason(Byte forceReason) {
		this.forceReason = forceReason;
	}

	public String getFuture() {
		return this.future;
	}

	public void setFuture(String future) {
		this.future = future;
	}

	public String getGrantDeny() {
		return this.grantDeny;
	}

	public void setGrantDeny(String grantDeny) {
		this.grantDeny = grantDeny;
	}

	public BigDecimal getHistoryPtid() {
		return this.historyPtid;
	}

	public void setHistoryPtid(BigDecimal historyPtid) {
		this.historyPtid = historyPtid;
	}

	public BigDecimal getIntAmt() {
		return this.intAmt;
	}

	public void setIntAmt(BigDecimal intAmt) {
		this.intAmt = intAmt;
	}

	public short getItemCount() {
		return this.itemCount;
	}

	public void setItemCount(short itemCount) {
		this.itemCount = itemCount;
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

	public String getMemo_Bal_Upd() {
		return this.memo_Bal_Upd;
	}

	public void setMemo_Bal_Upd(String memo_Bal_Upd) {
		this.memo_Bal_Upd = memo_Bal_Upd;
	}

	public BigDecimal getMemoFloat() {
		return this.memoFloat;
	}

	public void setMemoFloat(BigDecimal memoFloat) {
		this.memoFloat = memoFloat;
	}

	public BigDecimal getMemoPostAmt() {
		return this.memoPostAmt;
	}

	public void setMemoPostAmt(BigDecimal memoPostAmt) {
		this.memoPostAmt = memoPostAmt;
	}

	public BigDecimal getMemoPtid() {
		return this.memoPtid;
	}

	public void setMemoPtid(BigDecimal memoPtid) {
		this.memoPtid = memoPtid;
	}

	public BigDecimal getNetAmt() {
		return this.netAmt;
	}

	public void setNetAmt(BigDecimal netAmt) {
		this.netAmt = netAmt;
	}

	public BigDecimal getNetAmt2() {
		return this.netAmt2;
	}

	public void setNetAmt2(BigDecimal netAmt2) {
		this.netAmt2 = netAmt2;
	}

	public Integer getNetamtReason() {
		return this.netamtReason;
	}

	public void setNetamtReason(Integer netamtReason) {
		this.netamtReason = netamtReason;
	}

	public Integer getNetamt2Reason() {
		return this.netamt2Reason;
	}

	public void setNetamt2Reason(Integer netamt2Reason) {
		this.netamt2Reason = netamt2Reason;
	}

	public BigDecimal getOnUsChks() {
		return this.onUsChks;
	}

	public void setOnUsChks(BigDecimal onUsChks) {
		this.onUsChks = onUsChks;
	}

	public Integer getOnuschksReason() {
		return this.onuschksReason;
	}

	public void setOnuschksReason(Integer onuschksReason) {
		this.onuschksReason = onuschksReason;
	}

	public String getPbUpdated() {
		return this.pbUpdated;
	}

	public void setPbUpdated(String pbUpdated) {
		this.pbUpdated = pbUpdated;
	}

	public BigDecimal getPenaltyAmt() {
		return this.penaltyAmt;
	}

	public void setPenaltyAmt(BigDecimal penaltyAmt) {
		this.penaltyAmt = penaltyAmt;
	}

	public String getPinEntered() {
		return this.pinEntered;
	}

	public void setPinEntered(String pinEntered) {
		this.pinEntered = pinEntered;
	}

	public BigDecimal getPkgAdj1() {
		return this.pkgAdj1;
	}

	public void setPkgAdj1(BigDecimal pkgAdj1) {
		this.pkgAdj1 = pkgAdj1;
	}

	public BigDecimal getPkgAdj2() {
		return this.pkgAdj2;
	}

	public void setPkgAdj2(BigDecimal pkgAdj2) {
		this.pkgAdj2 = pkgAdj2;
	}

	public byte getPodStatus() {
		return this.podStatus;
	}

	public void setPodStatus(byte podStatus) {
		this.podStatus = podStatus;
	}

	public Timestamp getPostingDtTm() {
		return this.postingDtTm;
	}

	public void setPostingDtTm(Timestamp postingDtTm) {
		this.postingDtTm = postingDtTm;
	}

	public BigDecimal getPtid() {
		return this.ptid;
	}

	public void setPtid(BigDecimal ptid) {
		this.ptid = ptid;
	}

	public String getRateSheetType1() {
		return this.rateSheetType1;
	}

	public void setRateSheetType1(String rateSheetType1) {
		this.rateSheetType1 = rateSheetType1;
	}

	public String getRateSheetType2() {
		return this.rateSheetType2;
	}

	public void setRateSheetType2(String rateSheetType2) {
		this.rateSheetType2 = rateSheetType2;
	}

	public String getRateType() {
		return this.rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getRateType2() {
		return this.rateType2;
	}

	public void setRateType2(String rateType2) {
		this.rateType2 = rateType2;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReferenceNo() {
		return this.referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public BigDecimal getRemTranPtid() {
		return this.remTranPtid;
	}

	public void setRemTranPtid(BigDecimal remTranPtid) {
		this.remTranPtid = remTranPtid;
	}

	public Short getRevSuperEmplId() {
		return this.revSuperEmplId;
	}

	public void setRevSuperEmplId(Short revSuperEmplId) {
		this.revSuperEmplId = revSuperEmplId;
	}

	public byte getReversal() {
		return this.reversal;
	}

	public void setReversal(byte reversal) {
		this.reversal = reversal;
	}

	public BigDecimal getRimAdj1() {
		return this.rimAdj1;
	}

	public void setRimAdj1(BigDecimal rimAdj1) {
		this.rimAdj1 = rimAdj1;
	}

	public BigDecimal getRimAdj2() {
		return this.rimAdj2;
	}

	public void setRimAdj2(BigDecimal rimAdj2) {
		this.rimAdj2 = rimAdj2;
	}

	public int getRimNo() {
		return this.rimNo;
	}

	public void setRimNo(int rimNo) {
		this.rimNo = rimNo;
	}

	public BigDecimal getRimPtid() {
		return this.rimPtid;
	}

	public void setRimPtid(BigDecimal rimPtid) {
		this.rimPtid = rimPtid;
	}

	public BigDecimal getSellReceiptNo() {
		return this.sellReceiptNo;
	}

	public void setSellReceiptNo(BigDecimal sellReceiptNo) {
		this.sellReceiptNo = sellReceiptNo;
	}

	public short getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(short sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public byte getSubSequence() {
		return this.subSequence;
	}

	public void setSubSequence(byte subSequence) {
		this.subSequence = subSequence;
	}

	public Short getSuperEmplId() {
		return this.superEmplId;
	}

	public void setSuperEmplId(Short superEmplId) {
		this.superEmplId = superEmplId;
	}

	public String getSuspectAcct() {
		return this.suspectAcct;
	}

	public void setSuspectAcct(String suspectAcct) {
		this.suspectAcct = suspectAcct;
	}

	public Short getTaxSuperEmplId() {
		return this.taxSuperEmplId;
	}

	public void setTaxSuperEmplId(Short taxSuperEmplId) {
		this.taxSuperEmplId = taxSuperEmplId;
	}

	public String getTfrAcctNo() {
		return this.tfrAcctNo;
	}

	public void setTfrAcctNo(String tfrAcctNo) {
		this.tfrAcctNo = tfrAcctNo;
	}

	public String getTfrAcctType() {
		return this.tfrAcctType;
	}

	public void setTfrAcctType(String tfrAcctType) {
		this.tfrAcctType = tfrAcctType;
	}

	public Integer getTfrChkNo() {
		return this.tfrChkNo;
	}

	public void setTfrChkNo(Integer tfrChkNo) {
		this.tfrChkNo = tfrChkNo;
	}

	public Short getTfrEmplId() {
		return this.tfrEmplId;
	}

	public void setTfrEmplId(Short tfrEmplId) {
		this.tfrEmplId = tfrEmplId;
	}

	public String getTfrPbUpdated() {
		return this.tfrPbUpdated;
	}

	public void setTfrPbUpdated(String tfrPbUpdated) {
		this.tfrPbUpdated = tfrPbUpdated;
	}

	public BigDecimal getTlCashTfrPtid() {
		return this.tlCashTfrPtid;
	}

	public void setTlCashTfrPtid(BigDecimal tlCashTfrPtid) {
		this.tlCashTfrPtid = tlCashTfrPtid;
	}

	public String getTlTranCode() {
		return this.tlTranCode;
	}

	public void setTlTranCode(String tlTranCode) {
		this.tlTranCode = tlTranCode;
	}

	public BigDecimal getTranAmt() {
		return this.tranAmt;
	}

	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}

	public byte getTranStatus() {
		return this.tranStatus;
	}

	public void setTranStatus(byte tranStatus) {
		this.tranStatus = tranStatus;
	}

	public Integer getTranchksReason() {
		return this.tranchksReason;
	}

	public void setTranchksReason(Integer tranchksReason) {
		this.tranchksReason = tranchksReason;
	}

	public BigDecimal getTransitChks() {
		return this.transitChks;
	}

	public void setTransitChks(BigDecimal transitChks) {
		this.transitChks = transitChks;
	}

	public BigDecimal getUsdEquivAmt() {
		return this.usdEquivAmt;
	}

	public void setUsdEquivAmt(BigDecimal usdEquivAmt) {
		this.usdEquivAmt = usdEquivAmt;
	}

	public BigDecimal getUsdEquivCashin() {
		return this.usdEquivCashin;
	}

	public void setUsdEquivCashin(BigDecimal usdEquivCashin) {
		this.usdEquivCashin = usdEquivCashin;
	}

	public BigDecimal getUsdEquivCashout() {
		return this.usdEquivCashout;
	}

	public void setUsdEquivCashout(BigDecimal usdEquivCashout) {
		this.usdEquivCashout = usdEquivCashout;
	}

	public BigDecimal getUsdEquivChkscash() {
		return this.usdEquivChkscash;
	}

	public void setUsdEquivChkscash(BigDecimal usdEquivChkscash) {
		this.usdEquivChkscash = usdEquivChkscash;
	}

	public BigDecimal getUsdEquivOnuschks() {
		return this.usdEquivOnuschks;
	}

	public void setUsdEquivOnuschks(BigDecimal usdEquivOnuschks) {
		this.usdEquivOnuschks = usdEquivOnuschks;
	}

	public BigDecimal getUsdEquivTranchks() {
		return this.usdEquivTranchks;
	}

	public void setUsdEquivTranchks(BigDecimal usdEquivTranchks) {
		this.usdEquivTranchks = usdEquivTranchks;
	}

	public Short getUtilityId() {
		return this.utilityId;
	}

	public void setUtilityId(Short utilityId) {
		this.utilityId = utilityId;
	}

	public Timestamp getValueDt() {
		return this.valueDt;
	}

	public void setValueDt(Timestamp valueDt) {
		this.valueDt = valueDt;
	}

}