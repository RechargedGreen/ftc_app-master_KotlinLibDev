package com.david.rechargedkotlinlibrary.internal.hardware.devices.sensors.encoders

import com.david.rechargedkotlinlibrary.internal.hardware.devices.RevHub
import com.david.rechargedkotlinlibrary.internal.util.MathUtil
import com.qualcomm.robotcore.hardware.DcMotorSimple

/**
 * Created by David Lukens on 8/8/2018.
 */
open class Encoder(private val HUB: RevHub, private val PORT: Int, private val PPR: Int, direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD) {
    private var resetTicks = 0
    private var secant = 1

    init {
        setDirection(direction)
        reset()
    }

    fun getRawTicks() = HUB.getEncoder(PORT) * secant
    fun reset() {
        resetTicks = getRawTicks()
    }

    fun getTicks() = getRawTicks() - resetTicks
    fun getRadians() = toRadians(getTicks())
    fun getRawRadians() = toRadians(getRawTicks())

    fun toRadians(ticks: Int): Double {
        return (ticks.toDouble() / PPR.toDouble()) * MathUtil.TAU
    }

    fun setDirection(direction: DcMotorSimple.Direction) {
        secant = if (direction == DcMotorSimple.Direction.REVERSE) -1 else 0
    }

    private var lastTicks: Int? = null

    fun tickChange(): Int? {
        var ticks = getTicks()
        var change: Int? = if (lastTicks != null) ticks - lastTicks!! else null
        lastTicks = ticks
        return change
    }

    fun radiansChange(): Double? {
        val change = tickChange()
        return if (change != null) toRadians(change) else null
    }

}