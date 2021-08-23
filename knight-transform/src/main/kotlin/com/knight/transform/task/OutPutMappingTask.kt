package transform.task

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.lang.StringBuilder
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

open class OutPutMappingTask : DefaultTask() {
    init {
        group = "outputmapping"
        description = "write mapping file"
    }

    @Input
    var variantName = project.objects.property(String::class.java)

//    @OutputFile
//    var outputMappingFile = newOutputFile()

//    @OutputFile
//    var outputMappingFile = getServices().get(ObjectFactory::class.java).fileProperty()

    @OutputFile // property needs an annotation
    var outputMappingFile = project.objects.fileProperty()

////    @Internal
//    var classes = project.objects.property(ArrayList::class.java)

    @Internal
    var classes = project.objects.listProperty(Any::class.java)

    @TaskAction
    fun writeMapping() {
        var loggable = false
        val mappingFile = outputMappingFile.get().asFile

        FileUtils.touch(mappingFile)
        val content = StringBuilder()
        if (loggable) println("outputtask size: ${classes.get().size}")
        (classes.get() as List<WeavedClass>).forEach {
            it?.takeIf { it ->
                it?.hasWeavedMethod()
            }?.let {
                val className = it.className
                val doubleCheckMethods = it.weavedMethods
                content.append(className).append("\n")
                if (loggable) println(className)
                doubleCheckMethods.forEach {
                    content.append("\u21E2 $it").append("\n")
                    if (loggable) println("\u21E2 $it")
                }
            }
        }
        mappingFile.writeText(content.toString())
//        println("Success wrote TXT mapping report to file://${outputMappingFile}")

    }

}