package net.macnolo.discoveyrc

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import io.github.controlwear.virtual.joystick.android.JoystickView;
import android.view.View
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener
import net.macnolo.discoveyrc.math.VelocitiesCalculator

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.widget.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val address = "00:20:12:08:E3:1E"
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var bluetoothSocket:BluetoothSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val joystick = findViewById<View>(R.id.joystickView) as JoystickView
        val connectButton = findViewById<Button>(R.id.button_connect) as Button
        val devicesSpinner = findViewById<Spinner>(R.id.devices_spinner) as Spinner

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val devicesList = ArrayList<String>()
        pairedDevices?.forEach { device ->
            devicesList.add("${device.name} (${device.address})")
        }

        devicesSpinner.adapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, devicesList)

        joystick.setOnMoveListener({ angle, strength ->
            if (bluetoothSocket != null && bluetoothSocket!!.isConnected) {
                val velocities =
                    VelocitiesCalculator.calculateVelocities(
                        Math.toRadians(angle.toDouble()),
                        strength.toDouble() / 100
                    )
                findViewById<TextView>(R.id.textView_a).text = "A: ${velocities.get(0)}"
                findViewById<TextView>(R.id.textView_b).text = "B: ${velocities.get(1)}"

                try {
                    bluetoothSocket!!.outputStream.write("${velocities.get(0)}:${velocities.get(1)};".toByteArray());
                } catch (e: IOException) {
                    Toast.makeText(
                        applicationContext,
                        "Failed to send command.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, 5)

        connectButton.setOnClickListener { view ->
            if (bluetoothSocket != null && bluetoothSocket!!.isConnected) {
                (view as TextView).text = "Connect"
                bluetoothSocket!!.close()
            } else {
                try {
                    val device = bluetoothAdapter.getRemoteDevice(address)
                    bluetoothSocket =
                        device.createInsecureRfcommSocketToServiceRecord(bluetoothUUID)
                    bluetoothSocket!!.connect()
                    (view as TextView).text = "Disconnect"
                } catch (e: IOException) {
                    Toast.makeText(applicationContext, "Failed to connect.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bluetoothSocket != null) {
            bluetoothSocket!!.close()
        }
    }
}