package jvmusin.universalconverter.fraction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Класс, описывающий дробь, где числителем и знаменателем являются списки элементов типа {@code T}.
 *
 * @param <T> тип элементов списков числителя и знаменателя.
 * @see Fraction
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ComplexFraction<T> extends Fraction<List<T>> {
  public ComplexFraction(List<T> numerator, List<T> denominator) {
    super(numerator, denominator);
  }

  @Override
  public String toString() {
    return "ComplexFraction(numerator="
        + getNumerator()
        + ", denominator="
        + getDenominator()
        + ")";
  }
}
