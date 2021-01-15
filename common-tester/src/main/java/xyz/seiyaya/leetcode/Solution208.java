package xyz.seiyaya.leetcode;

/**
 * 实现字典树
 * @author wangjia
 * @version v1.0
 * @date 2021/1/15 18:11
 */
public class Solution208 {

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        String item  = "apple";
        boolean app = trie.search(item);
        System.out.printf("%s is %s",item,app);
        System.out.println();
    }



    private static class Trie {

        private TrieNode root;

        /** Initialize your data structure here. */
        public Trie() {
            this.root = new TrieNode();
        }

        /** Inserts a word into the trie. */
        public void insert(String word) {
            TrieNode cur = root;
            for(int i=0;i<word.length();i++){
                char c = word.charAt(i);
                if(cur.children[c - 'a'] == null){
                    cur.children[c - 'a'] = new TrieNode(c);
                }
                cur = cur.children[c - 'a'];
            }
        }

        /** Returns if the word is in the trie. */
        public boolean search(String word) {
            TrieNode cur = root;
            for(int i=0;i<word.length();i++){
                if(word.charAt(i) != cur.children[word.charAt(i) - 'a'].val){
                    return false;
                }
                cur = cur.children[word.charAt(i) - 'a'];
            }
            for(int i=0;i<26;i++){
                if(cur.children[i] != null){
                    return false;
                }
            }
            return true;
        }

        /**
         * 递归搜索
         * @param node
         * @param word
         * @return
         */
        public boolean searchRecursion(TrieNode node,String word){
            if(node == null){
                return false;
            }
            if(word.length() > 0){
                if(node.children[word.charAt(0) - 'a'] == null){
                    return false;
                }else if(word.length() == 1 && word.charAt(0) == node.children[word.charAt(0) - 'a'].val){
                    return true;
                }
                return searchRecursion(node.children[word.charAt(0) - 'a'],word.substring(1));
            }
            return false;
        }

        /** Returns if there is any word in the trie that starts with the given prefix. */
        public boolean startsWith(String prefix) {
            return searchRecursion(root,prefix);
        }
    }

    /**
     * 字典树节点
     */
    private static class TrieNode{
        char val;
        TrieNode[] children = new TrieNode[26];

        public TrieNode(){

        }

        public TrieNode(char val){
            this.val = val;
        }
    }
}
