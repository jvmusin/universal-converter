package jvmusin.universalconverter.web;

import jvmusin.universalconverter.fraction.ComplexFraction;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class WebUtils {
  /**
   * Конвертирует выражение из запроса на конвертацию в дробь {@link ComplexFraction}.
   *
   * <p>Игнорирует все пробелы в выражении.
   *
   * @param expression выражение, которое необходимо конвертировать.
   * @return Дробь, соответствующую выражению.
   * @throws IllegalArgumentException если выражение равно {@code null}.
   * @throws MalformedExpressionException если не удалось конвертировать выражение.
   */
  public static ComplexFraction<String> convertExpressionToFraction(String expression) {
    Assert.notNull(expression, "Выражение не может быть равно null");
    if (expression.contains(" ")) return convertExpressionToFraction(expression.replace(" ", ""));
    if (expression.isEmpty() || expression.equals("1"))
      return new ComplexFraction<>(emptyList(), emptyList());

    int[] delimiterPositions =
        IntStream.range(0, expression.length()).filter(i -> expression.charAt(i) == '/').toArray();
    switch (delimiterPositions.length) {
      case 0:
        {
          String[] parts = expression.split("[*]", -1);
          if (Arrays.stream(parts).anyMatch(String::isEmpty)) {
            throw new MalformedExpressionException(
                "Выражение имеет знак умножения, "
                    + "не окружённый с обеих сторон величинами измерения: "
                    + expression);
          }
          return new ComplexFraction<>(asList(parts), emptyList());
        }
      case 1:
        {
          int delimiterPosition = delimiterPositions[0];
          String numerator = expression.substring(0, delimiterPosition);
          String denominator = expression.substring(delimiterPosition + 1);
          if (numerator.isEmpty() || denominator.isEmpty()) {
            throw new MalformedExpressionException(
                "Знак деления должен разделять две непустые части: " + expression);
          }
          return new ComplexFraction<>(
              convertExpressionToFraction(numerator).getNumerator(),
              convertExpressionToFraction(denominator).getNumerator());
        }
      default:
        {
          throw new MalformedExpressionException(
              "В выражении разрешено не больше одного знака деления: " + expression);
        }
    }
  }
}
