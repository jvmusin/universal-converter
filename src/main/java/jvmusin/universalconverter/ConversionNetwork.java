package jvmusin.universalconverter;

import jvmusin.universalconverter.exception.ConversionNetworkBuildException;
import jvmusin.universalconverter.exception.NoSuchMeasurementException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сеть величин измерения.
 * <p>
 * Позволяет переводить единицы измерения от одной к другой в пределах одной и той же системы измерения.
 * <p>
 * Назначает каждой едиинце измерения вес в зависимости от её
 * относительной величины по отношению к корневой единице измерения.
 * <p>
 * Например, если корневая единица измерения - метр, то у метра будет вес,
 * равный единице, а у километра вес, равный {@code 1000}.
 * Если же корневой величиной измерения был километр, то его вес будет единицей, а вес метра - {@code 1/1000}.
 *
 * @param <TMeasurement> тип единицы измерения
 */
public class ConversionNetwork<TMeasurement> {
    /**
     * Словарь, в котором для каждой единицы измерения этой сети лежит соответствующий вес.
     */
    private final Map<TMeasurement, Double> weights;

    /**
     * Строит сеть величин измерения, используя граф конвертации {@code conversionGraph}
     * и корневой элемент {@code root}.
     *
     * @param conversionGraph граф конвертации
     * @param root            корневой элемент сети
     * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации
     * @see Utils#buildConversionGraph(List)
     */
    public ConversionNetwork(Map<TMeasurement, List<ConversionRule<TMeasurement>>> conversionGraph, TMeasurement root) {
        weights = new Builder(conversionGraph, root).build();
    }

    /**
     * Вовзращает все единицы измерения в этой сети
     *
     * @return все единицы измерения в этой сети
     */
    public Set<TMeasurement> getMeasurements() {
        return weights.keySet();
    }

    /**
     * Конвертирует величину измерения в присвоенный ей коэффициент.
     *
     * @param measurement единица измерения
     * @return коэффициент, назначенный этой единице измерения
     */
    public double convertToCoefficient(TMeasurement measurement) {
        Double result = weights.get(measurement);
        if (result == null)
            throw new NoSuchMeasurementException("В этой сети нет нужной единицы измерения: " + measurement);
        return result;
    }

    /**
     * Класс, используемый для построения сети конвертации.
     * <p>
     * Позволяет рекурсивно дойти до всех достижимых из {@code root} единиц измерения
     * и проставить им соответствующие правилам веса.
     * Необходим для того, чтобы после построения сети
     * не держать лишних ссылок на граф конвертаций и на корневой элемент.
     */
    @RequiredArgsConstructor
    private class Builder {
        /**
         * Максимальная разность, допустимая в циклах.
         * Величины сравнивать напрямую нельзя из-за погрешностей при работе с типом {@code double}.
         */
        private static final double MAXIMAL_OK_DIFFERENCE = 1e-15;

        /**
         * Граф величин измерений, построенный на правилах.
         * Данный {@code Builder} полагается на корректность его построения,
         * а именно
         */
        private final Map<TMeasurement, List<ConversionRule<TMeasurement>>> conversionGraph;

        /**
         * Корневой элемент.
         */
        private final TMeasurement root;

        /**
         * Словарь, в котором после построения сети для каждой величины измерения будет лежать её вес.
         */
        private final Map<TMeasurement, Double> weights = new HashMap<>();

        /**
         * Рекурсивно строит сеть величин измерения, содержащую элемент {@code root}.
         * Если в сети есть некорректные правила конвертации, выбрасывается {@link ConversionNetworkBuildException}.
         * После построения сети все итоговые веса будут лежать в {@link #weights}.
         *
         * @param measurement текущая величина измерения
         * @param weight      вес текущей величины измерения
         * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации в сети
         */
        private void buildNetwork(TMeasurement measurement, double weight) {
            weights.put(measurement, weight);
            for (ConversionRule<TMeasurement> rule : conversionGraph.get(measurement)) {
                var nextCoefficient = weight / rule.getSmallPieceCount();
                if (!weights.containsKey(rule.getSmallPiece())) {
                    buildNetwork(rule.getSmallPiece(), nextCoefficient);
                } else if (Math.abs(weights.get(rule.getSmallPiece()) - nextCoefficient) > MAXIMAL_OK_DIFFERENCE) {
                    throw new ConversionNetworkBuildException("В сети существует бесконечно увеличивающийся цикл");
                }
            }
        }

        /**
         * Строит сеть величин измерения.
         *
         * @return словарь, ключом которого является величина измерения, значением - её вес
         * @throws ConversionNetworkBuildException при наличии некорректных правил конвертации в сети
         * @see #buildNetwork(TMeasurement, double)
         */
        public Map<TMeasurement, Double> build() {
            buildNetwork(root, 1);
            return weights;
        }
    }
}

