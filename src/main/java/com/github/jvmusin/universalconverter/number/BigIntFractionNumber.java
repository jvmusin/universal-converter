package com.github.jvmusin.universalconverter.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
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

  /**
   * {@link MathContext}, используемый для перевода числа в строку.
   *
   * <p>Берёт 34 значащие цифры у числа и округляет по правилам математики ({@link
   * RoundingMode#HALF_UP}).
   */
  private static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_UP);

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
   *   <li>Если числитель и знаменатель не взаимно просты, то в результирующей дроби они будут
   *       сокращены до взаимно простых.
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
    BigInteger newNumerator = numerator.multiply(other.numerator);
    BigInteger newDenominator = denominator.multiply(other.denominator);
    return new BigIntFractionNumber(newNumerator, newDenominator);
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
    BigInteger newNumerator = numerator.multiply(other.denominator);
    BigInteger newDenominator = denominator.multiply(other.numerator);
    return new BigIntFractionNumber(newNumerator, newDenominator);
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

  @Override
  public boolean isPositive() {
    return numerator.signum() > 0;
  }

  /**
   * Берёт 34 значащие цифры у числа и округляет по правилам математики ({@link
   * RoundingMode#HALF_UP}).
   *
   * @return Строковое представление текущего числа.
   */
  @Override
  public String toString() {
    BigDecimal numerator = new BigDecimal(this.numerator);
    BigDecimal denominator = new BigDecimal(this.denominator);
    return numerator.divide(denominator, MATH_CONTEXT).toPlainString();
  }
}
