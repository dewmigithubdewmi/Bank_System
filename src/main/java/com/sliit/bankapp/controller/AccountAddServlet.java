// src/main/java/com/sliit/bankapp/controller/AccountAddServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/account/add")
public class AccountAddServlet extends HttpServlet {
    private AccountDAO dao;

    @Override public void init() {
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Show the form
        req.getRequestDispatcher("/WEB-INF/views/account-add.jsp")
           .forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User u = (User) req.getAttribute("currentUser");
        if (u == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String number = req.getParameter("accountNumber");
        String pin    = req.getParameter("pin");
        try {
            Account a = dao.findByNumberAndPin(number, pin);
            if (a == null) {
                req.setAttribute("error", "Invalid account number or PIN");
                doGet(req, resp);
                return;
            }
            // Link it to current user
            dao.linkToUser(a.getId(), u.getId());
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            throw new ServletException("Cannot add account", e);
        }
    }
}
