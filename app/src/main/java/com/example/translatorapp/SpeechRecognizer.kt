package com.example.translatorapp

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast


class SpeechToText(
    private val language: String,
    private val context: Context,
    private val onTextRecognized: (String) -> Unit
) : RecognitionListener {

    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    init {
        speechRecognizer.setRecognitionListener(this)
    }

    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        }
        speechRecognizer.startListening(intent)
    }

    private fun stopListening() {
        speechRecognizer.stopListening()
    }

    override fun onReadyForSpeech(params: android.os.Bundle?) {
        // Called when the speech recognition service is ready to start listening.
        Toast.makeText(
            context,
            "Ready for speech",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBeginningOfSpeech() {
        // Called when the user has started to speak.
        Toast.makeText(
            context,
            "You are speaking",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRmsChanged(rmsdB: Float) {
        // Called when the RMS (Root Mean Square) of the audio being processed has changed.
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        // Called when partial recognition results are available.
    }

    override fun onEndOfSpeech() {
        // Called when the user has finished speaking.
        stopListening()
        Toast.makeText(
            context,
            "Stopped speaking",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onError(error: Int) {
        // Called when there is an error in recognition.
    }

    override fun onResults(results: android.os.Bundle?) {
        // Called when the recognition is successful.
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.let {
            if (it.isNotEmpty()) {
                onTextRecognized.invoke(it[0])
            }
        }
    }

    override fun onPartialResults(partialResults: android.os.Bundle?) {
        // Called when partial recognition results are available.
    }

    override fun onEvent(eventType: Int, params: android.os.Bundle?) {
        // Reserved for future events.
    }
}