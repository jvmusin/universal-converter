package jvmusin.universalconverter.exception;

/**
 * Исключение, выбрасываемое, если длина числителя и знаменателя дроби не совпадает.
 */
public class MismatchedDimensionalityException extends ConversionException {
    public MismatchedDimensionalityException(String message) {
        super(message);
    }
}
