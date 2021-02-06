package com.netsworks.processors

import com.beust.klaxon.*
import com.netsworks.pojos.Table

/**
 * Read P4 json's file and create table
 */
class P4Parser private constructor() {
    companion object {

        fun fillTable(path: String): Table {
            val node = Parser.default().parse(path) as JsonObject
            val tables = (node["pipelines"] as JsonArray<*>)["tables"] as JsonArray<*>
            val table = (tables[0] as JsonArray<*>)[0] as JsonObject
            return Klaxon().parse<Table>(table.toJsonString(prettyPrint = true))!!
        }
    }
}