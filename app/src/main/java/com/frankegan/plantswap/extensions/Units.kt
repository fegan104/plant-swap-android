package com.frankegan.plantswap.extensions

inline class Kilometers(val distance: Double)

val Double.km: Kilometers
    get() = Kilometers(this)

val Int.km: Kilometers
    get() = Kilometers(this.toDouble())