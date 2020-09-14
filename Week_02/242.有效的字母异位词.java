import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * @lc app=leetcode.cn id=242 lang=java
 *
 * [242] 有效的字母异位词
 *
 * https://leetcode-cn.com/problems/valid-anagram/description/
 *
 * algorithms
 * Easy (61.30%)
 * Likes:    249
 * Dislikes: 0
 * Total Accepted:    136.9K
 * Total Submissions: 223K
 * Testcase Example:  '"anagram"\n"nagaram"'
 *
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * 
 * 示例 1:
 * 
 * 输入: s = "anagram", t = "nagaram"
 * 输出: true
 * 
 * 
 * 示例 2:
 * 
 * 输入: s = "rat", t = "car"
 * 输出: false
 * 
 * 说明:
 * 你可以假设字符串只包含小写字母。
 * 
 * 进阶:
 * 如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
 * 
 */

// @lc code=start
class Solution {
    public boolean isAnagram(String s, String t) {
        // 第一时间想到的解法
        // if (s.length() != t.length()) {
        // return false;
        // }
        // Map<Character, Integer> map = new HashMap<>();
        // for (int i = 0; i < length; i++) {
        // char charAt = s.charAt(i);
        // if (map.containsKey(charAt)) {
        // map.put(charAt, map.get(charAt) + 1);
        // } else {
        // map.put(charAt, 1);
        // }
        // }
        // for (int i = 0; i < t.length(); i++) {
        // char ch = t.charAt(i);
        // if (map.containsKey(ch)) {
        // map.put(ch, map.get(ch) - 1);
        // }
        // if (map.get(ch) != null && map.get(ch) == 0) {
        // map.remove(ch);
        // }
        // }
        // return map.isEmpty();
        // 优化后的hash表解法
        // if (s.length() != t.length()) {
        // return false;
        // }
        // int[] table = new int[26];
        // for (int i = 0; i < s.length(); i++) {
        // table[s.charAt(i) - 'a']++;
        // }
        // for (int i = 0; i < t.length(); i++) {
        // table[t.charAt(i) - 'a']--;
        // if (table[t.charAt(i) - 'a'] < 0) {
        // return false;
        // }
        // }
        // return true;
        // 排序解法
        if (s.length() != t.length()) {
            return false;
        }
        char[] s1 = s.toCharArray();
        char[] t1 = t.toCharArray();
        Arrays.sort(s1);
        Arrays.sort(t1);
        return Arrays.equals(s1, t1);
    }
}
// @lc code=end
