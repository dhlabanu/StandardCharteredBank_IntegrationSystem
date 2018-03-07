package zw.co.posb.ejb.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import zw.co.posb.entity.AdGbBranch;
import zw.co.posb.entity.AdGbCrncy;
import zw.co.posb.entity.AdGbRsm;
import zw.co.posb.entity.DpHistory;
import zw.co.posb.entity.ReversalsTb;
import zw.co.posb.entity.StanchartTb;
import zw.co.posb.entity.TlItemCapture;
import zw.co.posb.entity.TlJournal;

@Local
public interface Cheque_CashDepositLocal {

	public List<TlJournal> getAllCashDeposits();
	public List<AdGbBranch> getAllPOSBBranches();
	public List<AdGbCrncy> getCurrency();
	public List<StanchartTb> getSCBAccounts();
	public List<TlItemCapture> getChequeDetails();
	public List<DpHistory> getReturnedCheqDeposits();
	public List<DpHistory> getReturnedCheqDepositsNullDescription();
	public List<DpHistory> getReversedCashDepositsReversals();
	public List<DpHistory> getCashChQReversalsNullDescription();
	public List<DpHistory> getCashDepositsRectified();
	public List<DpHistory> getCashDepositsRectifiedNullDescription();
	public List<TlJournal> getAllCashDepositsWithNoDescription();
	public List<TlJournal> getAllCashDeposits_Reversed();
	public TlJournal getAccount(String description);
	public void fetcherIntraday();
	public void fetcherEOD();
	public List<AdGbRsm> getTrans_Teller();
	
	public ReversalsTb create(ReversalsTb reversals);
	public void reversals();
	public List<ReversalsTb> getAllCashChequeReversals();

}
