package jvmusin.universalconverter.number

operator fun <TNumber : Number<TNumber>> TNumber.times(other: TNumber): TNumber = multiplyBy(other)
operator fun <TNumber : Number<TNumber>> TNumber.div(other: TNumber): TNumber = divideBy(other)
