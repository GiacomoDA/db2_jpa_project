package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.persistence.NoResultException;
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
	
	@EJB(name = "it.polimi.db2.jpaproject.services/MobilePhoneService")
	private MobilePhoneService mobilePhoneService;
	
	@EJB(name = "it.polimi.db2.jpaproject.services/FixedInternetService")
	private FixedInternetService fixedInternetService;
	
	@EJB(name = "it.polimi.db2.jpaproject.services/MobileInternetService")
	private MobileInternetService mobileInternetService;
	
	@EJB(name = "it.polimi.db2.jpaproject.services/ValidityPeriodService")
	private ValidityPeriodService validityPeriodService;

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
		List<String> optionals;
		List<ValidityPeriod> validityPeriods;
		List<String> validityMonths;
		List<String> validityFee;
		List<MobilePhone> mobilePhone = null;
		List<String> minutes;
		List<String> sms;
		List<String> mFee;
		List<String> sFee;
		List<FixedInternet> fixedInternet = null;
		List<String> fxGigabytes;
		List<String> fxGFee;
		List<MobileInternet> mobileInternet = null;
		List<String> mbGigabytes;
		List<String> mbGFee;


		try {
			name = StringEscapeUtils.escapeJava(request.getParameter("name"));
			//useless??
			services = Arrays.asList(Optional.ofNullable(request.getParameterValues("service")).orElse(new String[0]));
			optionals = Arrays.asList(Optional.ofNullable(request.getParameterValues("optional")).orElse(new String[0]));
			
			//mobilePhone
			minutes = Arrays.asList(Optional.ofNullable(request.getParameterValues("minutes")).orElse(new String[0]));
			sms = Arrays.asList(Optional.ofNullable(request.getParameterValues("sms")).orElse(new String[0]));
			mFee = Arrays.asList(Optional.ofNullable(request.getParameterValues("extraMinFee")).orElse(new String[0]));
			sFee = Arrays.asList(Optional.ofNullable(request.getParameterValues("extraSmsFee")).orElse(new String[0]));
			if (!minutes.isEmpty() && !sms.isEmpty() && !mFee.isEmpty() && !sFee.isEmpty()) {
				mobilePhone = mobilePhoneService.newMobilePhone(minutes, sms, mFee, sFee);
			}
			
			//fixedInternet
			fxGigabytes = Arrays.asList(Optional.ofNullable(request.getParameterValues("fiGb")).orElse(new String[0]));
			fxGFee = Arrays.asList(Optional.ofNullable(request.getParameterValues("fiGbFee")).orElse(new String[0]));
			if (!fxGigabytes.isEmpty() && !fxGFee.isEmpty()) {
				fixedInternet = fixedInternetService.newFixedInternet(fxGigabytes, fxGFee);
			}
			
			//mobileInternet 
			mbGigabytes = Arrays.asList(Optional.ofNullable(request.getParameterValues("miGb")).orElse(new String[0]));
			mbGFee = Arrays.asList(Optional.ofNullable(request.getParameterValues("miGbFee")).orElse(new String[0]));
			if (!mbGigabytes.isEmpty() && !mbGFee.isEmpty()) {
				mobileInternet = mobileInternetService.newMobileInternet(mbGigabytes, mbGFee);
			}
			
			//validityPeriod(s)
			validityMonths = Arrays.asList(Optional.ofNullable(request.getParameterValues("months")).orElse(new String[0]));
			validityFee = Arrays.asList(Optional.ofNullable(request.getParameterValues("fee")).orElse(new String[0]));
			if(validityMonths.isEmpty() || validityFee.isEmpty()) {
				throw new IllegalArgumentException();
			}
			validityPeriods = validityPeriodService.newValidityPeriods(validityMonths, validityFee);
			
			if (name == null || name.isEmpty())
				throw new Exception("Missing or empty value");
		} catch (NumberFormatException numberException) {
			request.getSession().setAttribute("errorMessage", "Missing or invalid value");
			String path = getServletContext().getContextPath() + "/PackageEditor";
			response.sendRedirect(path);
			return;
		} catch (IllegalArgumentException noVPArguments) {
			request.getSession().setAttribute("errorMessage", "Need at least one validity period");
			String path = getServletContext().getContextPath() + "/PackageEditor";
			response.sendRedirect(path);
			return;
		} catch (DuplicateKeyException duplicateMonth) {
			request.getSession().setAttribute("errorMessage", "Duplicate validity period duration");
			String path = getServletContext().getContextPath() + "/PackageEditor";
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
				return;
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid values");
			return;	
		}
		
		packageService.addPackage(name, services.contains("fixedPhone"), mobilePhone, fixedInternet, mobileInternet, validityPeriods, optionals);

		String path = getServletContext().getContextPath() + "/PackageEditor";
		response.sendRedirect(path);
	}

	public void destroy() {
	}

}
