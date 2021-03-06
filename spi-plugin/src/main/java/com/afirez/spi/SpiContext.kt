package com.afirez.spi

import com.knight.transform.BaseContext
import com.knight.transform.BaseExtension
import org.gradle.api.Project

/**
 * https://github.com/afirez/spi
 */
class SpiContext(project: Project, extension: BaseExtension) : BaseContext<BaseExtension>(project, extension) {

    val extensionsMap = HashMap<String, HashMap<String, String>>()

}