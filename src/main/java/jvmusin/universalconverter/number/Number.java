package jvmusin.universalconverter.number;

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
   * @return Произведение текущего значения на {@code other}.
   */
  TNumber multiplyBy(TNumber other);

  /**
   * Делит текущее значение на другое.
   *
   * @param other значение, на которое нужно разделить текущее число.
   * @return Частное от деления текущего значения на {@code other}.
   */
  TNumber divideBy(TNumber other);

  /**
   * Возвращает обратное значение вида {@code 1/this}.
   *
   * @return Обратное значение.
   */
  TNumber inverse();

  /**
   * Проверяет, равны ли текущее и заданное значения с учётом погрешности (далее "Примерно равны").
   *
   * <p>Операция не обязана быть транзитивной, то есть если значения {@code A} и {@code B} примерно
   * равны и {@code B} и {@code C} примерно равны, то {@code A} и {@code C} не обязательно будут
   * примерно равны.
   *
   * @param other значение, которое нужно проверить на приблизительное равенство с текущим числом.
   * @return {@code true}, если текущее и заданное значения равны с учётом погрешности и {@code
   *     false} иначе.
   */
  boolean isNearlyEqualTo(TNumber other);

  /**
   * Проверяет, положительно ли текущее значение с учётом погрешности.
   *
   * @return {@code true}, если текущее значение положительно с учётом погрешности и {@code false}
   *     иначе.
   */
  boolean isNearlyPositive();
}
