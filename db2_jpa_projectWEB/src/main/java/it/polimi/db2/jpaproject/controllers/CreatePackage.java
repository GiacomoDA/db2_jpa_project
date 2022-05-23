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

	@EJB(name = "it.polimi.db2.jpaproject.services/ServiceService")
	private ServiceService serviceService;

	@EJB(name = "it.polimi.db2.jpaproject.services/OptionalService")
	private OptionalService optionalService;

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
		String name;
		List<String> services;
		List<String> validityPeriods;
		List<String> optionals;
		MobilePhone mobilePhone = null;
		Integer minutes = null;
		Integer sms = null;
		BigDecimal mFee = null;
		BigDecimal sFee = null;
		FixedInternet fixedInternet = null;
		Integer fxGigabytes = null;
		BigDecimal fxGFee = null;
		MobileInternet mobileInternet = null;
		Integer mbGigabytes = null;
		BigDecimal mbGFee = null;


		try {
			name = StringEscapeUtils.escapeJava(request.getParameter("name"));
			services = Arrays.asList(Optional.ofNullable(request.getParameterValues("service")).orElse(new String[0]));
			optionals = Arrays.asList(Optional.ofNullable(request.getParameterValues("optional")).orElse(new String[0]));
			//mobilePhone
			if (services.contains("mobilePhone")) {
				minutes = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("MPMinutes")));
				sms = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("MPSMS")));
				mFee = new BigDecimal(StringEscapeUtils.escapeJava(request.getParameter("MPMinutesFee")));
				sFee = new BigDecimal(StringEscapeUtils.escapeJava(request.getParameter("MPSMSFee")));
				mobilePhone = serviceService.newMobilePhone(minutes, sms, sFee, sFee);
			}
			//fixedInternet
			if (services.contains("fixedInternet")) {
				fxGigabytes = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("FIGB")));
				fxGFee = new BigDecimal(StringEscapeUtils.escapeJava(request.getParameter("FIGBFee")));
				fixedInternet = serviceService.newFixedInternet(fxGigabytes, fxGFee);
			}
			//mobileInternet
			if (services.contains("mobileInternet")) {
				mbGigabytes = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("MIGB")));
				mbGFee = new BigDecimal(StringEscapeUtils.escapeJava(request.getParameter("MIGBFee")));
				mobileInternet = serviceService.newMobileInternet(mbGigabytes, mbGFee);
			}
			if (name == null || name.isEmpty())
				throw new Exception("Missing or empty value");
		} catch (NumberFormatException numberException) {
			request.getSession().setAttribute("errorMessage", "Invalid value");
			String path = getServletContext().getContextPath() + "/GoToPackageEditor";
			response.sendRedirect(path);
			return;
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid values");
			return;
		}
		
		try {
			if (packageService.findPackageByName(name) != null) {
				request.getSession().setAttribute("errorMessage", "This package name is taken");
				String path = getServletContext().getContextPath() + "/PackageEditor";
				response.sendRedirect(path);
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid values");
			return;	
		}

		packageService.addPackage(name, services.contains("fixedPhone"), mobilePhone, fixedInternet, mobileInternet, null, optionals);
		
		String path;

		/*try {
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
		}*/
	}

	public void destroy() {
	}

}
