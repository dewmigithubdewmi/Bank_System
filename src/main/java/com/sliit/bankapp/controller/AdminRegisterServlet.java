// src/main/java/com/sliit/bankapp/controller/AdminRegisterServlet.java
package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.UserDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/admin-register")            // ← public URL
public class AdminRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;   // remove IDE warning
    private UserDAO dao;

    @Override public void init() {
        dao = new UserDAO(new DBConnectionManager(getServletContext()));
    }

    /* ───── GET : show blank form ───────────────────────── */
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        // nothing special to prepare – simply forward
        req.getRequestDispatcher("/WEB-INF/views/admin-register.jsp")
           .forward(req, resp);
    }

    /* ───── POST : create admin account ─────────────────── */
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        // build User object from form fields
        User u = new User();
        u.setFirstName(req.getParameter("firstName"));
        u.setLastName (req.getParameter("lastName"));
        u.setEmail    (req.getParameter("email").trim().toLowerCase());
        u.setPassword (req.getParameter("password"));
        u.setRole("ADMIN");

        try {
            dao.insert(u);                             // may raise duplicate-e-mail
            // success → go to normal login
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
        catch (Exception ex) {
            /* duplicate e-mail? give friendly feedback instead of 500 */
            if (ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
                req.setAttribute("error",
                        "That e-mail address is already registered.");
                req.setAttribute("oldAdmin", u);      // so JSP can refill fields
                req.getRequestDispatcher("/WEB-INF/views/admin-register.jsp")
                   .forward(req, resp);
                return;
            }
            // anything else – still an internal error
            throw new ServletException("Could not register admin", ex);
        }
    }
}
