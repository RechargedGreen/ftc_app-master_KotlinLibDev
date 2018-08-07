package com.david.rechargedkotlinlibrary.internal.hardware.devices

import com.david.rechargedkotlinlibrary.internal.hardware.management.RobotTemplate
import com.david.rechargedkotlinlibrary.internal.hardware.management.ThreadedSubsystem
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxNackException
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse

class RevHub(robot: RobotTemplate, config: String) : ThreadedSubsystem(robot) {
    private val delegate = hMap.get(LynxModule::class.java, config)
    private var response: LynxGetBulkInputDataResponse? = null

    init {
        enablePhoneCharging(false)
    }

    override fun update() {
        val command = LynxGetBulkInputDataCommand(delegate)
        try {
            response = command.sendReceive()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (e: LynxNackException) {
            // TODO: no ideal what we need to do here
        }
    }

    fun enablePhoneCharging(value: Boolean) = delegate.enablePhoneCharging(value)
    fun getEncoder(motorZ: Int) = response?.getEncoder(motorZ)?:0
    fun getDigitalInput(digitalInputZ: Int) = response?.getDigitalInput(digitalInputZ)?:false
    fun getAnalogInput(inputZ: Int) = response?.getAnalogInput(inputZ)?:0.0
    fun getVelocity(motorZ: Int) = response?.getVelocity(motorZ)?:0.0
    fun isAtTarget(motorZ: Int) = response?.isAtTarget(motorZ)?:false
    fun isOverCurrent(motorZ: Int) = response?.isOverCurrent(motorZ)?:false
}