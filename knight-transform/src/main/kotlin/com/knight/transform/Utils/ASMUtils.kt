package transform.Utils

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.TransformInput
import com.android.build.gradle.AppExtension
import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import org.gradle.api.Project
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader


object ASMUtils {
    fun isPrivate(access: Int): Boolean {
        return access and ACC_PRIVATE != 0
    }

    fun isPublic(access: Int): Boolean {
        return access and ACC_PUBLIC != 0
    }

    fun isStatic(access: Int): Boolean {
        return access and ACC_STATIC != 0
    }

    fun isFinal(access: Int): Boolean {
        return access and Opcodes.ACC_FINAL == Opcodes.ACC_FINAL
    }

    fun isInt(desc: String): Boolean {
        return "I" == desc
    }

    fun convertSignature(name: String, desc: String): String {
        val method = Type.getType(desc)
        val sb = StringBuilder()
        sb.append(method.returnType.className).append(" ").append(name)
        sb.append("(")
        for (i in 0 until method.argumentTypes.size) {
            sb.append(method.argumentTypes[i].className)
            if (i != method.argumentTypes.size - 1) {
                sb.append(",")
            }
        }
        sb.append(")")
        return sb.toString()
    }

    fun isSelfFile(className: String): Boolean {
        if (className.startsWith("android/") || className.startsWith("kotlin/")) {
            return false
        }
        return true
    }

    @Throws(MalformedURLException::class)
    fun getClassLoader(inputs: Collection<TransformInput>,
                       referencedInputs: Collection<TransformInput>,
                       project: Project): URLClassLoader {
        val urls = ImmutableList.Builder<URL>()
        val androidJarPath = getAndroidJarPath(project)
        val file = File(androidJarPath)
        val androidJarURL = file.toURI().toURL()
        urls.add(androidJarURL)
        for (totalInputs in Iterables.concat(inputs, referencedInputs)) {
            for (directoryInput in totalInputs.directoryInputs) {
                if (directoryInput.file.isDirectory) {
                    urls.add(directoryInput.file.toURI().toURL())
                }
            }
            for (jarInput in totalInputs.jarInputs) {
                if (jarInput.file.isFile) {
                    urls.add(jarInput.file.toURI().toURL())
                }
            }
        }
        val allUrls = urls.build()
        val classLoaderUrls = allUrls.toTypedArray()
        return URLClassLoader(classLoaderUrls)
    }

    /**
     * /Users/quinn/Documents/Android/SDK/platforms/android-28/android.jar
     */
    private fun getAndroidJarPath(project: Project): String {
        val appExtension = project.properties["android"] as AppExtension?
        var sdkDirectory = appExtension!!.sdkDirectory.absolutePath
        val compileSdkVersion = appExtension.compileSdkVersion
        sdkDirectory = sdkDirectory + File.separator + "platforms" + File.separator
        return sdkDirectory + compileSdkVersion + File.separator + "android.jar"
    }
}