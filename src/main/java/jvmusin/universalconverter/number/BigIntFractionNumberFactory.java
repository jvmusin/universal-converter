package jvmusin.universalconverter.number;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;

/** Фабрика, используемая для создания чисел типа {@link BigIntFractionNumber}. */
public class BigIntFractionNumberFactory implements NumberFactory<BigIntFractionNumber> {

  @Override
  public BigIntFractionNumber one() {
    return new BigIntFractionNumber(ONE, ONE);
  }

  /**
   * Парсит строку в дробь.
   *
   * Если строка равна {@code null}, то выбрасывается {@link IllegalArgumentException}.
   * Иначе парсит строку через создание {@link BigDecimal}.
   *
   * @param s строка для парсинга в дробь.
   * @return Спарсенную из строки дробь.
   * @throws IllegalArgumentException если строка {@code s == null}.
   * @throws NumberFormatException если число спарсить не удалось.
   */
  @Override
  public BigIntFractionNumber parse(String s) {
    Assert.notNull(s, "Строка не может быть null");
    BigDecimal decimal = new BigDecimal(s);
    BigInteger unscaled = decimal.unscaledValue();
    int scale = decimal.scale();
    BigInteger numerator = unscaled.multiply(TEN.pow(Math.max(0, -scale)));
    BigInteger denominator = TEN.pow(Math.max(0, scale));
    return new BigIntFractionNumber(numerator, denominator);
  }
}
