package com.github.jvmusin.universalconverter.converter.factory;

import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.converter.MeasurementConverter;
import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException;
import com.github.jvmusin.universalconverter.number.Number;
import java.util.List;

/**
 * Фабрика, используемая для создания {@link MeasurementConverter}.
 *
 * <p>Создаваемые этой фабрикой конвертеры оперируют весами типа {@link TWeight}.
 *
 * @param <TWeight> тип весов, используемых создаваемыми конвертерами.
 */
public interface MeasurementConverterFactory<TWeight extends Number<TWeight>> {
  /**
   * Создаёт {@link MeasurementConverter} на весах типа {@link TWeight} из правил конвертации {@code
   * conversionRules}.
   *
   * @param conversionRules правила конвертации с весами типа {@link TWeight}.
   * @return {@link MeasurementConverter}, работающий по правилам, указанным в {@code
   *     conversionRules}.
   * @throws MeasurementConverterBuildException при ошибке построения конвертера.
   */
  MeasurementConverter<TWeight> create(List<ConversionRule<TWeight>> conversionRules);
}