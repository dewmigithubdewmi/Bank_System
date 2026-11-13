package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.PayeeDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/** Lists current user’s payees and offers “Add New Payee”. */
@WebServlet("/billpay")
public class BillPayHomeServlet extends HttpServlet {

    private PayeeDAO dao;

    @Override public void init() {
        dao = new PayeeDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException, IOException {

        User u = (User) req.getAttribute("currentUser");
        try { req.setAttribute("payees", dao.findByUser(u.getId())); }
        catch (Exception e) { req.setAttribute("payees", null); }

        req.getRequestDispatcher("/WEB-INF/views/billpay-home.jsp")
           .forward(req, resp);
    }
}
