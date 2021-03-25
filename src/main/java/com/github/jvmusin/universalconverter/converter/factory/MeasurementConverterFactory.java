package com.github.jvmusin.universalconverter.converter.factory;

import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.converter.MeasurementConverter;
import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException;
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraph;
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraphFactory;
import com.github.jvmusin.universalconverter.number.Number;
import com.github.jvmusin.universalconverter.number.NumberFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/**
 * Фабрика, используемая для создания {@link MeasurementConverter}.
 *
 * <p>Создаваемые этой фабрикой конвертеры оперируют весами типа {@link TWeight}.
 *
 * @param <TWeight> тип весов, используемых создаваемыми конвертерами.
 */
@RequiredArgsConstructor
public class MeasurementConverterFactory<TWeight extends Number<TWeight>> {

  /** Фабрика, используемая для создания весов типа {@link TWeight}. */
  protected final NumberFactory<TWeight> weightFactory;

  /** Фабрика, используемая для создания графов конвертации. */
  private final ConversionGraphFactory<TWeight> conversionGraphFactory;

  /**
   * Создаёт {@link MeasurementConverter} на весах типа {@link TWeight} из правил конвертации {@code
   * conversionRules}.
   *
   * @param conversionRules правила конвертации с весами типа {@link TWeight}.
   * @return {@link MeasurementConverter}, работающий по правилам, указанным в {@code
   *     conversionRules}.
   * @throws MeasurementConverterBuildException при ошибке построения конвертера.
   */
  public MeasurementConverter<TWeight> create(List<ConversionRule<TWeight>> conversionRules) {
    try {
      Assert.notNull(conversionRules, "Список правил равен null");
      Assert.noNullElements(conversionRules, "В правилах конвертации присутствует null");
      ConversionGraph<TWeight> conversionGraph = conversionGraphFactory.create(conversionRules);
      return new MeasurementConverter<>(conversionGraph, weightFactory);
    } catch (Exception e) {
      throw new MeasurementConverterBuildException("Не удалось построить MeasurementConverter", e);
    }
  }
}
