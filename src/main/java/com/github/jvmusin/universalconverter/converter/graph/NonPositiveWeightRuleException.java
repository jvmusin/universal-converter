package com.github.jvmusin.universalconverter.converter.graph;

/** Выбрасывается, если нашлось правило с неположительным весом. */
public class NonPositiveWeightRuleException extends RuntimeException {
  public NonPositiveWeightRuleException(String message) {
    super(message);
  }
}
