package jvmusin.universalconverter;

import jvmusin.universalconverter.exception.ConversionException;
import jvmusin.universalconverter.exception.NoSuchMeasurementException;

/**
 * Конвертер, позволяющий получать коэффициент соотношения одной величины измерения к другой.
 *
 * @param <TMeasurement> тип величины измерения
 */
public interface MeasurementConverter<TMeasurement> {
    /**
     * Находит коэффициент соотношения величины измерения {@code from} к величине измерения {@code to}.
     * <p>
     * Если представить дробь {@code from} в виде {@code a/b}, а дробь {@code to} в виде {@code c/d},
     * то эта функция найдёт коэффициент {@code K} такой, что {@code a/b = K * c/d}.
     *
     * @param from величина измерения, из которой производится перевод
     * @param to   величина измерения, в которую произодится перевод
     * @return коэффициент соотношения величины измерения {@code from} к величине измерения {@code to}
     * @throws NoSuchMeasurementException если в дробях присутствует неизвестная единица измерения
     * @throws ConversionException        если конвертация невозможна по иным причинам
     */
    double convert(ComplexFraction<TMeasurement> from, ComplexFraction<TMeasurement> to);
}
