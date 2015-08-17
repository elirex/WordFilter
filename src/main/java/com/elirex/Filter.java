package com.elirex;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sheng (Elirex) on 2015/8/17.
 */
public class Filter {

    private static final String LOG_TAG = Filter.class.getSimpleName();

    public static final int TYPE_MIN_MATCH = 1;
    public static final int TYPE_MAX_MATCH = 2;

    private FilterWordsPool filterWordsPool = null;
    private Map wordPoolMap;
    private String replace = "*";

    public Filter() {
        filterWordsPool = FilterWordsPool.initWordPool();
    }

    public Filter(String... words) {
        filterWordsPool = FilterWordsPool.initWordPool(words);
    }

    public Filter(File... files) {
        filterWordsPool = FilterWordsPool.initWordPool(files);
    }

    public int getTotalWords() {
        return filterWordsPool.getTotalWords();
    }

    public void addWords(String... words) {
        filterWordsPool.addWords(words);
    }

    public void addWordsFromFile(File... files) {
        filterWordsPool.addWordsFromFile(files);
    }

    /**
     * Sets replace word (Default is "*").
     * @param str
     */
    public void setReplace(String str) {
        replace = str;
    }

    public Words filter(String str, int matchType) {
        wordPoolMap = filterWordsPool.getWordPoolMap();
        Words words = new Words(str);
        int length = str.length();
        for(int i = 0; i < length; i++) {
            int matchLength = searchWordPool(str, i, matchType);
            if(matchLength > 0) {
                words.setHasFilterWord(true);
                words.addMatchWord(str.substring(i, i + matchLength), i, i + matchLength);
                i = i + matchLength - 1;
            }
        }
        words = replace(words, replace);
        return words;
    }

    public Words replace(Words words, String replaceWord) {
        Words resultWords = words;
        HashMap<String, ArrayList<Words.Position>> matchWords = resultWords.getMatchWords();
        if(matchWords != null && matchWords.size() >= 0) {
            String resultStr = resultWords.getOrignalString();
            String replaceWords = null;
            for (String word : matchWords.keySet()) {
                replaceWords = getReplaceWord(replaceWord, word.length());
                resultStr = resultStr.replaceAll(word, replaceWords);
            }
            resultWords.setReplaceString(resultStr);
        }
        return resultWords;
    }

    private String getReplaceWord(String replaceWord, int lenght) {
        String replaceWords = "";
        for(int i = 0; i < lenght; i++) {
            replaceWords += replaceWord;
        }
        return replaceWords;
    }

    private int searchWordPool(String str, int beginIndex, int matchType) {
        boolean flag = false;
        int matchlength = 0;
        Map currentMap = wordPoolMap;
        Map preMap = null;
        int length = str.length();
        int index = 0;
        for(int i = beginIndex; i < length; i++) {
            char keyChar = str.charAt(i);
            // currentMap = (Map) currentMap.get(keyChar);
            if(currentMap.get(keyChar) != null) {
            // if(currentMap != null) {
                preMap = currentMap;
                currentMap = (Map) currentMap.get(keyChar);
                matchlength++;
                if("1".equals(currentMap.get("isEnd"))) {
                    flag = true;
                    if(matchType == TYPE_MIN_MATCH) {
                        break;
                    }
                }
                index = i;
            } else {
                if(preMap != null && preMap.get(keyChar) != null) {
                    matchlength++;
                    currentMap = preMap;
                } else {
                    break;
                }
            }
        }
        if(matchlength < 2 && flag) {
            if(str.length() > 1) {
                if(beginIndex > 0) {
                    char preChar = str.charAt(index -1);
                    char nextChar = str.charAt(index + 1);
                    boolean pre = preChar != 0 || preChar != ' ' || preChar != '\n';
                    boolean next = nextChar != 0 || nextChar != ' ' || nextChar != '\n';
                    if(pre && next) {
                        matchlength = 0;
                    }
                }
            }
        } else {
            matchlength = 0;
        }
        return matchlength;
    }

}
