/* src/main/java/com/sliit/bankapp/controller/AdminAdvertDeleteServlet.java */
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AdvertisementDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/advert-delete")
public class AdminAdvertDeleteServlet extends HttpServlet {

    private AdvertisementDAO dao;
    @Override public void init(){ dao=new AdvertisementDAO(new DBConnectionManager(getServletContext())); }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User admin=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("id"));
        try{
            dao.delete(id,admin.getId());
            resp.sendRedirect(req.getContextPath()+"/admin/home?msg=advertDeleted");
        }catch(Exception e){ throw new ServletException("Cannot delete advert",e); }
    }
}
