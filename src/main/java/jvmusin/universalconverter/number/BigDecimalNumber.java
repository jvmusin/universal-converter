package jvmusin.universalconverter.number;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import lombok.Data;

/**
 * Обёртка для чисел типа {@link BigDecimal}.
 *
 * <p>Внутри себя держит кроме самого числа типа {@link BigDecimal} ещё и {@link MathContext},
 * используемый при выполении операций над {@link BigDecimal}.
 *
 * <p>При выполнении любых операций, возвращающих {@link BigDecimalNumber}, сохраняет текущий {@link
 * MathContext} в результате.
 *
 * <p>Представляет из себя золотую середину между абсолютно точным, но затратным {@link
 * BigIntFractionNumber} и быстро теряющим точность, но лёгким {@link DoubleNumber}.
 */
@Data
public class BigDecimalNumber implements Number<BigDecimalNumber> {

  /** Текущее значение. */
  private final BigDecimal value;

  /** {@link MathContext} для выполнения операций над {@link BigDecimal}. */
  private final MathContext mathContext;

  @Override
  public BigDecimalNumber multiplyBy(BigDecimalNumber other) {
    return new BigDecimalNumber(value.multiply(other.value, mathContext), mathContext);
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
    return new BigDecimalNumber(value.divide(other.value, mathContext), mathContext);
  }

  /**
   * Возвращает обратное значение вида {@code 1/this}.
   *
   * @return {@code 1 / this}.
   * @throws ArithmeticException если {@code this = 0}.
   */
  @Override
  public BigDecimalNumber inverse() {
    return new BigDecimalNumber(ONE.divide(value, mathContext), mathContext);
  }

  @Override
  public boolean isPositive() {
    return value.signum() > 0;
  }

  @Override
  public String toString() {
    return value.toPlainString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BigDecimalNumber that = (BigDecimalNumber) o;
    return value.compareTo(that.value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
