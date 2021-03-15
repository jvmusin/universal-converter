package jvmusin.universalconverter.number;

import java.util.Locale;
import lombok.Data;

/** Число, представляемое как {@code double} внутри. */
@Data
public class DoubleNumber implements Number<DoubleNumber> {
  /** Максимальное допустимое отклонение. */
  public static final double EPS = 1e-20;

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

  /**
   * Проверяет, что текущее и заданное значения отличаются не более, чем на {@code EPS}.
   *
   * @param other значение для проверки на равенство.
   * @return Равны ли приблизительно текущее и заданное значения.
   */
  @Override
  public boolean isNearlyEqualTo(DoubleNumber other) {
    return Math.abs(value - other.value) <= EPS;
  }

  /**
   * Проверяет, что текущее значение строго положительно, то есть больше, чем {@code EPS}.
   *
   * @return Положительно ли текущее значение.
   */
  @Override
  public boolean isNearlyPositive() {
    return value > EPS;
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
