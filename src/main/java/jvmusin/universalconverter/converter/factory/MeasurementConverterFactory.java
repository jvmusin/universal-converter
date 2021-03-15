package jvmusin.universalconverter.converter.factory;

import java.util.List;
import jvmusin.universalconverter.converter.ConversionRule;
import jvmusin.universalconverter.converter.MeasurementConverter;
import jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException;
import jvmusin.universalconverter.number.Number;

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
