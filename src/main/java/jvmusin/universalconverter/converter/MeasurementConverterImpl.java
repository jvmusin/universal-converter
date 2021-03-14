package jvmusin.universalconverter.converter;

import jvmusin.universalconverter.converter.exception.ConversionException;
import jvmusin.universalconverter.converter.exception.MismatchedDimensionalityException;
import jvmusin.universalconverter.converter.exception.NoSuchMeasurementException;
import jvmusin.universalconverter.converter.network.ConversionNetwork;
import jvmusin.universalconverter.fraction.ComplexFraction;
import jvmusin.universalconverter.fraction.Fraction;
import jvmusin.universalconverter.number.Number;
import jvmusin.universalconverter.number.NumberFactory;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;
import static java.util.Comparator.comparing;
import static jvmusin.universalconverter.ListUtils.mergeLists;

/**
 * Для нахождения коэффициента {@code K} в уравнении вида {@code a/b = K * c/d}
 * используется преобразование вида {@code K = (a/b)*(d/c) = (a*d)/(b*c)}.
 * <p>
 * Получение коэффициентов дроби происходит через приведение
 * числителя и знаменателя к корневой величине соответствующей сети.
 * <p>
 * Допустим, необходимо перевести километры в сантиметры, а корневой
 * величиной измерения являются метры.
 * Тогда вес километра равен {@code 1000}, вес метра равен {@code 1}, вес сантиметра равен {@code 1/100=0.01}.
 * <p>
 * Сначала приводим {@code 1км} к метрам, для этого домножим {@code 1км}
 * на вес килмотера ({@code 1000}), получим {@code 1000м}.
 * Затем приводим {@code 1000м} к сантиметрам,
 * для этого поделим {@code 1000м} на вес сантиметра ({@code 0.01}), получим {@code 100'000см}.
 * Получается, в одном километре сто тысяч сантиметров.
 * <p>
 * Для того, чтобы получить коэффициент сложной дроби,
 * где числитель и знаменатель состоят из нескольких величин измерения,
 * построим какую-нибудь биекцию между элементами числителя и знаменателя,
 * для каждой пары элементов биекции получим коэффициент соотношения одной величины к другой
 * и затем перемножим все полученные коэффициенты.
 * <p>
 * Чтобы такое преобразование работало, требуется, чтобы указанная биекция существовала.
 * Биекция существует, если для каждой отдельно взятой сети в числителе и в знаменателе
 * имеется одинаковое количество величин, принадлежащих этой сети.
 * Для создания такой биекции все элементы как числителя,
 * так и знаменателя, сортируются по индексу сети, которой они принадлежат.
 * Если несколько элементов имеют один и тот же индекс сети, их относительный порядок не важен.
 * Затем биекцией, если она существует, будет такое множество пар элементов,
 * где из числителя и из знаменателя взяты два элемента на одинаковых индексах.
 *
 * @param <TWeight> тип весов, используемых конвертером.
 */
@RequiredArgsConstructor
public class MeasurementConverterImpl<TWeight extends Number<TWeight>> implements MeasurementConverter<TWeight> {
    /**
     * Список всех известных сетей.
     */
    private final List<ConversionNetwork<TWeight>> networks;

    /**
     * Словарь, в котором для каждой единицы измерения указан индекс сети, которой она принадлежит.
     */
    private final Map<String, Integer> measurementToNetworkIndex;

    /**
     * Фабрика, используемая для создания весов типа {@link TWeight}.
     */
    private final NumberFactory<TWeight> numberFactory;

    /**
     * Возвращает индекс сети, в которой находится величина измерения {@code measurement}.
     *
     * @param measurement величина измерения, для которой нужно вернуть индекс сети, которой она принадлежит.
     * @return Индекс сети, которой принадлежит {@code measurement}.
     * @throws NoSuchMeasurementException если такой единицы измерения нет ни в одной известной сети.
     */
    private int getNetworkIndex(String measurement) {
        Integer index = measurementToNetworkIndex.get(measurement);
        if (index == null) throw new NoSuchMeasurementException("Единица измерения не найдена: " + measurement);
        return index;
    }

    /**
     * Конвертирует дробь вида {@code numerator / denominator} в коэффициент соотношения числителя к знаменателю.
     * <p>
     * Для того, чтобы получить коэффициент, берётся вес числителя и делится на вес знаменателя.
     *
     * @param numerator   числитель дроби.
     * @param denominator знаменатель дроби.
     * @return Соотношение числителя к знаменателю.
     * @throws ConversionException если {@code numerator} и {@code denominator} относятся к разным сетям.
     */
    private TWeight convert(String numerator, String denominator) {
        int networkNumIndex = getNetworkIndex(numerator);
        int networkDenIndex = getNetworkIndex(denominator);
        if (networkNumIndex != networkDenIndex) {
            Fraction<String> fraction = new Fraction<>(numerator, denominator);
            throw new ConversionException(
                    MessageFormat.format("Невозможно конвертировать дробь {0} в коэффициент", fraction)
            );
        }
        ConversionNetwork<TWeight> network = networks.get(networkNumIndex);
        return network.convertToCoefficient(numerator).divideBy(network.convertToCoefficient(denominator));
    }

    /**
     * Конвертирует сложную дробь вида {@code numerator / denominator}
     * в коэффициент соотношения числителя к знаменателю.
     * Дробь называется сложной потому что и числитель,
     * и знаменатель представляют собой списки величин измерения.
     *
     * @param numerator   числитель дроби.
     * @param denominator знаменатель дроби.
     * @return Коэффициент соотношения числителя к знаменателю.
     * @throws MismatchedDimensionalityException если длины списков различаются.
     * @throws ConversionException               если не существует биекции между элементами
     *                                           {@code numerator} и {@code denominator},
     *                                           где два элемента могут быть соединены только если
     *                                           они оба относятся к одной и той же сети.
     */
    private TWeight convert(List<String> numerator, List<String> denominator) {
        if (numerator.size() != denominator.size()) {
            throw new MismatchedDimensionalityException(
                    "Результирующая дробь должна иметь одинаковое количество элементов в числителе и в знаменателе"
            );
        }

        numerator.sort(comparing(this::getNetworkIndex));
        denominator.sort(comparing(this::getNetworkIndex));

        TWeight result = numberFactory.one();
        for (int i = 0; i < numerator.size(); i++)
            result = result.multiplyBy(convert(numerator.get(i), denominator.get(i)));
        return result;
    }

    /**
     * Проверяет, известны ли все единицы измерения в списке.
     *
     * @param measurements единицы измерения для проверки.
     * @throws NoSuchMeasurementException если в списке есть неизвестная единица измерения.
     */
    private void makeSureMeasurementsExist(List<String> measurements) {
        measurements.forEach(this::getNetworkIndex);
    }

    /**
     * Проверяет, известны ли все единицы измерения в числителе и в знаменателе дроби.
     *
     * @param fraction дробь для проверки.
     * @throws NoSuchMeasurementException если в дроби есть неизвестная единица измерения.
     */
    private void makeSureMeasurementsExist(ComplexFraction<String> fraction) {
        makeSureMeasurementsExist(fraction.getNumerator());
        makeSureMeasurementsExist(fraction.getDenominator());
    }

    /**
     * Находит коэффициент соотношения величины измерения {@code from} к величине измерения {@code to}.
     * <p>
     * Если представить дробь {@code from} в виде {@code a/b}, а дробь {@code to} в виде {@code c/d},
     * то эта функция найдёт коэффициент {@code K} такой, что {@code a/b = K * c/d}.
     * <p>
     * Для того, чтобы найти этот коэффициент, используется преобразование вида {@code K = (a*d)/(b*c)}.
     *
     * @param from величина измерения, из которой производится перевод.
     * @param to   величина измерения, в которую производится перевод.
     * @return Коэффициент соотношения величины измерения {@code from} к величине измерения {@code to}.
     * @throws NoSuchMeasurementException если в дробях присутствует неизвестная единица измерения.
     * @throws ConversionException        если в результирующей дроби числитель и знаменатель имеют разную длину
     *                                    или если невозможно создать биекцию между элементами числителя и знаменателя,
     *                                    где элементы могут быть соединены
     *                                    только если они принадлежат одной и той же сети.
     */
    @Override
    public TWeight convert(ComplexFraction<String> from, ComplexFraction<String> to) {
        makeSureMeasurementsExist(from);
        makeSureMeasurementsExist(to);
        List<String> numerator = mergeLists(from.getNumerator(), to.getDenominator());
        List<String> denominator = mergeLists(from.getDenominator(), to.getNumerator());
        try {
            return convert(numerator, denominator);
        } catch (Exception e) {
            String msg = format("Не удалось конвертировать дробь {0} в {1}: {2}", from, to, e.getMessage(), e);
            throw new ConversionException(msg, e);
        }
    }
}
