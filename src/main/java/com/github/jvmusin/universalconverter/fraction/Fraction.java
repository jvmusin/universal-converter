package com.github.jvmusin.universalconverter.fraction;

import lombok.Data;

/**
 * Класс, описывающий дробь {@code числитель/знаменатель}.
 *
 * @param <T> тип числителя и знаменателя.
 */
@Data
public class Fraction<T> {

  /** Числитель. */
  private final T numerator;

  /** Знаменатель. */
  private final T denominator;

  @SuppressWarnings("unused") // Used by Lombok in ComplexFraction
  public Fraction() {
    this(null, null);
  }

  public Fraction(T numerator, T denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }
}
