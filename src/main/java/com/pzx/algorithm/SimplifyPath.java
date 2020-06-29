package com.pzx.algorithm;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 简化绝对路径问题
 * 输入：/a//b////c/d//././/..  输出：/a/b/c
 * 开头必须是/，结尾不能是/。..对于根目录不起作用
 */
public class SimplifyPath {

    /**
     * 自己想的算法
     * 不足之处在于将所有元素都入栈，判断条件复杂
     * @param path
     * @return
     */
    private static String simplifyPath(String path) {

        Deque<Character> stack = new LinkedList<>();
        String result = "";
        path = path + "/";//避免路径最后以.为结尾

        for(char c : path.toCharArray()){
            if(c == '/'){
                if(stack.isEmpty()){
                    stack.push(c);
                    continue;
                }

                if(stack.peek() == '/')
                    continue;

                if(stack.peek() == '.'){
                    stack.pop();//.出栈
                    if(stack.peek() == '.'){
                        stack.pop();
                        if(stack.size()!=1){
                            //如果只剩一个元素，即为/，则在根目录，不需要向上目录移动
                            stack.pop();//除去栈顶的/
                            while(stack.peek()!='/')
                                stack.pop();
                        }
                    }
                    //栈顶元素不为. 说明在当前目录，则直接判断下一个字母即可
                    continue;
                }
                //栈顶为字母
                stack.push(c);
            }else{
                //如果遇到字母或者点，则直接入栈
                stack.push(c);
            }
        }
        while(!stack.isEmpty()){
            result = stack.pop() + result;
        }

        return result.endsWith("/")&& result.length()!=1 ? result.substring(0,result.length()-1):result;
    }

    /**
     *利用栈解决
     * 算法过程：
     * 1.将字符串用/分割，则分割后的元素有四种情况：空字符串、字母、一个点、两个点
     * 2.遍历分割后的数组
     * 3.遇到空字符串或者一个点，直接略过
     * 4.遇到两个点，判断栈是否为空，为空则说明当前目录为根目录，则略过。否则出栈栈顶元素，代表跳跃到上级目录
     * 5.遇到字母直接入栈。
     * 6.遍历结束之后，如果栈为空，则说明是根目录，返回/
     * 7.依次从栈底取元素进行拼接，返回结果。
     *
     * @param path
     * @return
     */
    private static String simplifyPath2(String path){
        Objects.requireNonNull(path);

        Deque<String> stack = new LinkedList<>();
        String[] pathElements = path.split("/");

        for(String element : pathElements){
            if("".equals(element) || ".".equals(element)){
                continue;
            }else if("..".equals(element)){
                if(!stack.isEmpty()){
                    stack.pop();
                }
            }else {
                stack.push(element);
            }
        }
        if (stack.isEmpty())
            return "/";
        StringBuilder builder = new StringBuilder();
        while (!stack.isEmpty()){
            builder.append("/" + stack.pollLast());
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(simplifyPath2("/home//foo/"));
    }
}
