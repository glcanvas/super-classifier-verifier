package com.networks.algorithms

import com.netsworks.algorithms.isOrderIndependent
import com.netsworks.pojos.*
import org.junit.Test



class OrderIndependentTest: BaseSuit() {

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