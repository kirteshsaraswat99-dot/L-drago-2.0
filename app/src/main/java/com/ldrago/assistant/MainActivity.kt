
package com.ldrago.assistant

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color
import java.util.Locale

class MainActivity : Activity() {

    private var tts: TextToSpeech? = null
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) {
            tts?.language = Locale.US
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(30, 30, 30, 30)
            setBackgroundColor(Color.rgb(15, 15, 20))
        }

        val title = TextView(this).apply {
            text = "L DRAGO"
            textSize = 36f
            setTextColor(Color.RED)
            gravity = Gravity.CENTER
        }

        status = TextView(this).apply {
            text = "YOUR ASSISTANT IS READY"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(10, 40, 10, 40)
        }

        val button = Button(this).apply {
            text = "ACTIVATE L DRAGO"
            setOnClickListener {
                startVoiceRecognition()
            }
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(button)

        setContentView(layout)
    }

    private fun startVoiceRecognition() {
        if (!android.speech.SpeechRecognizer.isRecognitionAvailable(this)) {
            status.text = "Speech recognition unavailable"
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to L Drago")
        }

        try {
            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            status.text = "Voice recognition error"
        }
    }

    @Deprecated("Deprecated in Android API")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )?.firstOrNull() ?: return

            status.text = "You said: $result"
            tts?.speak(
                "I heard you. L Drago is listening.",
                TextToSpeech.QUEUE_FLUSH,
                null,
                "reply"
            )
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
    }
}
