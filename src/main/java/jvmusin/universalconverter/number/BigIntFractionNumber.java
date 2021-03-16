package jvmusin.universalconverter.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Locale;
import lombok.Data;

/**
 * Дробь на {@link BigInteger} значениях.
 *
 * <p>В качестве числителя и знаменателя хранит {@link BigInteger}-ы. Предоставляет абсолютную
 * точность за счёт более медленных операций и большим потреблением памяти по сравнению с
 * примитивами.
 */
@Data
public class BigIntFractionNumber implements Number<BigIntFractionNumber> {

  /** Числитель. */
  private final BigInteger numerator;

  /** Знаменатель. */
  private final BigInteger denominator;

  /**
   * Создаёт {@link BigIntFractionNumber} из числителя {@code numerator} и знаменателя {@code
   * denominator}.
   *
   * <ul>
   *   <li>Если знаменатель равен нулю, бросает {@link ArithmeticException};
   *   <li>Если числитель равен нулю, то создастся дробь {@code 0/1};
   *   <li>Если результирующая дробь отрицательна, то знак минуса в результирующей дроби останется
   *       только у числителя;
   *   <li>Если числитель и знаменатель не взаимнопросты, то в результрующей дроби они будут
   *       сокращены до взаимнопростых.
   * </ul>
   *
   * @param numerator числитель.
   * @param denominator знаменатель.
   * @throws ArithmeticException если знаменатель равен нулю.
   */
  public BigIntFractionNumber(BigInteger numerator, BigInteger denominator) {
    if (denominator.equals(BigInteger.ZERO)) {
      throw new ArithmeticException("Знаменатель не может быть нулём");
    }

    if (numerator.equals(BigInteger.ZERO)) {
      this.numerator = BigInteger.ZERO;
      this.denominator = BigInteger.ONE;
      return;
    }

    if (denominator.compareTo(BigInteger.ZERO) < 0) {
      numerator = numerator.negate();
      denominator = denominator.negate();
    }

    BigInteger gcd = numerator.abs().gcd(denominator);
    this.numerator = numerator.divide(gcd);
    this.denominator = denominator.divide(gcd);
  }

  @Override
  public BigIntFractionNumber multiplyBy(BigIntFractionNumber other) {
    return new BigIntFractionNumber(
        numerator.multiply(other.numerator), denominator.multiply(other.denominator));
  }

  /**
   * Делит текущее значение на другое.
   *
   * @param other значение, на которое нужно разделить текущее число.
   * @return Частное от деления текущего значения на {@code other}.
   * @throws ArithmeticException при делении на {@code 0}.
   */
  @Override
  public BigIntFractionNumber divideBy(BigIntFractionNumber other) {
    return new BigIntFractionNumber(
        numerator.multiply(other.denominator), denominator.multiply(other.numerator));
  }

  /**
   * Возвращает обратное значение вида {@code denominator/numerator}.
   *
   * @return Обратное значение.
   * @throws ArithmeticException если дробь равна нулю.
   */
  @Override
  public BigIntFractionNumber inverse() {
    return new BigIntFractionNumber(denominator, numerator);
  }

  /**
   * Проверяет, положительна ли текущая дробь.
   *
   * @return {@code true}, если текущая дробь положительна и {@code false} иначе.
   */
  @Override
  public boolean isNearlyPositive() {
    return numerator.compareTo(BigInteger.ZERO) > 0;
  }

  @Override
  public String toString() {
    // TODO test it
    MathContext mc = MathContext.DECIMAL128;
    BigDecimal d = new BigDecimal(numerator).divide(new BigDecimal(denominator), mc);
    return String.format(Locale.ENGLISH, "%f", d);
    //        String result = d.toPlainString();
    //        return result.replace(',', '.');
  }
}
