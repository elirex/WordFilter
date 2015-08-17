package com.elirex;

import java.io.*;
import java.util.*;

/**
 * Created by Sheng (Elirex) on 2015/8/17.
 */
class FilterWordsPool {

    public static final String LOG_TAG = FilterWordsPool.class.getSimpleName();

    private static FilterWordsPool filterWordsPool = null;

    private Map wordPoolMap;

    private int totalWords;

    private FilterWordsPool() {

    }

    private FilterWordsPool(String... words) {
        addWords(words);
    }

    private FilterWordsPool(File... files) {
        addWordsFromFile(files);
    }

    public static FilterWordsPool initWordPool() {
        if(filterWordsPool == null) {
            filterWordsPool = new FilterWordsPool();
        }
        return filterWordsPool;
    }

    public static FilterWordsPool initWordPool(String... words) {
        if(filterWordsPool == null) {
            filterWordsPool = new FilterWordsPool(words);
        } else {
            filterWordsPool.addWords(words);
        }
        return filterWordsPool;
    }

    public static FilterWordsPool initWordPool(File... files) {
        if(filterWordsPool == null) {
            filterWordsPool = new FilterWordsPool(files);
        } else {
            filterWordsPool.addWordsFromFile(files);
        }
        return filterWordsPool;
    }

    /**
     * Returns word pool map.
     * @return
     */
    public Map getWordPoolMap() {
        return wordPoolMap;
    }

    /**
     * Adds word to word pool map.
     * @param words
     */
    public void addWords(String... words) {
        Set<String> wordSet = new HashSet<String>();
        for(String word : words) {
            totalWords++;
            wordSet.add(word);
        }
        buildWordPoolMap(wordSet);
    }

    /**
     * Returns number of word in pool.
     * @return
     */
    public int getTotalWords() {
       return totalWords;
    }

    /**
     * Adds words to pool map from file.
     * @param files
     */
    public void addWordsFromFile(File... files) {
        Set<String> wordSet = new HashSet<String>();
        for(File file : files) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), "UTF-8"));
                String line = "";
                while((line = reader.readLine()) != null) {
                    line = line.trim();
                    if(line.startsWith("\uFEFF")) {
                        line = line.replace("\uFEFF", "");
                    }
                    wordSet.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        System.out.println(LOG_TAG + " Closing reader error");
                    }
                }
            }
        }
        buildWordPoolMap(wordSet);
    }

    /**
     * Builds the word pool map
     * @param wordSet
     */
    private void buildWordPoolMap(Set<String> wordSet) {
        if(filterWordsPool == null) {
            wordPoolMap = new HashMap(wordSet.size());
        }
        Map currentMap = null;
        Map<String, String> newMap = null;
        for(String key : wordSet) {
            totalWords++;
            currentMap = wordPoolMap;
            int length = key.length();
            for(int i = 0; i < length; i++) {
                char keyChar = key.charAt(i);
                Object tmpMap = currentMap.get(keyChar);
                if(tmpMap != null) {
                    currentMap = (Map) tmpMap;
                } else {
                    newMap = new HashMap<String, String>();
                    newMap.put("isEnd", "0");
                    currentMap.put(keyChar, newMap);
                    currentMap = newMap;
                }

                if(i == length - 1) {
                    currentMap.put("isEnd", "1");
                }
            }
        }
    }

}
