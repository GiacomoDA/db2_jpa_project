package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.math.BigDecimal;

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

import it.polimi.db2.jpaproject.services.*;

@WebServlet("/CreateOptional")
public class CreateOptional extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.jpaproject.services/OptionalService")
	private OptionalService optionalService;

	public CreateOptional() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String optionalName = null;
		BigDecimal monthlyFee = null;

		try {
			optionalName = StringEscapeUtils.escapeJava(request.getParameter("name"));
			monthlyFee = new BigDecimal(StringEscapeUtils.escapeJava(request.getParameter("fee")));
			if (optionalName == null || optionalName.isEmpty())
				throw new Exception("Missing or empty value");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid values");
			return;
		}

		String path;

		try {
			if (optionalService.checkOptionals(optionalName) != null && !optionalService.checkOptionals(optionalName).isEmpty()) {
				throw new Exception("Already existing Optional!");
			}
			optionalService.addOptional(optionalName, monthlyFee);

			path = "/WEB-INF/OptionalEditor.html";
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path, context, response.getWriter());

		} catch (Exception e) {
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			context.setVariable("errorOptionalCreation", "Already existing Optional!");
			path = "/WEB-INF/OptionalEditor.html";
			templateEngine.process(path, context, response.getWriter());
		}

	}

	public void destroy() {
	}

}
