package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.CommentDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment/delete")
public class CommentDeleteServlet extends HttpServlet {

    private CommentDAO dao;
    @Override public void init(){ dao=new CommentDAO(new DBConnectionManager(getServletContext())); }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User u=(User)req.getAttribute("currentUser");
        int id   = Integer.parseInt(req.getParameter("id"));
        int adId = Integer.parseInt(req.getParameter("adId"));
        try{
        	dao.delete(id,u.getId()); 
        	
        	
        }catch(Exception e){ 
        	throw new ServletException(e); }
        resp.sendRedirect(req.getContextPath()+"/ad?id="+adId);
    }
}
