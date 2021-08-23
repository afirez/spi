package transform.task

import java.io.Serializable
import java.util.LinkedHashSet

class WeavedClass(val className: String) : Serializable {

    val weavedMethods = LinkedHashSet<String>()


    fun addWeavedMethod(methodSignature: String) {
        weavedMethods.add(methodSignature)
    }


    fun getWeavedMethods(): Set<String> {
        return weavedMethods
    }

    fun hasWeavedMethod(): Boolean {
        return weavedMethods.size > 0
    }

}