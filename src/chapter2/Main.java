package chapter2;

import java.util.Arrays;

/**
 * @author xqdd
 * @date 2020/7/28 13:42
 */
@SuppressWarnings({"rawtypes", "ManualArrayCopy", "unchecked"})
public class Main {

    public static void main(String[] args) {
        Integer[] a = {-1, 0, 1, 2, 3, 61, 66};
//        mergeSort(a, 0, a.length - 1);
//        quickSort3(a, 0, a.length - 1);
        heapSort(a);
        System.out.println(Arrays.toString(a));
    }

    /**
     * 堆排序，注意a[0]忽略掉
     */
    public static void heapSort(Comparable[] a) {
        //第一步，将普通数组转化为堆
        /**1.上浮法，遍次数为n
         for (int i = 1; i < a.length; i++) {
         swim(a, i);
         }
         **/
        //2.下潜法，比较高效，遍历次数为n/2
        int n = a.length;
        for (int k = n / 2; k >= 1; k--) {
            sink(a, k, n);
        }


        //第二步，将堆转为有序的数组
        //这里类似选择排序，慢慢的把最大的元素选出来放到后面
        while (n > 1) {
            //此时最大的元素肯定在最上面，依次将 最大的元素放最后
            headExchange(a, 1, n--);
            //然后重新构造堆
            sink(a, 1, n);
        }
    }

    /**
     * 堆排序,将小的元素下潜
     */
    public static void sink(Comparable[] a, int k, int n) {
        //下潜直到n
        while (2 * k <= n) {
            //获得子节点
            int j = 2 * k;
            //选两个子节点中比较大的那一个
            if (j < n && less(a, j, j + 1)) {
                j++;
            }
            //若比子节点大，则结束
            if (!less(a, k, j)) {
                break;
            }
            //符合要求，交换父子节点
            headExchange(a, k, j);
            //当前节点设为子节点
            k = j;
        }
    }

    /**
     * 堆排序,将大的元素上浮
     */
    public static void swim(Comparable[] a, int k) {
        // k大于1并且当前节点大于父节点，则交换
        while (k > 1 && less(a, k / 2, k)) {
            headExchange(a, k / 2, k);
            k = k / 2;
        }
    }

    public static boolean less(Comparable[] a, int k, int n) {
        return less(a[k - 1], a[n - 1]);
    }

    public static void headExchange(Comparable[] a, int k, int n) {
        exchange(a, k - 1, n - 1);
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
        //直到处理到最后一个已知的上界，即最后一个重复的v
        while (i <= gt) {
            int com = a[i].compareTo(v);
            //当前的值小于v，与下界进行交换，下界加1，下界值一定为v，所以i++处理下一个值
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
