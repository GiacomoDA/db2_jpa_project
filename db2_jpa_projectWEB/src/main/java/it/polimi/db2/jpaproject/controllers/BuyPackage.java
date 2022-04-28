package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.jpaproject.services.*;
import it.polimi.db2.jpaproject.entities.*;

@WebServlet("/BuyPackage")
public class BuyPackage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.mission.services/OrderService")
	private OrderService orderService;

	@EJB(name = "it.polimi.db2.mission.services/PackageService")
	private PackageService packageService;

	public BuyPackage() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> optionals = Arrays.asList(Optional.ofNullable(request.getParameterValues("optional")).orElse(new String[0]));
		int packageId = Integer.parseInt(request.getParameter("packageId"));
		int months = Integer.parseInt(request.getParameter("period"));
		String date = request.getParameter("date");
		
		String path;
		LocalDate activationDate;
		
		try {
			activationDate = LocalDate.parse(date);
		}
		catch (DateTimeParseException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date not valid");
			return;
		}
		
		if (!activationDate.isAfter(LocalDate.now())) {			
			ServicePackage servicePackage;

			try {			
				servicePackage = packageService.findPackageById(packageId);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
				return;
			}
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			context.setVariable("errorMessageDate", "The selected date is not valid");
			context.setVariable("package", servicePackage);
			path = "/WEB-INF/Package.html";
			templateEngine.process(path, context, response.getWriter());			
		} else {			
			Order order = orderService.createOrder(packageId, activationDate, months, optionals);
			request.getSession().setAttribute("order", order);
	
			path = "/WEB-INF/OrderSummary.html";
			ServletContext servletContext = getServletContext();
			final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
	
			templateEngine.process(path, context, response.getWriter());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
