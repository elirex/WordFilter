package com.elirex;

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

    private Words filter(String str, int matchType) {
        Words words = new Words(str);
        String replaceStr = str;
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
        String resultStr = resultWords.getOrignalString();
        String replaceWords = null;
        for(String word : matchWords.keySet()) {
            replaceWords = getReplaceWord(replaceWord, word.length());
            resultStr = resultStr.replaceAll(word, replaceWords);
        }
        resultWords.setReplaceString(resultStr);
        return resultWords;
    }

    private String getReplaceWord(String replaceWord, int lenght) {
        String replaceWords = replaceWord;
        for(int i = 0; i < lenght; i++) {
            replaceWords += replaceWord;
        }
        return replaceWords;
    }

    private int searchWordPool(String str, int beginIndex, int matchType) {
        boolean flag = false;
        int matchlength = 0;
        Map currentMap = wordPoolMap;
        int length = str.length();
        for(int i = beginIndex; i < length; i++) {
            char keyChar = str.charAt(i);
            currentMap = (Map) currentMap.get(keyChar);
            if(currentMap != null) {
                matchlength++;
                if("1".equals(currentMap.get("isEnd"))) {
                    flag = true;
                    if(matchType == TYPE_MIN_MATCH) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if(matchlength < 2 || !flag) {
            matchlength = 0;
        }
        return matchlength;
    }

}
