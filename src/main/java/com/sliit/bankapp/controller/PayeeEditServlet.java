package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.PayeeDAO;
import com.sliit.bankapp.model.Payee;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/billpay/edit")
public class PayeeEditServlet extends HttpServlet {

    private PayeeDAO dao;

    @Override
    public void init() {
        dao = new PayeeDAO(new DBConnectionManager(getServletContext()));
    }

    /*
     * Step 1: Show the form populated with the existing Payee.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getAttribute("currentUser");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Safely parse the `id` query-param
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int payeeId;
        try {
            payeeId = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Load and verify ownership
        Payee existing;
        try {
            existing = dao.findByIdAndUser(payeeId, user.getId());
        } catch (Exception e) {
            throw new ServletException(e);
        }
        if (existing == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Forward to the shared JSP (payee-add.jsp), with `oldPayee` set
        req.setAttribute("oldPayee", existing);
        req.getRequestDispatcher("/WEB-INF/views/payee-add.jsp")
           .forward(req, resp);
    }

    /*
     * Step 2: Process the form submission and persist updates.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getAttribute("currentUser");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Must have come from our form’s hidden field
        String idParam = req.getParameter("payeeId");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int payeeId;
        try {
            payeeId = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String billType  = req.getParameter("billType");
        String nick      = trim(req.getParameter("nick"));

        String identifier = null;
        String acctName   = null;
        String error      = null;

        switch (billType) {
            case "WATER":
            case "CURRENT":
                identifier = trim(req.getParameter("accountNo"));
                acctName   = trim(req.getParameter("accountName"));
                if (!identifier.matches("\\d{6,20}")) {
                    error = "Account number must contain 6–20 digits";
                } else if (acctName.isEmpty()) {
                    error = "Account name required";
                }
                break;

            case "TELEPHONE":
                identifier = trim(req.getParameter("mobileNo"));
                acctName   = "";  // not used for telephone
                if (!identifier.matches("(\\+94|0)7\\d{8}")) {
                    error = "Mobile number must be +947XXXXXXXX or 07XXXXXXXX";
                }
                break;

            case "CREDIT":
                identifier = trim(req.getParameter("cardNo"));
                acctName   = "";  // not used for credit
                if (!identifier.matches("\\d{16}")) {
                    error = "Credit-card number must be exactly 16 digits";
                }
                break;

            default:
                error = "Unknown bill type";
        }

        // Reconstruct a Payee to re-show on error
        Payee p = new Payee();
        p.setId(payeeId);
        p.setUserId(user.getId());
        p.setBillType(billType);
        p.setIdentifier(identifier);
        p.setAcctName(acctName);
        p.setNickName(nick);

        if (error != null) {
            // On validation error, bounce back to the form
            req.setAttribute("error", error);
            req.setAttribute("oldPayee", p);
            req.getRequestDispatcher("/WEB-INF/views/payee-add.jsp")
               .forward(req, resp);
            return;
        }

        // All good → persist and redirect
        try {
            dao.update(p);
            resp.sendRedirect(req.getContextPath() + "/billpay");
        } catch (Exception e) {
            throw new ServletException("Cannot update payee", e);
        }
    }

    private static String trim(String s) {
        return (s == null) ? "" : s.trim();
    }
}
