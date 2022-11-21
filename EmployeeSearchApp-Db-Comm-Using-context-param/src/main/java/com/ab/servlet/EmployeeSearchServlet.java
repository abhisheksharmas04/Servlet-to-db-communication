package com.ab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmployeeSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String GET_EMP_DETAILS_BY_ENO = "SELECT EMPNO,ENAME,JOB,SAL,DETPNO FROM EMP WHERE EMPNO=?";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// get access to servlet config object:
		ServletConfig cg = getServletConfig();
		// read init values
		String driver = cg.getInitParameter("driver");
		String url = cg.getInitParameter("url");
		String username = cg.getInitParameter("dbuser");
		String password = cg.getInitParameter("password");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException cne) {
			System.out.println("Oracle driver class not found");
		}

		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");

		int empNo = Integer.parseInt(request.getParameter("eno"));

		try (Connection con = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement ps = con.prepareStatement(GET_EMP_DETAILS_BY_ENO)) {
				ps.setInt(1, empNo);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						pw.println(
								"<h1 style='color:blue;text-align:center'>" + empNo + " Employee details are: </h1>");
						pw.println(
								"<h2 style='color:blue;text-align:center'> Employee No" + rs.getInt(1) + " <br> </h2>");
						pw.println("<h2 style='color:blue;text-align:center'> Employee Name" + rs.getString(2)
								+ " <br> </h2>");
					} else {
						pw.println("<h1 style='color:blue;text-align:center'> No records found for " + empNo + "</h1>");
					}
				}
			}
			pw.println("<a href='input.html'>home</a>");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
