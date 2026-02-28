package com.example.ir_remote_for_moodlight.ir

import android.content.Context
import android.hardware.ConsumerIrManager

class IRController(context: Context) {

    private val irManager =
        context.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager?

    private val ADDRESS = 0xEF00
    private val CARRIER = 38000

    fun sendCommand(command: Int) {
        if (irManager == null || !irManager.hasIrEmitter()) {
            println("No IR emitter found")
            return
        }

        val pattern = buildExtendedNEC(ADDRESS, command)
        irManager.transmit(CARRIER, pattern)
    }

    private fun buildExtendedNEC(address: Int, command: Int): IntArray {

        val result = mutableListOf<Int>()

        // NEC leader
        result.add(9000)
        result.add(4500)

        // 16-bit address (LSB first)
        appendBits(result, address, 16)

        // 8-bit command
        appendBits(result, command, 8)

        // 8-bit inverse command
        appendBits(result, command xor 0xFF, 8)

        // final mark
        result.add(560)

        return result.toIntArray()
    }

    private fun appendBits(out: MutableList<Int>, value: Int, bitCount: Int) {
        for (i in 0 until bitCount) {
            val bit = (value shr i) and 1

            out.add(560) // mark

            if (bit == 1)
                out.add(1690) // space for 1
            else
                out.add(560)  // space for 0
        }
    }
}