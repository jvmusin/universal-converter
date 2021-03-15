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
   * Проверяет, равны ли текущее и заданное значения с учётом погрешности (далее "Примерно равны").
   *
   * <p>Операция не обязана быть транзитивной, то есть если значения {@code A} и {@code B} примерно
   * равны и {@code B} и {@code C} примерно равны, то {@code A} и {@code C} не обязательно будут
   * примерно равны.
   *
   * <p>Если реализация предполагает хранение чисел с абсолютной точностью, то этот метод равносилен
   * методу {@link #equals}.
   *
   * @param other значение, которое нужно проверить на приблизительное равенство с текущим числом.
   * @return {@code this ≈ other}.
   */
  boolean isNearlyEqualTo(TNumber other);

  /**
   * Проверяет, положительно ли текущее значение с учётом погрешности.
   *
   * <p>Если реализация предполагает хранение чисел с абсолютной точностью, то этот метод проверяет,
   * что {@code this > 0}.
   *
   * @return {@code this > 0}.
   */
  boolean isNearlyPositive();
}
