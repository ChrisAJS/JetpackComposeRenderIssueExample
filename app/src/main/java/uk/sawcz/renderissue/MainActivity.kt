package uk.sawcz.renderissue

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.sawcz.renderissue.ui.theme.RenderIssueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = findViewById<View>(android.R.id.content)
        val brokenOnDrawListener = BrokenOnDrawListener(contentView)
        val functionalOnDrawListener = FunctionalOnDrawListener(contentView)

        setContent {
            var content by remember { mutableStateOf("") }
            var breakUi by remember { mutableStateOf(false) }
            var dontBreakUi by remember { mutableStateOf(false) }

            LaunchedEffect(breakUi) {
                if (breakUi) {
                    contentView.viewTreeObserver.addOnDrawListener(brokenOnDrawListener)
                } else {
                    contentView.viewTreeObserver.removeOnDrawListener(brokenOnDrawListener)
                }
            }

            LaunchedEffect(dontBreakUi) {
                if (dontBreakUi) {
                    contentView.viewTreeObserver.addOnDrawListener(functionalOnDrawListener)
                } else {
                    contentView.viewTreeObserver.removeOnDrawListener(functionalOnDrawListener)
                }
            }

            RenderIssueTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = breakUi, onCheckedChange = {
                                breakUi = it
                                dontBreakUi = false
                            })
                            Text("Break UI")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = dontBreakUi, onCheckedChange = {
                                dontBreakUi = it
                                breakUi = false
                            })
                            Text("Perform same work but via Handler")
                        }
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Observe frozen cursor") },
                            value = content,
                            onValueChange = { content = it }
                        )

                    }
                }
            }
        }
    }

    class BrokenOnDrawListener(private val contentView: View) : ViewTreeObserver.OnDrawListener {

        private val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        private val canvas = Canvas(bitmap)

        override fun onDraw() {
            contentView.draw(canvas)
        }
    }

    class FunctionalOnDrawListener(private val contentView: View) : ViewTreeObserver.OnDrawListener {

        private val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        private val canvas = Canvas(bitmap)
        private val handler = Handler(Looper.getMainLooper())

        override fun onDraw() {
            handler.post {
                contentView.draw(canvas)
            }
        }
    }
}