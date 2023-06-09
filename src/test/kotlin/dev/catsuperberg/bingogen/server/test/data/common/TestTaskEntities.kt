package dev.catsuperberg.bingogen.server.test.data.common

import dev.catsuperberg.bingogen.server.common.FloatRange
import dev.catsuperberg.bingogen.server.repository.Task
import org.joda.time.Duration
import kotlin.random.Random

object TestTaskEntities {
    private val games = listOf("Galactic Odyssey", "Shadow Realm Chronicles", "Neon Nightscape", "Lost in the Labyrinth", "Cybernetic Crusade")
    private val taskSheets = listOf("basic", "hard", "only stats")

    val baseResultEntity = Task {
        game = "test"
        taskSheet = "test"
        shortText = "short"
        description = "description"
        subject = "subject"
        firstVariant = "var1"
        secondVariant = "var2"
        range = FloatRange(1f, 2f)
        distribution = 3f
        unit = "unit"
        timeToKeep = Duration.standardMinutes(5)
        fromStart = true
    }

    private var increment: Long = 0

    val multiGameAndSheetEntities: List<Task> = games.flatMap { gameValue ->
        (taskSheets + listOf("$gameValue-unique")).flatMap { sheetValue ->
            List(5) {
                baseResultEntity.copy().apply {
                    game = gameValue
                    taskSheet = sheetValue
                    shortText = randomSting()
                    timeToKeep = Duration.standardSeconds(6 * ++increment * increment)
                }
            }
        }
    }

    val singleSheetEntities: List<Task> = List(80) {
        baseResultEntity.copy().apply {
            shortText = randomSting(10)
        }
    }

    private fun randomSting(length: Int = 5) = (1..length).joinToString("") { Random.nextInt(36).toString(36) }

    val blankResultEntity = baseResultEntity.copy().also {
        it.description = null
        it.subject = null
        it.firstVariant = null
        it.secondVariant = null
        it.range = null
        it.distribution = null
        it.unit = null
        it.timeToKeep = null
        it.fromStart = false
    }

    val multiGameAndSheetEntitiesBlank = multiGameAndSheetEntities + listOf(blankResultEntity)
}
