package com.netsworks.pojos

import com.beust.klaxon.Json

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