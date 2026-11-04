package com.example.ui.components

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class MontanaVoiceSpeaker(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentSpokenText = MutableStateFlow("")
    val currentSpokenText: StateFlow<String> = _currentSpokenText.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                // Try Spanish (Cuban/Latin American cadence) or default
                val result = engine.setLanguage(Locale("es", "US"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.getDefault())
                }
                // Tony Montana voice tuning: slightly gruff deep pitch with fast, aggressive rate
                engine.setPitch(0.85f)
                engine.setSpeechRate(1.15f)

                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                        _currentSpokenText.value = ""
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                        _currentSpokenText.value = ""
                    }
                })
            }
        }
    }

    fun speakMontana(text: String) {
        if (tts == null) return
        stop()
        // Strip markdown asterisks or code formatting for speech
        val cleaned = text
            .replace(Regex("[*#_`|\\[\\]]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        _currentSpokenText.value = cleaned
        _isSpeaking.value = true
        tts?.speak(cleaned, TextToSpeech.QUEUE_FLUSH, null, "MONTANA_UTTERANCE_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentSpokenText.value = ""
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
