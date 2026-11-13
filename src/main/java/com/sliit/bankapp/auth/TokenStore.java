package com.sliit.bankapp.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TokenStore {
    private static final Map<String,Integer> TOKENS = new ConcurrentHashMap<>();
    private TokenStore() {}
    public static void put(String t,int id){ TOKENS.put(t,id); }
    public static Integer get(String t){ return TOKENS.get(t); }
    public static void remove(String t){ TOKENS.remove(t); }
}
