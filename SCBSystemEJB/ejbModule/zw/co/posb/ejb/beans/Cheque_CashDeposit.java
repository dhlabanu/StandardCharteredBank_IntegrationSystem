package zw.co.posb.ejb.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import zw.co.posb.constants.Constant;
import zw.co.posb.ejb.beans.interfaces.Cheque_CashDepositLocal;
import zw.co.posb.ejb.beans.interfaces.Cheque_CashDepositRemote;
import zw.co.posb.entity.AdGbBranch;
import zw.co.posb.entity.AdGbCrncy;
import zw.co.posb.entity.AdGbRsm;
import zw.co.posb.entity.DpHistory;
import zw.co.posb.entity.ReversalsTb;
import zw.co.posb.entity.StanchartTb;
import zw.co.posb.entity.TlItemCapture;
import zw.co.posb.entity.TlJournal;
import zw.co.posb.utility.Utility;

@Stateless
public class Cheque_CashDeposit implements Cheque_CashDepositRemote, Cheque_CashDepositLocal {
	
	public Cheque_CashDeposit() {
		
	}
	
	@PersistenceContext(unitName = "SCBSystemEJB")
	private EntityManager em;

	@Override
	public List<TlJournal> getAllCashDeposits() {
		return em.createNamedQuery("TlJournal.findAll", TlJournal.class).getResultList();
	}

	@Override
	public List<AdGbBranch> getAllPOSBBranches() {
		return em.createNamedQuery("AdGbBranch.findAll", AdGbBranch.class).getResultList();
	}

	@Override
	public List<AdGbCrncy> getCurrency() {
		return em.createNamedQuery("AdGbCrncy.findAll", AdGbCrncy.class).getResultList();
	}

	@Override
	public List<StanchartTb> getSCBAccounts() {
			return em.createNamedQuery("StanChart.findAll", StanchartTb.class).getResultList();
	}

	@Override
	public List<TlItemCapture> getChequeDetails() {
		return em.createNamedQuery("TlItemCapture.findAll", TlItemCapture.class).getResultList();
	}
	
	@Override
	public List<AdGbRsm> getTrans_Teller() {
		return em.createNamedQuery("AdGbRsm.findAll", AdGbRsm.class).getResultList();
	}
	
	@Override
	public List<TlJournal> getAllCashDepositsWithNoDescription() {
		return em.createNamedQuery("TlJournal.findAllNULLDesc", TlJournal.class).getResultList();
	}
	
	@Override
	public List<TlJournal> getAllCashDeposits_Reversed() {
		return em.createNamedQuery("TlJournal.findAllReveredCashDeposits", TlJournal.class).getResultList();
	}
	
	@Override
	public TlJournal getAccount(String description) {
		return em.find(TlJournal.class, description);
	}
	
	@Override
	public List<DpHistory> getReturnedCheqDeposits() {
		return em.createNamedQuery("DpHistory.findAllChqReturns", DpHistory.class).getResultList();
	}
	
	@Override
	public List<DpHistory> getReturnedCheqDepositsNullDescription() {
		return em.createNamedQuery("DpHistory.findAllChqReturnsWithNullDescription", DpHistory.class).getResultList();
	}
	
	@Override
	public List<DpHistory> getReversedCashDepositsReversals() {
		return em.createNamedQuery("DpHistory.findAllCashDepositReversals", DpHistory.class).getResultList();
	}
	
	@Override
	public List<DpHistory> getCashDepositsRectified() {
		return em.createNamedQuery("DpHistory.findAllCashDepositsRectified", DpHistory.class).getResultList();
	}
	
	@Override
	public List<DpHistory> getCashChQReversalsNullDescription() {
		return em.createNamedQuery("DpHistory.findAllCashDepositReversalsNullDescription", DpHistory.class).getResultList();
	}
	
	@Override
	public List<DpHistory> getCashDepositsRectifiedNullDescription() {
		return em.createNamedQuery("DpHistory.findAllCashDepositsRectifiedNullDescription", DpHistory.class).getResultList();
	}
	
	@Override
	public List<ReversalsTb> getAllCashChequeReversals() {
		return em.createNamedQuery("ReversalsTb.findAll", ReversalsTb.class).getResultList();
	}
	
	@Override
	public ReversalsTb create(ReversalsTb reversals) {
		em.persist(reversals);
		return reversals;
	}
	
	//Generate a Unique Reference Number if Reversal = 2
	@Override
	public void reversals() {
		//System.out.println("Insert into reversals_tb");
		List<TlJournal> fileList = getAllCashDeposits_Reversed();
		
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String todaysDate = sdf.format(currentDate.getTime());
		//Picking todays date
		//System.out.println("Todays Date " + todaysDate);
		
		for(TlJournal a : fileList){
			if(todaysDate.equals(sdf.format(a.getCreateDt()))){
				BigDecimal ptid = a.getPtid();
				//String p_tid = String.valueOf(ptid);
				ReversalsTb rev = (ReversalsTb) em.find(ReversalsTb.class, ptid);
				if(rev != null){
						
				}else {
					ReversalsTb rt = new ReversalsTb();
					rt.setPtid(a.getPtid());
					rt.setRefno(Utility.generateRef());
					rt.setDtime(new Timestamp(System.currentTimeMillis()));
					create(rt);
					System.out.println(rt.getRefno() + "|" + rt.getDtime() + "|" + rt.getPtid() + "--- CREATED");
					break;
				}
			}
		}
	}
	
	@Schedule(hour = "8-23", minute = "*/1", dayOfWeek = "Mon-Sat")
	public void runIfReversalCodeChanges(){
		System.out.println("Creating unique reference number");
		reversals();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fetcherIntraday() {
		
		List<ReversalsTb> reversedTrans = getAllCashChequeReversals();
	
		List<DpHistory> reversed_cashAccProc = getReversedCashDepositsReversals();
		List<DpHistory> rectified_cashDepAccProc = getCashDepositsRectified();
		List<DpHistory> reversed_chqcashTransNullDesc = getCashChQReversalsNullDescription();
		List<DpHistory> rectified_cashDepAccProcNullDesc = getCashDepositsRectifiedNullDescription();
		
		List<TlJournal> reversed_cash = getAllCashDeposits_Reversed();
		List<AdGbRsm> teller_userName = getTrans_Teller();
		List<TlJournal> fileList = getAllCashDeposits();
		List<TlJournal> fileListError = getAllCashDepositsWithNoDescription();
		List<AdGbBranch> branches = getAllPOSBBranches();
		List<AdGbCrncy> currency = getCurrency();
		List<TlItemCapture> chq_details = getChequeDetails();
		List<DpHistory> returned_chqs = getReturnedCheqDeposits();
		List<DpHistory> returned_chqsNullDesc = getReturnedCheqDepositsNullDescription();
		
		Calendar currentDate = Calendar.getInstance();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		
		String INTRADAY_DEPOSIT = "IDAY_" + DATE_FORMAT.format(currentDate.getTime()) + "_SEQ.txt";
		//String LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		String WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String todaysDate = sdf.format(currentDate.getTime());
		//Picking todays date
		System.out.println("Todays Date " + todaysDate);
		
		File file = new File(Constant.FILEPATH_WINDOWS_INTRADAY_DEPOSIT + INTRADAY_DEPOSIT);
		//File file = new File(Constant.FILEPATH_LINUX_INTRADAY_DEPOSIT + INTRADAY_DEPOSIT);
		String absolutePath = file.getAbsolutePath();
		PrintWriter prWr = null;
		
		//File file_rev = new File(Constant.FILE_PATH_LINUX_REV_INTRADAY_DEPOSIT + INTRADAY_DEPOSIT);
		File file_rev = new File(Constant.FILE_PATH_WINDOWS_REV_INTRADAY_DEPOSIT + INTRADAY_DEPOSIT);
		String rev_absolutePath = file_rev.getAbsolutePath();
		PrintWriter prWr_chqs_returned = null;
		
		
		File file_err = new File(Constant.FILEPATH_WINDOWS_INVALID_ACCOUNTS + WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS);
		//File file_err = new File(Constant.FILEPATH_LINUX_INVALID_ACCOUNTS + LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS);
		String absolutePath_err = file_err.getAbsolutePath();
		PrintWriter prWr_err = null;
		
		try{
			prWr = new PrintWriter(absolutePath);
			prWr.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			prWr_err = new PrintWriter(absolutePath_err);
			prWr_err.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Teller UserName" + "|" +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			prWr_chqs_returned = new PrintWriter(rev_absolutePath);
			prWr_chqs_returned.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");		
			
			/*
			 *	Blank SCB on cash deposit
			 */
			for(TlJournal err : fileListError){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					try{
						Calendar currentDateTime = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						//Get the current time
						Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
						currentDateTime.setTime(currentTime);
						//System.out.println("Current Time - " + currentDateTime.getTime());
						Date endTime = currentDateTime.getTime();
								
						Calendar currentDateTime1 = Calendar.getInstance();
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
						//Get the time 30 minutes less
						Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
						currentDateTime1.setTime(currentTime1);
						currentDateTime1.add(Calendar.MINUTE, -30);
						//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
						Date startTime = currentDateTime1.getTime();
								
						Calendar currentDateTime2 = Calendar.getInstance();
						Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(err.getPostingDtTm()));
						currentDateTime2.setTime(db_tljournalTime);
						//System.out.println("Database current Time - " + currentDateTime2.getTime());
								
						Date db_time = currentDateTime2.getTime();
						if(db_time.after(startTime) && db_time.before(endTime)){
							if(err.getTlTranCode().equals("103")){
								for(AdGbRsm teller : teller_userName){
									if(err.getEmplId() == teller.getEmployeeId()){
										String tellerusername = teller.getUserName();
										for(AdGbBranch br : branches){
											if(err.getBranchNo() == br.getBranchNo()){
												String branchName = br.getName1();
												for(AdGbCrncy c: currency){
													if(err.getExchCrncyId() == c.getCrncyId()){
														String currencyCode = c.getIsoCode();
														String status = "00";
														if(err.getExchCrncyId() == 5){
															prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getDescription() + "|" + err.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", err.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|"  + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
															System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getDescription() + "|" + err.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", err.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
														}else {
															
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}
			
			//Blank SCB account on cheque deposit
			for (TlItemCapture t : chq_details){
				for(TlJournal err : fileListError){
					for(AdGbRsm teller : teller_userName){
						if(err.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								for(AdGbCrncy c : currency){
									if(t.getCrncyId() == c.getCrncyId()){
										String currencyCode = c.getIsoCode();
										if(t.getBranchNo() == br.getBranchNo()){
											if(err.getPtid().compareTo(t.getJournalPtid())== 0){
												try{
													Calendar currentDateTime = Calendar.getInstance();
													DateFormat dateFormat = new SimpleDateFormat("HH:mm");
													//Get the current time
													Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
													currentDateTime.setTime(currentTime);
													//System.out.println("Current Time - " + currentDateTime.getTime());
													Date endTime = currentDateTime.getTime();
															
													Calendar currentDateTime1 = Calendar.getInstance();
													DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
													//Get the time 30 minutes less
													Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
													currentDateTime1.setTime(currentTime1);
													currentDateTime1.add(Calendar.MINUTE, -30);
													//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
													Date startTime = currentDateTime1.getTime();
															
													Calendar currentDateTime2 = Calendar.getInstance();
													Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(t.getCreateDt()));
													currentDateTime2.setTime(db_tljournalTime);
													//System.out.println("Database current Time - " + currentDateTime2.getTime());
															
													Date db_time = currentDateTime2.getTime();
													if(db_time.after(startTime) && db_time.before(endTime)){
														if(todaysDate.equals(sdf.format(t.getEffectiveDt()))){
															if(err.getTlTranCode().equals("105")){
																String status = "00";
																Calendar cal = Calendar.getInstance();
																for(int i=3 ; i<=3 ; i++){
																	cal.setTime(err.getValueDt());
																	for(int processing_days=i ; processing_days!=0 ; processing_days--) {
																		cal.add(Calendar.DATE, 1);
																		if(cal.get(Calendar.DAY_OF_WEEK)==7) {
																			cal.add(Calendar.DATE, 2);
																		}
																	}
																}
																System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + err.getDescription() + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");	
																prWr_err.println(t.getPtid() + "|" + t.getAcctNo() + "|" + err.getDescription() + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
															}
														}
													}
												}catch(ParseException e){
													e.printStackTrace();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			//Blank Description Field on CHQ and Cash Reversals using 157
			for(DpHistory err : reversed_chqcashTransNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					try{
						Calendar currentDateTime = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						//Get the current time
						Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
						currentDateTime.setTime(currentTime);
						//System.out.println("Current Time - " + currentDateTime.getTime());
						Date endTime = currentDateTime.getTime();
								
						Calendar currentDateTime1 = Calendar.getInstance();
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
						//Get the time 30 minutes less
						Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
						currentDateTime1.setTime(currentTime1);
						currentDateTime1.add(Calendar.MINUTE, -30);
						//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
						Date startTime = currentDateTime1.getTime();
								
						Calendar currentDateTime2 = Calendar.getInstance();
						Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(err.getPostingDtTm()));
						currentDateTime2.setTime(db_tljournalTime);
						//System.out.println("Database current Time - " + currentDateTime2.getTime());
						
						Date db_time = currentDateTime2.getTime();
						if(db_time.after(startTime) && db_time.before(endTime)){
							for(AdGbRsm teller : teller_userName){
								if(err.getEmplId() == teller.getEmployeeId()){
									String tellerusername = teller.getName();
									for(AdGbBranch br : branches){
										if(err.getOrigBranchNo() == br.getBranchNo()){
											String branchName = br.getName1();
											String currencyCode = "USD";
											String status = "03";
											if(err.getExchCrncyId() == 5){
												System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
												prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
											}
										}
									}
								}
							}
						}
					}catch(ParseException e){
						e.printStackTrace();
					}	
				}
			}
			
			//Blank Description Field on CASH DEPOSITS using 117 on Account Processing
			for(DpHistory err : rectified_cashDepAccProcNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					try{
						Calendar currentDateTime = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						//Get the current time
						Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
						currentDateTime.setTime(currentTime);
						//System.out.println("Current Time - " + currentDateTime.getTime());
						Date endTime = currentDateTime.getTime();
								
						Calendar currentDateTime1 = Calendar.getInstance();
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
						//Get the time 30 minutes less
						Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
						currentDateTime1.setTime(currentTime1);
						currentDateTime1.add(Calendar.MINUTE, -30);
						//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
						Date startTime = currentDateTime1.getTime();
								
						Calendar currentDateTime2 = Calendar.getInstance();
						Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(err.getPostingDtTm()));
						currentDateTime2.setTime(db_tljournalTime);
						//System.out.println("Database current Time - " + currentDateTime2.getTime());
						
						Date db_time = currentDateTime2.getTime();
						if(db_time.after(startTime) && db_time.before(endTime)){
							for(AdGbRsm teller : teller_userName){
								if(err.getEmplId() == teller.getEmployeeId()){
									String tellerusername = teller.getName();
									for(AdGbBranch br : branches){
										if(err.getOrigBranchNo() == br.getBranchNo()){
											String branchName = br.getName1();
											String currencyCode = "USD";
											String status = "03";
											if(err.getExchCrncyId() == 5){
												System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
												prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR]");
											}
										}
									}
								}
							}
						}
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}
			
			//Blank Description Field on CHEQUE RETURNS using 159
			for(DpHistory err : returned_chqsNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					try{
						Calendar currentDateTime = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						//Get the current time
						Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
						currentDateTime.setTime(currentTime);
						//System.out.println("Current Time - " + currentDateTime.getTime());
						Date endTime = currentDateTime.getTime();
								
						Calendar currentDateTime1 = Calendar.getInstance();
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
						//Get the time 30 minutes less
						Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
						currentDateTime1.setTime(currentTime1);
						currentDateTime1.add(Calendar.MINUTE, -30);
						//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
						Date startTime = currentDateTime1.getTime();
								
						Calendar currentDateTime2 = Calendar.getInstance();
						Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(err.getPostingDtTm()));
						currentDateTime2.setTime(db_tljournalTime);
						//System.out.println("Database current Time - " + currentDateTime2.getTime());
						
						Date db_time = currentDateTime2.getTime();
						if(db_time.after(startTime) && db_time.before(endTime)){
							for(AdGbRsm teller : teller_userName){
								if(err.getEmplId() == teller.getEmployeeId()){
									String tellerusername = teller.getName();
									for(AdGbBranch br : branches){
										if(err.getOrigBranchNo() == br.getBranchNo()){
											String branchName = br.getName1();
											String currencyCode = "USD";
											String status = "03";
											if(err.getExchCrncyId() == 5){
												System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|CHQNUMBER|REASON]");
												prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|CHQNUMBER|REASON]");
											}
										}
									}
								}
							}
						}
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}
			
			//Cheques Captured with Wrong Account or SCB Account not entered on Item Capture
			List<Object[]> results = em.createQuery("SELECT t.ptid, t.effectiveDt, CASE WHEN t.acctNo = '' THEN 'Blank SCBAcc' WHEN t.acctNo <> '100310015095' THEN t.acctNo ELSE '100310015095' END, t.checkNo, t.amount, t.journalPtid, a.description, t.branchNo, br.name1, c.isoCode, a.reference, a.valueDt, a.postingDtTm, tel.name, t.createDt, a.tranAmt FROM TlItemCapture t, TlJournal a, AdGbBranch br, AdGbCrncy c, AdGbRsm tel WHERE t.journalPtid=a.ptid AND t.branchNo=br.branchNo AND t.crncyId=c.crncyId AND t.emplId=tel.employeeId").getResultList();
			
			for(Object[] result : results){
				if(todaysDate.equals(sdf.format(result[1]))){
					try{
						Calendar currentDateTime = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						//Get the current time
						Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
						currentDateTime.setTime(currentTime);
						//System.out.println("Current Time - " + currentDateTime.getTime());
						Date endTime = currentDateTime.getTime();
								
						Calendar currentDateTime1 = Calendar.getInstance();
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
						//Get the time 30 minutes less
						Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
						currentDateTime1.setTime(currentTime1);
						currentDateTime1.add(Calendar.MINUTE, -30);
						//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
						Date startTime = currentDateTime1.getTime();
								
						Calendar currentDateTime2 = Calendar.getInstance();
						Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format((Date)result[14]));
						currentDateTime2.setTime((Date)db_tljournalTime);
						//System.out.println("Database current Time - " + currentDateTime2.getTime());
								
						Date db_time = currentDateTime2.getTime();
						if(db_time.after(startTime) && db_time.before(endTime)){
							Calendar cal = Calendar.getInstance();
							String status = "00";
							for(int i=3 ; i<=3 ; i++){
								cal.setTime((Date) result[11]);
								for(int processing_days=i ; processing_days!=0 ; processing_days--) {
									cal.add(Calendar.DATE, 1);
									if(cal.get(Calendar.DAY_OF_WEEK)==7) {
										cal.add(Calendar.DATE, 2);
									}
								}
							}
							if(result[2].equals("100310015095")){
								//Correct SCB get ignored
							}else {
								System.out.println(result[0] + "|" + result[2] + "|" + result[6] + "|" + result[7] + "|" + result[13] + "|" + result[8]  + "|" + result[3] + "|" + sdf.format(result[1]) + "|" + Constant.CHQ_PAYMENT_TYPE + "|" + String.format("%.2f",result[4]) + "|" + result[9] + "|" + "|" + "|" + result[10] + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(result[11]) + "|" + sdf.format(result[12]) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " [INCORRECT SCB ACCOUNT OR SCBACC NOT CAPTURED ON A CHEQUE DEPOSIT OF " + String.format("%.2f",result[15]) + "]");
								prWr_err.println(result[0] + "|" + result[2] + "|" + result[6] + "|" + result[7] + "|" + result[13] + "|" + result[8]  + "|" + result[3] + "|" + sdf.format(result[1]) + "|" + Constant.CHQ_PAYMENT_TYPE + "|" + String.format("%.2f",result[4]) + "|" + result[9] + "|" + "|" + "|" + result[10] + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(result[11]) + "|" + sdf.format(result[12]) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " [INCORRECT SCB ACCOUNT OR SCBACC NOT CAPTURED ON A CHEQUE DEPOSIT OF " + String.format("%.2f",result[15]) + "]");
							}
						}
						
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}

			//CASH DEPOSIT
			for(TlJournal a : fileList){
				for(AdGbRsm teller : teller_userName){
					if(a.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						if(todaysDate.equals(sdf.format(a.getCreateDt()))){
							try{
								Calendar currentDateTime = Calendar.getInstance();
								DateFormat dateFormat = new SimpleDateFormat("HH:mm");
								//Get the current time
								Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
								currentDateTime.setTime(currentTime);
								//System.out.println("Current Time - " + currentDateTime.getTime());
								Date endTime = currentDateTime.getTime();
										
								Calendar currentDateTime1 = Calendar.getInstance();
								DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
								//Get the time 30 minutes less
								Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
								currentDateTime1.setTime(currentTime1);
								currentDateTime1.add(Calendar.MINUTE, -30);
								//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
								Date startTime = currentDateTime1.getTime();
										
								Calendar currentDateTime2 = Calendar.getInstance();
								Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(a.getPostingDtTm()));
								currentDateTime2.setTime(db_tljournalTime);
								//System.out.println("Database current Time - " + currentDateTime2.getTime());
										
								Date db_time = currentDateTime2.getTime();
								if(db_time.after(startTime) && db_time.before(endTime)){
									String acct_no = (a.getDescription()).replaceAll("\\s+", "");
									StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
									for(AdGbBranch br : branches){
										if(a.getBranchNo() == br.getBranchNo()){
											String branchName = br.getName1();
											for(AdGbCrncy c: currency){
												if(a.getExchCrncyId() == c.getCrncyId()){
													String currencyCode = c.getIsoCode();
													if(a.getTlTranCode().equals("103")){
														String status = "00";
														if(a.getExchCrncyId() == 5){
															if(stanchart_acc != null){
																prWr.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
																System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															}else {
																prWr_err.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");
																System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");	
															}
														}else {
															prWr_err.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");	
														}
													}
												}
											}	
										}
									}
								}
							}catch(ParseException e){
								e.printStackTrace();
							}
						}
					}
				}
			}
			
			//CASH REVERSALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
			for(TlJournal a : reversed_cash){
				for(ReversalsTb r : reversedTrans){
					String formattedDATE = sdf.format(r.getDtime());
					if(todaysDate.equals(formattedDATE)){
						String acct_no = (a.getDescription()).replaceAll("\\s+", "");
						StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
						System.out.println("************************************************ " + todaysDate + " DENNIS HLABANU " + formattedDATE);
						if(a.getPtid().compareTo(r.getPtid()) == 0){
							System.out.println("***************************************** " + a.getPtid() + "::" + r.getPtid());
								try{
									Calendar currentDateTime = Calendar.getInstance();
									DateFormat dateFormat = new SimpleDateFormat("HH:mm");
									//Get the current time
									Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
									currentDateTime.setTime(currentTime);
									System.out.println("********************************************************************Current Time - " + currentDateTime.getTime());
									Date endTime = currentDateTime.getTime();
														
									Calendar currentDateTime1 = Calendar.getInstance();
									DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
									//Get the time 30 minutes less
									Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
									currentDateTime1.setTime(currentTime1);
									currentDateTime1.add(Calendar.MINUTE, -30);
									System.out.println("**********************************************************************Current Time less 30 minutes " + currentDateTime1.getTime());
									Date startTime = currentDateTime1.getTime();
														
									Calendar currentDateTime2 = Calendar.getInstance();
									Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(r.getDtime()));
									currentDateTime2.setTime(db_tljournalTime);
									System.out.println("***********************************************************************Database current Time - " + currentDateTime2.getTime());
														
									Date db_time = currentDateTime2.getTime();
									if(db_time.after(startTime) && db_time.before(endTime)){
										for(AdGbBranch br : branches){
											if(a.getBranchNo() == br.getBranchNo()){
												String branchName = br.getName1();
												for(AdGbCrncy c: currency){
													if(a.getExchCrncyId() == c.getCrncyId()){
														String currencyCode = c.getIsoCode();
														if(a.getTlTranCode().equals("103")){
															//String originalStatus = "00";
															String status = "03";
															String defaultReversalReason = "CASH DEPOSIT REVERSED";
															if(a.getExchCrncyId() == 5){
																if(stanchart_acc != null){
																	System.out.println("CHECKING IF I CAN PICK THE REF NUMBER****************: " + r.getRefno());
																	//prWr.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + originalStatus + "|" + "|" + "|" + "|"  + "|" + "|");
																	//System.out.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + originalStatus + "|" + "|" + "|" + "|"  + "|" + "|");
																	prWr_chqs_returned.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + r.getPtid() +  "|" + defaultReversalReason +  "|" + "|" + "|");
																	System.out.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + r.getPtid() + "|" + defaultReversalReason + "|" + "|" + "|");
																}else {
																	
																}
															}
														}
													}
												}	
											}
										}	
									}else {
													
									}
								}catch(ParseException e){
									e.printStackTrace();
								}
								
						}
					}
					
				}
			}
			
			//CHEQUE REVERSALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
			for(TlJournal a : reversed_cash){
				for (TlItemCapture t : chq_details){
					for(ReversalsTb r : reversedTrans){
						String formattedDATE = sdf.format(r.getDtime());
						if(todaysDate.equals(formattedDATE)){
							String acct_no = (a.getDescription()).replaceAll("\\s+", "");
							StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
							if(a.getPtid().compareTo(t.getJournalPtid())== 0){
								if(a.getPtid().compareTo(r.getPtid()) == 0){
									try{
										Calendar currentDateTime = Calendar.getInstance();
										DateFormat dateFormat = new SimpleDateFormat("HH:mm");
										//Get the current time
										Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
										currentDateTime.setTime(currentTime);
										Date endTime = currentDateTime.getTime();
																
										Calendar currentDateTime1 = Calendar.getInstance();
										DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
										//Get the time 30 minutes less
										Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
										currentDateTime1.setTime(currentTime1);
										currentDateTime1.add(Calendar.MINUTE, -30);
										Date startTime = currentDateTime1.getTime();
																
										Calendar currentDateTime2 = Calendar.getInstance();
										Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(r.getDtime()));
										currentDateTime2.setTime(db_tljournalTime);
																
										Date db_time = currentDateTime2.getTime();
										if(db_time.after(startTime) && db_time.before(endTime)){
											for(AdGbBranch br : branches){
												if(a.getBranchNo() == br.getBranchNo()){
													//String branchName = br.getName1();
													for(AdGbCrncy c: currency){
														if(a.getExchCrncyId() == c.getCrncyId()){
															String currencyCode = c.getIsoCode();
															if(a.getTlTranCode().equals("105")){
																String status = "03";
																String defaultReversalReason = "CHQ DEPOSIT REVERSED";
																if(a.getExchCrncyId() == 5){
																	if(stanchart_acc != null){
																		System.out.println(r.getRefno().concat(t.getPtid().toString())  + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + t.getPtid() + "|" + defaultReversalReason + "|" + "|" + "|" );	
																		prWr_chqs_returned.println(r.getRefno().concat(t.getPtid().toString()) + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT+  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + t.getPtid() + "|" + defaultReversalReason + "|" + "|" +  "|");
																	}else {
																		
																	}		
																}
															}
														}
													}	
												}
											}	
										}else {
															
										}
									}catch(ParseException e){
										e.printStackTrace();
									}
										
								}
							}
						}
					}
				}
			}
			
			//CHEQUE DEPOSIT
			for (TlItemCapture t : chq_details){
				for(TlJournal a : fileList){
					for(AdGbRsm teller : teller_userName){
						if(a.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								for(AdGbCrncy c : currency){
									if(t.getCrncyId() == c.getCrncyId()){
										String currencyCode = c.getIsoCode();
										if(t.getBranchNo() == br.getBranchNo()){
											if(a.getPtid().compareTo(t.getJournalPtid())== 0){
												if(todaysDate.equals(sdf.format(t.getEffectiveDt()))){
													try{
														Calendar currentDateTime = Calendar.getInstance();
														DateFormat dateFormat = new SimpleDateFormat("HH:mm");
														//Get the current time
														Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
														currentDateTime.setTime(currentTime);
														//System.out.println("Current Time - " + currentDateTime.getTime());
														Date endTime = currentDateTime.getTime();
																
														Calendar currentDateTime1 = Calendar.getInstance();
														DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
														//Get the time 30 minutes less
														Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
														currentDateTime1.setTime(currentTime1);
														currentDateTime1.add(Calendar.MINUTE, -30);
														//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
														Date startTime = currentDateTime1.getTime();
																
														Calendar currentDateTime2 = Calendar.getInstance();
														Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(t.getCreateDt()));
														currentDateTime2.setTime(db_tljournalTime);
														//System.out.println("Database current Time - " + currentDateTime2.getTime());
														
														Date db_time = currentDateTime2.getTime();
														if(db_time.after(startTime) && db_time.before(endTime)){
															if(a.getTlTranCode().equals("105")){
																String status = "00";
																String acct_no = a.getDescription().replaceAll("\\s+", "");
																StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
																Calendar cal = Calendar.getInstance();
																for(int i=3 ; i<=3 ; i++){
																	cal.setTime(a.getValueDt());
																	for(int processing_days=i ; processing_days!=0 ; processing_days--) {
																		cal.add(Calendar.DATE, 1);
																		if(cal.get(Calendar.DAY_OF_WEEK)==7) {
																			cal.add(Calendar.DATE, 2);
																		}
																	}
																}
																if(stanchart_acc != null){
																	System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");	
																	prWr.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
																}else {
																	System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": INCORRECT SCB ACCOUNT]");	
																	prWr_err.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");
																}
															}
														}
													}catch(ParseException e){
														e.printStackTrace();
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			
			//Returned Cheque Deposits on Account processing
			for(DpHistory ret : returned_chqs){
				for(AdGbRsm teller : teller_userName){
					if(ret.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						for(AdGbBranch br : branches){
							if(ret.getOrigBranchNo() == br.getBranchNo()){
								String branchName = br.getName1();
								String currencyCode = "USD";
								String status = "02";
								if(todaysDate.equals(sdf.format(ret.getCreateDt()))){
									try{
										Calendar currentDateTime = Calendar.getInstance();
										DateFormat dateFormat = new SimpleDateFormat("HH:mm");
										//Get the current time
										Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
										currentDateTime.setTime(currentTime);
										//System.out.println("Current Time - " + currentDateTime.getTime());
										Date endTime = currentDateTime.getTime();
												
										Calendar currentDateTime1 = Calendar.getInstance();
										DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
										//Get the time 30 minutes less
										Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
										currentDateTime1.setTime(currentTime1);
										currentDateTime1.add(Calendar.MINUTE, -30);
										//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
										Date startTime = currentDateTime1.getTime();
												
										Calendar currentDateTime2 = Calendar.getInstance();
										Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(ret.getPostingDtTm()));
										currentDateTime2.setTime(db_tljournalTime);
										//System.out.println("Database current Time - " + currentDateTime2.getTime());
										
										Date db_time = currentDateTime2.getTime();
										if(db_time.after(startTime) && db_time.before(endTime)){
											if(ret.getDescription().replaceAll("\\s", "").contains("|")){
												String[] splitDesc = ret.getDescription().replaceAll("\\s", "").split("\\|");
												//System.out.println("Number of pipes : " + (splitDesc.length - 1));
												if((splitDesc.length - 1) == 2){
													String scb_acc = splitDesc[0];
													String chq_num = splitDesc[1];
													String reason = splitDesc[2];
													StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, scb_acc);
													if(ret.getExchCrncyId() == 5){
														for(TlItemCapture t : chq_details){
															String chqnumber = Integer.toString(t.getCheckNo());
															if(chq_num.equals(chqnumber)){
																BigDecimal original_ref = t.getPtid();
																if(stanchart_acc != null){
																	System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo()+ "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|");	
																	prWr_chqs_returned.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo() + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|");
																}else {
																	System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|" + " CAPTURED INFORMATION: [" + ret.getDescription() + "] [INCORRECT SCB ACCOUNT]");	
																	prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|" + " CAPTURED INFORMATION: [" + ret.getDescription() + "] [INCORRECT SCB ACCOUNT]");
																}	
															}
														}
													}else {
														//NOT A USD TRANSACTION
													}
												}else {
													System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|" + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");	
													prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|"  + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");
												}
											}else {
												System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|" + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");	
												prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|"  + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");
											}
										}
									}catch(ParseException e){
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
			
			//Reversed CASH and CHEQUE Deposits on Account processing
			for(DpHistory rev : reversed_cashAccProc){
				for(AdGbRsm teller : teller_userName){
					if(rev.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						for(AdGbBranch br : branches){
							if(rev.getOrigBranchNo() == br.getBranchNo()){
								String branchName = br.getName1();
								String currencyCode = "USD";
								String status = "03";
								if(todaysDate.equals(sdf.format(rev.getCreateDt()))){
									try{
										Calendar currentDateTime = Calendar.getInstance();
										DateFormat dateFormat = new SimpleDateFormat("HH:mm");
										//Get the current time
										Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
										currentDateTime.setTime(currentTime);
										System.out.println("Current Time - " + currentDateTime.getTime());
										Date endTime = currentDateTime.getTime();
												
										Calendar currentDateTime1 = Calendar.getInstance();
										DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
										//Get the time 30 minutes less
										Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
										currentDateTime1.setTime(currentTime1);
										currentDateTime1.add(Calendar.MINUTE, -30);
										System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
										Date startTime = currentDateTime1.getTime();
												
										Calendar currentDateTime2 = Calendar.getInstance();
										Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(rev.getPostingDtTm()));
										currentDateTime2.setTime(db_tljournalTime);
										System.out.println("Database current Time - " + currentDateTime2.getTime());
										
										Date db_time = currentDateTime2.getTime();
										if(db_time.after(startTime) && db_time.before(endTime)){
											if(rev.getDescription().replaceAll("\\s", "").contains("|")){
												String[] splitDesc = rev.getDescription().replaceAll("\\s", "").split("\\|");
												System.out.println("Number of pipes : " + (splitDesc.length - 1));
												if((splitDesc.length - 1) == 2){
													String scb_acc = splitDesc[0];
													String referencePtid = splitDesc[1];
													String reason = splitDesc[2];
													
													StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, scb_acc);
													BigDecimal ref_num = new BigDecimal(referencePtid);
													if(rev.getExchCrncyId() == 5){
														for(TlJournal tl : fileList){
															if(ref_num.compareTo(tl.getPtid()) == 0 && tl.getTlTranCode().equals("103")){
																if(stanchart_acc != null){
																	System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo()+ "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|");	
																	prWr_chqs_returned.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|");
																}else {
																	System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|:" + scb_acc + " -[INCORRECT SCB ACCOUNT]");	
																	prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|:" + scb_acc + " -[INCORRECT SCB ACCOUNT]");
																}	
															}
														}
														for(TlItemCapture t : chq_details){
															if(ref_num.compareTo(t.getJournalPtid())== 0){
																if(stanchart_acc != null){
																	System.out.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|"  + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + t.getPtid() + "|" + reason + "|" + "|" + "|");	
																	prWr_chqs_returned.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + t.getPtid() + "|" + reason + "|" + "|" + "|");
																}else {
																	System.out.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|"  + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + reason + "|" + "|" + "| :" + scb_acc + " -[INCORRECT SCB ACCOUNT]");	
																	prWr_err.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + reason + "|" + "|" + "| :" + scb_acc + " -[INCORRECT SCB ACCOUNT]");
																}
															}else {
																
															}	
														}
													}else {
														//NOT A USD TRANSACTION
													}
												}else {
													System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
													prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
												}
											}else {
												System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
												prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
											}
										}
									}catch(ParseException e){
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
	
			//Rectified Cash Deposits on Account processing USING 117
			for(DpHistory cash_dep : rectified_cashDepAccProc){
				for(AdGbRsm teller : teller_userName){
					if(cash_dep.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						if(todaysDate.equals(sdf.format(cash_dep.getCreateDt()))){
							try{
								Calendar currentDateTime = Calendar.getInstance();
								DateFormat dateFormat = new SimpleDateFormat("HH:mm");
								//Get the current time
								Date currentTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(currentDateTime.getTime()));
								currentDateTime.setTime(currentTime);
								//System.out.println("Current Time - " + currentDateTime.getTime());
								Date endTime = currentDateTime.getTime();
										
								Calendar currentDateTime1 = Calendar.getInstance();
								DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
								//Get the time 30 minutes less
								Date currentTime1 = new SimpleDateFormat("HH:mm").parse(dateFormat1.format(currentDateTime1.getTime()));
								currentDateTime1.setTime(currentTime1);
								currentDateTime1.add(Calendar.MINUTE, -30);
								//System.out.println("Current Time less 30 minutes " + currentDateTime1.getTime());
								Date startTime = currentDateTime1.getTime();
										
								Calendar currentDateTime2 = Calendar.getInstance();
								Date db_tljournalTime = new SimpleDateFormat("HH:mm").parse(dateFormat.format(cash_dep.getPostingDtTm()));
								currentDateTime2.setTime(db_tljournalTime);
								//System.out.println("Database current Time - " + currentDateTime2.getTime());
								
								Date db_time = currentDateTime2.getTime();
								if(db_time.after(startTime) && db_time.before(endTime)){
									for(AdGbBranch br : branches){
										if(cash_dep.getOrigBranchNo() == br.getBranchNo()){
											String branchName = br.getName1();
											String currencyCode = "USD";
											String status = "00";
											if(cash_dep.getDescription().replaceAll("\\s", "").contains("|")){
												String[] splitDesc = cash_dep.getDescription().replaceAll("\\s", "").split("\\|");
												//System.out.println("Number of pipes : " + (splitDesc.length - 1));
												if((splitDesc.length - 1) == 1){
													String acct_no = splitDesc[0];
													String depositorname = splitDesc[1];
													StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
													if(cash_dep.getExchCrncyId() == 5){
														if(stanchart_acc != null){
															prWr.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
														}else {
															prWr_err.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] [INCORRECT SCB ACCOUNT]");
															System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|" + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] [INCORRECT SCB ACCOUNT]");
														}
													}
												}else {
													//NOT A USD ACCOUNT
												}
											}else {
												prWr_err.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + "|" + cash_dep.getOrigBranchNo() + "|"  + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
												System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|"  + "|" + cash_dep.getOrigBranchNo() + "|" + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
											}
										}
									}
								}
							}catch(ParseException e){
								e.printStackTrace();
							}
						}
					}
				}
			}	
		
			System.out.println("Intraday Deposits " + absolutePath);
			System.out.println("Invalid Intraday Deposits " + absolutePath_err);
			System.out.println("Intraday REV " + rev_absolutePath);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}finally{
			prWr.flush();
			prWr.close();
			
			prWr_err.flush();
			prWr_err.close();
			
			prWr_chqs_returned.flush();
			prWr_chqs_returned.close();
		}
		testSendEmail();
	}
	
	@Schedule(hour = "8-16", minute = "*/30", dayOfWeek = "Mon-Sat")
    public void someServerTask(){
		System.out.println("File generated every 30 minutes");
		fetcherIntraday();			
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void fetcherEOD() {
		
		List<ReversalsTb> reversedTrans = getAllCashChequeReversals();
		
		List<DpHistory> reversed_cashAccProc = getReversedCashDepositsReversals();
		List<DpHistory> rectified_cashDepAccProc = getCashDepositsRectified();
		List<DpHistory> reversed_chqcashTransNullDesc = getCashChQReversalsNullDescription();
		List<DpHistory> rectified_cashDepAccProcNullDesc = getCashDepositsRectifiedNullDescription();
		
		List<TlJournal> reversed_cash = getAllCashDeposits_Reversed();
		List<AdGbRsm> teller_userName = getTrans_Teller();
		
		List<TlJournal> fileList = getAllCashDeposits();
		List<TlJournal> fileListError = getAllCashDepositsWithNoDescription();
		List<AdGbBranch> branches = getAllPOSBBranches();
		List<AdGbCrncy> currency = getCurrency();
		List<TlItemCapture> chq_details = getChequeDetails();
		List<DpHistory> returned_chqs = getReturnedCheqDeposits();
		List<DpHistory> returned_chqsNullDesc = getReturnedCheqDepositsNullDescription();
		
		Calendar currentDate = Calendar.getInstance();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		String EOD_DEPOSIT = "EOD_" + DATE_FORMAT.format(currentDate.getTime()) + "_SEQ.txt";
		//String LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		String WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String todaysDate = sdf.format(currentDate.getTime());
		//Picking todays date
		System.out.println("Todays Date " + todaysDate);
		
		//SCB DAILY BACKUPS
		int year = currentDate.get(Calendar.YEAR);
		//String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		int month = currentDate.get(Calendar.MONTH);
		
		//WINDOWS
		File bk_dir_win = new File(Constant.WINDOWS_SCB_DAILY_BACKUP + year + "\\" + Utility.getMonth(month) + "\\" + todaysDate.substring(0, 2) + "\\" + "zw_posbeodcsv");
		File bk_dir_reversals_win = new File(Constant.WINDOWS_SCB_DAILY_BACKUP + year + "\\" + Utility.getMonth(month) + "\\" + todaysDate.substring(0, 2) + "\\" + "zw_posbeodcsv_liq");
		if(!bk_dir_win.exists()){
			bk_dir_win.mkdirs();
		}
		if(!bk_dir_reversals_win.exists()){
			bk_dir_reversals_win.mkdirs();
		}
		
		//LINUX
		/*File bk_dir = new File(Constant.LINUX_SCB_DAILY_BACKUP + year + "/" + Utility.getMonth(month) + "/" + todaysDate.substring(0, 2) + "/" + "zw_posbeodcsv");
		File bk_dir_reversals = new File(Constant.LINUX_SCB_DAILY_BACKUP + year + "/" + Utility.getMonth(month) + "/" + todaysDate.substring(0, 2) + "/" + "zw_posbeodcsv_liq");
		if(!bk_dir.exists()){
			bk_dir.mkdirs();
		}
		if(!bk_dir_reversals.exists()){
			bk_dir_reversals.mkdirs();
		}*/
		
		String monthInWords = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		//File readingthePath = new File(Constant.LINUX_SCB_DAILY_BACKUP + year + "/" + Utility.getMonth(month) + "/" + todaysDate.substring(0, 2));
		File readingthePath = new File(Constant.WINDOWS_SCB_DAILY_BACKUP + year + "\\" + Utility.getMonth(month) + "\\" + todaysDate.substring(0, 2));
		String path_name = readingthePath.getAbsolutePath();
		//System.out.println("Backup File Path " + path_name);
		//System.out.println("GRABBING YEAR " + path_name.substring(36, 40));
		//System.out.println("GRABBING MONTH " + path_name.substring(41,46));
		//System.out.println("GRABBING DAY " + path_name.substring(47,49));
		
		//if(path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2))){
			//System.out.println(path_name.substring(path_name.length() -2) + "#######" + todaysDate.substring(0, 2));
		//}
		
		//File bk_eod = new File(Constant.LINUX_SCB_DAILY_BACKUP + year + "/" + Utility.getMonth(month) + "/" + todaysDate.substring(0, 2) + "/" + "zw_posbeodcsv" + "/" + EOD_DEPOSIT);
		File bk_eod = new File(Constant.WINDOWS_SCB_DAILY_BACKUP + year + "\\" + Utility.getMonth(month) + "\\" + todaysDate.substring(0, 2) + "\\" + "zw_posbeodcsv" + "\\" + EOD_DEPOSIT);
		String backup_eod_path = bk_eod.getAbsolutePath();
		PrintWriter pwr_eod = null;
				
		//File bk_eod_reversals = new File(Constant.LINUX_SCB_DAILY_BACKUP + year + "/" + Utility.getMonth(month) + "/" + todaysDate.substring(0, 2) + "/" + "zw_posbeodcsv_liq" + "/" + EOD_DEPOSIT);
		File bk_eod_reversals = new File(Constant.WINDOWS_SCB_DAILY_BACKUP + year + "\\" + Utility.getMonth(month) + "\\" + todaysDate.substring(0, 2) + "\\" + "zw_posbeodcsv_liq" + "\\" + EOD_DEPOSIT);
		String backup_eod_path_reversals = bk_eod_reversals.getAbsolutePath();
		PrintWriter pwr_eod_reversals = null;
				
	
		File file = new File(Constant.FILEPATH_WINDOWS_EOD_DEPOSIT + EOD_DEPOSIT);
		//File file = new File(Constant.FILEPATH_LINUX_EOD_DEPOSIT + EOD_DEPOSIT);
		String absolutePath = file.getAbsolutePath();
		PrintWriter prWr = null;
		
		//File file_rev = new File(Constant.FILE_PATH_LINUX_REV_EOD_DEPOSIT+ EOD_DEPOSIT);
		File file_rev = new File(Constant.FILE_PATH_WINDOWS_REV_EOD_DEPOSIT + EOD_DEPOSIT);
		String rev_absolutePath = file_rev.getAbsolutePath();
		PrintWriter prWr_chqs_returned = null;
				
		File file_err = new File(Constant.FILEPATH_WINDOWS_INVALID_ACCOUNTS + WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS);
		//File file_err = new File(Constant.FILEPATH_LINUX_INVALID_ACCOUNTS + LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS);
		String absolutePath_err = file_err.getAbsolutePath();
		PrintWriter prWr_err = null;
		
		/*LINUX EDITION
		 * if((path_name.substring(36, 40).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(41, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
		 * WINDOWS EDITION
		 * if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
		 */
		
		try{
			prWr = new PrintWriter(absolutePath);
			prWr.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			prWr_err = new PrintWriter(absolutePath_err);
			prWr_err.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Teller UserName" + "|" +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			prWr_chqs_returned = new PrintWriter(rev_absolutePath);
			prWr_chqs_returned.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");	
			
			pwr_eod = new PrintWriter(backup_eod_path);
			pwr_eod.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			pwr_eod_reversals = new PrintWriter(backup_eod_path_reversals);
			pwr_eod_reversals.println("Unique Reference Number" + "|" + 
					"Customer Account Number" + "|" +
					"SCB Account Number" + "|" + 
					"Branch Code" + "|"  +
					"Branch Name" + "|" + 
					"Cheque No" + "|"  + 
					"Cheque Date" + "|"  + 
					"Payment Type" + "|" + 
					"Deposit Slip Amount" + "|" + 
					"Currency Code" + "|" + 
					"Depositor Code" + "|" + 
					"Depositor Name" + "|" + 
					"Reference 1" + "|" + 
					"Reference 2" + "|" + 
					"Deposit Slip Number" + "|" + 
					"Liquidation Date" + "|" + 
					"Fund Transfer Date" + "|" + 
					"Batch Date " + "|" +
					"Status" + "|" + 
					"Filler Field 1" + "|" + 
					"Filler Field 2"+ "|" + 
					"Filler Field 3" + "|" + 
					"Filler Field 4" + "|" + 
					"Filler Field 5");
			
			//Blank SCB on cash deposit
			for(TlJournal err : fileListError){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					if(err.getTlTranCode().equals("103")){
						for(AdGbRsm teller : teller_userName){
							if(err.getEmplId() == teller.getEmployeeId()){
								String tellerusername = teller.getName();
								for(AdGbBranch br : branches){
									if(err.getBranchNo() == br.getBranchNo()){
										String branchName = br.getName1();
										for(AdGbCrncy c: currency){
											if(err.getExchCrncyId() == c.getCrncyId()){
												String currencyCode = c.getIsoCode();
												String status = "00";
												if(err.getExchCrncyId() == 5){
													prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getDescription() + "|" + err.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", err.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|"  + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
													System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getDescription() + "|" + err.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", err.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
												}else {
													
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			//Blank SCB account on cheque deposit
			for (TlItemCapture t : chq_details){
				for(TlJournal err : fileListError){
					for(AdGbRsm teller : teller_userName){
						if(err.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								for(AdGbCrncy c : currency){
									if(t.getCrncyId() == c.getCrncyId()){
										String currencyCode = c.getIsoCode();
										if(t.getBranchNo() == br.getBranchNo()){
											if(err.getPtid().compareTo(t.getJournalPtid())== 0){
												if(todaysDate.equals(sdf.format(t.getEffectiveDt()))){
													if(err.getTlTranCode().equals("105")){
														String status = "00";
														Calendar cal = Calendar.getInstance();
														for(int i=3 ; i<=3 ; i++){
															cal.setTime(err.getValueDt());
															for(int processing_days=i ; processing_days!=0 ; processing_days--) {
																cal.add(Calendar.DATE, 1);
																if(cal.get(Calendar.DAY_OF_WEEK)==7) {
																	cal.add(Calendar.DATE, 2);
																}
															}
														}
														System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + err.getDescription() + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");	
														prWr_err.println(t.getPtid() + "|" + t.getAcctNo() + "|" + err.getDescription() + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + err.getReference() + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(err.getValueDt()) + "|" + sdf.format(err.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [SCB ACCOUNT NOT CAPTURED]");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			//Blank Description Field on CHQ and Cash Reversals using 157
			for(DpHistory err : reversed_chqcashTransNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					for(AdGbRsm teller : teller_userName){
						if(err.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								if(err.getOrigBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
									String currencyCode = "USD";
									String status = "03";
									if(err.getExchCrncyId() == 5){
										System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
										prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
									}
								}
							}
						}
					}
				}
			}
			
			//Blank Description Field on CASH DEPOSITS using 117 on Account Processing
			for(DpHistory err : rectified_cashDepAccProcNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					for(AdGbRsm teller : teller_userName){
						if(err.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								if(err.getOrigBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
									String currencyCode = "USD";
									String status = "03";
									if(err.getExchCrncyId() == 5){
										System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
										prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR]");
									}
								}
							}
						}
					}
				}
			}
			
			//Blank Description Field on CHEQUE RETURNS using 159
			for(DpHistory err : returned_chqsNullDesc){
				if(todaysDate.equals(sdf.format(err.getCreateDt()))){
					for(AdGbRsm teller : teller_userName){
						if(err.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								if(err.getOrigBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
									String currencyCode = "USD";
									String status = "03";
									if(err.getExchCrncyId() == 5){
										System.out.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|CHQNUMBER|REASON]");
										prWr_err.println(err.getPtid() + "|" + err.getAcctNo() + "|" + err.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", err.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + sdf.format(err.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + err.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|CHQNUMBER|REASON]");
									}
								}
							}
						}
					}
				}
			}
			
			//Cheques Captured with Wrong Account or SCB Account not entered on Item Capture
			List<Object[]> results = em.createQuery("SELECT t.ptid, t.effectiveDt, CASE WHEN t.acctNo = '' THEN 'Blank SCBAcc' WHEN t.acctNo <> '100310015095' THEN t.acctNo ELSE '100310015095' END, t.checkNo, t.amount, t.journalPtid, a.description, t.branchNo, br.name1, c.isoCode, a.reference, a.valueDt, a.postingDtTm, tel.name, a.tranAmt FROM TlItemCapture t, TlJournal a, AdGbBranch br, AdGbCrncy c, AdGbRsm tel WHERE t.journalPtid=a.ptid AND t.branchNo=br.branchNo AND t.crncyId=c.crncyId AND t.emplId=tel.employeeId").getResultList();
			
			for(Object[] result : results){
				if(todaysDate.equals(sdf.format(result[1]))){
					Calendar cal = Calendar.getInstance();
					String status = "00";
					for(int i=3 ; i<=3 ; i++){
						cal.setTime((Date) result[11]);
						for(int processing_days=i ; processing_days!=0 ; processing_days--) {
							cal.add(Calendar.DATE, 1);
							if(cal.get(Calendar.DAY_OF_WEEK)==7) {
								cal.add(Calendar.DATE, 2);
							}
						}
					}
					if(result[2].equals("100310015095")){
						
					}else {
						System.out.println(result[0] + "|" + result[2] + "|" + result[6] + "|" + result[7] + "|" + result[13] + "|" + result[8]  + "|" + result[3] + "|" + sdf.format(result[1]) + "|" + Constant.CHQ_PAYMENT_TYPE + "|" + String.format("%.2f",result[4]) + "|" + result[9] + "|" + "|" + "|" + result[10] + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(result[11]) + "|" + sdf.format(result[12]) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " [INCORRECT SCB ACCOUNT OR SCBACC NOT CAPTURED ON A CHEQUE DEPOSIT OF " + String.format("%.2f",result[14]) + "]");
						prWr_err.println(result[0] + "|" + result[2] + "|" + result[6] + "|" + result[7] + "|" + result[13] + "|" + result[8]  + "|" + result[3] + "|" + sdf.format(result[1]) + "|" + Constant.CHQ_PAYMENT_TYPE + "|" + String.format("%.2f",result[4]) + "|" + result[9] + "|" + "|" + "|" + result[10] + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(result[11]) + "|" + sdf.format(result[12]) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " [INCORRECT SCB ACCOUNT OR SCBACC NOT CAPTURED ON A CHEQUE DEPOSIT OF " + String.format("%.2f",result[14]) + "]");
					}
				}
			}
			
			//CASH DEPOSIT
			for(TlJournal a : fileList){
				for(AdGbRsm teller : teller_userName){
					if(a.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						if(todaysDate.equals(sdf.format(a.getCreateDt()))){
							String acct_no = (a.getDescription()).replaceAll("\\s+", "");
							StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
							for(AdGbBranch br : branches){
								if(a.getBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
									for(AdGbCrncy c: currency){
										if(a.getExchCrncyId() == c.getCrncyId()){
											String currencyCode = c.getIsoCode();
											if(a.getTlTranCode().equals("103")){
												String status = "00";
												if(a.getExchCrncyId() == 5){
													if(stanchart_acc != null){
														prWr.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
														System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
														if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
															pwr_eod.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
														}
													}else {
														prWr_err.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");
														System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");	
													}
												}else {
													prWr_err.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|" + tellerusername + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
													System.out.println(a.getPtid() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|" + teller_userName + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");	
												}
											}
										}
									}	
								}
							}	
						}
					}
				}
			}
			
			//CASH REVERSALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
			for(TlJournal a : reversed_cash){
				String acct_no = (a.getDescription()).replaceAll("\\s+", "");
				StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
				for(ReversalsTb r : reversedTrans){
					String formattedDATE = sdf.format(r.getDtime());
					if(todaysDate.equals(formattedDATE)){
						if(a.getPtid().compareTo(r.getPtid()) == 0){
							//System.out.println("***************************************** " + a.getPtid() + "::" + r.getPtid());
							for(AdGbBranch br : branches){
								if(a.getBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
										for(AdGbCrncy c: currency){
											if(a.getExchCrncyId() == c.getCrncyId()){
												String currencyCode = c.getIsoCode();
												if(a.getTlTranCode().equals("103")){
													//String originalStatus = "00";
													String status = "03";
													String defaultReversalReason = "CASH DEPOSIT REVERSED";
													if(a.getExchCrncyId() == 5){
														if(stanchart_acc != null){
															System.out.println("CHECKING IF I CAN PICK THE REF NUMBER****************: " + r.getRefno());
															//prWr.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + originalStatus + "|" + "|" + "|" + "|"  + "|" + "|");
															//System.out.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + originalStatus + "|" + "|" + "|" + "|"  + "|" + "|");
															prWr_chqs_returned.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + r.getPtid() +  "|" + defaultReversalReason +  "|" + "|" + "|");
															if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
																pwr_eod_reversals.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|"  + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + r.getPtid() +  "|" + defaultReversalReason +  "|" + "|" + "|");
															}
															System.out.println(r.getRefno() + "|" + a.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "")  + "|" + a.getBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT + "|" + String.format("%.2f", a.getCashIn()) + "|" + currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + r.getPtid() + "|" + defaultReversalReason + "|" + "|" + "|");
														}else {
															
														}
													}
												}
											}
										}	
									}
								}	
							}
						}
					}
				}
			
			//CHEQUE REVERSALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
			for(TlJournal a : reversed_cash){
				String acct_no = (a.getDescription()).replaceAll("\\s+", "");
				StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
				for (TlItemCapture t : chq_details){
					for(ReversalsTb r : reversedTrans){
						String formattedDATE = sdf.format(r.getDtime());
						if(todaysDate.equals(formattedDATE)){
							if(a.getPtid().compareTo(t.getJournalPtid())== 0){
								if(a.getPtid().compareTo(r.getPtid()) == 0){
									for(AdGbBranch br : branches){
										if(a.getBranchNo() == br.getBranchNo()){
											//String branchName = br.getName1();
											for(AdGbCrncy c: currency){
												if(a.getExchCrncyId() == c.getCrncyId()){
													String currencyCode = c.getIsoCode();
													if(a.getTlTranCode().equals("105")){
														String status = "03";
														String defaultReversalReason = "CHQ DEPOSIT REVERSED";
														if(a.getExchCrncyId() == 5){
															if(stanchart_acc != null){
																System.out.println(r.getRefno().concat(t.getPtid().toString()) + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + t.getPtid() + "|" + defaultReversalReason + "|" + "|" + "|" );	
																if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
																	pwr_eod_reversals.println(r.getRefno().concat(t.getPtid().toString()) + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + t.getPtid() + "|" + defaultReversalReason + "|" + "|" + "|" );
																}
																prWr_chqs_returned.println(r.getRefno().concat(t.getPtid().toString()) + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT+  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + t.getPtid() + "|" + defaultReversalReason + "|" + "|" +  "|");
															}else {
																
															}
															
														}
													}
												}
											}	
										}
									}
								}
							}
						}
					}
				}
			}	
			
			//CHEQUE DEPOSIT
			for (TlItemCapture t : chq_details){
				for(TlJournal a : fileList){
					for(AdGbRsm teller : teller_userName){
						if(a.getEmplId() == teller.getEmployeeId()){
							String tellerusername = teller.getName();
							for(AdGbBranch br : branches){
								for(AdGbCrncy c : currency){
									if(t.getCrncyId() == c.getCrncyId()){
										String currencyCode = c.getIsoCode();
										if(t.getBranchNo() == br.getBranchNo()){
											if(a.getPtid().compareTo(t.getJournalPtid())== 0){
												if(todaysDate.equals(sdf.format(t.getEffectiveDt()))){
													if(a.getTlTranCode().equals("105")){
														String status = "00";
														String acct_no = a.getDescription().replaceAll("\\s+", "");
														StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
														Calendar cal = Calendar.getInstance();
														for(int i=3 ; i<=3 ; i++){
															cal.setTime(a.getValueDt());
															for(int processing_days=i ; processing_days!=0 ; processing_days--) {
																cal.add(Calendar.DATE, 1);
																if(cal.get(Calendar.DAY_OF_WEEK)==7) {
																	cal.add(Calendar.DATE, 2);
																}
															}
														}
														if(stanchart_acc != null){
															System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															prWr.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
																pwr_eod.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
															}
														}else {
															System.out.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|"+ sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");	
															prWr_err.println(t.getPtid() + "|" + t.getAcctNo() + "|" + (a.getDescription()).replaceAll("\\s+", "") + "|" + t.getBranchNo() + "|" + tellerusername + "|" + br.getName1() +  "|" +  t.getCheckNo() + "|" + sdf.format(t.getCreateDt()) + "|" + Constant.CHQ_PAYMENT_TYPE +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + a.getReference() + "|" + "|" + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(a.getValueDt()) + "|" + sdf.format(a.getPostingDtTm()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + ": [INCORRECT SCB ACCOUNT]");
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			
			//Returned Cheque Deposits on Account processing
			for(DpHistory ret : returned_chqs){
				for(AdGbRsm teller : teller_userName){
					if(ret.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						for(AdGbBranch br : branches){
							if(ret.getOrigBranchNo() == br.getBranchNo()){
								String branchName = br.getName1();
								String currencyCode = "USD";
								String status = "02";
								if(todaysDate.equals(sdf.format(ret.getCreateDt()))){
									if(ret.getDescription().replaceAll("\\s", "").contains("|")){
										String[] splitDesc = ret.getDescription().replaceAll("\\s", "").split("\\|");
										//System.out.println("Number of pipes : " + (splitDesc.length - 1));
										if((splitDesc.length - 1) == 2){
											String scb_acc = splitDesc[0];
											String chq_num = splitDesc[1];
											String reason = splitDesc[2];
											StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, scb_acc);
											if(ret.getExchCrncyId() == 5){
												for(TlItemCapture t : chq_details){
													String chqnumber = Integer.toString(t.getCheckNo());
													if(chq_num.equals(chqnumber)){
														BigDecimal original_ref = t.getPtid();
														if(stanchart_acc != null){
															System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo()+ "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|");	
															prWr_chqs_returned.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo() + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|");
															if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
																pwr_eod_reversals.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo()+ "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|");
															}
														}else {
															System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|" + " CAPTURED INFORMATION: [" + ret.getDescription() + "] [INCORRECT SCB ACCOUNT]");	
															prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + scb_acc + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" +  chq_num + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" + original_ref +  "|" + reason + "|" + "|"  + "|" + " CAPTURED INFORMATION: [" + ret.getDescription() + "] [INCORRECT SCB ACCOUNT]");
														}	
													}
												}
											}else {
												//NOT A USD TRANSACTION
											}
										}else {
											System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|" + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");	
											prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|"  + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");
										}
									}else {
										System.out.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|" + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");	
										prWr_err.println(ret.getPtid() + "|" + ret.getAcctNo() + "|" + "|" + ret.getOrigBranchNo() + "|" +  tellerusername + "|" + branchName +  "|" + "|" + sdf.format(ret.getCreateDt()) + "|" + Constant.CHEQUE_RETURN +  "|" +  String.format("%.2f", ret.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + sdf.format(ret.getCreateDt()) + "|" + status + "|" +  "|"  + "|" + "|"  + " CAPTURED INFORMATION: [" + ret.getDescription() + "] CHEQUE RETURN EXPECTED FORMAT : [SCBACCNO|CHQNO|REASON]");
									}
								}
							}
						}
					}
				}
			}
		
			//Reversed CASH and CHEQUE Deposits on Account processing
			for(DpHistory rev : reversed_cashAccProc){
				for(AdGbRsm teller : teller_userName){
					if(rev.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						for(AdGbBranch br : branches){
							if(rev.getOrigBranchNo() == br.getBranchNo()){
								String branchName = br.getName1();
								String currencyCode = "USD";
								String status = "03";
								if(todaysDate.equals(sdf.format(rev.getCreateDt()))){
									if(rev.getDescription().replaceAll("\\s", "").contains("|")){
										String[] splitDesc = rev.getDescription().replaceAll("\\s", "").split("\\|");
										System.out.println("Number of pipes : " + (splitDesc.length - 1));
										if((splitDesc.length - 1) == 2){
											String scb_acc = splitDesc[0];
											String referencePtid = splitDesc[1];
											String reason = splitDesc[2];
													
											StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, scb_acc);
											BigDecimal ref_num = new BigDecimal(referencePtid);
											if(rev.getExchCrncyId() == 5){
												for(TlJournal tl : fileList){
													if(ref_num.compareTo(tl.getPtid()) == 0 && tl.getTlTranCode().equals("103")){
														if(stanchart_acc != null){
															System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo()+ "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|");	
															prWr_chqs_returned.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|");
														}else {
															System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|:" + scb_acc + " -[INCORRECT SCB ACCOUNT]");	
															prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" + "|" + "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + ref_num +  "|" + reason + "|" + "|"  + "|:" + scb_acc + " -[INCORRECT SCB ACCOUNT]");
														}	
													}
												}
												for(TlItemCapture t : chq_details){
													if(ref_num.compareTo(t.getJournalPtid())== 0){
														if(stanchart_acc != null){
															System.out.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|"  + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + t.getPtid() + "|" + reason + "|" + "|" + "|");	
															prWr_chqs_returned.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + t.getPtid() + "|" + reason + "|" + "|" + "|");
														}else {
															System.out.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|"  + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + reason + "|" + "|" + "| :" + scb_acc + " -[INCORRECT SCB ACCOUNT]");	
															prWr_err.println(rev.getPtid().toString().concat(t.getPtid().toString()) + "|" + rev.getAcctNo() + "|" + scb_acc + "|" + rev.getOrigBranchNo() + "|" + tellerusername + "|" + branchName +  "|" +  t.getCheckNo() + "|" + sdf.format(rev.getCreateDt()) + "|" + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", t.getAmount()) + "|" +  currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + reason + "|" + "|" + "| :" + scb_acc + " -[INCORRECT SCB ACCOUNT]");
														}
													}else {
																
													}	
												}
											}else {
												//NOT A USD TRANSACTION
											}
										}else {
											System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
											prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
										}
									}else {
										System.out.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
										prWr_err.println(rev.getPtid() + "|" + rev.getAcctNo() + "|" + rev.getOrigBranchNo()+ "|" + tellerusername + "|" + branchName +  "|" + "|" +  "|"  + Constant.REVERSAL_OF_CREDIT +  "|" +  String.format("%.2f", rev.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|"+ sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + sdf.format(rev.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|" + " CAPTURED INFORMATION: [" + rev.getDescription() + "] CHQ OR CASH REVERSAL EXPECTED FORMAT : [SCBACCNO|UNIQUE_REFNO|REASON]");
									}
								}
							}
						}
					}
				}
			}
				
			//Rectified Cash Deposits on Account processing using 117
			for(DpHistory cash_dep : rectified_cashDepAccProc){
				for(AdGbRsm teller : teller_userName){
					if(cash_dep.getEmplId() == teller.getEmployeeId()){
						String tellerusername = teller.getName();
						if(todaysDate.equals(sdf.format(cash_dep.getCreateDt()))){
							for(AdGbBranch br : branches){
								if(cash_dep.getOrigBranchNo() == br.getBranchNo()){
									String branchName = br.getName1();
									String currencyCode = "USD";
									String status = "00";
									if(cash_dep.getDescription().replaceAll("\\s", "").contains("|")){
										String[] splitDesc = cash_dep.getDescription().replaceAll("\\s", "").split("\\|");
										//System.out.println("Number of pipes : " + (splitDesc.length - 1));
										if((splitDesc.length - 1) == 1){
											String acct_no = splitDesc[0];
											String depositorname = splitDesc[1];
											StanchartTb stanchart_acc = (StanchartTb) em.find(StanchartTb.class, acct_no);
											if(cash_dep.getExchCrncyId() == 5){
												if(stanchart_acc != null){
													prWr.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
													if((path_name.substring(32, 36).equals(String.valueOf(currentDate.get(Calendar.YEAR)))) && (path_name.substring(37, path_name.length()-3).equals(monthInWords)) && (path_name.substring(path_name.length() -2).equals(todaysDate.substring(0, 2)))){
														pwr_eod.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
													}
													System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|");
												}else {
													prWr_err.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|"  + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] [INCORRECT SCB ACCOUNT]");
													System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + acct_no + "|" + cash_dep.getOrigBranchNo() + "|" + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + depositorname + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] [INCORRECT SCB ACCOUNT]");
												}
											}
										}else {
											//NOT A USD ACCOUNT
										}
									}else {
										prWr_err.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|" + "|" + cash_dep.getOrigBranchNo() + "|"  + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|"  + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
										System.out.println(cash_dep.getPtid() + "|" + cash_dep.getAcctNo() + "|"  + "|" + cash_dep.getOrigBranchNo() + "|" + tellerusername + "|" + branchName + "|" + "|" + "|"  + Constant.CASH_PAYMENT_TYPE + "|" + String.format("%.2f", cash_dep.getAmt()) + "|" + currencyCode + "|" + "|" + "|" + "|" + "|" + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + sdf.format(cash_dep.getCreateDt()) + "|" + status + "|" + "|" + "|" + "|"  + "|" + "|" + " CAPTURED INFORMATION: [" + cash_dep.getDescription() + "] EXPECTED FORMAT : [SCBACCNO|DEPOSITOR NAME]");
									}
								}
							}
						}
					}
				}
			}
			
			System.out.println("EOD Deposits " + absolutePath);
			System.out.println("Invalid EOD Deposits " + absolutePath_err);
			System.out.println("EOD REV " + rev_absolutePath);
			System.out.println("zw_posbeodcsv backup" + backup_eod_path);
			System.out.println("zw_posbeodcsv_liq backup" + backup_eod_path_reversals);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}finally{
			prWr.flush();
			prWr.close();
			
			prWr_err.flush();
			prWr_err.close();
			
			prWr_chqs_returned.flush();
			prWr_chqs_returned.close();
			
			pwr_eod.flush();
			pwr_eod.close();
			
			pwr_eod_reversals.flush();
			pwr_eod_reversals.close();
			
		}
		testSendEmail();
	}
	
	@Schedule(hour = "18", dayOfWeek = "Mon-Sat", year = "*")
	//@Schedule(hour = "8-20", minute = "*/2", dayOfWeek = "Mon-Sat")
    public void generateEODReport(){
    	System.out.println("EOD transactions generated every at 1700hrs........................");
    	fetcherEOD();
    }
	
	public void testSendEmail() {
		//SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		
		Calendar currentDate = Calendar.getInstance();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String todaysDate = sdf.format(currentDate.getTime());
		
		//String LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		String WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS = "REJECTS_ACCOUNTS_SCB_DEPOSITS_" + DATE_FORMAT.format(currentDate.getTime()) + ".txt" ;
		
		//File rejects = new File(Constant.FILEPATH_LINUX_INVALID_ACCOUNTS + LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS);
		File rejects = new File(Constant.FILEPATH_WINDOWS_INVALID_ACCOUNTS + WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS);
		System.out.println("File Path::: " + rejects.getName());
		String[] to = new String[1];
		to[0] = "dhlabanu@posb.co.zw";
		//to[1] = "pkumire@posb.co.zw";
		//to[2] = "dmabuzve@posb.co.zw";
		//to[3] = "snyamowa@posb.co.zw";
		System.out.println(rejects.getName() + " ====================");
		System.out.println("Checking for equality " + rejects.getName().substring(30, 38) + ":" + todaysDate);
		if(todaysDate.equals(rejects.getName().substring(30, 38))){
			try{
				FileReader fr = new FileReader(rejects);
				BufferedReader br = new BufferedReader(fr);
				String line;

				line = br.readLine();
				while((line = br.readLine()) != null){
					if(line.length() == 0){
						
					}else {
						//sendEmail("/home/dhlabanu/SCB_Proj/SCB_InvalidAccountsReport/" + LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS, todaysDate, to);
						sendEmail("C:\\SCB_ProjectFiles\\SCB_InvalidAccountsReport\\" + WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS, todaysDate, to);
						break;
					}
				}
				br.close();
			}catch(FileNotFoundException fne){
				//System.out.println("File" + LINUX_INVALID_ACCOUNTS_SCB_DEPOSITS + " not found in the specified path");
				System.out.println("File" + WINDOWS_INVALID_ACCOUNTS_SCB_DEPOSITS + " not found in the specified path");
			}catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}else {
			
		}
		
	}

	public String sendEmail(String fileName, String emailId, String[] to) {

		String from = "reports@posb.co.zw";
		String host = "10.50.30.215";
		String pass = "possible";

		String subject = "Standard Chartered Bank Rejects Deposits Notification";
		String BodyText = "Dear valued Client \n\nAttached is a notification for Standard Chartered Bank Deposits with blank SCB Accounts, wrong accounts and non-USD deposits\n\nRegards  \n\nPOSB Bank ";

		Properties props = System.getProperties();
		System.out.println("From :: " + from);
		System.out.println("Host :: " + host);
		System.out.println("Password :: " + pass);
		System.out.println("To :: " + to[0]);

		props.put("mail.smtp.host", host);
		props.put("mail.password", pass);
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", true);

		Session session = Session.getDefaultInstance(props, null);

		try {
			MimeMessage message = new MimeMessage(session);
			//
			// Set Sender
			//
			message.setFrom(new InternetAddress(from));
			//
			// SetRecipient
			//
			InternetAddress[] toAddress = new InternetAddress[to.length];
			for (int i = 0; i < toAddress.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
				System.out.println("To recepient :: " + toAddress[i]);
			}

			//
			// Set Subject
			//
			message.setSubject(subject);
			//
			// Set Sending date
			//
			message.setSentDate(new Date());

			//
			// Set the email text
			//
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(BodyText);

			//
			// Set the email attachment file
			//
			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource(fileName) {
				// @Override
				public String getContentType() {
					return "application/octet-stream";
				}
			};
			attachmentPart.setDataHandler(new DataHandler(fileDataSource));
			attachmentPart.setFileName("POSB Generated File - " + emailId + ".txt");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messagePart);
			multipart.addBodyPart(attachmentPart);

			message.setContent(multipart);

			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return Constant.EMAIL_SENT_SUCCESSFULLY;

		} catch (MessagingException e) {
			// e.printStackTrace();
			System.out.println(e);
			return e.getMessage();
		}

	}

}