package com.sliit.bankapp.controller;

import com.sliit.bankapp.auth.TokenStore;
import com.sliit.bankapp.dao.UserDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private UserDAO dao;
    @Override public void init() { dao = new UserDAO(new DBConnectionManager(getServletContext())); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User u = new User();
        u.setFirstName(req.getParameter("firstName"));
        u.setLastName (req.getParameter("lastName"));
        u.setEmail    (req.getParameter("email"));
        u.setPassword (req.getParameter("password"));
        u.setRole("NORMAL");

        try { dao.insert(u); }
        catch (Exception ex) { throw new ServletException(ex); }

        String token = UUID.randomUUID().toString();
        TokenStore.put(token, u.getId());
        Cookie c = new Cookie("AUTH_TOKEN", token);
        c.setHttpOnly(true); c.setPath(req.getContextPath()); c.setMaxAge(86_400);
        resp.addCookie(c);
        resp.sendRedirect(req.getContextPath()+"/home");
    }
}
