package com.knight.transform.Utils

import org.objectweb.asm.Opcodes

import java.util.regex.Matcher
import java.util.regex.Pattern

object TypeUtil {
    private val paramsPat = Pattern.compile("(\\[?[BCZSIJFD])|(L[^;]+;)")

    fun removeFirstParam(desc: String): String {
        if (desc.startsWith("()")) {
            return desc
        }
        var index = 1
        var c = desc[index]
        while (c == '[') {
            index++
            c = desc[index]
        }
        if (c == 'L') {
            while (desc[index] != ';') {
                index++
            }
        }
        return "(" + desc.substring(index + 1)
    }

    fun getParameterCountFromMethodDesc(desc: String): Int {
        val beginIndex = desc.indexOf('(') + 1
        val endIndex = desc.lastIndexOf(')')
        val paramsDesc = desc.substring(beginIndex, endIndex)
        if (paramsDesc.isEmpty()) return 0
        var count = 0
        val matcher = paramsPat.matcher(paramsDesc)
        while (matcher.find()) {
            count++
        }
        return count
    }

    fun desc2Name(desc: String): String {
        return if (!desc.startsWith("L") && !desc.endsWith(";")) {
            desc
        } else desc.substring(1, desc.length - 1)
    }

    fun descToStatic(access: Int, desc: String, className: String): String {
        var desc = desc
        if (access and Opcodes.ACC_STATIC == 0) {
            desc = "(L" + className.replace('.', '/') + ";" + desc.substring(1)
        }
        return desc
    }

    fun descToNonStatic(desc: String): String {
        return "(" + desc.substring(desc.indexOf(';') + 1)
    }

    fun parseArray(index: Int, desc: String): Int {
        var index = index
        while (desc[index] == '[') index++
        if (desc[index] == 'L') {
            while (desc[index] != ';') index++
        }
        return index
    }

    fun parseObject(index: Int, desc: String): Int {
        var index = index
        while (desc[index] != ';') index++
        return index
    }

    fun isStatic(access: Int): Boolean {
        return access and Opcodes.ACC_STATIC == Opcodes.ACC_STATIC
    }

    fun isAbstract(access: Int): Boolean {
        return access and Opcodes.ACC_ABSTRACT == Opcodes.ACC_ABSTRACT
    }

    fun isSynthetic(access: Int): Boolean {
        return access and Opcodes.ACC_SYNTHETIC == Opcodes.ACC_SYNTHETIC
    }

    fun isPrivate(access: Int): Boolean {
        return access and Opcodes.ACC_PRIVATE == Opcodes.ACC_PRIVATE
    }

    fun isPublic(access: Int): Boolean {
        return access and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC
    }

    fun isProtected(access: Int): Boolean {
        return access and Opcodes.ACC_PROTECTED == Opcodes.ACC_PROTECTED
    }

    fun resetAccessScope(access: Int, scope: Int): Int {
        return access and (Opcodes.ACC_PRIVATE or Opcodes.ACC_PUBLIC or Opcodes.ACC_PROTECTED).inv() or scope
    }

    fun isInterface(access: Int): Boolean {
        return access and Opcodes.ACC_INTERFACE == Opcodes.ACC_INTERFACE
    }

    fun isInt(desc: String): Boolean {
        return "I" == desc
    }

    fun isFinal(access: Int): Boolean {
        return access and Opcodes.ACC_FINAL == Opcodes.ACC_FINAL
    }
}
