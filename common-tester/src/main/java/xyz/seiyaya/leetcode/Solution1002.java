package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-common-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author wangjia
 * @version 1.0
 * @date 2020/10/14 9:12
 */
public class Solution1002 {

    public static void main(String[] args) {
        List<String> strings = new Solution1002().commonChars(new String[]{"bella", "label", "roller"});
        System.out.println(strings);
    }

    public List<String> commonChars(String[] A) {
        String first = A[0];
        int[] count = new int[26];
        for(char character : first.toCharArray()){
            count[character-'a']++;
        }

        for(int i=1;i<A.length;i++){
            int[] temp = new int[26];
            char[] chars = A[i].toCharArray();
            for(int j=0;j<chars.length;j++){
                temp[chars[j]-'a']++;
            }
            for(int j=0;j<26;j++){
                count[j] = Math.min(count[j],temp[j]);
            }
        }

        List<String> result = new ArrayList<>();
        for(int i=0;i<26;i++){
            if(count[i] > 0){
                for(int j=0;j<count[i];j++){
                    char tmp = (char)('a'+i);
                    result.add(String.valueOf(tmp));
                }
            }
        }
        return result;
    }
}
