package com.sliit.bankapp.model;

import java.sql.Timestamp;

public class Comment {
    private int id, adId, userId;
    private String content, author;   // author = first+last from JOIN
    private Timestamp createdAt;

    /* getters / setters */
    public int getId(){
    	return id;}         
    public void setId(int v){
    	id=v;}
    public int getAdId(){
    	return adId;}    
    public void setAdId(int v){
    	adId=v;}
    public int getUserId(){
    	return userId;}  
    public void setUserId(int v){
    	userId=v;}
    public String getContent(){
    	return content;}
    public void setContent(String v){
    	content=v;}
    public String getAuthor(){
    	return author;}  
    public void setAuthor(String v){
    	author=v;}
    public Timestamp getCreatedAt(){
    	return createdAt;} 
    public void setCreatedAt(Timestamp t){
    	createdAt=t;}
}
