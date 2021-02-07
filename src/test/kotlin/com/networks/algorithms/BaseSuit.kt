package com.networks.algorithms

import com.netsworks.pojos.*

sealed class MatcherBuilder {

    fun extract(): MatchKey {
        return when (this) {
            is ExactMatcher -> MatchKey("exact", this.key)
            is TernaryMatcher -> MatchKey("ternary", this.key, this.mask)
        }
    }

    fun extractType(): String {
        return when (this) {
            is ExactMatcher -> "exact"
            is TernaryMatcher -> "ternary"
        }
    }

}

data class ExactMatcher(val key: String) : MatcherBuilder()
data class TernaryMatcher(val key: String, val mask: String) : MatcherBuilder()

open class BaseSuit {

    protected val exactEntryFunction = { keys: List<Pair<MatcherBuilder, MatcherBuilder>> ->
        val exactKeys = listOf(
                Key(keys[0].first.extractType(), listOf("a")),
                Key(keys[0].second.extractType(), listOf("b")),
        )
        val entries = ArrayList<Entry>()
        for (it in keys.indices) {
            entries.add(
                    Entry(
                            listOf(
                                    keys[it].first.extract(),
                                    keys[it].second.extract(),
                            ),
                            it,
                            ActionEntry(it)
                    )
            )
        }
        val primeActions = listOf(0, 1, 2)
        Table(exactKeys, primeActions, entries)
    }
}