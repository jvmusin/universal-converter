package jvmusin.universalconverter.converter.exception;

/** Выбрасывается, если длина числителя и знаменателя дроби не совпадает. */
public class MismatchedDimensionalityException extends ConversionException {
  public MismatchedDimensionalityException(String message) {
    super(message);
  }
}
