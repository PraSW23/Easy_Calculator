package com.example.easy_calculator;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, SolTv;
    MaterialButton buttonC, button_openB, button_closeB, button_div, button_7, button_8, button_9, button_mul, button_4, button_5, button_6, button_add, button_1, button_2, button_3, button_sub, button_ac, button_0, button_dot, button_equals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        SolTv = findViewById(R.id.sol_tv);

        assign(buttonC, R.id.button_c);
        assign(button_openB, R.id.button_openB);
        assign(button_closeB, R.id.button_closeB);
        assign(button_div, R.id.button_divide);
        assign(button_7, R.id.button_7);
        assign(button_8, R.id.button_8);
        assign(button_9, R.id.button_9);
        assign(button_mul, R.id.button_mul);
        assign(button_4, R.id.button_4);
        assign(button_5, R.id.button_5);
        assign(button_6, R.id.button_6);
        assign(button_add, R.id.button_plus);
        assign(button_1, R.id.button_1);
        assign(button_2, R.id.button_2);
        assign(button_3, R.id.button_3);
        assign(button_sub, R.id.button_sub);
        assign(button_ac, R.id.button_ac);
        assign(button_0, R.id.button_0);
        assign(button_dot, R.id.button_dot);
        assign(button_equals, R.id.button_equals);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String btn_text = button.getText().toString();
        String data_to_cal = SolTv.getText().toString();

        if (btn_text.equals("AC")) {
            SolTv.setText("");
            resultTv.setText("0");
        } else if (btn_text.equals("C")) {
            if (data_to_cal.length() > 0)
                SolTv.setText(data_to_cal.substring(0, data_to_cal.length() - 1));
            else {
                SolTv.setText("");
            }
        } else if (btn_text.equals("=")) {
            // Calculate the result
            try {
                String result = calculateResult(data_to_cal);
                if (result != null) {
                    resultTv.setText(result);
                    SolTv.setText(result);
                } else {
                    resultTv.setText("Error");
                }
            } catch (ArithmeticException e) {
                resultTv.setText("Error: Division by zero");
            } catch (Exception e) {
                resultTv.setText("Error: Invalid expression");
            }
        } else {
            // Append the clicked button's text to the expression
            SolTv.append(btn_text);
        }
    }

    private String calculateResult(String expression) {
        // Remove unnecessary spaces from the expression
        expression = expression.replaceAll("\\s+", "");

        // Stack to store numbers
        Stack<Double> numbers = new Stack<>();
        // Stack to store operators
        Stack<Character> operators = new Stack<>();

        // Iterate through the expression
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                // Keep appending digits to form the number
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;
                // Push the parsed number to the stack
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processOperator(numbers, operators);
                }
                operators.pop(); // Remove '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    processOperator(numbers, operators);
                }
                operators.push(ch);
            }
        }

        // Process any remaining operators
        while (!operators.isEmpty()) {
            processOperator(numbers, operators);
        }

        // The final result should be on the top of the numbers stack
        if (!numbers.isEmpty()) {
            return String.valueOf(numbers.peek());
        } else {
            return null;
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        } else if (op == '*' || op == '/') {
            return 2;
        } else {
            return 0; // Assuming '(' has the lowest precedence
        }
    }

    private void processOperator(Stack<Double> numbers, Stack<Character> operators) {
        char op = operators.pop();
        double num2 = numbers.pop();
        double num1 = numbers.pop();
        double result = 0;
        switch (op) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 == 0) {
                    throw new ArithmeticException();
                }
                result = num1 / num2;
                break;
        }
        numbers.push(result);
    }

    void assign(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
}
