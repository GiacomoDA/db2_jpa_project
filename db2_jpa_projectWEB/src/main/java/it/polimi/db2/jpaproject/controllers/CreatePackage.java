package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import it.polimi.db2.jpaproject.entities.*;
import it.polimi.db2.jpaproject.services.*;

@WebServlet("/CreatePackage")
public class CreatePackage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.jpaproject.services/PackageService")
	private PackageService packageService;

	public CreatePackage() {
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
		Integer id = null; 								//how do I assign it since is not up to the user?
		String name = null;
		Boolean fixedPhone = null;
		MobilePhone mobilePhone = null;
		FixedInternet fixedInternet = null;
		MobileInternet mobileInternet = null;
		List<ValidityPeriod> validityPeriods = null;
		List<Optional> optionals = null;


		try {
			name = StringEscapeUtils.escapeJava(request.getParameter("name"));
			//mobilePhone
			String minutes = StringEscapeUtils.escapeJava(request.getParameter("MPMinutes"));
			String sms = StringEscapeUtils.escapeJava(request.getParameter("MPSMS"));
			String mFee = StringEscapeUtils.escapeJava(request.getParameter("MPMinutesFee"));
			String sFee = StringEscapeUtils.escapeJava(request.getParameter("MPSMSFee"));
			//fixedInternet
			String fxGigabytes = StringEscapeUtils.escapeJava(request.getParameter("FIGB"));
			String fxGFee = StringEscapeUtils.escapeJava(request.getParameter("FIGBFee"));
			//mobileInternet
			String mbGigabytes = StringEscapeUtils.escapeJava(request.getParameter("MIGB"));
			String mbGFee = StringEscapeUtils.escapeJava(request.getParameter("MIGBFee"));
			List<String> isFixed = Arrays.asList(Optional.ofNullable(request.getParameterValues("optional")).orElse(new String[0]));
			if (name == null || name.isEmpty())
				throw new Exception("Missing or empty value");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing needed values");
			return;
		}

		String path;

		try {
			if (packageService.findPackageByName(name) != null) {
				throw new Exception("Already existing Package!");
			}
			BigDecimal minutesFee = new BigDecimal(mFee);
			BigDecimal smsFee = new BigDecimal(sFee);
			BigDecimal fxGigabytesFee = new BigDecimal(fxGFee);
			BigDecimal mbGigabytesFee = new BigDecimal(mbGFee);
			packageService.addOptional(optionalName, fee);

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
