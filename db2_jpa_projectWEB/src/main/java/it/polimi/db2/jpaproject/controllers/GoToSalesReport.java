package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/SalesReport")
public class GoToSalesReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.mission.services/SalesServices")
	private SalesServices salesServices;
	
	@EJB(name = "it.polimi.db2.mission.services/UserService")
	private UserService userService;
	
	@EJB(name = "it.polimi.db2.mission.services/SuspendedOrder")
	private OrderService orderService;

	public GoToSalesReport() {
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
		List<ValidityPeriodSales> validitySales = null;
		List<PackageSales> packageSales = null;
		List<OptionalSales> optionalSales = null;
		List<InsolventUser> insolventUsers = null;
		List<SuspendedOrder> suspendedOrder = null;

		try {	
			validitySales = salesServices.findAllValidityPurchase();
			packageSales = salesServices.findAllPackagePurchase();
			optionalSales = salesServices.findAllOptionalPurchases();
			insolventUsers = userService.findAllInsolventUser();
			suspendedOrder = orderService.suspendedOrderList();
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
			return;
		}

		String path = "/WEB-INF/SalesReport.html";
		ServletContext servletContext = getServletContext();
		final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		context.setVariable("validitySales", validitySales);
		context.setVariable("packageSales", packageSales);
		context.setVariable("optionalSales", optionalSales);
		context.setVariable("insolventUserList", insolventUsers);
		context.setVariable("suspendedOrderList", suspendedOrder);

		templateEngine.process(path, context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
