package xyz.seiyaya.leetcode.struct;

/**
 * 动态数组
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 17:35
 */
public class DynamicArray<E> {

    private E[] data;

    /**
     * 数组的大小
     */
    private int size;

    public DynamicArray(int size){
        data = (E[]) new Object[size];
    }

    public DynamicArray(){
        this(10);
    }

    public int getSize() {
        return size;
    }

    public int getCapacity(){
        return data.length;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void addFirst(E element){
        add(0,element);
    }

    public void addLast(E element){
        add(size,element);
    }

    public void add(int index, E element){
        if(index < 0 || index > size){
            throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size");
        }

        if(size == data.length){
            // 元素上限，进行数组扩容
            resize(2 * data.length);
        }

        for(int i= size - 1;i >= index ;i--){
            data[i+1] = data[i];
        }
        data[index] = element;
        size++;
    }

    public void resize(int newSize){
        E[] newData = (E[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public E get(int index){
        if(index < 0 || index >= size){
            throw new IllegalArgumentException("Get failed. index is Illegal.");
        }
        return data[index];
    }

    public E getFirst(){
        return get(0);
    }

    public E getLast(){
        return get(size-1);
    }

    public void set(int index,E newElement){
        if (index < 0 || index >= size){
            throw new IllegalArgumentException("Set failed. index is Illegal.");
        }
        data[index] = newElement;
    }

    public boolean contains(E item){
        for(E e : data){
            if(e.equals(item)){
                return true;
            }
        }
        return false;
    }

    public int indexOf(E item){
        for (int i = 0; i < size; i++) {
            if(data[i].equals(item)){
                return i;
            }
        }
        return -1;
    }

    public E remove(int index){
        if (index < 0 || index >= size){
            throw new IllegalArgumentException("Remove failed. index is Illegal.");
        }

        E result = data[index];
        for (int i = index + 1; i < size; i++) {
            data[i-1] = data[i];
        }
        size--;
        // 修改对象引用，垃圾回收机制回收
        data[size] = null;

        // 动态缩小数组一半容量
        if(size == data.length/4 && data.length/2 != 0){
            resize(data.length/2);
        }
        return result;
    }

    public E removeFirst(){
        return remove(0);
    }

    public E removeLast(){
        return remove(size - 1);
    }

    public boolean removeElement(E e){
        int index = indexOf(e);
        if(index != -1){
            remove(index);
            return true;
        }
        return false;
    }
}
