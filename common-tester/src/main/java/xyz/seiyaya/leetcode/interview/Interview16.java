package xyz.seiyaya.leetcode.interview;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/22 13:35
 */
public class Interview16 {

    public static void main(String[] args) {
        boolean b = new Interview16().patternMatching("abba", "dogcatcatdog");
        System.out.println(String.format("result:%s",b));
    }

    /**
     * pattern = "abba", value = "dogcatcatdog"
     * @param pattern
     * @param value
     * @return
     */
    public boolean patternMatching(String pattern, String value) {
        for(int i=0,j=i+1;i<pattern.length() && j<pattern.length();i++){
            StringBuilder patternSb = new StringBuilder();
            int start = 0;
            int end = 0;
            while(end < value.length()){
                int start1 = end+1;
                patternSb.append(value.charAt(end));
                if(start1+patternSb.length() >= value.length()){
                    break;
                }
                System.out.println( patternSb.toString() + " --> " + value.substring(start1,start1+patternSb.length()));
                if(pattern.charAt(i) != pattern.charAt(j) ){
                    // ab
                    if(start1+patternSb.length() < value.length() && patternSb.toString().equals(value.substring(start1,start1+patternSb.length()))){
                        return false;
                    }
                }else{
                    // aa
                    if(!patternSb.toString().equals(value.substring(start1,start1+patternSb.length()))){
                        return false;
                    }
                }
                end++;
            }
            patternSb = new StringBuilder();
            start++;
            end = start;
        }
        return true;
    }
}
