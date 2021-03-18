package com.github.jvmusin.universalconverter.web;

import com.github.jvmusin.universalconverter.converter.exception.ConversionException;
import com.github.jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Класс, позволяющий отлавливать исключения, бросаемые конвертером, и возвращать соответствующий
 * код ошибки клиенту.
 */
@RestControllerAdvice
public class ConversionExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Отлавливает исключение {@link ConversionException} и возвращает клиенту код {@code 400 Bad
   * Request} и текст исключения в теле ответа.
   *
   * @param e выброшенное исключение.
   * @return {@link ResponseEntity} с кодом {@code 400} и текстом исключения в теле ответа.
   */
  @ExceptionHandler(ConversionException.class)
  public ResponseEntity<String> handleConversionException(ConversionException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  /**
   * Отлавливает исключение {@link NoSuchMeasurementException} и возвращает клиенту код {@code 404
   * Not Found} и текст исключения в теле ответа.
   *
   * @param e выброшенное исключение.
   * @return {@link ResponseEntity} с кодом {@code 404} и текстом исключения в теле ответа.
   */
  @ExceptionHandler(NoSuchMeasurementException.class)
  public ResponseEntity<String> handleNoSuchMeasurementException(NoSuchMeasurementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }
}
