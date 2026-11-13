package com.sliit.bankapp.controller;

import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.dao.PayeeDAO;
import com.sliit.bankapp.model.Payee;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/billpay/pay")
public class PayBillServlet extends HttpServlet {

    private PayeeDAO    pDao;
    private AccountDAO  aDao;

    @Override public void init() {
        DBConnectionManager dbm = new DBConnectionManager(getServletContext());
        pDao = new PayeeDAO(dbm);
        aDao = new AccountDAO(dbm);
    }

    /* ─── Show form ───────────────────────────────────────── */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int payeeId = paramToInt(req.getParameter("id"), resp);
        if (payeeId == 0) return;

        User u = (User) req.getAttribute("currentUser");
        try {
            Payee payee = pDao.findByIdAndUser(payeeId, u.getId());
            if (payee == null) { resp.sendError(404); return; }

            req.setAttribute("payee",   payee);
            req.setAttribute("accounts", aDao.findByUser(u.getId()));
            req.getRequestDispatcher("/WEB-INF/views/pay-bill.jsp")
               .forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    /* ─── Handle payment ─────────────────────────────────── */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int payeeId  = paramToInt(req.getParameter("payeeId"),  resp);
        int fromId   = paramToInt(req.getParameter("fromId"),   resp);
        double amt   = Double.parseDouble(req.getParameter("amount"));

        if (amt <= 0) {
            req.setAttribute("error", "Amount must be positive");
            doGet(req, resp);      // redisplay form
            return;
        }

        try {
            aDao.debit(fromId, amt);         // ← real money movement
            // here you could also log the payment into a separate table

            resp.sendRedirect(req.getContextPath() + "/billpay");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    /* helper */
    private int paramToInt(String s, HttpServletResponse resp) throws IOException {
        try { return Integer.parseInt(s); }
        catch (Exception ex) { resp.sendError(400); return 0; }
    }
}
