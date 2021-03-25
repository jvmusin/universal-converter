package com.github.jvmusin.universalconverter.converter.exception;

/** Выбрасывается, если произошла ошибка при конвертации величин измерения. */
public class ConversionException extends RuntimeException {
  public ConversionException(String message) {
    super(message);
  }

  public ConversionException(String message, Throwable cause) {
    super(message, cause);
  }
}
