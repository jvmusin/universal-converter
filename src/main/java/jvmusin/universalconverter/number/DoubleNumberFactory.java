package jvmusin.universalconverter.number;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Фабрика, используемая для создания чисел типа {@link DoubleNumber}.
 */
@Component
public class DoubleNumberFactory implements NumberFactory<DoubleNumber> {
    @Override
    public DoubleNumber one() {
        return new DoubleNumber(1);
    }

    @Override
    public DoubleNumber parse(String s) {
        Assert.notNull(s, "Строка не может быть null");
        return new DoubleNumber(Double.parseDouble(s));
    }
}
