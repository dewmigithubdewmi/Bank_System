// src/main/java/com/sliit/bankapp/controller/AdminAdvertCreateServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AdvertisementDAO;
import com.sliit.bankapp.model.Advertisement;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/advert-add")
public class AdminAdvertCreateServlet extends HttpServlet {

    private AdvertisementDAO dao;
    @Override public void init() {
        dao = new AdvertisementDAO(new DBConnectionManager(getServletContext()));
    }

    /* show blank form */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        req.getRequestDispatcher("/WEB-INF/views/admin-advert-add.jsp")
           .forward(req, resp);
    }

    /* save advert */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User admin = (User) req.getAttribute("currentUser");   // set by AuthFilter

        Advertisement ad = new Advertisement();
        ad.setAdminId(admin.getId());
        ad.setTitle     (req.getParameter("title").trim());
        ad.setRules     (req.getParameter("rules").trim());
        ad.setDescription(req.getParameter("description").trim());

        /* tiny validation */
        if (ad.getTitle().isEmpty() || ad.getRules().isEmpty() || ad.getDescription().isEmpty()) {
            req.setAttribute("error","All fields are required");
            req.setAttribute("oldAd", ad);
            doGet(req, resp);
            return;
        }

        try {
            dao.insert(ad);
            resp.sendRedirect(req.getContextPath()+"/admin/home?msg=advertSaved");
        } catch (Exception e) {
            throw new ServletException("Cannot save advert", e);
        }
    }
}
