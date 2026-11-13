// src/main/java/com/sliit/bankapp/controller/AdminHomeServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AdvertisementDAO;      // ★ fixed import
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Admin landing page.  The AuthFilter already guarantees that only users
 * with role ADMIN reach any /admin/* URL, so no extra role-checks are needed.
 */
@WebServlet("/admin/home")
public class AdminHomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /* use the correct DAO class */
    private transient AdvertisementDAO advertDAO;

    @Override
    public void init() {
        advertDAO = new AdvertisementDAO(
            new DBConnectionManager(getServletContext())
        );
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        User admin = (User) req.getAttribute("currentUser");  // set by AuthFilter

        try {
            // may return empty list if no adverts yet
            req.setAttribute("adverts", advertDAO.findAll());
        } catch (Exception e) {
            /* If the DB is down we just show "no adverts" –
               you can log the exception if you like. */
            req.setAttribute("adverts", null);
        }

        req.setAttribute("admin", admin);   // optional greeting

        req.getRequestDispatcher("/WEB-INF/views/admin-home.jsp")
           .forward(req, resp);
    }
}
