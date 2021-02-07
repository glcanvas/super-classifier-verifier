package com.networks.algorithms

import com.netsworks.algorithms.isOrderIndependent
import com.netsworks.pojos.*
import org.junit.Test


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

class OrderIndependentTest {


    private val exactEntryFunction = { keys: List<Pair<MatcherBuilder, MatcherBuilder>> ->
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

    @Test
    fun exactIndependentTest_1() {
        val keys = listOf(Pair(ExactMatcher("0x1"), ExactMatcher("0x1")))
        org.junit.Assert.assertTrue(isOrderIndependent(exactEntryFunction(keys)))
    }

    @Test
    fun exactIndependentTest_2() {
        val keys = listOf(
                Pair(ExactMatcher("0x1"), ExactMatcher("0x1")),
                Pair(ExactMatcher("0x2"), ExactMatcher("0x1")))
        org.junit.Assert.assertTrue(isOrderIndependent(exactEntryFunction(keys)))
    }

    @Test
    fun exactIndependentTest_3() {
        val keys = listOf(
                Pair(ExactMatcher("0x1"), ExactMatcher("0x1")),
                Pair(ExactMatcher("0x2"), ExactMatcher("0x2")))
        org.junit.Assert.assertTrue(isOrderIndependent(exactEntryFunction(keys)))
    }

    @Test
    fun exactDependentTest_1() {
        val keys = listOf(
                Pair(ExactMatcher("0x1"), ExactMatcher("0x1")),
                Pair(ExactMatcher("0x1"), ExactMatcher("0x1")))
        org.junit.Assert.assertFalse(isOrderIndependent(exactEntryFunction(keys)))
    }

    @Test
    fun ternaryIndependentTest_1() {
        val keys = listOf(
                Pair(TernaryMatcher("0x1", "0xf"), ExactMatcher("0x1")),
                Pair(TernaryMatcher("0x1", "0x0"), ExactMatcher("0x0"))
        )
        org.junit.Assert.assertTrue(isOrderIndependent(exactEntryFunction(keys)))
    }

    @Test
    fun ternaryDependentTest_1() {
        val keys = listOf(
                Pair(TernaryMatcher("0x1", "0xf"), ExactMatcher("0x1")),
                Pair(TernaryMatcher("0x1", "0x0"), ExactMatcher("0x1"))
        )
        org.junit.Assert.assertFalse(isOrderIndependent(exactEntryFunction(keys)))
    }
}