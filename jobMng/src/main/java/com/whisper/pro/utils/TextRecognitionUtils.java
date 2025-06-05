package com.whisper.pro.utils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.EditDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class TextRecognitionUtils {

    /**
     * Jaccard相似度
     *  集合的交集与集合的并集的比例。
     * @param str
     * @param sttStr
     * @return
     */
    public static double calculateSimilaritybyJaccard(String str, String sttStr) {
        Set<Character> setA = new HashSet<>();
        for (char c : str.toCharArray()) {
            setA.add(c);
        }

        Set<Character> setB = new HashSet<>();
        for (char c : sttStr.toCharArray()) {
            setB.add(c);
        }

        // 计算交集和并集的大小
        Set<Character> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<Character> union = new HashSet<>(setA);
        union.addAll(setB);

        // 计算Jaccard相似度
        double result = (double) intersection.size() / union.size();

        return parseDouble(result);
    }

    /**
     * Levenshtein  莱文斯坦距离
     *  计算将一个字符串转换为另一个字符串所需的最少单字符编辑操作（插入、删除或替换）的数量。
     * @param str
     * @param sttStr
     * @return
     */
    public static double calculateSimilarityByLevenshtein(String str, String sttStr) {
        EditDistance<Integer> editDistance = new LevenshteinDistance();
        // 计算两个字符串之间的编辑距离
        Integer distance = editDistance.apply(str, sttStr);
        // 计算两个字符串的最大长度
        int maxLength = Math.max(str.length(), sttStr.length());
        // 根据Levenshtein距离计算相似度
        return parseDouble(1.0 - (double) distance / maxLength);
    }

    public static double calculateSimilarityByLevenshtein(List<String> strs, List<String> sttStrs) {
        EditDistance<Integer> editDistance = new LevenshteinDistance();
        // 定义一个变量，用于存放总的编辑距离分数
        double totalDistance = 0.0;
        // 遍历两个list中的每个字符串
        for (String s1 : strs) {
            for (String s2 : sttStrs) {
                // 计算两个字符串之间的编辑距离
                int distance = editDistance.apply(s1, s2);
                // 累加到总的编辑距离分数
                totalDistance += distance;
            }
        }
        // 计算两个list的平均编辑距离分数
        double averageDistance = totalDistance / (strs.size() * sttStrs.size());
        // 计算两个list的相似度分数，这里使用1 - distance / max_length的公式
        double similarity = 1 - averageDistance / Math.max(strs.get(0).length(), sttStrs.get(0).length());
        return parseDouble(similarity);
    }

//    /**
//     * 余弦相似性
//     *  字符空间向量之间夹角的余弦值
//     * @param str
//     * @param sttStr
//     * @return
//     */
//    public static double calculateSimilarityByCosine(String str, String sttStr){
//
//        // 创建一个余弦相似度对象
//        StringMetric metric = StringMetrics.cosineSimilarity();
//        // 定义一个变量，用于存放总的相似度分数
//        double totalSimilarity = 0.0;
//        // 遍历两个list中的每个字符串
//        for (String s1 : list1) {
//            for (String s2 : list2) {
//                // 计算两个字符串的相似度
//                float similarity = metric.compare(s1, s2);
//                // 累加到总的相似度分数
//                totalSimilarity += similarity;
//            }
//        }
//        // 计算两个list的平均相似度分数
//        double averageSimilarity = totalSimilarity / (list1.size() * list2.size());
//    }

    /**
     * 格式化返回值为8位小数点
     * @param num
     * @return
     */
    private static Double parseDouble(Double num){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        String formattedResult = decimalFormat.format(num);
        return Double.parseDouble(formattedResult);
    }

    public static void main(String[] args) {
        String str = "Tilly, a little fox, loved her bright red balloon. She carried it everywhere. It’s my favorite balloon!” Tilly said.";
        String sttStr = "Tilly, a little fox, loved her bright red balloon. She carried it everywhere. My name's Tilly. It's my favourite balloon. Tilly said.";
        System.out.println(calculateSimilaritybyJaccard(
                str,sttStr
                ));

        System.out.println(calculateSimilarityByLevenshtein(
                str,sttStr
        ));
    }
}
