package jvmusin.universalconverter.web;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.Map;
import jvmusin.universalconverter.converter.MeasurementConverter;
import jvmusin.universalconverter.converter.factory.CsvFileSourcedMeasurementConverterFactory;
import jvmusin.universalconverter.number.BigDecimalNumberFactory;
import jvmusin.universalconverter.number.Number;
import jvmusin.universalconverter.number.NumberFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/** Основной класс, запускающий {@code Spring Boot} приложение. */
@SpringBootApplication
public class UniversalConverterApplication {
  /**
   * Метод, запускающий приложение.
   *
   * <p>При старте берёт из командной строки первый аргумент в качесте пути до {@code .csv} файла с
   * правилами конвертации и кладёт его в свойство {@code "csv"}.
   *
   * <p>Все остальные аргументы командной строки игнорируются.
   *
   * @param args аргументы командной строки.
   * @throws IndexOutOfBoundsException если в аргументах командной строки ничего не было передано.
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(UniversalConverterApplication.class);
    app.setDefaultProperties(Map.of("csv", args[0]));
    app.run();
  }

  /**
   * Создаёт {@link MeasurementConverter} из файла, путь до которого лежит в свойстве с именем
   * {@code csv}.
   *
   * @param factory фабрика, с помощью которой создаётся {@link MeasurementConverter}.
   * @param csvFileName путь до файла, из которого будет собран {@link MeasurementConverter}.
   * @return {@link MeasurementConverter}, построенный из файла {@code csvFileName}.
   */
  @Bean
  @ConditionalOnProperty("csv")
  public MeasurementConverter<?> measurementConverter(
      CsvFileSourcedMeasurementConverterFactory<?> factory, @Value("${csv}") String csvFileName) {
    return factory.create(Paths.get(csvFileName));
  }

  /**
   * Создаёт фабрику конвертеров, умеющую читать правила из {@code .csv} файла.
   *
   * @param numberFactory фабрика чисел, которые будут использоваться в получившейся фабрике
   *     конвертеров.
   * @param <TWeight> тип веса, используемого в фабрике конвертеров.
   * @return Фабрику конвертеров, умеющую читать правила из {@code .csv} файла и работать с весами
   *     типа {@link TWeight}.
   */
  @Bean
  public <TWeight extends Number<TWeight>>
      CsvFileSourcedMeasurementConverterFactory<TWeight> measurementConverterFactory(
          NumberFactory<TWeight> numberFactory) {
    return new CsvFileSourcedMeasurementConverterFactory<>(numberFactory);
  }

  /**
   * Создаёт фабрику чисел, используемых в качестве веса в конвертере.
   *
   * <p>Для того, чтобы подменить реализацию типа чисел, достаточно в этом бине создать фабрику
   * другого типа или как-то иначе подменить бин типа {@link NumberFactory}.
   *
   * @return Фабрику чисел, используемых в качестве веса в конвертере.
   */
  @SuppressWarnings("CommentedOutCode")
  @Bean
  public NumberFactory<?> numberFactory() {
    return bigDecimalNumberFactory(150);
    // return new BigIntFractionNumberFactory();
    // return new DoubleNumberFactory();
  }

  /**
   * Создаёт фабрику {@link BigDecimalNumberFactory}.
   *
   * <p>Получающиеся {@link BigDecimal} будут содержать {@code digits} значащих цифр, а два значения
   * будут равны, если они отличаются не более, чем на <code>10<sup>comparingDigits</sup></code>.
   *
   * @param digits количество значащих цифр, хранящихся в числах, создаваемых фабрикой.
   * @return Фабрику {@link BigDecimalNumberFactory}.
   */
  @SuppressWarnings("SameParameterValue")
  private BigDecimalNumberFactory bigDecimalNumberFactory(int digits) {
    MathContext mathContext = new MathContext(digits, RoundingMode.HALF_EVEN);
    return new BigDecimalNumberFactory(mathContext);
  }
}
