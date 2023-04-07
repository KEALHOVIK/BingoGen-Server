package dev.catsuperberg.bingogen.server.usecases

import dev.catsuperberg.bingogen.server.common.Grid
import dev.catsuperberg.bingogen.server.presentation.TaskDTO
import dev.catsuperberg.bingogen.server.repository.TaskRepository

class BoardCompiler(private val repository: TaskRepository, private val taskMapper: ITaskMapper) {
    class NotEnoughEntriesException(message: String) : Exception(message)

    fun compile(sideCount: Int, game: String, taskSheet: String): Grid<TaskDTO> {
        val taskCount = sideCount * sideCount
        val rawTasks = repository.findAllTasks(game, taskSheet)
        if(rawTasks.size < taskCount)
            throw NotEnoughEntriesException("Not enough tasks in database for board with side of $sideCount")
        val tasks = rawTasks.shuffled().take(taskCount)
        val dtoList = tasks.map(taskMapper::map)
        return Grid(dtoList.chunked(sideCount))
    }
}
