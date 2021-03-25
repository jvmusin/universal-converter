package com.github.jvmusin.universalconverter.converter;

import static com.github.jvmusin.universalconverter.ListUtils.mergeLists;
import static java.text.MessageFormat.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.github.jvmusin.universalconverter.converter.exception.ConversionException;
import com.github.jvmusin.universalconverter.converter.exception.MismatchedDimensionalityException;
import com.github.jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import com.github.jvmusin.universalconverter.converter.graph.ConversionGraph;
import com.github.jvmusin.universalconverter.converter.graph.WeightedMeasurement;
import com.github.jvmusin.universalconverter.fraction.ComplexFraction;
import com.github.jvmusin.universalconverter.number.Number;
import com.github.jvmusin.universalconverter.number.NumberFactory;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

/**
 * Конвертер величин измерения.
 *
 * <p>Для нахождения коэффициента {@code K} в уравнении вида {@code a/b = K * c/d} используется
 * преобразование вида {@code K = (a/b)*(d/c) = (a*d)/(b*c)}.
 *
 * <p>Получение коэффициентов дроби происходит через приведение числителя и знаменателя к корневой
 * величине соответствующей сети.
 *
 * <p>Допустим, необходимо перевести километры в сантиметры, а корневой величиной измерения являются
 * метры. Тогда вес километра равен {@code 1000}, вес метра равен {@code 1}, вес сантиметра равен
 * {@code 1/100=0.01}.
 *
 * <p>Сначала приводим {@code 1км} к метрам, для этого домножим {@code 1км} на вес километра ({@code
 * 1000}), получим {@code 1000м}. Затем приводим {@code 1000м} к сантиметрам, для этого поделим
 * {@code 1000м} на вес сантиметра ({@code 0.01}), получим {@code 100'000см}. Получается, в одном
 * километре сто тысяч сантиметров.
 *
 * <p>Для того, чтобы получить коэффициент сложной дроби, где числитель и знаменатель состоят из
 * нескольких величин измерения, построим какую-нибудь биекцию между элементами числителя и
 * знаменателя, для каждой пары элементов биекции получим коэффициент соотношения одной величины к
 * другой и затем перемножим все полученные коэффициенты.
 *
 * <p>Чтобы такое преобразование работало, требуется, чтобы указанная биекция существовала. Биекция
 * существует, если для каждой отдельно взятой сети в числителе и в знаменателе имеется одинаковое
 * количество величин, принадлежащих этой сети. Для создания такой биекции все элементы как
 * числителя, так и знаменателя, сортируются по индексу сети, которой они принадлежат. Если
 * несколько элементов имеют один и тот же индекс сети, их относительный порядок не важен. Затем
 * биекцией, если она существует, будет такое множество пар элементов, где из числителя и из
 * знаменателя взяты два элемента на одинаковых индексах.
 *
 * @param <TWeight> тип весов, используемых конвертером.
 */
@RequiredArgsConstructor
public class MeasurementConverter<TWeight extends Number<TWeight>> {

  /** Граф конвертаций, в соответствии с которым работает конвертер. */
  private final ConversionGraph<TWeight> conversionGraph;

  /** Фабрика, используемая для создания весов типа {@link TWeight}. */
  private final NumberFactory<TWeight> weightFactory;

  /**
   * Конвертирует дробь вида {@code numerator / denominator} в коэффициент соотношения числителя к
   * знаменателю.
   *
   * <p>Для того, чтобы получить коэффициент, берётся вес числителя и делится на вес знаменателя.
   *
   * @param numerator числитель дроби.
   * @param denominator знаменатель дроби.
   * @return Соотношение числителя к знаменателю.
   * @throws ConversionException если {@code numerator} и {@code denominator} относятся к разным
   *     сетям.
   */
  private TWeight convertSingleMeasurements(
      WeightedMeasurement<TWeight> numerator, WeightedMeasurement<TWeight> denominator) {
    if (numerator.getNetworkIndex() != denominator.getNetworkIndex()) {
      throw new ConversionException("Невозможно конвертировать дробь в коэффициент");
    }
    return numerator.getWeight().divideBy(denominator.getWeight());
  }

  /**
   * Конвертирует сложную дробь вида {@code numerator / denominator} в коэффициент соотношения
   * числителя к знаменателю. Дробь называется сложной потому что и числитель, и знаменатель
   * представляют собой списки величин измерения.
   *
   * @param numerator числитель дроби.
   * @param denominator знаменатель дроби.
   * @return Коэффициент соотношения числителя к знаменателю.
   * @throws MismatchedDimensionalityException если длины списков различаются.
   * @throws ConversionException если не существует биекции между элементами {@code numerator} и
   *     {@code denominator}, где два элемента могут быть соединены только если они оба относятся к
   *     одной и той же сети.
   */
  private TWeight convertMeasurementLists(
      List<WeightedMeasurement<TWeight>> numerator,
      List<WeightedMeasurement<TWeight>> denominator) {
    if (numerator.size() != denominator.size()) {
      throw new MismatchedDimensionalityException(
          "Результирующая дробь должна иметь одинаковое количество элементов в числителе и в"
              + " знаменателе");
    }
    return IntStream.range(0, numerator.size())
        .mapToObj(i -> convertSingleMeasurements(numerator.get(i), denominator.get(i)))
        .reduce(weightFactory.one(), Number::multiplyBy);
  }

  /**
   * Возвращает величину измерения по названию. Если величина не найдена, выбрасывает {@link
   * NoSuchMeasurementException}.
   *
   * @param name название величины измерения.
   * @return Величину измерения.
   * @throws NoSuchMeasurementException если величина измерения не найдена.
   */
  private WeightedMeasurement<TWeight> getMeasurement(String name) {
    var m = conversionGraph.getMeasurement(name);
    if (m == null) throw new NoSuchMeasurementException("Величина измерения не найдена: " + name);
    return m;
  }

  /**
   * Возвращает список, состоящий из величин измерения с названиями, заданными в {@code
   * measurements}, отсортированный по {@link WeightedMeasurement#getNetworkIndex()}.
   *
   * @param measurements Величины измерения для подготовки.
   * @return Отсортированные по {@link WeightedMeasurement#getNetworkIndex()} взвешенные величины
   *     измерения {@link WeightedMeasurement}.
   */
  private List<WeightedMeasurement<TWeight>> prepareMeasurements(List<String> measurements) {
    return measurements.stream()
        .map(this::getMeasurement)
        .sorted(comparing(WeightedMeasurement::getNetworkIndex))
        .collect(toList());
  }

  /**
   * Находит коэффициент соотношения величины измерения {@code from} к величине измерения {@code
   * to}.
   *
   * <p>Если представить дробь {@code from} в виде {@code a/b}, а дробь {@code to} в виде {@code
   * c/d}, то эта функция найдёт коэффициент {@code K} такой, что {@code a/b = K * c/d}.
   *
   * <p>Для того, чтобы найти этот коэффициент, используется преобразование вида {@code K =
   * (a*d)/(b*c)}.
   *
   * @param from величина измерения, из которой производится перевод.
   * @param to величина измерения, в которую производится перевод.
   * @return Коэффициент соотношения величины измерения {@code from} к величине измерения {@code
   *     to}.
   * @throws NoSuchMeasurementException если в дробях присутствует неизвестная величина измерения.
   * @throws ConversionException если в результирующей дроби числитель и знаменатель имеют разную
   *     длину или если невозможно создать биекцию между элементами числителя и знаменателя, где
   *     элементы могут быть соединены только если они принадлежат одной и той же сети.
   */
  public TWeight convertFractions(ComplexFraction<String> from, ComplexFraction<String> to) {
    try {
      var numerator = prepareMeasurements(mergeLists(from.getNumerator(), to.getDenominator()));
      var denominator = prepareMeasurements(mergeLists(from.getDenominator(), to.getNumerator()));
      return convertMeasurementLists(numerator, denominator);
    } catch (NoSuchMeasurementException e) {
      throw e;
    } catch (Exception e) {
      String msg =
          format("Не удалось конвертировать дробь {0} в {1}: {2}", from, to, e.getMessage());
      throw new ConversionException(msg, e);
    }
  }
}
