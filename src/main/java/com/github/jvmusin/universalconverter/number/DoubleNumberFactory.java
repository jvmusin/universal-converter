package com.github.jvmusin.universalconverter.number;

import org.springframework.util.Assert;

/** Фабрика, используемая для создания чисел типа {@link DoubleNumber}. */
public class DoubleNumberFactory implements NumberFactory<DoubleNumber> {
  @Override
  public DoubleNumber one() {
    return new DoubleNumber(1);
  }

  /**
   * Парсит строку в число.
   *
   * <p>Парсинг производится методом {@link Double#parseDouble(String)}.
   *
   * @param s строка для парсинга в число.
   * @return Спарсенное число.
   * @throws IllegalArgumentException если строка равна {@code null}.
   * @throws NumberFormatException если число спарсить не удалось.
   * @see Double#parseDouble(String)
   */
  @Override
  public DoubleNumber parse(String s) {
    Assert.notNull(s, "Строка не может быть null");
    return new DoubleNumber(Double.parseDouble(s));
  }
}
