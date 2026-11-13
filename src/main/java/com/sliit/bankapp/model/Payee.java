package com.sliit.bankapp.model;

public class Payee {
    private int id, userId;
    private String billType, identifier, acctName, nickName;

    public int    getId()          {
    	return id; }
    public void   setId(int id)    { 
    	this.id = id; }

    public int    getUserId()      { 
    	return userId; }
    public void   setUserId(int v) { 
    	this.userId = v; }

    public String getBillType()         { 
    	return billType; }
    public void   setBillType(String v) { 
    	this.billType = v; }

    public String getIdentifier()           {
    	return identifier; }
    public void   setIdentifier(String v)   { 
    	this.identifier = v; }

    public String getAcctName()             { 
    	return acctName; }
    public void   setAcctName(String v)     { 
    	this.acctName = v; }

    public String getNickName()             { 
    	return nickName; }
    public void   setNickName(String v)     { 
    	this.nickName = v; }
}
