package zw.co.posb.constants;


public class Constant {
	
	public static final String	CASH_PAYMENT_TYPE	= "CASH";
	public static final String	CHQ_PAYMENT_TYPE	= "CHQ";
	public static final String CHEQUE_RETURN = "RET";
	public static final String REVERSAL_OF_CREDIT = "REV";
	
	public static String FILEPATH_WINDOWS_INTRADAY_DEPOSIT = "C:\\Program Files\\host2host\\documents\\collections\\out\\zw_posbidcsv\\";
	public static String FILEPATH_WINDOWS_EOD_DEPOSIT = "C:\\Program Files\\host2host\\documents\\collections\\out\\zw_posbeodcsv\\";
	public static String FILE_PATH_WINDOWS_REV_INTRADAY_DEPOSIT = "C:\\Program Files\\host2host\\documents\\collections\\out\\zw_posbidcsv_liq\\";
	public static String FILE_PATH_WINDOWS_REV_EOD_DEPOSIT = "C:\\Program Files\\host2host\\documents\\collections\\out\\zw_posbeodcsv_liq\\";
	
	
	public static String FILEPATH_LINUX_INTRADAY_DEPOSIT = "/home/dhlabanu/SCB_Proj/host2host/documents/collections/out/zw_posbidcsv/";
	public static String FILEPATH_LINUX_EOD_DEPOSIT = "/home/dhlabanu/SCB_Proj/host2host/documents/collections/out/zw_posbeodcsv/";
	public static String FILE_PATH_LINUX_REV_INTRADAY_DEPOSIT = "/home/dhlabanu/SCB_Proj/host2host/documents/collections/out/zw_posbidcsv_liq/";
	public static String FILE_PATH_LINUX_REV_EOD_DEPOSIT = "/home/dhlabanu/SCB_Proj/host2host/documents/collections/out/zw_posbeodcsv_liq/";
	
	public static final String	EMAIL_SENT_SUCCESSFULLY	 = "EMAIL_SENT_SUCCESSFULLY";
	
	//Linux path
	public static String FILEPATH_LINUX_INVALID_ACCOUNTS = "/home/dhlabanu/SCB_Proj/SCB_InvalidAccountsReport/";
	
	//Windows path
	public static String FILEPATH_WINDOWS_INVALID_ACCOUNTS = "C:\\SCB_ProjectFiles\\SCB_InvalidAccountsReport\\";
	
	//DAILY BACKUP
	public static String LINUX_SCB_DAILY_BACKUP = "/home/dhlabanu/SCB_Proj/SCB_BACKUPS/";
	public static String WINDOWS_SCB_DAILY_BACKUP = "C:\\SCB_ProjectFiles\\SCB_BACKUPS\\";

}
