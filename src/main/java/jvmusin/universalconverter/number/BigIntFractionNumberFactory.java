package jvmusin.universalconverter.number;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.util.Assert;

/** Фабрика, используемая для создания чисел типа {@link BigIntFractionNumber}. */
public class BigIntFractionNumberFactory implements NumberFactory<BigIntFractionNumber> {

  @Override
  public BigIntFractionNumber one() {
    return new BigIntFractionNumber(ONE, ONE);
  }

  /**
   * Парсит строку в дробь.
   *
   * <p>Если строка равна {@code null}, то выбрасывается {@link IllegalArgumentException}. Иначе
   * парсит строку через конструктор {@link BigDecimal#BigDecimal(String) BigDecimal(String)}.
   *
   * @param s строка для парсинга в дробь.
   * @return Спарсенную из строки дробь.
   * @throws IllegalArgumentException если строка {@code s == null}.
   * @throws NumberFormatException если число спарсить не удалось.
   * @see BigDecimal#BigDecimal(String) BigDecimal(String)
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
