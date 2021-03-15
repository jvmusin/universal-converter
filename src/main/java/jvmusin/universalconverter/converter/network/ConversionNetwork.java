package jvmusin.universalconverter.converter.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jvmusin.universalconverter.converter.ConversionRule;
import jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import jvmusin.universalconverter.converter.network.exception.ConversionNetworkBuildException;
import jvmusin.universalconverter.converter.network.exception.InfinitelyIncreasingCycleException;
import jvmusin.universalconverter.converter.network.exception.NonPositiveWeightRuleException;
import jvmusin.universalconverter.number.Number;
import jvmusin.universalconverter.number.NumberFactory;
import lombok.RequiredArgsConstructor;

/**
 * Сеть величин измерения.
 *
 * <p>Позволяет переводить единицы измерения от одной к другой в пределах одной и той же системы
 * счисления.
 *
 * <p>Назначает каждой единице измерения вес в зависимости от её относительной величины по отношению
 * к корневой единице измерения.
 *
 * <p>Например, если корневая единица измерения - метр, то у метра будет вес, равный единице, а у
 * километра вес, равный {@code 1000}. Если же корневой величиной измерения был километр, то его вес
 * будет единицей, а вес метра - {@code 1/1000}.
 *
 * @param <TWeight> тип весов, используемых сетью.
 */
public class ConversionNetwork<TWeight extends Number<TWeight>> {
  /** Словарь, в котором для каждой единицы измерения этой сети лежит соответствующий ей вес. */
  private final Map<String, TWeight> weights;

  /**
   * Строит сеть величин измерения по графу конвертации {@code conversionGraph}, начиная с корневого
   * элемента {@code root}.
   *
   * @param conversionGraph граф конвертации.
   * @param root корневой элемент сети.
   * @param numberFactory фабрика, используемая для создания весов.
   * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации.
   * @see Builder#build()
   */
  public ConversionNetwork(
      Map<String, List<ConversionRule<TWeight>>> conversionGraph,
      String root,
      NumberFactory<TWeight> numberFactory) {
    weights = new Builder(conversionGraph, root, numberFactory).build();
  }

  /**
   * Возвращает все единицы измерения в этой сети.
   *
   * @return Все единицы измерения в этой сети.
   */
  public Set<String> getMeasurements() {
    return weights.keySet();
  }

  /**
   * Конвертирует величину измерения в присвоенный ей коэффициент.
   *
   * @param measurement единица измерения.
   * @return Коэффициент, присвоенный этой единице измерения.
   * @throws NoSuchMeasurementException если запрашиваемой единицы измерения в этой сети нет.
   */
  public TWeight convertToCoefficient(String measurement) {
    TWeight result = weights.get(measurement);
    if (result == null)
      throw new NoSuchMeasurementException(
          "В этой сети нет нужной единицы измерения: " + measurement);
    return result;
  }

  /**
   * Класс, используемый для построения сети конвертации.
   *
   * <p>При запуске {@link Builder#build()}, рекурсивно ходит по графу {@code conversionGraph} и для
   * каждой посещённой вершины запоминает коэффициент её веса относительно корневой вершины.
   */
  @RequiredArgsConstructor
  private class Builder {
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

    /**
     * Рекурсивно строит сеть величин измерения, содержащую элемент {@code root}. Если в сети есть
     * некорректные правила конвертации, выбрасывается {@link ConversionNetworkBuildException}.
     * После построения сети все итоговые веса будут лежать в {@link #weights}.
     *
     * @param measurement текущая величина измерения.
     * @param weight вес текущей величины измерения.
     * @throws NonPositiveWeightRuleException при наличии в сети правил с неположительным весом.
     * @throws InfinitelyIncreasingCycleException при наличии в сети циклов с бесконечно
     *     увеличивающимся весом.
     * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации в сети.
     */
    private void buildNetwork(String measurement, TWeight weight) {
      weights.put(measurement, weight);
      for (ConversionRule<TWeight> rule : conversionGraph.get(measurement)) {
        if (!rule.getSmallPieceCount().isNearlyPositive()) {
          throw new NonPositiveWeightRuleException(
              "В сети существует правило с неположительным весом: " + rule);
        }
        var nextCoefficient = weight.divideBy(rule.getSmallPieceCount());
        if (!weights.containsKey(rule.getSmallPiece())) {
          buildNetwork(rule.getSmallPiece(), nextCoefficient);
        } else if (!weights.get(rule.getSmallPiece()).isNearlyEqualTo(nextCoefficient)) {
          throw new InfinitelyIncreasingCycleException(
              "В сети существует бесконечно увеличивающийся цикл с бесконечно увеличивающимся"
                  + " весом");
        }
      }
    }

    /**
     * Строит сеть величин измерения.
     *
     * @return Словарь, ключом которого является величина измерения, значением - её вес.
     * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации в сети.
     * @see #buildNetwork(String, TWeight)
     */
    public Map<String, TWeight> build() {
      buildNetwork(root, weightFactory.one());
      return weights;
    }
  }
}
