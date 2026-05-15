package com.example.matrusneha.data.ai

import com.example.matrusneha.data.local.NutritionLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class AiSuggestionService {
    private val apiKey = "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"

    suspend fun getDailyHealthSuggestion(
        pregnancyWeek: Int,
        todayKicks: Int,
        nutritionLogs: List<NutritionLog>
    ): String {
        val missedFoods = nutritionLogs.filter { !it.isCompleted }.joinToString(", ") { it.foodName }
        val eatenFoods = nutritionLogs.filter { it.isCompleted }.joinToString(", ") { it.foodName }

        val prompt = """
            You are a kind, motherly AI companion for a rural pregnant woman in Karnataka, India. 
            She is in Week $pregnancyWeek of her pregnancy.
            Today she felt $todayKicks baby kicks.
            She has eaten: $eatenFoods.
            She missed eating: $missedFoods.
            
            Give a 2-sentence encouraging health tip in simple English. 
            Focus on local Karnataka food benefits (like Ragi, Jowar, or Soppu) if relevant.
            Be very warm and use emojis. Do not use medical jargon.
        """.trimIndent()

        return try {
            withContext(Dispatchers.IO) {
                val url = URL("https://openrouter.ai/api/v1/chat/completions")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer $apiKey")
                connection.setRequestProperty("HTTP-Referer", "http://matrusneha.app")
                connection.setRequestProperty("X-Title", "Matru-Sneh")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonPayload = """
                    {
                      "model": "google/gemini-1.5-flash",
                      "messages": [
                        {"role": "user", "content": "${prompt.replace("\n", " ").replace("\"", "\\\"")}"}
                      ]
                    }
                """.trimIndent()

                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(jsonPayload)
                writer.flush()
                writer.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStr = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(responseStr)
                    val content = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    content
                } else {
                    generateLocalFallbackSuggestion(pregnancyWeek, todayKicks, nutritionLogs)
                }
            }
        } catch (e: Exception) {
            generateLocalFallbackSuggestion(pregnancyWeek, todayKicks, nutritionLogs)
        }
    }

    private fun generateLocalFallbackSuggestion(
        pregnancyWeek: Int,
        todayKicks: Int,
        nutritionLogs: List<NutritionLog>
    ): String {
        val ironChecked = nutritionLogs.any { it.category == "Iron Foods" && it.isCompleted }
        
        return if (!ironChecked && nutritionLogs.isNotEmpty()) {
            "💡 You're doing great in Week $pregnancyWeek, but remember to eat your Iron (Greens) today for the baby's growth!"
        } else if (todayKicks < 5) {
            "👶 Keep an eye on your kick counts today, aim for 10 kicks!"
        } else {
            "💖 Perfect day! Your nutrition and kick counts are wonderful."
        }
    }
}
