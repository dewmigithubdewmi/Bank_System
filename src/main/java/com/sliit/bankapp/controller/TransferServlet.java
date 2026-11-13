package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    private AccountDAO dao;

    @Override
    public void init() {
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    /** Show the form */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User u = (User) req.getAttribute("currentUser");
        try {
            req.setAttribute("accounts", dao.findByUser(u.getId()));
        } catch (Exception e) {
            req.setAttribute("accounts", null);
        }
        req.getRequestDispatcher("/WEB-INF/views/transfer.jsp")
           .forward(req, resp);
    }

    /** Handle the money transfer */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int    fromId   = Integer.parseInt(req.getParameter("fromId"));
        String toNumber = req.getParameter("toNumber");
        double amount   = Double.parseDouble(req.getParameter("amount"));

        try {
            dao.transfer(fromId, toNumber, amount);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            doGet(req, resp);                   // redisplay form with error
        }
    }
}
