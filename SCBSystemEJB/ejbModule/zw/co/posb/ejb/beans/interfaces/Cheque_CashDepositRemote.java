package zw.co.posb.ejb.beans.interfaces;

import java.util.List;

import javax.ejb.Remote;

import zw.co.posb.entity.TlJournal;

@Remote
public interface Cheque_CashDepositRemote {
	
	public List<TlJournal> getAllCashDeposits();

}
