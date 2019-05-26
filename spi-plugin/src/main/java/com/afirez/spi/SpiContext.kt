package com.afirez.spi

import com.knight.transform.BaseContext
import com.knight.transform.BaseExtension
import org.gradle.api.Project

class SpiContext(project: Project, extension: BaseExtension) : BaseContext<BaseExtension>(project, extension) {

    val serviceMap = HashMap<String, HashMap<String, String>>()

}