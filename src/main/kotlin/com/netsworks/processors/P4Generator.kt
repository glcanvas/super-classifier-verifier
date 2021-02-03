package com.netsworks.processors

import java.io.File
import java.io.FileWriter
import java.io.Writer

/**
 * Use template from resources dir then generate correct P4 compatibility file
 */
class P4Generator private constructor(){
    companion object {
        private const val TEMPLATE_NAME = "/template.p4"


        /**
         * Generate plain p4 file which contains only one header and only one classifier with all fields in.
         */
        fun buildP4Class(fileName: String, fieldsCount: Int, actionsCount: Int) {
            val file = File(fileName)
            // 1
            val headers = IntRange(0, fieldsCount).joinToString(separator = "\n") { "bit<8> header_${it};" }
            // 2
            val defineActions = IntRange(0, actionsCount).joinToString(separator = "\n") { "action operation_${it}() {}" }
            // 3
            val classifierHeaders = IntRange(0, fieldsCount).joinToString(separator = "\n") { "hdr.generated_headers.header_${it}: exact;" }
            // 4
            val actionsDef = IntRange(0, actionsCount).joinToString(separator = "\n") { "operation_${it};" }
            // 5
            val matchActions =
                    IntRange(0, actionsCount).joinToString(separator = "\n") { action ->
                        IntRange(0, fieldsCount).map { action }.joinToString(",", "(", "): operation_${action}();")
                    }
            val template = buildTemplate()
            val formattedTemplate = template.format(headers, defineActions, classifierHeaders, actionsDef, matchActions)
            val fileWriter: Writer = FileWriter(file)
            fileWriter.use {
                it.write(formattedTemplate)
            }
        }

        private fun buildTemplate(): String {
            return P4Generator::class.java.getResource(TEMPLATE_NAME).readText()
        }
    }
}

fun main(args: Array<String>) {
    P4Generator.buildP4Class("ff.p4", 5, 2);
}
