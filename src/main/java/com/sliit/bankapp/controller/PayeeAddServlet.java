package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.PayeeDAO;
import com.sliit.bankapp.model.Payee;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/** GET = form  | POST = validate + insert + redirect */
@WebServlet("/billpay/add")
public class PayeeAddServlet extends HttpServlet {

    private PayeeDAO dao;

    @Override public void init() {
        dao = new PayeeDAO(new DBConnectionManager(getServletContext()));
    }

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/payee-add.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException, IOException {

        User u = (User) req.getAttribute("currentUser");
        if (u == null) { resp.sendError(403); return; }

        String billType = req.getParameter("billType");
        String nick     = trim(req.getParameter("nick"));

        Payee p = new Payee();
        p.setUserId(u.getId());
        p.setBillType(billType);
        p.setNickName(nick);
        
        /* ─── validation ──────────────────────────────── */
        String error = null;

        switch (billType) {
            case "WATER":
            case "CURRENT":
                String accNo   = trim(req.getParameter("accountNo"));
                String accName = trim(req.getParameter("accountName"));

                if (!accNo.matches("\\d{6,20}"))
                    error = "Account number must contain 6–20 digits";
                else if (accName.isEmpty())
                    error = "Account name required";

                p.setIdentifier(accNo);
                p.setAcctName(accName);
                break;

            case "TELEPHONE":
                String mobile = trim(req.getParameter("mobileNo"));
                if (!mobile.matches("(\\+94|0)7\\d{8}"))
                    error = "Mobile number must be +947XXXXXXXX or 07XXXXXXXX";
                p.setIdentifier(mobile);
                break;

            case "CREDIT":
                String cardNo = trim(req.getParameter("cardNo"));
                if (!cardNo.matches("\\d{16}"))
                    error = "Credit-card number must be exactly 16 digits";
                p.setIdentifier(cardNo);
                break;

            default:
                error = "Unknown bill type: " + billType;
                break;
        }


        /* ─── on error → back to form ─────────────────── */
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("oldPayee", p);
            req.getRequestDispatcher("/WEB-INF/views/payee-add.jsp").forward(req, resp);
            return;
        }

        /* ─── persist ─────────────────────────────────── */
        try {
            dao.insert(p);
            resp.sendRedirect(req.getContextPath() + "/billpay");
        } catch (Exception e) {
            throw new ServletException("Cannot save payee", e);
        }
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
}
