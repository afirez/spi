package com.knight.transform.Utils

import com.knight.transform.BaseContext
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

/**
 * description
 *
 * @author liyachao
 * @date 2019/3/23
 */
object PrintAllTaskUtil {
    val TAG = "PrintAllTaskUtil"
    //用来记录 task 的执行时长等信息
    val timeCostMap = LinkedHashMap<String, TaskExecTimeInfo>()

    private val taskExecutionListener = object : TaskExecutionListener {
        override fun beforeExecute(task: Task) {
            timeCostMap[task.path] = TaskExecTimeInfo(System.currentTimeMillis(), 0, task.path, 0)
        }

        override fun afterExecute(task: Task, p1: TaskState) {
            timeCostMap[task.path]?.let {
                it.endTime = System.currentTimeMillis()
                it.totalTime = it.endTime - it.startTime
            }
        }
    }

    private val buildListener = object : BuildListener {
        override fun buildFinished(buildResult: BuildResult) {
            val sb = StringBuilder()
            sb.append("build finished, now println all task execution time: ")
            timeCostMap.forEach {
                sb.append("\n${it.key} [${it.value.totalTime}]")
            }
            Log.e(sb.toString())
        }

        override fun projectsLoaded(p0: Gradle) {
        }

        override fun buildStarted(p0: Gradle) {
        }

        override fun projectsEvaluated(p0: Gradle) {
        }

        override fun settingsEvaluated(p0: Settings) {
        }

    }

    fun printAllTasks(context: BaseContext<*>) {
        context.project.gradle.addListener(taskExecutionListener)
        context.project.gradle.addBuildListener(buildListener)
    }

    fun printAllTasks(project: Project) {
        project.gradle.addListener(taskExecutionListener)
        project.gradle.addBuildListener(buildListener)
    }
}

data class TaskExecTimeInfo(var startTime: Long = 0, var endTime: Long = 0,
                            var path: String = "", var totalTime: Long = 0)