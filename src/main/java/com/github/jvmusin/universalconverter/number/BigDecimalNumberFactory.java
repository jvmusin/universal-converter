package com.github.jvmusin.universalconverter.number;

import java.math.BigDecimal;
import java.math.MathContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/** Фабрика, используемая для создания {@link BigDecimalNumber}. */
@RequiredArgsConstructor
public class BigDecimalNumberFactory implements NumberFactory<BigDecimalNumber> {

  /** {@link MathContext}, используемый для всех операций над числами, создаваемыми фабрикой. */
  private final MathContext mathContext;

  @Override
  public BigDecimalNumber one() {
    return new BigDecimalNumber(BigDecimal.ONE, mathContext);
  }

  /**
   * Парсит строку в число.
   *
   * <p>Парсинг производится конструктором {@link BigDecimal#BigDecimal(String) BigDecimal(String)}.
   *
   * @param s строка для парсинга в число.
   * @return {@link BigDecimalNumber}, соответствующий строке {@code s}.
   * @throws IllegalArgumentException если строка {@code s} равна {@code null}.
   * @see BigDecimal#BigDecimal(String) BigDecimal(String)
   */
  @Override
  public BigDecimalNumber parse(String s) {
    Assert.notNull(s, "Строка не может быть null");
    return new BigDecimalNumber(new BigDecimal(s), mathContext);
  }
}
