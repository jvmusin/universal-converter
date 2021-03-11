package jvmusin.universalconverter.web;

import jvmusin.universalconverter.ComplexFraction;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class WebUtils {
    /**
     * Конвертирует выражение из запроса на конвертацию в дробь {@link ComplexFraction}.
     *
     * @param expression выражение, которое необходимо конвертировать
     * @return дробь, соответствующую выражению
     * @throws MalformedExpressionException если не удалось конвертировать выражение
     */
    public static ComplexFraction<String> convertExpressionToFraction(String expression) {
        if (expression.contains(" ")) return convertExpressionToFraction(expression.replace(" ", ""));
        if (expression.isEmpty() || expression.equals("1")) return new ComplexFraction<>(emptyList(), emptyList());

        int[] pos = IntStream.range(0, expression.length()).filter(i -> expression.charAt(i) == '/').toArray();
        switch (pos.length) {
            case 0: {
                String[] parts = expression.split("\\*", -1);
                if (Arrays.stream(parts).anyMatch(String::isEmpty)) {
                    throw new MalformedExpressionException(
                            "Выражение имеет знак умножения, " +
                                    "не окружённый с обеих сторон величинами измерения: " + expression);
                }
                return new ComplexFraction<>(asList(parts), emptyList());
            }
            case 1: {
                String[] parts = expression.split("/", -1);
                String a = parts[0], b = parts[1];
                if (a.isEmpty() || b.isEmpty()) {
                    throw new MalformedExpressionException(
                            "Знак деления должен разделять две непустые части: " + expression
                    );
                }
                return new ComplexFraction<>(
                        convertExpressionToFraction(a).getNumerator(),
                        convertExpressionToFraction(b).getNumerator()
                );
            }
            default: {
                throw new MalformedExpressionException(
                        "В выражении разрешено не больше одного знака деления: " + expression
                );
            }
        }
    }
}
