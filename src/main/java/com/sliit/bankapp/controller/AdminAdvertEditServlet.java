/* src/main/java/com/sliit/bankapp/controller/AdminAdvertEditServlet.java */
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AdvertisementDAO;
import com.sliit.bankapp.model.Advertisement;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/advert-edit")
public class AdminAdvertEditServlet extends HttpServlet{

    private AdvertisementDAO dao;
    @Override public void init(){ dao=new AdvertisementDAO(new DBConnectionManager(getServletContext())); }

    /* -------- show form with existing data -------- */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User admin=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("id"));
        try{
            Advertisement ad=dao.findByIdAndAdmin(id,admin.getId());
            if(ad==null){ resp.sendError(404); return; }
            req.setAttribute("oldAd",ad);                         // reuse the same JSP
            req.getRequestDispatcher("/WEB-INF/views/admin-advert-add.jsp")
               .forward(req,resp);
        }catch(Exception e){ throw new ServletException(e); }
    }

    /* -------- save changes -------- */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User admin=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("adId"));

        Advertisement ad=new Advertisement();
        ad.setId(id);
        ad.setAdminId(admin.getId());
        ad.setTitle(req.getParameter("title").trim());
        ad.setRules(req.getParameter("rules").trim());
        ad.setDescription(req.getParameter("description").trim());

        /* quick validation */
        if(ad.getTitle().isEmpty()||ad.getRules().isEmpty()||ad.getDescription().isEmpty()){
            req.setAttribute("error","All fields are required");
            req.setAttribute("oldAd",ad);
            req.getRequestDispatcher("/WEB-INF/views/admin-advert-add.jsp")
               .forward(req,resp);
            return;
        }

        try{
            dao.update(ad);
            resp.sendRedirect(req.getContextPath()+"/admin/home?msg=advertUpdated");
        }catch(Exception e){ throw new ServletException("Cannot update advert",e); }
    }
}
