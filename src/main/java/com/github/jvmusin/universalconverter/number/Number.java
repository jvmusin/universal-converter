package com.github.jvmusin.universalconverter.number;

/**
 * Обёртка над числом.
 *
 * <p>Используется как обёртка над типами данных для простой взаимозаменяемости.
 *
 * <p>Предполагается, что обёртка будет использоваться как рекурсивный дженерик, то есть
 *
 * <pre>class ConcreteClass implements Number&lt;ConcreteClass&gt; { ... }</pre>
 *
 * @param <TNumber> тип числа.
 */
public interface Number<TNumber extends Number<TNumber>> {

  /**
   * Умножает текущее значение на другое.
   *
   * @param other значение, на которое нужно умножить текущее число.
   * @return {@code this * other}.
   */
  TNumber multiplyBy(TNumber other);

  /**
   * Делит текущее значение на другое.
   *
   * <p>В некоторых реализациях может бросать {@link ArithmeticException} при делении на {@code 0}.
   *
   * @param other значение, на которое нужно разделить текущее число.
   * @return {@code this / other}.
   */
  TNumber divideBy(TNumber other);

  /**
   * Возвращает обратное значение вида {@code 1/this}.
   *
   * <p>В некоторых реализациях может бросать {@link ArithmeticException} при попытке получить
   * обратное к нулю.
   *
   * @return {@code 1 / this}.
   */
  TNumber inverse();

  /**
   * Проверяет, положительно ли текущее значение.
   *
   * @return {@code this > 0}.
   */
  boolean isPositive();
}
