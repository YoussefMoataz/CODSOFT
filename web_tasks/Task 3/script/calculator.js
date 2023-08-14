class Calculator {
    getPrecedence(operator) {
        switch (operator) {
            case '+':
                return 1;
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    applyOperation(operand1, operand2, operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            default:
                return 0.0;
        }
    }

    evaluate(exp) {
        let expression = exp;

        if (expression[0] === '-') {
            expression = ('-' + expression.substring(1).replace('-', '+-'));
        } else {
            expression = expression.replace('-', '+-');
        }

        expression = expression.replace('*+-', '*-').replace('%', '/100');

        let i = 0;

        const numbers = [];
        const operators = [];

        while (i < expression.length) {
            if (expression[i] === '-') {
                // handle negative numbers
                i++;
                let num = '-';

                // build the number char by char.
                while (i < expression.length && (expression[i].match(/\d/) || expression[i] === '.')) {
                    if (expression[i] === '.') {
                        num += '.';
                    } else {
                        num += expression[i] - '0';
                    }
                    i++;
                }
                if (num.length > 1) {
                    numbers.unshift(parseFloat(num));
                } else {
                    operators.unshift(num.charAt(0));
                }
                i--;
            } else if (expression[i].match(/\d/)) {
                // handle numbers
                let num = '';

                // build the number char by char.
                while (i < expression.length && (expression[i].match(/\d/) || expression[i] === '.')) {
                    if (expression[i] === '.') {
                        num += '.';
                    } else {
                        num += expression[i] - '0';
                    }
                    i++;
                }
                numbers.unshift(parseFloat(num));
                i--;
            } else {
                // finish operators with higher precedence
                while (operators.length > 0 && this.getPrecedence(expression[i]) < this.getPrecedence(operators[0])) {
                    const num2 = numbers.shift();
                    const num1 = numbers.shift();
                    const op = operators.shift();
                    numbers.unshift(this.applyOperation(num1, num2, op));
                }
                operators.unshift(expression[i]);
            }
            i++;
        }

        // finish the remaining operators
        while (operators.length > 0) {
            const num2 = numbers.shift();
            const num1 = numbers.shift();
            const op = operators.shift();
            numbers.unshift(this.applyOperation(num1, num2, op));
        }

        return numbers[0];
    }
}
