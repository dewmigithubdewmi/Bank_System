package com.sliit.bankapp.auth;

import com.sliit.bankapp.dao.UserDAO;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig config) {
        // Initialize our DAO using the same DBConnectionManager
        userDAO = new UserDAO(new DBConnectionManager(config.getServletContext()));
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();

        // ─── PUBLIC (no auth required) ─────────────────────────────────
        boolean publicPath =
               path.startsWith("/css/")      // static
            || path.startsWith("/js/")
            || path.equals("/login")       // login flow
            || path.equals("/login.jsp")
            || path.equals("/register")    // user-signup
            || path.equals("/register.jsp")
            || path.equals("/admin-register")    // admin-signup
            || path.equals("/admin-register.jsp");

        if (publicPath) {
            chain.doFilter(request, response);
            return;
        }

        // ─── LOOK UP AUTH_TOKEN COOKIE ────────────────────────────────
        String token = Optional.ofNullable(req.getCookies())
            .flatMap(arr ->
                {
                    for (Cookie c : arr) {
                        if ("AUTH_TOKEN".equals(c.getName()))
                            return Optional.of(c.getValue());
                    }
                    return Optional.empty();
                }
            )
            .orElse(null);

        // ─── FETCH CURRENT USER FROM TOKEN ─────────────────────────────
        User current = null;
        if (token != null) {
            Integer userId = TokenStore.get(token);
            if (userId != null) {
                try {
                    current = userDAO.findById(userId);
                } catch (Exception ignored) {}
            }
        }
        if (current != null) {
            req.setAttribute("currentUser", current);
        }

        // ─── ACCESS CONTROL ───────────────────────────────────────────
        boolean adminArea = path.startsWith("/admin/");
        if (adminArea) {
            // Only ADMIN role may access /admin/*
            if (current == null || !"ADMIN".equals(current.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        } else {
            // All other (non-public) paths require at least a logged-in user
            if (current == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to clean up
    }
}
