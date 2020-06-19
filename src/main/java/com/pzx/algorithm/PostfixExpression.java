package com.pzx.algorithm;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * 后缀表达式问题
 * 中缀表达式：a + b * c + ( d * e + f ) * g   后缀表达式：abc * + de * f + g * +
 *涉及三个问题：
 * 1、中缀表达式转后缀表达式
 * 2、后缀表达式的计算
 * 3、后缀表达式转中缀表达式
 *
 * @author PZX
 * 2020.6.19
 */
public class PostfixExpression {

    /*
    private enum Operator{
        ADD("+", 1){
            @Override
            public double operator(double x, double y) {
                return x + y;
            }
        },
        SUBTRACT("-", 1){
            @Override
            public double operator(double x, double y) {
                return x - y;
            }
        },
        MULTIPLY("*", 2){
            @Override
            public double operator(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/", 2){
            @Override
            public double operator(double x, double y) {
                return x / y;
            }
        };

        private String expression;
        private int priority;

        public abstract double operator(double x, double y);

        Operator(String expression, int priority){
            this.expression = expression;
            this.priority = priority;
        }

    }
     */

    /**
     * 中缀表达式转后缀表达式
     * 算法过程：
     * 循环遍历中缀表达式
     * 1.遇到操作数，直接输出到结果
     * 2.遇到运算符（加减乘除）：弹出所有优先级大于或者等于该运算符的栈顶元素（不弹出左括号），直到发现优先级更低的元素为止，然后将该运算符入栈
     * 3.遇到左括号：将其入栈（左括号优先级最高，且只有遇到右括号时才会弹出栈，但不输出到结果当中）
     * 4.遇到右括号：执行出栈操作，直到弹出栈的是左括号，左括号不输出，右括号不入栈。
     *
     * @param infixExpression
     * @return
     */
    private static List<String> infix2Postfix(List<String> infixExpression){

        Map<String, Integer> operatorPriority = new HashMap<>();
        operatorPriority.put("+",1);
        operatorPriority.put("-",1);
        operatorPriority.put("*",2);
        operatorPriority.put("/",2);
        operatorPriority.put("(",3);

        List<String> postfixExpression = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        for(String element : infixExpression){
            if(element.equals("+")||element.equals("-")||element.equals("*")||element.equals("/")){
                while (!stack.isEmpty() && !stack.peek().equals("(") && operatorPriority.get(stack.peek()) >=  operatorPriority.get(element)){
                    postfixExpression.add(stack.pop());
                }
                stack.push(element);
            }else if (element.equals("(")){
                stack.push(element);
            }else if (element.equals(")")){
                while (!stack.isEmpty() &&!stack.peek().equals("(")){
                    postfixExpression.add(stack.pop());
                }
                stack.pop();//将（弹出栈，没有（则报出异常
            }else {
                postfixExpression.add(element);
            }
        }
        //将栈中剩余的操作符弹出
        while (!stack.isEmpty()){
            postfixExpression.add(stack.pop());
        }
        return postfixExpression;
    }

    /**
     * 利用后缀表达式计算结果
     * 算法过程：
     * 1.循环遍历后缀表达式
     * 2.遇到操作数，则入栈
     * 3.遇到操作符，则出栈两个元素进行操作符计算（先出栈的为被操作数），将结果再次入栈
     * 4.遍历结束，出栈得到最终结果
     * @param postfixExpression
     * @return
     */
    private static double calculatePostfixExpression(List<String> postfixExpression){
        Map<String, BiFunction<Double,Double,Double>> operator = new HashMap<>();
        operator.put("+",(x, y)->x+y);
        operator.put("-",(x, y)->x-y);
        operator.put("*",(x, y)->x*y);
        operator.put("/",(x, y)->x/y);

        Deque<Double> stack = new LinkedList<>();
        for(String element : postfixExpression){
            if(element.equals("+")||element.equals("-")||element.equals("*")||element.equals("/")){
                double secondOperatorNum = stack.pop();
                double fistOperatorNum = stack.pop();
                double tmp =  operator.get(element).apply(fistOperatorNum, secondOperatorNum);
                stack.push(tmp);
            }else {
                stack.push(Double.parseDouble(element));
            }
        }
        return stack.pop();

    }

    public static void main(String[] args) {
        System.out.println(calculatePostfixExpression(infix2Postfix(resolveInfixExpressionStr("4+3*3-6/3+7-3*2"))));
    }

    /**
     * 将字符串表达式解析为字符串数组
     * @param infixExpressionStr
     * @return
     */
    private static List<String> resolveInfixExpressionStr(String infixExpressionStr){
        infixExpressionStr = infixExpressionStr.replaceAll(" ","");//去除所有空格
        List<String> infixExpression = new ArrayList<>();
        int lastOperatorIndex = -1;
        for(int i = 0; i< infixExpressionStr.length(); i++){
            char c = infixExpressionStr.charAt(i);
            if (c=='+' || c=='-' || c=='*' || c=='/' || c=='(' || c== ')'){
                if (lastOperatorIndex != i-1)//防止括号操作符和运算符邻接导致插入空字符串
                    infixExpression.add(infixExpressionStr.substring(lastOperatorIndex + 1, i));
                infixExpression.add(c + "");
                lastOperatorIndex = i;
            }
        }
        infixExpression.add(infixExpressionStr.substring(lastOperatorIndex + 1));
        return infixExpression;
    }

}
