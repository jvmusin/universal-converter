package jvmusin.universalconverter

val sampleRules
    get() = listOf(
        ConversionRule("м", "см", 100.0),
        ConversionRule("мм", "м", 0.001),
        ConversionRule("км", "м", 1000.0),
        ConversionRule("час", "мин", 60.0),
        ConversionRule("мин", "с", 60.0),
    )

fun <T> List<ConversionRule<T>>.toConversionGraph(): Map<T, List<ConversionRule<T>>> = Utils.buildConversionGraph(this)

val sampleConverter: MeasurementConverter<String>
    get() = MeasurementConverterFactoryImpl().create(sampleRules)

val sampleConversionGraph: MutableMap<String, MutableList<ConversionRule<String>>>
    get() = Utils.buildConversionGraph(sampleRules)