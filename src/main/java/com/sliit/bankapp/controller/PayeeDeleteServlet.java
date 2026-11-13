// src/main/java/com/sliit/bankapp/controller/PayeeDeleteServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.PayeeDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/billpay/delete")
public class PayeeDeleteServlet extends HttpServlet {
    private PayeeDAO dao;
    @Override public void init(){ dao=new PayeeDAO(new DBConnectionManager(getServletContext())); }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        User u=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("payeeId"));
        try{
            dao.delete(id,u.getId());
            resp.sendRedirect(req.getContextPath()+"/billpay");
        }catch(Exception e){ throw new ServletException(e); }
    }
}
