package com.elirex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nickwang on 2015/8/17.
 */
public class Words {

    private String orignalString;
    private String replaceString;
    private boolean hasFilterWord;
    private HashMap<String, ArrayList<Position>> filterWord;

    public Words(String str) {
        orignalString = str;
    }

    public void setHasFilterWord(boolean b) {
        hasFilterWord = b;
    }

    public void setReplaceString(String replace) {
        replaceString = replace;
    }

    public String getOrignalString() {
        return orignalString;
    }

    public String getReplaceString() {
        return replaceString;
    }

    public boolean hasFilterWord() {
        return hasFilterWord;
    }

    public HashMap<String, ArrayList<Position>> getMatchWords() {
        return filterWord;
    }

    public void addMatchWord(String matchWord, int begin, int end) {
        if(filterWord == null) {
            filterWord = new HashMap<String, ArrayList<Position>>();
        }
        Position position = new Position(begin, end);
        if(filterWord.containsKey(matchWord)) {
           filterWord.get(matchWord).add(position);
        } else {
            ArrayList<Position> newPositionList = new ArrayList<Position>();
            newPositionList.add(position);
            filterWord.put(matchWord, newPositionList);
        }

    }

    public class Position {

        private int begin;
        private int end;

        public Position(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public int begin() {
            return begin;
        }

        public int end() {
            return end;
        }

    }

}
