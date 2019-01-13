package com.demo.service;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService {
    private Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    private static final String MOSAIC = "***";

    private TrieNode treeRoot;

    public SensitiveService() {
        afterPropertiesSet();
    }

    /**
     * Build trie tree
     */
    public void afterPropertiesSet() {
        treeRoot = new TrieNode();

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(reader);
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                addWord(word);
            }
        } catch (IOException e) {
            logger.error("Wrong in afterPropertiesSet file reading: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Wrong in afterPropertiesSet file reading inputStream close: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Filter the sensitive words in text
     * @param text
     * @return
     */
    public String filter(String text) {
        StringBuilder result = new StringBuilder();
        int begin = 0, cur = 0;
        TrieNode p = treeRoot;
        while (cur < text.length()) {
            char c = text.charAt(cur);

            // Skip the characters like "&$(*$&" between the sensitive word
            if (isSymbol(c) && p != treeRoot) {
                cur++;
                continue;
            }

            // Build tree
            if (p.getSubNodes().containsKey(c)) {
                p = p.getSubNodes().get(c);
                if (p.isEnd()) {
                    result.append(MOSAIC);
                    p = treeRoot;
                }
                cur++;
            } else {
                if (p == treeRoot) {
                    result.append(c);
                } else {
                    result.append(text.substring(begin, cur));
                    p = treeRoot;
                }
                begin = ++cur;
            }
        }

        if (p != treeRoot) {
            if (p.isEnd()) {
                result.append(MOSAIC);
                p = treeRoot;
            } else {
                result.append(text.substring(begin, cur));
            }
        }

        return result.toString();
    }


    /**
     * Add new word into sensitive tree
     * @param word
     */
    private void addWord(String word) {
        word = word.trim();
        char[] wordArr = word.toCharArray();
        TrieNode p = treeRoot;
        for (int i = 0; i < wordArr.length; i++) {
            // ignore symbol in the sensitive word
            if (isSymbol(wordArr[i])) {
                continue;
            }

            // add word
            if (p.getSubNodes().containsKey(wordArr[i])) {
                p = p.getSubNodes().get(wordArr[i]);
            } else {
                TrieNode node = new TrieNode();
                p.getSubNodes().put(wordArr[i], node);
                p = node;
            }

            // set end
            if (i == wordArr.length - 1) {
                p.setEnd(true);
            }
        }
    }

    /**
     * Judge if c is a symbol
     * @param c
     * @return
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !CharUtils.isAsciiNumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    /**
     * TrieNode of trie tree
     */
    private class TrieNode {
        private boolean end = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }

        public Map<Character, TrieNode> getSubNodes() {
            return subNodes;
        }

        public void setSubNodes(Map<Character, TrieNode> subNodes) {
            this.subNodes = subNodes;
        }
    }

    public static void main(String[] args) {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.afterPropertiesSet();
        System.out.println("finish");
    }
}
