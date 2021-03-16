package jvmusin.universalconverter.number;

import java.util.Locale;
import lombok.Data;

/** Число, представляемое как {@code double} внутри. */
@Data
public class DoubleNumber implements Number<DoubleNumber> {
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
   * Возвращает текущее значение с {@code 20} знаками после запятой.
   *
   * @return Текущее значение с {@code 20} знаками после запятой.
   */
  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "%.20f", value);
  }
}
