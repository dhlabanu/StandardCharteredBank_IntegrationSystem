package zw.co.posb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zw.co.posb.ejb.beans.interfaces.Cheque_CashDepositLocal;

@WebServlet("/SCBTest")
public class SCBTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	@EJB
	private Cheque_CashDepositLocal cashDepositBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet SCBTestServlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>SCB Project</h1>");
		cashDepositBean.fetcherIntraday();	
		cashDepositBean.fetcherEOD();
		out.println("</body>");
		out.println("</html>");
	}
	
}
