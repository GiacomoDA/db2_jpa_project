package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.jpaproject.services.EmployeeService;
import it.polimi.db2.jpaproject.entities.Employee;
import it.polimi.db2.jpaproject.exceptions.CredentialsException;
import javax.persistence.NonUniqueResultException;

@WebServlet("/EmployeeLogin")
public class EmployeeLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.jpaproject.services/EmployeeService")
	private EmployeeService employeeService;

	public EmployeeLogin() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = null;
		String password = null;
		
		try {
			username = StringEscapeUtils.escapeJava(request.getParameter("username"));
			password = StringEscapeUtils.escapeJava(request.getParameter("password"));
			if (username == null || password == null || username.isEmpty() || password.isEmpty())
				throw new Exception("Missing or empty credential value");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credentials value");
			return;
		}
		
		Employee employee;
		
		try {
			employee = employeeService.checkCredentials(username, password);
		} catch (CredentialsException | NonUniqueResultException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		
		if (employee == null) {
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			context.setVariable("errorMessageLogin", "Incorrect username or password");
			path = "/WEB-INF/EmployeeLogin.html";
			templateEngine.process(path, context, response.getWriter());
		} else {
			request.getSession().setAttribute("employee", employee);
			path = getServletContext().getContextPath() + "/SalesReport";
			response.sendRedirect(path);
		}

	}

	public void destroy() {
	}
}