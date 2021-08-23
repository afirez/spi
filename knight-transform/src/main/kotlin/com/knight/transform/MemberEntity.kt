package com.knight.transform

open class MemberEntity(var access: Int, val className: String, val name: String, val desc: String, var type: Int = FIELD) {

    companion object {
        const val FIELD = 0
        const val METHOD = 1
    }

}