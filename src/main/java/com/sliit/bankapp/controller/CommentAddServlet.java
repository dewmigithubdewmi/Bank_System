package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.CommentDAO;
import com.sliit.bankapp.model.Comment;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment/add")
public class CommentAddServlet extends HttpServlet {

    private CommentDAO dao;
    @Override public void init(){ dao=new CommentDAO(new DBConnectionManager(getServletContext())); }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User u=(User)req.getAttribute("currentUser");
        if(u==null){
        	resp.sendError(403);return;}

        int adId=Integer.parseInt(req.getParameter("adId"));
        String text=req.getParameter("content").trim();
        if(text.isEmpty()){ 
        	resp.sendRedirect(req.getContextPath()+"/ad?id="+adId);
        	return; 
        	}

        Comment c=new Comment();
        c.setAdId(adId); c.setUserId(u.getId()); c.setContent(text);

        try{
        	dao.insert(c);
       
        }catch(Exception e){ 
        	throw new ServletException(e);}
        resp.sendRedirect(req.getContextPath()+"/ad?id="+adId);
    }
}
