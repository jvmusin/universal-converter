package jvmusin.universalconverter.web;

import jvmusin.universalconverter.fraction.ComplexFraction;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Выбрасывается, если выражение, указанное в запросе на конвертацию не удалось конвертировать в
 * дробь {@link ComplexFraction}.
 *
 * <p>При выбрасывании, клиенту возвращается код ошибки {@code 400 Bad Request}.
 */
@ResponseStatus(BAD_REQUEST)
public class MalformedExpressionException extends RuntimeException {
  public MalformedExpressionException(String message) {
    super(message);
  }
}
