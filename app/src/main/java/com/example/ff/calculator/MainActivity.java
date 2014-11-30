package com.example.ff.calculator;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Vibrator;

/**
 * 计算器
 */
public class MainActivity extends Activity implements OnClickListener{


    private StringBuffer strBuf = null;
    private StringBuffer old;
    private Vibrator vibrator;

    // 显示
    private EditText show = null;
    private EditText odshow = null;

    // 计算器键盘
    private Integer[] btns = new Integer[]{
            R.id.MC,R.id.MR,R.id.MS,R.id.Madd,R.id.Mdelete,
            R.id.log,R.id.ln,R.id.daoshu,R.id.jiecheng,R.id.xiangfanshu,
            R.id.clear, R.id.back,R.id.yu,R.id.square,R.id.pingf,
            R.id.sin,R.id.cos,R.id.tan,R.id.e,R.id.pi,
            R.id.seven, R.id.eight, R.id.nine, R.id.divide, R.id.left,
            R.id.four, R.id.five, R.id.six, R.id.multiply, R.id.right,
            R.id.one, R.id.tow, R.id.three, R.id.minus,
            R.id.zero, R.id.decimal, R.id.plus, R.id.calc };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar =getActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        show = (EditText) findViewById(R.id.rs_show);
        odshow = (EditText)findViewById(R.id.re_show);

        strBuf = new StringBuffer();

        // 给每个按键添加事件
        for(Integer id: btns){

            Button btn = (Button) findViewById(id);

            // 添加监听
            btn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        Button btn = (Button) findViewById(id);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern ={0,50};
        vibrator.vibrate(pattern,-1);

        String content = "";

        switch (id) {


            case R.id.clear:

                strBuf.setLength(0);

                break;

            case R.id.back:

                content = strBuf.length() > 1? strBuf.substring(0, strBuf.length() - 1): content;

                strBuf = new StringBuffer(content);
                break;
            case R.id.xiangfanshu:{
                if (strBuf.toString().toCharArray()[0]=='-'){
                    strBuf.delete(0,1);
                }else {
                    strBuf.insert(0,'-');
                }
                content = strBuf.toString();
                strBuf = new StringBuffer(content);
                break;
            }
            case R.id.daoshu: {
                double i = Double.valueOf(strBuf.toString());
                double m = 1 / i;
                content = Double.toString(m);
                strBuf = new StringBuffer(content);
                break;
            }
            case R.id.calc:
                content = calculate(strBuf.toString());
                strBuf = new StringBuffer(content);
                odshow.setText(content);
                odshow.setSelection(content.length());
                break;
            case R.id.Madd:
                content = strBuf.toString();
                old =new StringBuffer(content);
                strBuf =new StringBuffer(content);
                break;
            case R.id.Mdelete:
                old = new StringBuffer("");
                content = strBuf.toString();
                strBuf = new StringBuffer(content);
                break;
            case R.id.MS:
                content = strBuf.toString();
                old =new StringBuffer(content);
                strBuf =new StringBuffer(content);
                break;
            case R.id.MC:
                old = new StringBuffer("");
                content = strBuf.toString();
                strBuf = new StringBuffer(content);
                break;
            case R.id.MR:
                content = old.toString();
                strBuf = new StringBuffer(content);
                old = new StringBuffer(content);
                break;
            default:

                String text = btn.getText().toString();

                content = strBuf.append(text).toString();

                break;
        }

        show.setText(content);

        show.setSelection(content.length());
    }

    // 计算结果
    public String calculate(String str){

        try{
            str = CalcUtil.calculate(str);

        }catch(Exception e){

            Toast.makeText(this, "表达式错误！", Toast.LENGTH_SHORT).show();
        }

        return str;
    }

}
