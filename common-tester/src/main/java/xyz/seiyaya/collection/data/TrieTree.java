package xyz.seiyaya.collection.data;

import java.util.TreeMap;

/**
 * 字典树，前缀树
 * @author wangjia
 * @version v1.0
 * @date 2021/1/15 13:54
 */
@SuppressWarnings("all")
public class TrieTree {

    private TrieNode root;

    private int size;

    public TrieTree(){
        root = new TrieNode();
        size = 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * 添加一个单词
     * @param word
     */
    public void add(String word){
        TrieNode cur = root;

        for(int i=0;i<word.length();i++){
            char c = word.charAt(i);
            if(cur.next.get(c) == null){
                cur.next.put(c,new TrieNode());
            }
            cur = cur.next.get(c);
        }

        if(!cur.isWord){
            cur.isWord = true;
            size++;
        }
    }

    /**
     * 递归添加单词
     * @param word
     */
    public void addRecursion(String word){
        root = addRecursion(root,word);
    }

    public TrieNode addRecursion(TrieNode root,String word){
        if(root == null){
            return null;
        }

        if(word.length() > 0){
            if(root.next.get(word.charAt(0)) == null){
                root.next.put(word.charAt(0),new TrieNode());
            }

            root.next.put(word.charAt(0),addRecursion(root.next.get(word.charAt(0)),word.substring(1)));
            if(word.length() == 1 && !root.next.get(word.charAt(0)).isWord){
                root.next.get(word.charAt(0)).isWord = true;
                size++;
            }
        }
        return root;
    }

    /**
     * 判断是否包含某个单词
     * @param word
     * @return
     */
    public boolean contains(String word){
        TrieNode cur = root;
        for(int i=0;i<word.length();i++){
            char c = word.charAt(i);
            if(cur.next.get(c) == null){
                return false;
            }
            cur = cur.next.get(c);
        }
        return cur.isWord;
    }

    /**
     * 递归查询是否包含某个单词
     * @param node
     * @param word
     * @return
     */
    public boolean containsRecursion(TrieNode node, String word){
        if(node == null){
            return false;
        }

        if(word.length() > 0){
            if(node.next.get(word.charAt(0)) == null){
                return false;
            }else if(word.length() == 1 && node.next.get(word.charAt(0)).isWord){
                return true;
            }
            return containsRecursion(node.next.get(word.charAt(0)),word.substring(1));
        }
        return false;
    }

    /**
     * 是否包含此前缀
     * @param prefix
     * @return
     */
    private boolean isPrefix(String prefix){
        TrieNode cur = root;
        for(int i=0;i<prefix.length();i++){
            char c = prefix.charAt(i);
            if(cur.next.get(c) == null){
                return false;
            }
            cur = cur.next.get(c);
        }
        return true;
    }

    /**
     * 递归查询是否包含次单词的前缀
     * @param word
     * @return
     */
    public boolean isPrefixRecursion(String word){
        return isPrefixRecursion(root,word);
    }

    private boolean isPrefixRecursion(TrieNode node, String word) {
        if(node == null){
            return false;
        }

        if(word.length() > 0){
            if(node.next.get(word.charAt(0)) == null){
                return false;
            }else if(word.length() == 1){
                return true;
            }
            return isPrefixRecursion(root.next.get(word.charAt(0)),word.substring(1));
        }
        return false;
    }

    /**
     * 字典树节点
     */
    private class TrieNode{

        boolean isWord;
        TreeMap<Character,TrieNode> next;


        public TrieNode(boolean isWord){
            this.isWord = isWord;
            next = new TreeMap<>();
        }

        public TrieNode(){
            this(false);
        }
    }
}
