package jvmusin.universalconverter.number;

import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/** Фабрика, используемая для создания чисел типа {@link BigIntFractionNumber}. */
public class BigIntFractionNumberFactory implements NumberFactory<BigIntFractionNumber> {
  private static final Pattern NUMBER_PATTERN =
      Pattern.compile("(?<before>\\d*)([.,](?<after>\\d*))?");

  @Override
  public BigIntFractionNumber one() {
    return new BigIntFractionNumber(ONE, ONE);
  }

  /**
   * Парсит строку в дробь.
   *
   * <ul>
   *   <li>Строка не должна быть равна {@code null};
   *   <li>Строка не должна быть пустой или состоять только из разделителя;
   *   <li>Разделитель целой и дробной части должен быть точкой или запятой;
   *   <li>Разделитель может отсутствовать в записи числа, что означает отсутствие дробной части;
   *   <li>Число может иметь максимум один разделитель в своей записи;
   *   <li>В строке должна быть хотя бы одна цифра;
   *   <li>Число не должно быть отрицательным;
   *   <li>Символы, отличные от разделителя, должны быть цифрами.
   * </ul>
   *
   * @param s строка для парсинга в дробь.
   * @return Спарсенную из строки дробь.
   * @throws IllegalArgumentException если строка {@code s == null}.
   * @throws NumberFormatException если число в строке имеет неверный формат.
   */
  @Override
  public BigIntFractionNumber parse(String s) {
    Assert.notNull(s, "Строка не может быть null");
    Matcher matcher = NUMBER_PATTERN.matcher(s);
    if (s.isEmpty() || (s.length() == 1 && !Character.isDigit(s.charAt(0))) || !matcher.matches()) {
      throw new NumberFormatException("Неверный формат числа: " + s);
    }
    String beforeDot = matcher.group("before");
    String afterDot = matcher.group("after");
    BigInteger shift = BigInteger.TEN.pow(afterDot == null ? 0 : afterDot.length());

    BigInteger before = beforeDot.isEmpty() ? ZERO : new BigInteger(beforeDot);
    BigInteger after = afterDot == null || afterDot.isEmpty() ? ZERO : new BigInteger(afterDot);

    return new BigIntFractionNumber(before.multiply(shift).add(after), shift);
  }
}
