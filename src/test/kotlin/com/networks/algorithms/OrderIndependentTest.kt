package com.networks.algorithms

import com.netsworks.algorithms.isOrderIndependent
import com.netsworks.pojos.*
import org.junit.Test

class OrderIndependentTest {


    private val exactKeys = listOf(
            Key("exact", listOf("a")),
            Key("exact", listOf("b")),
    )

    private val exactEntry1 = { i: Int ->
        Entry(
                listOf(
                        MatchKey(
                                "exact",
                                "0x1"
                        ),
                        MatchKey(
                                "exact",
                                "0x1"
                        )
                ),
                i,
                ActionEntry(i)
        )
    }
    private val primeActions = listOf(0, 1, 2)
    private val exactEntries1 = listOf(exactEntry1(0))
    private val table1 = Table(exactKeys, primeActions, exactEntries1)


    private val exactEntries2 = listOf(exactEntry1(0), exactEntry1(1))
    private val table2 = Table(exactKeys, primeActions, exactEntries2)

    @Test
    fun independentTest_1() {
        org.junit.Assert.assertTrue(isOrderIndependent(table1))
    }

    @Test
    fun dependentTest_1() {
        org.junit.Assert.assertFalse(isOrderIndependent(table2))
    }
}