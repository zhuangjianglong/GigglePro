package com.whisper.pro.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class TextCache {

    public static final String ORI_TEXT = "TEXT";
    public static final String ORI_AUDIO = "AUDIO";

    /**
     * 三级Map缓存数据
     *  key=语言标识，value=
     *      key=文本标识，value=
     *          key=来源TEXT/AUDIO，value=
     */
    private static Map<String,Map<Integer, Map<String,String>>> T_M = new ConcurrentHashMap<>();

    public static String get(String lan, Integer id, String ori){
        Map<Integer, Map<String, String>> lanMap = T_M.get(lan);
        if(lanMap != null){
            Map<String, String> idMap = lanMap.get(id);
            if(idMap != null){
                return idMap.get(ori);
            }
        }
        return null;
    }

    public static void set(String lan, Integer id, String ori, String text){
        Map<Integer, Map<String, String>> lanMap = T_M.get(lan);
        if(lanMap == null){
            setLanMap(lan,id,ori,text);
        }else{
            Map<String, String> idMap = lanMap.get(id);
            if(idMap == null){
                setIdMap(lanMap,id,ori,text);
            }else{
                idMap.put(ori,text);
            }
        }
    }

    private static void setLanMap(String lan, Integer id, String ori, String text){
        Map<Integer, Map<String, String>> lanMap = new ConcurrentHashMap<>();
        Map<String, String> idMap = new ConcurrentHashMap<>();;
        idMap.put(ori, text);
        lanMap.put(id, idMap);
        T_M.put(lan,lanMap);
    }

    private static void setIdMap(Map<Integer, Map<String, String>> lanMap, Integer id, String ori, String text){
        Map<String, String> idMap = new ConcurrentHashMap<>();;
        idMap.put(ori, text);
        lanMap.put(id, idMap);
    }

}
