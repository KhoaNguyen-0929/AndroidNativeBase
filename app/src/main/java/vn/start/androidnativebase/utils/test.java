package vn.start.androidnativebase.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

class Solution {
    public int firstUniqChar(String s) {
        HashMap<Character, Boolean> hashMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if( hashMap.containsKey(s.charAt(i))){
                hashMap.put(s.charAt(i), false);
            }else {
                hashMap.put(s.charAt(i), true);
            }

        }

        for (int i = 0; i < s.length(); i++) {
            if (Boolean.TRUE.equals(hashMap.get(s.charAt(i)))) {
                return i;
            }
        }
        return -1;
    }
}