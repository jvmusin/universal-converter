package jvmusin.universalconverter.web;

import jvmusin.universalconverter.ComplexFraction;
import lombok.Data;

/**
 * Класс, описывающий запрос на конвертацию единицы измерения из {@code from} в {@code to}.
 * <p>
 * Используется как сущность, посылаемая в теле запроса на {@code HTTP} метод {@code /convert}.
 */
@Data
public class ConvertMeasurementValuesRequest {
    private final String from;
    private final String to;

    /**
     * Конвертирует выражение из поля {@code from} в дробь типа {@link ComplexFraction}.
     *
     * @return дробь, соответствующую значению {@code from}.
     * @throws MalformedExpressionException если выражение не удалось конвертировать в дробь
     */
    public ComplexFraction<String> fromFraction() {
        return WebUtils.convertExpressionToFraction(from);
    }

    /**
     * Конвертирует выражение из поля {@code to} в дробь типа {@link ComplexFraction}.
     *
     * @return дробь, соответствующую значению {@code to}.
     * @throws MalformedExpressionException если выражение не удалось конвертировать в дробь
     */
    public ComplexFraction<String> toFraction() {
        return WebUtils.convertExpressionToFraction(to);
    }
}