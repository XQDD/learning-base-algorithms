package chapter2;

import java.util.Arrays;

/**
 * @author xqdd
 * @date 2020/7/28 13:42
 */
@SuppressWarnings({"rawtypes", "ManualArrayCopy", "unchecked"})
public class Main {

    public static void main(String[] args) {
        Integer[] a = {15, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 4, 6, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
//        mergeSort(a, 0, a.length - 1);
        quickSort3(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }

    /**
     * 三向切分的快速排序(小于、等于、等于)
     * 用于重复数据比较多的数组
     */
    public static void quickSort3(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int lt = lo;
        int i = lo + 1;
        int gt = hi;
        var v = a[lo];
        //这是一个不断将相同的v聚集到中间的过程
        //知道处理到最后一个已知的边界，即最后一个重复的v
        while (i <= gt) {
            int com = a[i].compareTo(v);
            //当前的值小于v，与下界进行交换，下界值一定为v，所以并且i++处理下一个值
            if (com < 0) {
                exchange(a, lt++, i++);
            }
            //当前的值大于v，与上界交换，上界减1，上界值不一定为v，所以i不加
            else if (com > 0) {
                exchange(a, i, gt--);
            }
            //相等，直接处理下一个
            else {
                i++;
            }
        }
        quickSort3(a, lo, lt - 1);
        quickSort3(a, gt + 1, hi);
    }


    /**
     * 快速排序
     */
    public static void quickSort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int i = lo;
        int j = hi + 1;
        var v = a[lo];
        while (true) {
            //将小于当前选定值的放左边，大于当前值的放右边
            while (less(a[++i], v)) {
                if (i == hi) {
                    break;
                }
            }
            while (less(v, a[--j])) {
                if (j == lo) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            //一次性交换两个不符合顺序的值
            exchange(a, i, j);
        }
        //最后将自己放到中间
        exchange(a, lo, j);

        //注意下标为j的数据不用处理，已经是最佳位置了
        quickSort(a, lo, j - 1);
        quickSort(a, j + 1, hi);
    }


    /**
     * 自底向上归并排序
     * 两两合并->四四合并->八八合并
     */
    public static void mergeSort(Comparable[] a) {
        for (int sz = 1; sz < a.length; sz = 2 * sz) {
            for (int lo = 0; lo < a.length - sz; lo += 2 * sz) {
                merge(a, lo, lo + sz - 1, Math.min(lo + 2 * sz - 1, a.length - 1));
            }
        }
    }


    /**
     * 自顶向下的归并排序
     */
    public static void mergeSort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, lo, mid);
        mergeSort(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }


    /**
     * 原地归并抽象方法
     * 注意a的左右两边是有序的
     */
    public static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        //aux为a中lo到hi的副本
        Comparable[] aux = new Comparable[a.length];
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            //i是左边索引，j是右边索引
            //当i大于mid时，表示左边已经用完，右边可以直接归并而无需比较
            if (i > mid) {
                a[k] = aux[j++];
            }
            //当j大于lw时，表示右边已经用完，左边可以直接归并而无需比较
            else if (j > hi) {
                a[k] = aux[i++];
            }
            //右边小于左边，取右边的值
            else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];
            }
            //反之取左边的值
            else {
                a[k] = aux[i++];
            }
        }
    }


    /**
     * 希尔排序：通过分批间隔的插入排序预处理数组，每次循环都能提升插入排序的性能，将插入排序的优势加大
     * 算法的最后一次循环就是普通插入排序
     * 简单高效且比快速排序好(无论空间还是时间)，适合中小数组
     */
    public static void shellSort(Comparable[] a) {
        int h = 1;
        while (h < a.length / 3) {
            h = 3 * h + 1;
        }
        while (h >= 1) {
            for (int i = h; i < a.length; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
                    exchange(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }


    /**
     * 插入排序：将新元素插入已经有序的序列中
     */
    public static void insertSort(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                exchange(a, j, j - 1);
            }
        }
    }

    /**
     * 选择排序：每次选择最大或者最小的值
     */
    public static void selectSort(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < a.length; j++) {
                if (less(a[j], a[minIndex])) {
                    minIndex = j;
                }
            }
            exchange(a, i, minIndex);
        }
    }


    /**
     * 冒泡排序：类似选择排序，都是选择出最值，只不过形式不一样，是通过每次的相邻的元素两两交换得到
     */
    public static void bubbleSort(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (less(a[j + 1], a[j])) {
                    exchange(a, j + 1, j);
                }
            }
        }
    }


    /**
     * 交换数组中两个数据的位置
     */
    public static void exchange(Comparable[] a, int indexA, int indexB) {
        var temp = a[indexA];
        a[indexA] = a[indexB];
        a[indexB] = temp;
    }

    /**
     * 是否小于
     */
    public static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }


}
