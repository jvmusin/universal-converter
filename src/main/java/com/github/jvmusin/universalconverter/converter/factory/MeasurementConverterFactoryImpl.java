package com.github.jvmusin.universalconverter.converter.factory;

import com.github.jvmusin.universalconverter.converter.MeasurementConverter;
import com.github.jvmusin.universalconverter.converter.MeasurementConverterImpl;
import com.github.jvmusin.universalconverter.converter.exception.MeasurementConverterBuildException;
import com.github.jvmusin.universalconverter.converter.network.ConversionNetwork;
import com.github.jvmusin.universalconverter.number.Number;
import com.github.jvmusin.universalconverter.number.NumberFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.converter.MeasurementConverterUtils;
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
public class MeasurementConverterFactoryImpl<TWeight extends Number<TWeight>>
    implements MeasurementConverterFactory<TWeight> {

  /** Фабрика, используемая для создания весов типа {@link TWeight}. */
  protected final NumberFactory<TWeight> weightFactory;

  @Override
  public MeasurementConverter<TWeight> create(List<ConversionRule<TWeight>> conversionRules) {
    try {
      Assert.notNull(conversionRules, "Список правил равен null");
      Assert.noNullElements(conversionRules, "В правилах конвертации присутствует null");
      Map<String, List<ConversionRule<TWeight>>> conversionGraph =
          MeasurementConverterUtils.buildConversionGraph(conversionRules);
      Set<String> usedMeasurements = new HashSet<>();
      Map<String, Integer> measurementToNetworkIndex = new HashMap<>();
      List<ConversionNetwork<TWeight>> networks = new ArrayList<>();
      for (Map.Entry<String, List<ConversionRule<TWeight>>> e : conversionGraph.entrySet()) {
        String measurement = e.getKey();
        if (!usedMeasurements.contains(measurement)) {
          int networkIndex = networks.size();
          ConversionNetwork<TWeight> network =
              new ConversionNetwork<>(conversionGraph, measurement, weightFactory);
          networks.add(network);
          Set<String> networkMeasurements = network.getMeasurements();
          usedMeasurements.addAll(networkMeasurements);
          for (String m : networkMeasurements) measurementToNetworkIndex.put(m, networkIndex);
        }
      }
      return new MeasurementConverterImpl<>(networks, measurementToNetworkIndex, weightFactory);
    } catch (Exception e) {
      throw new MeasurementConverterBuildException("Не удалось построить MeasurementConverter", e);
    }
  }
}
