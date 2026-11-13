package com.sliit.bankapp.controller;
//class//
import com.sliit.bankapp.dao.AccountDAO;
import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Removes ( *un-links* ) an account from the currently-logged-in user.
 * • NORMAL  users may only remove **their own** accounts.  
 * • ADMIN   users may remove / unlink **any** account.
 *
 * Expects: POST  /account/remove   with parameter  accountId
 *current.getRole()
	current.getId()
	acc.getUserId()
	These get methods show encapsulation—accessing private data through public getters.
 	This hides internal variables and protects data integrity
 */
@WebServlet("/account/remove")
public class AccountRemoveServlet extends HttpServlet {

    private AccountDAO dao;

    @Override
    public void init() {//new object//
        dao = new AccountDAO(new DBConnectionManager(getServletContext()));
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        /* ---------- current user ---------- */
        User current = (User) req.getAttribute("currentUser");
        if (current == null) {           // AuthFilter should prevent this
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        /* ---------- parse accountId ---------- */
        int accountId;
        try {
            accountId = Integer.parseInt(req.getParameter("accountId"));
        } catch (NumberFormatException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad accountId");
            return;
        }

        /* ---------- authorisation check ---------- */
        try {
            Account acc = dao.findById(accountId);
            if (acc == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean admin = "ADMIN".equals(current.getRole());
            boolean owns  = acc.getUserId() == current.getId();

            if (!admin && !owns) {           // normal users ⇒ must own it
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            /* ---------- unlink ---------- */
            dao.unlink(accountId);           // set user_id = NULL
            resp.sendRedirect(req.getContextPath() + "/home");

        } catch (Exception e) {
            throw new ServletException("Unable to remove account", e);
        }
    }
}
