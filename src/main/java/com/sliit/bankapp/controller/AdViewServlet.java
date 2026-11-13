// src/main/java/com/sliit/bankapp/controller/AdViewServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AdvertisementDAO;
import com.sliit.bankapp.dao.CommentDAO;                       // ★ NEW ★
import com.sliit.bankapp.model.Advertisement;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/ad")                 // /ad?id=123
public class AdViewServlet extends HttpServlet {

    private AdvertisementDAO advertDAO;
    private CommentDAO        commentDAO;                     // ★ NEW ★

    @Override public void init() {
        DBConnectionManager dbm = new DBConnectionManager(getServletContext());
        advertDAO  = new AdvertisementDAO(dbm);
        commentDAO = new CommentDAO(dbm);                     // ★ NEW ★
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        int id;
        try { id = Integer.parseInt(req.getParameter("id")); }
        catch (Exception ex) { resp.sendError(400); return; }

        try {
            Advertisement ad = advertDAO.findById(id);        // public lookup
            if (ad == null) { resp.sendError(404); return; }

            /* -------- attach data needed by the JSP -------- */
            req.setAttribute("ad", ad);
            req.setAttribute("comments", commentDAO.findByAd(id));      // ★ NEW ★

            req.getRequestDispatcher("/WEB-INF/views/ad-view.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Cannot load advertisement", e);
        }
    }
}
