package com.example.ff.calculator;

/**
 * Created by FF on 2014/11/28.
 */
import android.app.ActionBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.lang.Math;

/**
 * 计算表达式工具类
 * 前缀、中缀、后缀表达式 详解地址
 * http://blog.csdn.net/antineutrino/article/details/6763722
 * @author wangwang
 */

public class CalcUtil {


    public static String calculate(String str){

        double rs = calcResult(toPolishNotation(str));

        // 整数去除小数点
        str = rs == (int)rs? String.valueOf((int)rs): String.valueOf(rs);

        return str;
    }



    /**
     * 计算前缀表达式结果
     * @param expression
     * @return
     */
    private static double calcResult(Stack<Object> expression){

        Object[] objs = new Object[expression.size()];

        int i = 0;
        while(!expression.isEmpty()){

            objs[i] = expression.pop();

            i++;
        }

        Stack<Double> nums = new Stack<Double>();


        for(int j = objs.length - 1; j >=0; j--){

            Object obj = objs[j];

            if(obj instanceof Double ){

                nums.add((Double)obj);
            }else{

                char op = (Character) obj;
                double rs ;
                if (op=='c'||op=='s'||op=='t'){
                    double d1 = nums.pop();
                    rs = angle(d1,op);
                }
                else {
                    double d1 = nums.pop();
                    double d2 = nums.pop();

                    rs = calc(d1, d2, op);
                }


                nums.add(rs);
            }
        }

        return nums.pop();
    }


    /**
     * 前缀表达式（前缀记法、波兰式）
     * 前缀表达式的运算符位于操作数之前
     *
     * @param str
     * @return
     */
    private static Stack<Object> toPolishNotation(String str){

        Stack<Character> s1 = new Stack<Character>();

        Stack<Object> expression = new Stack<Object>();

        char[] charArr = str.toCharArray();

        List<Character> list = new ArrayList<Character>();


        for(int i = charArr.length - 1; i >= 0;){

            char key = charArr[i];

            if(isOperator(key)){

                if(!list.isEmpty()){
                    double num = getNum(list);

                    expression.add(num);
                }

                if(s1.isEmpty() || s1.peek() == ')'){

                    s1.add(key);
                }else{

                    char top = s1.peek();

                    int priority = priorityCompare(key, top);

                    if(priority >= 0){

                        s1.add(key);
                    }else{

                        top = s1.pop();

                        expression.add(top);

                        continue;
                    }
                }

            }else if(key == '('){

                if(!list.isEmpty()){
                    double num = getNum(list);

                    expression.add(num);
                }

                while(')' != s1.peek()){

                    char top = s1.pop();

                    expression.add(top);
                }

                s1.pop();
            }else if(key == ')'){

                s1.add(key);
            }else{

                list.add(key);
            }

            if(i ==  0){

                if(!list.isEmpty()){
                    double num = getNum(list);

                    expression.add(num);
                }

                while(!s1.isEmpty()){

                    expression.add(s1.pop());
                }
            }

            i--;
        }

        return expression;
    }

    private static double getNum(List<Character> list){

        double num = 0;

        char[] numArr = new char[list.size()+50];

        for(int i = list.size() - 1, j = 0; i >= 0; i--, j++){

            numArr[j] = list.get(i) ;
            if (numArr[j]=='s'){
                j-=2;
                numArr[j]='s';
                break;
            }
            if (numArr[j]=='c'){
                j-=2;
                numArr[j]='c';
                break;
            }
            if (numArr[j]=='t'){
                j-=2;
                numArr[j]='t';
                break;
            }
            if (numArr[j]=='e'){
                numArr[j]='2';
                numArr[j+1]='.';
                numArr[j+2]='7';
                numArr[j+3]='1';
                j+=4;
            }
            else if (numArr[j]=='π'){
                numArr[j]='3';
                numArr[j+1]='.';
                numArr[j+2]='1';
                numArr[j+3]='4';
                j+=3;
            }


        }

        list.clear();

        String str = new String(numArr);

        num = Double.parseDouble(str);

        return num;
    }

    /**
     * 计算两数的四则运算结果
     *
     * @param num1
     * @param num2
     * @param op
     * @return
     * @throws IllegalArgumentException
     */
    private static double calc(double num1, double num2, char op)
            throws IllegalArgumentException {

        switch (op) {
            case '%':
                return num1 %num2;
            case '^':
                return Math.pow(num1,num2);
            case '√':
                return Math.pow(num2,1/num1);
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case 'x':
                return num1 * num2;
            case '÷':
                if (num2 == 0) throw new IllegalArgumentException("divisor can't be 0.");
                return num1 / num2;
            default:
                return 0;
        }
    }

    /**
     * 比较两个运算符的优先级
     *
     * @param op1
     * @param op2
     *
     * @return 1：高，0：相等，-1：低
     */
    private static int priorityCompare(char op1, char op2) {

        switch (op1) {

            case '+':case '-':case '%':
                return (op2 == 'x' || op2 == '÷'||op2=='^'||op2== '√'||op2=='s'||op2=='c'||op2=='t'? -1 : 0);
            case 'x': case '÷':
                if (op2== '+' || op2 == '-'||op2=='%'  ){
                    return 1;
                }
                else if(op2=='^'||op2== '√'||op2=='s'||op2=='c'||op2=='t'){
                    return -1;
                }
                else return 0;
            case 's':case 'c':case 't':
                if (op2=='^'||op2== '√'){
                    return -1;
                }
                else if(op2== '+' || op2 == '-'||op2=='%' ){
                    return 1;
                }
                else return 0;
            case '^':case '√':
                return (op2== '^'||op2== '√'?0:-1);
        }

        return 1;
    }
    private static double angle(double num,char op){
        switch (op){
            case 's':
                return Math.sin(num*Math.PI/180);
            case 'c':
                return Math.cos(num*Math.PI/180);
            case 't':
                return Math.tan(num*Math.PI/180);
            default:
                return 0;
        }
    }

    /**
     * 判断是否为运算符
     * @param c
     * @return
     */
    private static boolean isOperator(char c) {
        return (c=='+' || c=='-' || c=='x' || c=='÷'|| c=='^'||c== '√'||c=='%'||c== 's' || c == 'c'||c=='t' );
    }
}