package com.networks.algorithms

import com.netsworks.algorithms.isCover
import org.junit.Test

class ClassifiersEquivalenceTest : BaseSuit() {

    @Test
    fun reflCoverTest() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x1"), ExactMatcher("0x1"))
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table1.entries))
    }

    @Test
    fun isCoverTest_1() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x1"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x1", "0xf"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x2", "0xf"), ExactMatcher("0x3")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }


    @Test
    fun isCoverTest_2() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x11"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x10", "0xf0"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x01", "0x0f"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isCoverTest_3() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x11", "0x0f"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x10", "0xf0"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x01", "0x0f"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isCoverTest_4() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x11", "0xff"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x10", "0xf0"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x01", "0x0f"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isCoverTest_5() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x11", "0xff"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x10"), ExactMatcher("0x1")),
                        Pair(ExactMatcher("0x01"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isCoverTest_6() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x11", "0xff"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x10"), ExactMatcher("0x1")),
                        // please note!!!
                        // implementation detail
                        Pair(ExactMatcher("0x03"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertTrue(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isNotCoverTest_1() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x11", "0xff"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x10"), ExactMatcher("0x1")),
                        // well 3 not accepted because 101
                        Pair(ExactMatcher("0x04"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertFalse(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isNotCoverTest_2() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x11"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x10"), ExactMatcher("0x1")),
                        Pair(ExactMatcher("0x02"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertFalse(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isNotCoverTest_3() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(ExactMatcher("0x22"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x10", "0xff"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x01", "0xff"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertFalse(isCover(table1.entries[0], table2.entries))
    }

    @Test
    fun isNotCoverTest_4() {
        val table1 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x22", "0xff"), ExactMatcher("0x1"))
                )
        )

        val table2 = exactEntryFunction(
                listOf(
                        Pair(TernaryMatcher("0x10", "0xff"), ExactMatcher("0x1")),
                        Pair(TernaryMatcher("0x01", "0xff"), ExactMatcher("0x1")),
                )
        )
        org.junit.Assert.assertFalse(isCover(table1.entries[0], table2.entries))
    }
}