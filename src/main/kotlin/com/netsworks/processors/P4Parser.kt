package com.netsworks.processors

import com.beust.klaxon.*

/**
 * Read P4 json's file and create table
 */
class P4Parser private constructor() {
    companion object {

        fun fillTable(path: String) {
            val node = Parser.default().parse(path) as JsonObject
            val tables = (node["pipelines"] as JsonArray<*>)["tables"] as JsonArray<*>
            val table = (tables[0] as JsonArray<*>)[0] as JsonObject
            val t = Klaxon().parse<Table>(table.toJsonString(prettyPrint = true))
            println(t)
            //println(table.toJsonString(prettyPrint = true))
        }
    }
}

data class Key(
        @Json(name = "match_type") val matchType: String,
        @Json(name = "target") val target: List<String>
)

data class MatchKey(
        @Json(name = "match_type") val matchType: String,
        @Json(name = "key") val key: String,
        @Json(name = "mask") val mask: String? = null
)

data class ActionEntry(@Json(name = "action_id") val actionId: Int)

data class Entry(
        @Json(name = "match_key") val matchKeys: List<MatchKey>,
        @Json(name = "priority") val id: Int,
        @Json(name = "action_entry") val actionEntry: ActionEntry,
)

data class Table(
        @Json(name = "key") val keys: List<Key>,
        @Json(name = "action_ids") val actions: List<Int>,
        @Json(name = "entries") val entries: List<Entry>
)


fun main() {
    P4Parser.fillTable("C:\\Users\\nikita\\IdeaProjects\\super-classifier-verifier\\verifier\\calc.json")
}