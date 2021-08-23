package com.knight.transform.Utils


import java.io.PrintWriter
import java.io.StringWriter

object Log {

    /**
     * Drawing toolbox
     */
    private val TOP_LEFT_CORNER = '╔'
    private val BOTTOM_LEFT_CORNER = '╚'
    private val MIDDLE_CORNER = '─'
    private val HORIZONTAL_DOUBLE_LINE = '║'
    private val DOUBLE_DIVIDER = "═════════════════════════════════════════════════"
    private val SINGLE_DIVIDER = "─────────────────────────────────────────────────"
    val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    val BR = System.getProperty("line.separator")     // 换行符


    private val debugLog: LogImp = object : LogImp {
        @Synchronized
        override fun i(msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.INFO), msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.INFO), msg))
            }
        }

        @Synchronized
        override fun d(msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.DEBUG), msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.DEBUG), msg))
            }
        }

        @Synchronized
        override fun w(msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.WARN), msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.WARN), msg))
            }
        }

        @Synchronized
        override fun e(msg: String) {
            if (msg.contains("\n")) {
                println(String.format(getMethodNames(Log.LogLevel.ERROR), msg.replace("\n".toRegex(), "\n║ ")))
            } else {
                println(String.format(getMethodNames(Log.LogLevel.ERROR), msg))
            }
        }

        @Synchronized
        override fun printErrStackTrace(tr: Throwable, format: String) {
            var log: String? = format
            if (log == null) {
                log = ""
            }
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            tr.printStackTrace(pw)
            log += "  $sw"
            println(String.format("[ERROR][%s]%s", log))
        }
    }

    var impl: LogImp = debugLog

    var isOpenLog: Boolean = true
    var logLevel: LogLevel = LogLevel.DEBUG

    fun setLogLevel(level: Int) {
        logLevel = when (level) {
            0 -> Log.LogLevel.ERROR
            1 -> Log.LogLevel.WARN
            2 -> Log.LogLevel.INFO
            3 -> Log.LogLevel.DEBUG
            else -> Log.LogLevel.INFO
        }
    }

    @JvmStatic
    fun e(msg: String) {
        if (isOpenLog && LogLevel.ERROR.value <= logLevel.value) {
            impl.e(msg)
        }
    }

    @JvmStatic
    fun w(msg: String) {
        if (isOpenLog && LogLevel.WARN.value <= logLevel.value) {
            impl.w(msg)
        }
    }

    @JvmStatic
    fun i(msg: String) {
        if (isOpenLog && LogLevel.INFO.value <= logLevel.value) {
            impl.i(msg)
        }
    }

    @JvmStatic
    fun d(msg: String) {
        if (isOpenLog && LogLevel.DEBUG.value <= logLevel.value) {
            impl.d(msg)
        }
    }

    @JvmStatic
    fun printErrStackTrace(tag: String, tr: Throwable, format: String) {
        impl.printErrStackTrace(tr, format)
    }

    private fun getMethodNames(logLevel: LogLevel): String {
        return if (Thread.currentThread().stackTrace.size >= 5) {
            val stack = Thread.currentThread().stackTrace[5]
            val split = stack.className.split('.')
            StringBuilder().append(TOP_BORDER).append(BR)
                    .append("║ ").append("[" + logLevel.name + "][" + split[split.size - 1] + "]%s").append(BR)
                    .append(BOTTOM_BORDER).toString()
        } else {
            StringBuilder().append(TOP_BORDER).append(BR)
                    .append("║ ").append("[" + logLevel.name + "]%s").append(BR)
                    .append(BOTTOM_BORDER).toString()
        }
    }


    interface LogImp {

        fun i(msg: String)

        fun w(msg: String)

        fun d(msg: String)

        fun e(msg: String)

        fun printErrStackTrace(tr: Throwable, format: String)

    }

    enum class LogLevel {
        ERROR {
            override val value: Int
                get() = 0
        },
        WARN {
            override val value: Int
                get() = 1
        },
        INFO {
            override val value: Int
                get() = 2
        },
        DEBUG {
            override val value: Int
                get() = 3
        };

        abstract val value: Int
    }
}
