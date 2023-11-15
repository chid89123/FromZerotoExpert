package com.wang.fromzerotoexpert.util;

import sun.text.normalizer.Trie;

import java.util.HashMap;
import java.util.Map;

//前缀树
public class TrieNode {

    // 关键词结束标识
    private boolean isEnd;

    // 子节点(key是下级字符,value是下级节点)
    private Map<Character, TrieNode> children;


    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public TrieNode() {
        children = new HashMap<Character, TrieNode>();
        isEnd = false;
    }

    /**
     * 插入敏感词， 创建前缀树
     * @param keyword 敏感词
     */
    public void insert(String keyword) {
        TrieNode node = this;
        for (int i = 0; i < keyword.length(); i++) {
            char ch = keyword.charAt(i);
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
            if (i == keyword.length() - 1) {
                node.isEnd = true;
            }
        }
    }

    /**
     * 检查是否是敏感词
     * @param target 目标字符串
     * @return true 包含 false 不包含
     */
    public boolean check(String target) {
        for (int i = 0; i < target.length(); i++) {
            char ch = target.charAt(i);
            TrieNode node = this;
            if (node.children.containsKey(ch)) {
                node = node.children.get(ch);
                if (node.isEnd()) {
                    return true;
                }
                else {
                    for (int j = i + 1; j < target.length(); j++) {
                        char c = target.charAt(j);
                        if (node.children.containsKey(c)) {
                            node = node.children.get(c);
                            if (node.isEnd()) {
                                return true;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
}
