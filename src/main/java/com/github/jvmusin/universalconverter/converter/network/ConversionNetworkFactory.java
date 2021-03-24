package com.github.jvmusin.universalconverter.converter.network;

import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.number.Number;
import com.github.jvmusin.universalconverter.number.NumberFactory;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/** Фабрика, используемая для построения сети конвертаций {@link ConversionNetwork}. */
public class ConversionNetworkFactory {

  /**
   * Строит сеть конвертаций по заданному графу {@code conversionGraph} и корневой величине
   * измерения {@code root} с использованием фабрики весов {@code weightFactory}.
   *
   * @param conversionGraph граф конвертаций, на котором строится сеть конвертаций.
   * @param root корневая величина измерений итоговой сети.
   * @param weightFactory фабрика весов, используемая для построения сети конвертаций.
   * @param <TWeight> тип весов, используемых в сети.
   * @return Сеть конвертаций.
   * @throws NonPositiveWeightRuleException при наличии неположительных коэффициентов в правилах
   *     конвертации.
   * @see Builder#build()
   */
  public <TWeight extends Number<TWeight>> ConversionNetwork<TWeight> create(
      Map<String, List<ConversionRule<TWeight>>> conversionGraph,
      String root,
      NumberFactory<TWeight> weightFactory) {
    Map<String, TWeight> weights = new Builder<>(conversionGraph, root, weightFactory).build();
    return new ConversionNetwork<>(weights);
  }

  /**
   * Класс, используемый для построения сети конвертации.
   *
   * <p>Отдельный класс используется для того, чтобы инкапсулировать множество структур,
   * используемых при создании сети конвертаций.
   *
   * <p>При запуске {@link Builder#build()}, рекурсивно ходит по графу {@code conversionGraph} и для
   * каждой посещённой вершины запоминает коэффициент её веса относительно корневой вершины.
   */
  @RequiredArgsConstructor
  private static class Builder<TWeight extends Number<TWeight>> {
    /** Граф величин измерений. */
    private final Map<String, List<ConversionRule<TWeight>>> conversionGraph;

    /** Корневой элемент. */
    private final String root;

    /** Фабрика, используемая для создания весов. */
    private final NumberFactory<TWeight> weightFactory;

    /**
     * Словарь, в котором после построения сети для каждой величины измерения этой сети будет лежать
     * её вес.
     */
    private final Map<String, TWeight> weights = new HashMap<>();

    /** Очередь, которой пользуется метод {@link #build()} при обходе в ширину. */
    private final Queue<MeasurementNode> queue = new ArrayDeque<>();

    /**
     * Сохраняет вес величины измерения.
     *
     * @param measurement имя величины измерения.
     * @param weight вес величины измерения.
     */
    private void save(String measurement, TWeight weight) {
      weights.put(measurement, weight);
      queue.add(new MeasurementNode(measurement, weight));
    }

    /**
     * Строит сеть величин измерения, содержащую элемент {@code root}. Если в сети есть правила с
     * неположительным весом, выбрасывается {@link NonPositiveWeightRuleException}. После построения
     * сети все итоговые веса будут лежать в {@link #weights}.
     *
     * <p>Построение происходит обходом графа конвертации в ширину.
     *
     * @return Словарь, ключом которого является величина измерения, значением - её вес.
     * @throws NonPositiveWeightRuleException при наличии в сети правил с неположительным весом.
     */
    public Map<String, TWeight> build() {
      save(root, weightFactory.one());
      while (!queue.isEmpty()) {
        MeasurementNode current = queue.poll();
        String currentMeasurement = current.getMeasurement();
        TWeight currentWeight = current.getWeight();
        for (ConversionRule<TWeight> rule : conversionGraph.get(currentMeasurement)) {
          if (!rule.getSmallPieceCount().isPositive()) {
            throw new NonPositiveWeightRuleException(
                "В сети существует правило с неположительным весом: " + rule);
          }
          TWeight bigPieceWeight = currentWeight.multiplyBy(rule.getSmallPieceCount());
          String bigPiece = rule.getBigPiece();
          if (!weights.containsKey(bigPiece)) {
            save(bigPiece, bigPieceWeight);
            queue.add(new MeasurementNode(bigPiece, bigPieceWeight));
          }

          // TODO: Compare bigPieceWeight to weights[rule.bigPiece]
          //  to ensure that there are no incorrect rules so that we can have
          //  infinitely-high-weight and infinitely-zero-weight cycles.
          //  It's not done because I have no clue how to correctly compare doubles here
          //  assuming that some precision losing persists.
          //  Checking this with BigIntFractionNumber is easy, but with others it's really tough.
        }
      }
      return weights;
    }

    /**
     * Класс, используемый для хранения информации о величине измерения в обходе в ширину в {@link
     * #build()}.
     */
    @Data
    private class MeasurementNode {
      /** Величина измерения. */
      private final String measurement;

      /** Вес величины измерения. */
      private final TWeight weight;
    }
  }
}
