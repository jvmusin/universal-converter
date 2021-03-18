package com.github.jvmusin.universalconverter.number;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.Data;

/** Число, представляемое как {@code double} внутри. */
@Data
public class DoubleNumber implements Number<DoubleNumber> {

  /**
   * {@link MathContext}, используемый для перевода числа в строку.
   *
   * <p>Берёт 34 значащие цифры у числа и округляет по правилам математики ({@link
   * RoundingMode#HALF_UP}).
   */
  private static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_UP);

  /** Текущее значение. */
  private final double value;

  @Override
  public DoubleNumber multiplyBy(DoubleNumber other) {
    return new DoubleNumber(value * other.value);
  }

  @Override
  public DoubleNumber divideBy(DoubleNumber other) {
    return new DoubleNumber(value / other.value);
  }

  @Override
  public DoubleNumber inverse() {
    return new DoubleNumber(1 / value);
  }

  @Override
  public boolean isPositive() {
    return value > 0;
  }

  /**
   * Берёт 34 значащие цифры у числа и округляет по правилам математики ({@link
   * RoundingMode#HALF_UP}).
   *
   * @return Строковое представление текущего числа.
   */
  @Override
  public String toString() {
    return new BigDecimal(value, MATH_CONTEXT).toPlainString();
  }
}
