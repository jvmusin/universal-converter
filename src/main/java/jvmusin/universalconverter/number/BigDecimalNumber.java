package jvmusin.universalconverter.number;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import lombok.Data;

/**
 * Обёртка для чисел типа {@link BigDecimal}.
 *
 * <p>Внутри себя держит кроме самого числа типа {@link BigDecimal} ещё и свойства {@link
 * BigDecimalNumberProperties}, используемые при выполении операций над {@link BigDecimal}.
 *
 * <p>При выполнении любых операций, возвращающих {@link BigDecimalNumber}, сохраняет текущие
 * свойства {@link BigDecimalNumberProperties} в результате.
 *
 * <p>Представляет из себя золотую середину между абсолютно точным, но затратным {@link
 * BigIntFractionNumber} и быстро теряющим точность, но лёгким {@link DoubleNumber}.
 */
@Data
class BigDecimalNumber implements Number<BigDecimalNumber> {

  /** Текущее значение. */
  private final BigDecimal value;

  /** Свойства для выполнения операций над {@link BigDecimal}. */
  private final BigDecimalNumberProperties props;

  @Override
  public BigDecimalNumber multiplyBy(BigDecimalNumber other) {
    return new BigDecimalNumber(value.multiply(other.value, props.getMathContext()), props);
  }

  /**
   * Делит текущее значение на другое.
   *
   * @param other значение, на которое нужно разделить текущее число.
   * @return {@code this / other}.
   * @throws ArithmeticException при делении на {@code 0}.
   */
  @Override
  public BigDecimalNumber divideBy(BigDecimalNumber other) {
    return new BigDecimalNumber(value.divide(other.value, props.getMathContext()), props);
  }

  /**
   * Возвращает обратное значение вида {@code 1/this}.
   *
   * @return {@code 1 / this}.
   * @throws ArithmeticException если {@code this = 0}.
   */
  @Override
  public BigDecimalNumber inverse() {
    return new BigDecimalNumber(ONE.divide(value, props.getMathContext()), props);
  }

  /**
   * Проверяет, что {@code this} и {@code other} отличаются не более, чем на {@link
   * BigDecimalNumberProperties#getMaximalDifferenceToBeEqual() props.maximalDifferenceToBeEqual}.
   *
   * @param other значение, которое нужно проверить на приблизительное равенство с текущим числом.
   * @return {@code abs(this - other) <= props.maximalDifferenceToBeEqual}.
   */
  @Override
  public boolean isNearlyEqualTo(BigDecimalNumber other) {
    MathContext mc = props.getMathContext();
    BigDecimal difference = value.subtract(other.value, mc).abs(mc);
    return difference.compareTo(props.getMaximalDifferenceToBeEqual()) <= 0;
  }

  /**
   * Проверяет, что текущее число больше нуля с учётом {@link
   * BigDecimalNumberProperties#getMaximalDifferenceToBeEqual() props.maximalDifferenceToBeEqual}.
   *
   * @return {@code this > props.maximalDifferenceToBeEqual}.
   */
  @Override
  public boolean isNearlyPositive() {
    return value.compareTo(props.getMaximalDifferenceToBeEqual()) > 0;
  }

  /**
   * Возвращает число в виде строки, не округляя его.
   *
   * @return Число в виде строки.
   * @see BigDecimal#toPlainString()
   */
  @Override
  public String toString() {
    return value.toPlainString();
  }
}