package com.ewha.pumpkin.academy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, T> ts = new HashMap<>();

    ts {
        ts.put("admin", new T(""));
    }

    public static void addUser(T t) {
        ts.put(t.getId(), t);
    }

    public static T findUserById(String userId) {
        return ts.get(userId);
    }

    public static Collection<T> findAll() {
        return ts.values();
    }
}
