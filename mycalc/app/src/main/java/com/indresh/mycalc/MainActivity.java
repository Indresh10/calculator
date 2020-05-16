package com.indresh.mycalc;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView t1, t2;//for text view
    boolean start_bracket, dot, last, error,operror;//toggle between conditions
    int i,cnt,lcnt;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.data);
        t2 = findViewById(R.id.output);
        dot = false;
        start_bracket = true;
        last = false;
    }


    @SuppressLint("SetTextI18n")
    public void number(View v) {
        if (i > 0)
            start_bracket = false;
        if (cnt == 10)
            Toast.makeText(this, "Can't enter more than 10 digits\nbefore or after decimal", Toast.LENGTH_SHORT).show();
        else {
            Button b=(Button) v;
            if (error){
                t1.setText(b.getText());
                error=false;
                t2.setText("");
            }
            else
            {
                if (t1.getText().toString().endsWith(")"))
                    opr(findViewById(R.id.Multi));
                t1.append(b.getText());
            }
            last = true;
            cnt++;
            operror=false;
            onEqual(t2);
        }
    }
    public void onClear(View v){
        t1.setText("");
        t2.setText("");
        dot=false;
        error=true;
        start_bracket=true;
        last=false;
        cnt=0;
    }

    @SuppressLint("SetTextI18n")
    public void point(View v) {
        if (!dot) {
            dot = true;
            t1.append(".");
            lcnt=cnt;
            cnt=0;
            last=false;
        }
    }


    @SuppressLint("SetTextI18n")
    public void brkt(View v) {
        if (start_bracket) {
            if (last)
                opr(findViewById(R.id.Multi));
            t1.append("(");
            i++;
            last=false;
        } else {
            t1.append(")");
            start_bracket = true;
            i--;
            last=true;
        }
        dot=true;
        lcnt=cnt;
        cnt=0;
        operror=false;
    }

    @SuppressLint("SetTextI18n")
    public void opr(View v) {
        if (last && !error) {
            Button b=(Button) v;
            if (b.getText().toString().equals("Mod"))
                t1.append("%");
            else
                t1.append(b.getText());
            last=false;
            dot=false;
            cnt=0;
        }
        operror=true;
    }

    public void backspc(View v){
        String text=t1.getText().toString();
        t2.setText("");
        if (text.length()>0) {
            if (text.endsWith(".")) {
                dot = false;
                cnt = lcnt;
            } else if (text.endsWith("(")) {
                start_bracket = true;
                cnt = lcnt;
                i--;
            } else if (text.endsWith(")")) {
                cnt = lcnt;
                i++;
            }
            text = text.substring(0, text.length() - 1);
            if (cnt > 0) {
                last = true;
            }
            t1.setText(text);
        }
    }

    public void answer(View v){
        onEqual(t2);
        error=true;
        last=true;
    }

    @SuppressLint("SetTextI18n")
    public void onEqual(TextView t){
        String txt = t1.getText().toString();
        if (operror){
            if (txt.endsWith("+") || txt.endsWith("-"))
                txt+="0";
            else
                txt+="1";
        }
        if (i>0){
            for (int j=i;j>0;j--)
                txt+=")";
        }
            Expression exp= new ExpressionBuilder(txt).build();
            try {
                DecimalFormat form=new DecimalFormat("##########.##########");
                double result = exp.evaluate();
                String res=form.format(result);
                t.setText(res);
                }
            catch (ArithmeticException ex) {
                t1.setText("Error");
                error = true;
                last = false;
            }
        }
    }