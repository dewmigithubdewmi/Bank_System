package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;

@WebServlet("/account/create")
public class AccountCreateServlet extends HttpServlet {

    private AccountDAO dao;
    private final SecureRandom rnd = new SecureRandom();

    @Override
    public void init() {
        // HttpServlet.getServletContext() is available now
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Retrieve the logged-in user set by AuthFilter
        User u = (User) req.getAttribute("currentUser");
        if (u == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Build new account from form parameters
        Account a = new Account();
        a.setUserId(u.getId());
        a.setAccountNumber(
            String.format("AC%010d", rnd.nextInt(1_000_000_000))
        );
        a.setPin(req.getParameter("pin"));
        a.setFullName(req.getParameter("fullName"));
        a.setNic(req.getParameter("nic"));
        a.setAddress(req.getParameter("address"));
        a.setMobile(req.getParameter("mobile"));
        a.setBalance(
            Double.parseDouble(req.getParameter("balance"))
        );

        // Persist and redirect
        try {
            dao.insert(a);
        } catch (Exception ex) {
            throw new ServletException("Failed to create account", ex);
        }
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
