package it.polimi.db2.jpaproject.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user") != null) {
			if (request.getSession().getAttribute("order") != null) {
				// if the user is logged in and there is an order associated to the session,
				// confirm the order
				Boolean outcome = Boolean.valueOf(request.getParameter("outcome"));
				Boolean paymentOutcome = orderService.confirmOrder((Order) request.getSession().getAttribute("order"),
						(User) request.getSession().getAttribute("user"), outcome);
				if (paymentOutcome != null) {
					// the payment was successful
				} else {
					// the payment was declined
				}
				request.getSession().removeAttribute("order");
				String path = getServletContext().getContextPath() + "/Home";
				response.sendRedirect(path);
			} else {
				// if at this point there is a user but no order, something is wrong
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User and order not found");
			}
		} else {
			if (request.getSession().getAttribute("order") != null) {
				// if there is no user in the session redirect to the login page
				String path = getServletContext().getContextPath() + "/index.html";
				response.sendRedirect(path);
			} else {
				// if there is no user and no order, something is wrong
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order not found");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
