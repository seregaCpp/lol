import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RPNCalculator {
    public static List<String> toRPN(String expression) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        List<String> tokens = tokenize(expression);

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && isOperator(operators.peek())) {
                    String top = operators.peek();

                    if ((isLeftAssociative(token) && priority(token) <= priority(top)) ||
                            (!isLeftAssociative(token) && priority(token) < priority(top))) {
                        output.add(operators.pop());
                    } else {
                        break;
                    }
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }

                if (operators.isEmpty()) {
                    throw new RuntimeException("Mismatched brackets");
                }

                operators.pop(); // убираем "("
            } else {
                throw new RuntimeException("Unknown token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new RuntimeException("Mismatched brackets");
            }
            output.add(op);
        }

        return output;
    }

    // Вычисление ОПН
    public static double evaluateRPN(List<String> rpn) {
        Stack<Double> stack = new Stack<>();

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new RuntimeException("There are not enough operands for the operation");
                }

                double b = stack.pop();
                double a = stack.pop();

                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new RuntimeException("Division by zero");
                        }
                        stack.push(a / b);
                        break;
                    case "^":
                        stack.push(Math.pow(a, b));
                        break;
                    default:
                        throw new RuntimeException("Unknown operation: " + token);
                }
            } else {
                throw new RuntimeException("Invalid token: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Calculating ERROR");
        }

        return stack.pop();
    }

    // Разбиение строки на токены
    public static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        expression = expression.replaceAll("\\s+", "");

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.' ||
                    (c == '-' && (i == 0 || expression.charAt(i - 1) == '(' ||
                            isOperatorChar(expression.charAt(i - 1))))) {

                StringBuilder number = new StringBuilder();
                number.append(c);
                i++;

                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }

                if (number.toString().equals("-")) {
                    tokens.add("0");
                    tokens.add("-");
                    continue;
                }

                tokens.add(number.toString());
            }

            else if (isOperatorChar(c) || c == '(' || c == ')') {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                throw new RuntimeException("Invalid symbol: " + c);
            }
        }

        return tokens;
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^");
    }

    public static boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static int priority(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    public static boolean isLeftAssociative(String op) {
        // ^ обычно правоассоциативная операция
        return !op.equals("^");
    }
}
