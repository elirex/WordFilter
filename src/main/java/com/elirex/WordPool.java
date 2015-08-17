package com.elirex;

import java.io.*;
import java.util.*;

/**
 * Created by Sheng (Elirex) on 2015/8/17.
 */
public class WordPool {

    public static final String LOG_TAG = WordPool.class.getSimpleName();

    private static WordPool wordPool = null;

    private Map wordPoolMap;

    private WordPool(String... paths) {
        buildWordPoolMap(paths);
    }

    public static WordPool getInstance(String... paths) {
        if(wordPool == null) {
            wordPool = new WordPool();
        }
        return wordPool;
    }

    /**
     * Returns word pool map
     * @return
     */
    public Map getWordPoolMap() {
        return wordPoolMap;
    }

    private void buildWordPoolMap(String... paths) {
        Set<String> wordSet = readWordFromFile(paths);
        addWordToPoolMap(wordSet);
    }

    private void addWordToPoolMap(Set<String> wordSet) {
        if(wordPool == null) {
            wordPoolMap = new HashMap(wordSet.size());
        }
        Map currentMap = null;
        Map<String, String> newMap = null;
        for(String key : wordSet) {
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
                    currentMap.put(keyChar, currentMap);
                    currentMap = newMap;
                }

                if(i == length - 1) {
                    currentMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * Reads the filtered words from file.
     * @param paths
     * @return
     */
    private Set<String> readWordFromFile(String... paths) {
        Set<String> wordSet = new HashSet<String>();
        for(String path : paths) {
            File file = new File(path);
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
        return wordSet;
    }

}
