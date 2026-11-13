package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.CommentDAO;
import com.sliit.bankapp.model.Comment;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment/edit")
public class CommentEditServlet extends HttpServlet {

    private CommentDAO dao;
    @Override public void init(){ dao=new CommentDAO(new DBConnectionManager(getServletContext())); }

    /* show small edit form */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User u=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("id"));
        try{
            Comment c=dao.findById(id);
            if(c==null || c.getUserId()!=u.getId()){ 
            	resp.sendError(403);
            	return; }
            
            req.setAttribute("comment",c);
            req.getRequestDispatcher("/WEB-INF/views/comment-edit.jsp")
               .forward(req,resp);
            
        }catch(Exception e){ 
        	throw new ServletException(e);}
    }

    /* save updated text */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {

        User u=(User)req.getAttribute("currentUser");
        int id=Integer.parseInt(req.getParameter("id"));
        String text=req.getParameter("content").trim();

        Comment c=new Comment();
        c.setId(id); c.setUserId(u.getId()); c.setContent(text);

        try{ 
        	dao.update(c); 
        	}
        catch(Exception e){
        	throw new ServletException(e); }

        int adId=Integer.parseInt(req.getParameter("adId"));
        resp.sendRedirect(req.getContextPath()+"/ad?id="+adId);
    }
}
