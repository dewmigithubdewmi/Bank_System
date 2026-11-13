package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/account/edit")
public class AccountEditServlet extends HttpServlet {
    private AccountDAO dao;

    @Override
    public void init() {
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User u = (User) req.getAttribute("currentUser");
        int accountId = Integer.parseInt(req.getParameter("accountId"));

        try {
            // load and authorize
            Account a = dao.findById(accountId);
            if (a == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!"ADMIN".equals(u.getRole()) && a.getUserId() != u.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // read edited fields
            String nic     = req.getParameter("nic");
            String address = req.getParameter("address");
            String mobile  = req.getParameter("mobile");
            double balance = Double.parseDouble(req.getParameter("balance"));

            // persist changes
            dao.updateDetails(accountId, nic, address, mobile, balance);

            // ← redirect to home instead of profile
            resp.sendRedirect(req.getContextPath() + "/home");

        } catch (Exception e) {
            throw new ServletException("Cannot update account", e);
        }
    }
}
