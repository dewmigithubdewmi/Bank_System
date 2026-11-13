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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO dao;

    @Override
    public void init() {
        // getServletContext() is now defined because we're extending javax.servlet.http.HttpServlet
        dao = new UserDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            User u = dao.findByEmailAndPassword(
                    req.getParameter("email"),
                    req.getParameter("password")
            );

            if (u == null) {
                req.setAttribute("error", "Invalid email or password");
                // forward back to your JSP
                req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(req, resp);
                return;
            }

            // generate & store token
            String token = UUID.randomUUID().toString();
            TokenStore.put(token, u.getId());

            Cookie c = new Cookie("AUTH_TOKEN", token);
            c.setHttpOnly(true);
            c.setPath(req.getContextPath());
            c.setMaxAge(86_400); // 1 day
            resp.addCookie(c);

            // redirect based on role
            String next = "/home";
            if ("ADMIN".equals(u.getRole())) {
                next = "/admin/home";
            }
            resp.sendRedirect(req.getContextPath() + next);

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
