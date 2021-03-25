package com.github.jvmusin.universalconverter.converter.graph;

import static com.github.jvmusin.universalconverter.ListUtils.groupList;
import static com.github.jvmusin.universalconverter.ListUtils.mapList;
import static com.github.jvmusin.universalconverter.ListUtils.mergeLists;

import com.github.jvmusin.universalconverter.converter.ConversionRule;
import com.github.jvmusin.universalconverter.number.Number;
import com.github.jvmusin.universalconverter.number.NumberFactory;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/**
 * Фабрика, используемая для создания графа конвертаций.
 *
 * <p>Все величины измерения, которые можно получить друг из друга, попадают в одну сеть
 * конвертаций. Все величины измерения получают свой вес, имеющий смысл *только в той сети
 * конвертаций, которой они принадлежат*.
 *
 * <p>Соответственно, чтобы получить соотношение одной величины измерения к другой, достаточно вес
 * первой разделить на вес второй.
 *
 * <p>Например, вес часа может быть равен {@code 3600}, а вес минуты равен {@code 60}, тогда,
 * разделив первое на второе, получим {@code 3600/60 = 60}, что означает, что в одном часе {@code
 * 60} минут.
 *
 * <p>Обратите внимание, что сами веса могут быть абсолютно любыми, важно лишь что соотношения этих
 * весов будут давать верный коэффициент.
 *
 * @param <TWeight> тип весов, используемых сетью.
 */
@RequiredArgsConstructor
public class ConversionGraphFactory<TWeight extends Number<TWeight>> {

  private final NumberFactory<TWeight> weightFactory;

  /**
   * Создаёт граф конвертаций по заданным правилам.
   *
   * @param rules правила, по которым создаётся граф конвертаций.
   * @return Граф конвертаций.
   */
  public ConversionGraph<TWeight> create(List<ConversionRule<TWeight>> rules) {
    Assert.notNull(rules, "Список правил не может быть равен null");
    Assert.noNullElements(rules, "Ни одно правило не должно быть равно null");
    for (var rule : rules) {
      if (!rule.getSmallPieceCount().isPositive()) {
        throw new NonPositiveWeightRuleException(
            "Правило имеет неположительный коэффициент: " + rule);
      }
    }
    var graph = buildRawGraph(rules);
    var weights = new Builder(graph).build();
    return new ConversionGraph<>(weights);
  }

  /**
   * Строит словарь, содержащий все прямые и обратные правила, в котором ключ берётся из {@link
   * ConversionRule#getSmallPiece()}, а значение - это все правила, которые имеют заданный ключом
   * {@link ConversionRule#getSmallPiece()}.
   *
   * @param rules правила конвертации.
   * @return Граф, построенный по правилам конвертации.
   */
  private Map<String, List<ConversionRule<TWeight>>> buildRawGraph(
      List<ConversionRule<TWeight>> rules) {
    var inverseRules = mapList(rules, ConversionRule::inverse);
    var allRules = mergeLists(rules, inverseRules);
    return groupList(allRules, ConversionRule::getSmallPiece);
  }

  /**
   * Класс, использующийся для построения графа конвертаций.
   *
   * <p>Класс является одноразовым и позволяет построить веса для заданного набора правил
   * конвертаций.
   *
   * <p>За корневую величину сети конвертаций выбирается любая величина, принадлежащая ей. Этой
   * величине назначается вес, равный единице, а все остальные величины, которые прямо или косвенно
   * можно получить из корневой (то есть они все принадлежат одной сети конвертаций), получают
   * коэффициент относительно корневой величины измерения.
   *
   * <p>При построении сетей конвертаций используется используется обхода в ширину.
   *
   * <p>При проходе по ребру, достаточно вес текущей величины умножить на значение, написанное на
   * ребре в {@link ConversionRule#getSmallPieceCount()}. Таким образом, если вес метра равен {@code
   * 10} и существует правило, где меньшая величина - метр, большая - километр, и {@link
   * ConversionRule#getSmallPieceCount()} равен {@code 1'000}, то километру будет присвоен вес,
   * равный {@code 10*1'000 = 10'000}. Если вес километра ({@code 10'000}) разделить на вес метра
   * ({@code 10}), получим {@code 1'000}, что означает, что километр в {@code 1'000} раз больше
   * метра.
   *
   * <p>Обход в ширину позволяет использовать меньше памяти на стеке, что важно при обработке
   * больших графов.
   *
   * <p>Также обход в ширину позволяет получать меньшую погрешность по сравнению с обходом в
   * грубину, потому что для каждой величины измерения используется путь наименьшей длины и,
   * соответственно, производится наименьшее количество умножений.
   */
  @RequiredArgsConstructor
  private class Builder {
    private final Map<String, List<ConversionRule<TWeight>>> rawGraph;
    private final Map<String, WeightedMeasurement<TWeight>> readyMeasurements = new HashMap<>();
    private final Queue<WeightedMeasurement<TWeight>> queue = new ArrayDeque<>();
    private int networksFoundSoFar;

    /**
     * Сохраняет величину измерения с названием {@code name} и весом {@code weight} в результат и
     * кладёт эту величину в очередь обхода в ширину.
     *
     * @param name название величины измерения.
     * @param weight вес величины измерения.
     */
    private void save(String name, TWeight weight) {
      var measurement = new WeightedMeasurement<>(name, networksFoundSoFar, weight);
      readyMeasurements.put(name, measurement);
      queue.add(measurement);
    }

    /**
     * Строит сеть конвертаций, содержащей величину измерения {@code root}.
     *
     * @param root корневой элемент сети конвертаций.
     */
    void buildNetwork(String root) {
      save(root, weightFactory.one());
      while (!queue.isEmpty()) {
        var cur = queue.poll();
        for (var rule : rawGraph.get(cur.getName())) {
          var nextMeasurement = rule.getBigPiece();
          if (readyMeasurements.containsKey(nextMeasurement)) continue;
          var nextWeight = cur.getWeight().multiplyBy(rule.getSmallPieceCount());
          save(nextMeasurement, nextWeight);
        }
      }
    }

    /**
     * Строит все сети конвертаций, содержащиеся в правилах.
     *
     * @return Словарь, в котором напротив имени каждой величины измерения записана сущность {@link
     *     WeightedMeasurement}.
     */
    public Map<String, WeightedMeasurement<TWeight>> build() {
      for (String measurement : rawGraph.keySet()) {
        if (!readyMeasurements.containsKey(measurement)) {
          buildNetwork(measurement);
          networksFoundSoFar++;
        }
      }
      return readyMeasurements;
    }
  }
}
