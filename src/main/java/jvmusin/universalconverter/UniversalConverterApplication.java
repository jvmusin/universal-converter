package jvmusin.universalconverter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;

/**
 * Основной класс, запускающий {@code Spring Boot} приложение.
 */
@SpringBootApplication
@Log4j2
public class UniversalConverterApplication {
    /**
     * Метод, запускающий приложение.
     * При старте берёт из командной строки первый аргумент в качесте пути до {@code .csv} файла
     * с правилами конвератции и изменяет его так, чтобы позже его можно было достать из
     * приложения по ключу {@code csv}.
     * <p>
     * Все остальные аргументы командной строки игнорируются.
     *
     * @param args агрументы командной строки
     * @throws IndexOutOfBoundsException если в аргументах командной строки ничего не было передано
     */
    public static void main(String[] args) {
        String csvFileName = args[0];
        SpringApplication.run(UniversalConverterApplication.class, "--csv=" + csvFileName);
    }

    /**
     * Создаёт {@link MeasurementConverter} из файла,
     * путь до которого лежит в свойстве с именем {@code csv}.
     *
     * @param factory     фабрика, с помощью которой создаётся {@link MeasurementConverter}
     * @param csvFileName путь до файла, из которого будет собран {@link MeasurementConverter}
     * @return {@link MeasurementConverter}, построенный из файла {@code csvFileName}
     */
    @Bean
    @ConditionalOnProperty("csv")
    public MeasurementConverter<String> measurementConverter(
            MeasurementConverterFactory factory, @Value("${csv}") String csvFileName) {
        return factory.create(Paths.get(csvFileName));
    }
}
