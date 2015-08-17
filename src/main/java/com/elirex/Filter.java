package com.elirex;

import java.io.File;
import java.util.Map;

/**
 * Created by Sheng (Elirex) on 2015/8/17.
 */
public class Filter {

    private static final String LOG_TAG = Filter.class.getSimpleName();

    public static final int TYPE_MIN_MATCH = 1;
    public static final int TYPE_MAX_MATCH = 2;

    private WordPool wordPool = null;
    private Map wordPoolMap;

    public Filter() {
        wordPool = WordPool.initWordPool();
    }

    public Filter(String... words) {
        wordPool = WordPool.initWordPool(words);
    }

    public Filter(File... files) {
        wordPool = WordPool.initWordPool(files);
    }

    public int getTotalWords() {
        return wordPool.getTotalWords();
    }

    private int searchWordPool(String txt, int beginIndex, int matchType) {
        boolean flag = false;
        int matchlength = 0;
        Map currentMap = wordPoolMap;
        int length = txt.length();
        for(int i = beginIndex; i < length; i++) {
            char keyChar = txt.charAt(i);
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
