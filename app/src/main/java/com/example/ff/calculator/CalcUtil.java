package com.example.ff.calculator;

/**
 * Created by FF on 2014/11/28.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.lang.Math;



public class CalcUtil {


    public static String calculate(String str){
        String nstr = dealstr(str);
        double rs = calcResult(toPolishNotation(nstr));

        // 整数去除小数点
        nstr = rs == (int)rs? String.valueOf((int)rs): String.valueOf(rs);

        return nstr;
    }




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
                if (op=='c'||op=='s'||op=='t'||op=='g'||op=='n'||op=='!'){
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



    private static String dealstr(String str){
        str=str.replace("sin","s");
        str=str.replace("cos","c");
        str=str.replace("tan","t");
        str=str.replace("log","g");
        str=str.replace("ln","n");
        for (int i=0;i<str.length();i++){
            if (str.toCharArray()[i]=='-'){
                if (str.toCharArray()[i-1]!='1'&&str.toCharArray()[i-1]!='2'&&str.toCharArray()[i-1]!='3'&&
                str.toCharArray()[i-1]!='4'&&str.toCharArray()[i-1]!='5'&&str.toCharArray()[i-1]!='6'&&str.toCharArray()[i-1]!='7'
                        &&str.toCharArray()[i-1]!='8'&&str.toCharArray()[i-1]!='9'&&str.toCharArray()[i-1]!='0'){
                    str.toString();
                    StringBuffer content = new StringBuffer(str);
                    content.insert(i,'0');
                    str =content.toString();
                    i+=1;
                }
                if (i==0){
                    str.toString();
                    StringBuffer content = new StringBuffer(str);
                    content.insert(i,'0');
                    str =content.toString();
                    i+=1;
                }
            }
        }
        return str;
    }
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
            if (numArr[j]=='e'){
                num = Math.E;
                break;
            }
            else if (numArr[j]=='π'){
                num = Math.PI;
                break;
            }

        }

        list.clear();

        String str = new String(numArr);
        if (num==0){
            num = Double.parseDouble(str);
        }


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
    private static int jiecheng(double  n){
        int m =1;
        for (;n>0;n--){
           m*=n;
        }
        return m;
    }
    private static double angle(double num,char op){
        switch (op){
            case '!':
                return jiecheng(num);
            case 'g':
                return Math.log10(num);
            case 'n':
                return Math.log(num);
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
        return (c=='+' || c=='-' || c=='x' || c=='÷'|| c=='^'||c== '√'||c=='%'||c== 's' || c == 'c'||c=='t'||c=='g'||c=='n'||c=='!' );
    }
}