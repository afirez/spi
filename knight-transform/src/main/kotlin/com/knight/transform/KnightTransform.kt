package transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.knight.transform.BaseContext
import com.knight.transform.IPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.Utils.TransFormUtil
import com.knight.transform.asm.CodeWeaver
import com.knight.transform.asm.ScanWeaver
import transform.Utils.ASMUtils

open class KnightTransform(private val context: BaseContext<*>, val iPlugin: IPlugin, val getTransformName: () -> String) : Transform() {
    private val codeWeaver: CodeWeaver = CodeWeaver(iPlugin)
    private val scanWeaver: ScanWeaver? = if (iPlugin.isNeedScanClass()) {
        ScanWeaver(iPlugin)
    } else null


    override fun getName(): String {
        return getTransformName.invoke()
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return if (context.isLibrary) TransformManager.PROJECT_ONLY else TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(invocation: TransformInvocation) {
        val classLoader = ASMUtils.getClassLoader(invocation.inputs, invocation.referencedInputs, context.project)
        scanWeaver?.let {
            it.classloader = classLoader
            it.isNeedScanRClass = iPlugin.isNeedScanWeaveRClass()
            val startTime = System.currentTimeMillis()
            TransFormUtil.transform(context, it, invocation)
            val costTime = System.currentTimeMillis() - startTime
            Log.i("$name : scan code has costed $costTime ms")
        }
        codeWeaver.run {
            val startTime = System.currentTimeMillis()
            classloader = classLoader
            isNeedScanRClass = iPlugin.isNeedScanWeaveRClass()
            TransFormUtil.transform(context, this, invocation)
            val costTime = System.currentTimeMillis() - startTime
            Log.i("$name : weave code has costed $costTime ms")
        }

    }
}