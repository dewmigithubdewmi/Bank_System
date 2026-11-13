package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/account/profile")
public class AccountProfileServlet extends HttpServlet {
    private AccountDAO dao;

    @Override
    public void init() {
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int accountId;
        try {
            accountId = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User u = (User) req.getAttribute("currentUser");
        try {
            Account a = dao.findById(accountId);
            if (a == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            // Ensure the current user owns it (or is ADMIN)
            if (!"ADMIN".equals(u.getRole()) && a.getUserId() != u.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            req.setAttribute("account", a);
            req.getRequestDispatcher("/WEB-INF/views/account-profile.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Error loading account profile", e);
        }
    }
}
