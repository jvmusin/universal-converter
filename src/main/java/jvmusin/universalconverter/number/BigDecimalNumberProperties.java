package jvmusin.universalconverter.number;

import java.math.BigDecimal;
import java.math.MathContext;
import lombok.Data;

/** Свойства операций для {@link BigDecimalNumber}. */
@Data
public class BigDecimalNumberProperties {

  /** {@link MathContext}, применяемый при всех операциях с {@link BigDecimal}. */
  private final MathContext mathContext;

  /** Максимальная разность между двумя {@link BigDecimal}, чтобы считаться равными. */
  private final BigDecimal maximalDifferenceToBeEqual;
}
