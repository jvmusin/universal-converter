package com.github.jvmusin.universalconverter.number;

/**
 * Фабрика для создания чисел типа {@link Number}.
 *
 * @param <TNumber> тип числа.
 */
public interface NumberFactory<TNumber extends Number<TNumber>> {
  /**
   * Возвращает единицу типа {@link TNumber}.
   *
   * @return Единицу типа {@link TNumber}.
   */
  TNumber one();

  /**
   * Парсит строку в число.
   *
   * @param s строка для парсинга в число.
   * @return Спарсенное число.
   * @throws IllegalArgumentException если строка равна {@code null}.
   * @throws NumberFormatException если число спарсить не удалось.
   */
  TNumber parse(String s);
}
