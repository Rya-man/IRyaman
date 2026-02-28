package com.ryaman.ir_remote_for_moodlight
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ir_remote_for_moodlight.ui.theme.IRRemoteformoodlightTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.example.ir_remote_for_moodlight.ir.IRController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IRRemoteformoodlightTheme {
                RemoteScreen()
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RemoteScreen() {
    val context = LocalContext.current
    val irController = remember { IRController(context) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        val remoteAspectRatio = 420f / 700f
        val remoteWidth = maxWidth
        val remoteHeight = remoteWidth / remoteAspectRatio

        Box(
            modifier = Modifier
                .width(remoteWidth)
                .height(remoteHeight)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->

                        val svgX = (offset.x / remoteWidth.toPx()) * 420f
                        val svgY = (offset.y / remoteHeight.toPx()) * 700f

                        println("Tapped SVG coords: $svgX , $svgY")

                        detectButton(svgX, svgY, irController)
                    }
                }
        ) {

            // Background remote vector
            Image(
                painter = painterResource(id = R.drawable.moodlight_remote),
                contentDescription = "Moodlight Remote",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // We will overlay text + click detection here next
        }
    }
}
fun detectButton(x: Float, y: Float, irController: IRController)
{
    fun insideCircle(cx: Float, cy: Float, r: Float): Boolean {
        val dx = x - cx
        val dy = y - cy
        return dx * dx + dy * dy <= r * r
    }
    fun insideRect(left: Float, top: Float, right: Float, bottom: Float): Boolean {
        return x in left..right && y in top..bottom
    }
    when {

        insideCircle(155f, 95f, 30f) -> irController.sendCommand(0x01) // Increase brightness

        insideCircle(75f, 95f, 30f)  -> irController.sendCommand(0x00) // Decrease brightness



        // Power

        insideCircle(265f, 95f, 30f) -> irController.sendCommand(0x02) // OFF

        insideCircle(345f, 95f, 30f) -> irController.sendCommand(0x03) // ON



        // Row 1

        insideCircle(85f, 210f, 35f)  -> irController.sendCommand(0x04) // Red

        insideCircle(175f, 210f, 35f) -> irController.sendCommand(0x05) // Green

        insideCircle(265f, 210f, 35f) -> irController.sendCommand(0x06) // Blue

        insideCircle(355f, 210f, 35f) -> irController.sendCommand(0x07) // White



        // Row 2

        insideCircle(85f, 300f, 35f)  -> irController.sendCommand(0x08) // Scarlet

        insideCircle(175f, 300f, 35f) -> irController.sendCommand(0x09) // Light green

        insideCircle(265f, 300f, 35f) -> irController.sendCommand(0x0A) // Periwinkle

        insideRect(325f, 280f, 385f, 340f) -> irController.sendCommand(0x0B) // FLASH



        // Row 3

        insideCircle(85f, 390f, 35f)  -> irController.sendCommand(0x0C) // Orange

        insideCircle(175f, 390f, 35f) -> irController.sendCommand(0x0D) // Mint

        insideCircle(265f, 390f, 35f) -> irController.sendCommand(0x0E) // Purple

        insideRect(325f, 370f, 385f, 430f) -> irController.sendCommand(0x0F) // STROBE



        // Row 4

        insideCircle(85f, 480f, 35f)  -> irController.sendCommand(0x10) // Tangerine

        insideCircle(175f, 480f, 35f) -> irController.sendCommand(0x11) // Sky

        insideCircle(265f, 480f, 35f) -> irController.sendCommand(0x12) // Rose

        insideRect(325f, 460f, 385f, 520f) -> irController.sendCommand(0x13) // FADE



        // Row 5

        insideCircle(85f, 570f, 35f)  -> irController.sendCommand(0x14) // Yellow

        insideCircle(175f, 570f, 35f) -> irController.sendCommand(0x15) // Aqua

        insideCircle(265f, 570f, 35f) -> irController.sendCommand(0x16) // Pink

        insideRect(325f, 550f, 385f, 610f) -> irController.sendCommand(0x17) // SMOOTH

        }
    Log.d("IRRemote", "No button")
}