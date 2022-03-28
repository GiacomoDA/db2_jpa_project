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

import it.polimi.db2.jpaproject.services.UserService;
import it.polimi.db2.jpaproject.entities.User;
import it.polimi.db2.jpaproject.exceptions.CredentialsException;
import javax.persistence.NonUniqueResultException;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.jpaproject.services/UserService")
	private UserService userService;

	public Login() {
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		
		User user;
		
		try {
			user = userService.checkCredentials(username, password);
		} catch (CredentialsException | NonUniqueResultException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			context.setVariable("errorMessageLogin", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, context, response.getWriter());
		} else {
			request.getSession().setAttribute("user", user);
			path = getServletContext().getContextPath() + "/Home";
			response.sendRedirect(path);
		}

	}

	public void destroy() {
	}
}