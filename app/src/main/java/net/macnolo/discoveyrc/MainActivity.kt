package net.macnolo.discoveyrc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import io.github.controlwear.virtual.joystick.android.JoystickView;
import android.view.View
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener
import net.macnolo.discoveyrc.math.VelocitiesCalculator


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val joystick = findViewById<View>(R.id.joystickView) as JoystickView

        joystick.setOnMoveListener { angle, strength ->
            val velocities =
                VelocitiesCalculator.calculateVelocities(Math.toRadians(angle.toDouble()), strength.toDouble() / 100)
            findViewById<TextView>(R.id.textViewA).text = "A: ${velocities.get(0)}"
            findViewById<TextView>(R.id.textViewB).text = "B: ${velocities.get(1)}"
        }
    }
}