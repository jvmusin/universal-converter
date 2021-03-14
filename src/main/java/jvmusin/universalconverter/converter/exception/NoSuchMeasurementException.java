package jvmusin.universalconverter.converter.exception;

/**
 * Выбрасывается, если величина измерения не найдена.
 */
public class NoSuchMeasurementException extends ConversionException {
    public NoSuchMeasurementException(String message) {
        super(message);
    }
}
