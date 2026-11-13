// src/main/java/com/sliit/bankapp/controller/HomeServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.dao.AdvertisementDAO;              // ★ NEW ★
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /* The AuthFilter attaches the logged-in user here */
        User u = (User) req.getAttribute("currentUser");

        /* One shared DB connection-helper for all DAOs in this request */
        DBConnectionManager dbm = new DBConnectionManager(getServletContext());

        try {
            /* ---- 1) Accounts that belong to the user ---- */
            AccountDAO accountDAO = new AccountDAO(dbm);
            req.setAttribute("accounts", accountDAO.findByUser(u.getId()));

            /* ---- 2) All published adverts (for everyone) ---- */
            AdvertisementDAO advertDAO = new AdvertisementDAO(dbm);     // ★ NEW ★
            req.setAttribute("publicAds", advertDAO.findAll());         // ★ NEW ★

        } catch (Exception e) {
            /* Graceful fallback – show empty lists if DB fails */
            req.setAttribute("accounts",  null);
            req.setAttribute("publicAds", null);                        // ★ NEW ★
        }

        /* Hand off to the JSP */
        req.getRequestDispatcher("/WEB-INF/views/home.jsp")
           .forward(req, resp);
    }
}
