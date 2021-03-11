package jvmusin.universalconverter;

import lombok.Data;

/**
 * Класс, описывающий дробь {@code числитель/знаменатель}.
 *
 * @param <T> тип числителя и знаменателя
 */
@Data
public class Fraction<T> {
    private final T numerator;
    private final T denominator;

    @SuppressWarnings("unused") // Used by Lombok in ComplexFraction
    public Fraction() {
        this(null, null);
    }

    public Fraction(T numerator, T denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
}
