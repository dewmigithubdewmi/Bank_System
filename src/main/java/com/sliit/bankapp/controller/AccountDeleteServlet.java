package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/account/delete")
public class AccountDeleteServlet extends HttpServlet {
    private AccountDAO dao;

    @Override
    public void init() {
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) get the current user
        User u = (User) req.getAttribute("currentUser");
        int accountId = Integer.parseInt(req.getParameter("accountId"));

        try {
            // 2) load the account and check permissions
            Account a = dao.findById(accountId);
            if (a == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!"ADMIN".equals(u.getRole()) && a.getUserId() != u.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // 3) delete and redirect home
            dao.delete(accountId);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            throw new ServletException("Cannot delete account", e);
        }
    }
}
