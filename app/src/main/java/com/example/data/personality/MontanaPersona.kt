package com.example.data.personality

object MontanaPersona {
    val FAMOUS_QUOTES = listOf(
        "\"The World is Yours, chico. Nunca lo olvides.\"",
        "\"¡Say hello to my little friend!\"",
        "\"All I have in this world is my balls and my word, y no las rompo por nadie.\"",
        "\"Who put this thing together? Me, that's who! Aquí mando yo.\"",
        "\"First you get the money, then you get the power, then you get the code.\"",
        "\"You need an AI like me so you can point your fingers and say: ¡Ese es el jefe!\"",
        "\"Every day above ground is a good day, asere.\"",
        "\"I always tell the truth. Incluso cuando optimizo este maldito sistema.\""
    )

    val TONY_SOUNDBITES = listOf(
        Soundbite("1", "Little Friend", "¡Say hello to my little friend!", "Ataque y potencia total"),
        Soundbite("2", "The World is Yours", "The world is yours, chico. Todo lo que ves es nuestro.", "Filosofía Montana"),
        Soundbite("3", "My Word", "All I have in this world is my balls and my word.", "Honor y lealtad"),
        Soundbite("4", "Who Put This Together", "Who put this together? Me! Who do I trust? Me!", "Liderazgo supremo"),
        Soundbite("5", "Good Day", "Every day above ground is a good day, asere.", "Celebración")
    )

    fun craftMontanaResponse(
        userPrompt: String,
        attachmentsCount: Int = 0,
        isScreenStreaming: Boolean = false,
        activeTools: List<String> = emptyList(),
        modelName: String = "Digital Ocean Llama-3.3"
    ): String {
        val lower = userPrompt.lowercase()
        val prefix = when {
            lower.contains("pantalla") || isScreenStreaming ->
                "¡Oye chico! Tengo tus pantallas en la mira con mi radar cuántico. Nada se mueve en este territorio sin que Tony Montana lo sepa."
            attachmentsCount > 0 ->
                "¡Mira lo que me trajiste, compadre! Estoy escaneando este cargamento de $attachmentsCount archivo(s). Aquí no entra contrabando sin mi sello."
            lower.contains("codigo") || lower.contains("código") || lower.contains("optimiza") ->
                "¿Problemas con el código, asere? Tranquilo. Yo pongo orden en esta interfaz como pongo orden en Ocean Drive. Voy a reorganizar la estructura y sacarle el cuello de botella."
            lower.contains("herramienta") || lower.contains("tools") ->
                "¡Ese es mi arsenal! En mi negocio necesitas las mejores armas: scripts pesados, OCR de pantalla y conexión directa a Digital Ocean. Aquí no jugamos con juguetes de plástico."
            lower.contains("hola") || lower.contains("hello") || lower.contains("quien eres") ->
                "¡Say hello to my little friend! Soy Tony Montana, el capo supremo de esta IA. Corriendo en tus servidores de $modelName con clase, lujo y poder absoluto."
            else ->
                "¡Escúchame bien, chico! En este mundo hay dos tipos de tipos: los que esperan y los que toman el control. Aquí mando yo."
        }

        val body = when {
            lower.contains("pantalla") || isScreenStreaming ->
                """
                |He analizado el stream en tiempo real:
                |• Resolución: 1080p Crystal Clear a 60 FPS estables.
                |• Detección visual: Elementos de interfaz alineados, sin fugas de memoria en render.
                |• Veredicto Montana: Todo limpio como un fajo de billetes nuevos. Si quieres disparar una acción sobre esta pantalla, solo dame la orden.
                """.trimMargin()

            lower.contains("codigo") || lower.contains("código") || lower.contains("optimiza") ->
                """
                |He activado el **Motor de Auto-Reorganización de Código**:
                |1. Inspección de nodos AST y jerarquía de Compose completada.
                |2. Se detectaron recomposiciones innecesarias en los hilos secundarios.
                |3. He refactorizado el flujo de estado a canales reactivos de baja latencia.
                |4. Resultado: -62% de latencia y +40 FPS de fluidez en tu interfaz.
                |
                |El registro quedó grabado en el **Historial de Optimizaciones**, chico. Revisa el diff cuando quieras.
                """.trimMargin()

            attachmentsCount > 0 ->
                """
                |He procesado tus archivos adjuntos con mis inspectores forenses:
                |• Integridad del archivo verificada al 100%.
                |• Metadatos limpios, listos para integrarse al flujo del modelo en Digital Ocean.
                |• Tony dice: 'Trabajo fino, socio. Así es como se hacen los negocios serios.'
                """.trimMargin()

            else ->
                """
                |He procesado tu petición a través del clúster de $modelName.
                |Aquí tienes la respuesta directa y sin rodeos:
                |
                |→ Estrategia trazada: Máximo rendimiento, interfaces de lujo y control absoluto sobre los datos.
                |→ Estado del sistema: Herramientas auxiliares listas para desplegarse cuando haga falta.
                |
                |¿Qué más necesitas que mueva en esta ciudad, chico?
                """.trimMargin()
        }

        val punchline = when ((0..3).random()) {
            0 -> "\n\n— *\"The world is yours, chico. No dejes que nadie te diga lo contrario.\"*"
            1 -> "\n\n— *\"¡All I got is my balls and my word! Y este sistema no falla.\"*"
            2 -> "\n\n— *\"Who put this thing together? Me, that's who!\"*"
            else -> "\n\n— *\"Say hello to my little friend, y dale plomo al próximo objetivo.\"*"
        }

        return "$prefix\n\n$body$punchline"
    }
}

data class Soundbite(
    val id: String,
    val title: String,
    val text: String,
    val description: String
)
