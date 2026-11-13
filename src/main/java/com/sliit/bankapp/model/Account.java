package com.sliit.bankapp.model;
//class//
public class Account {
    private int id, userId;
    private String accountNumber, pin;
    private String fullName, nic, address, mobile;
    private double balance;
//All field are Private(Encapsulation)//
    /* getters & setters */ 
    //Access via getters (Abstraction)//
   
    public int getId()                 {
    	return id; }
    
    public void setId(int id)          {
    	this.id = id; }
    
    public int getUserId()             { 
    	return userId; }
    
    public void setUserId(int v)       {
    	this.userId = v; }
    
    public String getAccountNumber()   { 
    	return accountNumber; }
    
    public void setAccountNumber(String v){
    	this.accountNumber = v; }
    
    public String getPin()             { 
    	return pin; }
    
    public void setPin(String v)       {
    	this.pin = v; }
    
    public String getFullName()        {
    	return fullName; }
    
    public void setFullName(String v)  {
    	this.fullName = v; }
    
    public String getNic()             { 
    	return nic; }
    
    public void setNic(String v)       { 
    	this.nic = v; }
    
    public String getAddress()         {
    	return address; }
    
    public void setAddress(String v)   {
    	this.address = v; }
    
    public String getMobile()          {
    	return mobile; }
    
    public void setMobile(String v)    {
    	this.mobile = v; }
    
    public double getBalance()         { 
    	return balance; }
    
    public void setBalance(double v)   { 
    	this.balance = v; }
}
