package chapter1;


import java.util.Stack;

/**
 * @author xqdd
 * @date 2020/7/23 9:20
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(evaluate("( 123 * ( 123 + 123 ) )"));
    }

    /**
     * 1.3.1.7 算术表达式求值 Dijkstra 的双栈算术表达式求值算法
     * 简化版，需要括号和空格
     * 示例值： ( 123 * ( 123 + 123 ) )
     */
    public static double evaluate(String formula) {
        var ops = new Stack<String>();
        var vals = new Stack<Double>();
        for (var s : formula.split("\\s+")) {
            switch (s) {
                case "(" -> {
                }
                case "+", "-", "*", "/", "sqrt" -> ops.push(s);
                case ")" -> {
                    var op = ops.pop();
                    var v = vals.pop();
                    v = switch (op) {
                        case "+" -> vals.pop() + v;
                        case "-" -> vals.pop() - v;
                        case "*" -> vals.pop() * v;
                        case "/" -> vals.pop() / v;
                        case "sqrt" -> Math.sqrt(v);
                        default -> throw new IllegalStateException("Unexpected value: " + op);
                    };
                    vals.push(v);
                }
                default -> vals.push(Double.parseDouble(s));
            }
        }
        return vals.pop();
    }

}
