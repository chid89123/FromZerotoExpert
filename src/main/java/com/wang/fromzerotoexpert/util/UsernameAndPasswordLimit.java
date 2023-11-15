package com.wang.fromzerotoexpert.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UsernameAndPasswordLimit {

    private static TrieNode root;

    static {
        root = new TrieNode();
        List<String> impolitePhrases = new ArrayList(Arrays.asList("傻逼", "草泥马","草"));
        for (String s: impolitePhrases) {
            root.insert(s);
        }
    }

    public int limitUsername(String username) {
        if (root.check(username)) {
            //存在敏感词
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param password
     * @return  返回0 成功 强度不够返回2
     */
    public int limitPassword(String password) {
        if (password.length() < 6 || password.length() > 20) {
            return 2;
        }
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,20}$");
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return 0;
        }
        return 2;
    }

}
