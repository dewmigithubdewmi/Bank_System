package com.sliit.bankapp.controller;

import com.sliit.bankapp.auth.TokenStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Remove token from our in-memory store
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("AUTH_TOKEN".equals(c.getName())) {
                    TokenStore.remove(c.getValue());
                    break;
                }
            }
        }

        // 2) Clear the cookie in the browser
        Cookie clear = new Cookie("AUTH_TOKEN", "");
        clear.setPath(req.getContextPath());
        clear.setMaxAge(0);
        resp.addCookie(clear);

        // 3) Redirect to login page
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
