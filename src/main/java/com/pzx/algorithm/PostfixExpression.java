package com.pzx.algorithm;

import javafx.util.Pair;

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

        List<String> postfixExpression = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        for(String element : infixExpression){
            if(operatorPriority.containsKey(element)){
                while (!stack.isEmpty() && !stack.peek().equals("(")
                        && operatorPriority.get(stack.peek()) >=  operatorPriority.get(element)){
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
            if(operator.containsKey(element)){
                double secondOperatorNumber = stack.pop();
                double fistOperatorNumber = stack.pop();
                double tmp =  operator.get(element).apply(fistOperatorNumber, secondOperatorNumber);
                stack.push(tmp);
            }else {
                stack.push(Double.parseDouble(element));
            }
        }
        return stack.pop();

    }

    /**
     * 后缀表达式转为中缀表达式
     * 算法描述：
     * 1.遍历后续表达式
     * 2.遇到操作数，则创建Pair（expression，operator），将表达式和操作符组成一个单元入栈。对于操作数来说，表达式为本身，操作符为空字符串
     * 3.遇到操作符，执行两次出栈操作（先出栈的为被操作数）。判断出栈的元素Pair中储存的操作符的优先级是否小于当前操作符，若小于，则将Pair中的表达式两边加括号。
     * 4.将加括号处理之后的两个表达式用当前操作符连接，构造新的Pair对象（新的表达式，当前操作符），入栈。
     * 5.遍历结束后，进行出栈操作获取中缀表达式。
     *
     * @param postfixExpression
     * @return
     */
    private static String postfix2Infix(List<String> postfixExpression){

        Map<String, Integer> operatorPriority = new HashMap<>();
        operatorPriority.put("+",1);
        operatorPriority.put("-",1);
        operatorPriority.put("*",2);
        operatorPriority.put("/",2);

        Deque<Pair<String,String>> stack = new LinkedList<>();
        for(String element : postfixExpression){
            if(operatorPriority.containsKey(element)){
                Pair<String,String> secondOperatorPair = stack.pop();
                Pair<String,String> fistOperatorPair = stack.pop();
                String secondOperatorNumber = secondOperatorPair.getKey();
                String fistOperatorNumber = fistOperatorPair.getKey();

                if (operatorPriority.getOrDefault(fistOperatorPair.getValue(),3) < operatorPriority.get(element)){
                    fistOperatorNumber = "(" + fistOperatorNumber + ")";
                }
                if (operatorPriority.getOrDefault(secondOperatorPair.getValue(),3) < operatorPriority.get(element)){
                    secondOperatorNumber = "(" + secondOperatorNumber + ")";
                }

                stack.push(new Pair<>(fistOperatorNumber + element + secondOperatorNumber, element));
            }else {
                stack.push(new Pair<>(element,""));//空字符串代表单个操作数，无操作符。
            }
        }
        return stack.pop().getKey();
    }

    public static void main(String[] args) {
        System.out.println(calculatePostfixExpression(infix2Postfix(resolveInfixExpressionStr("4+3*3-6/3+(7-3)*2-3*4/2+3"))));
        System.out.println(postfix2Infix(infix2Postfix(resolveInfixExpressionStr("4+3*3-6/3+(7-3)*2-3*4/2+3"))));
    }

    /**
     * 将字符串表达式解析为字符串数组
     * 1.遍历字符串，当遇到操作符时，将遇到的上一个操作符和当前操作符之间的字符串提取，存入结果列表。
     * 2.将当前操作符也存入结果列表。更改遇到的上一个操作符位置为当前位置。
     * 3.遍历结束后，将遇到的最后一个操作符之后的字符串存入列表。
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
