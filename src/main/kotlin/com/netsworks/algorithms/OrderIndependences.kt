package com.netsworks.algorithms

import com.netsworks.pojos.Entry
import com.netsworks.pojos.Key
import com.netsworks.pojos.MatchKey
import com.netsworks.pojos.Table
import java.lang.RuntimeException


/**
 * Check that Entries don't intersect i.e. here don't exists two entries which match the same packet
 */
fun isOrderIndependent(table: Table): Boolean {
    val entries = table.entries

    for (i in entries.indices) {
        for (j in i + 1 until entries.size) {
            val entry1 = entries[i]
            val entry2 = entries[j]
            val key = table.keys
            if (isIntersect(entry1, entry2, key)) {
                return false
            }
        }
    }
    return true
}

private fun isIntersect(entry1: Entry, entry2: Entry, keys: List<Key>): Boolean {
    assert(entry1.matchKeys.size == entry2.matchKeys.size)
    assert(entry1.matchKeys.size == keys.size)
    var intersectCount = 0
    for (i in entry1.matchKeys.indices) {
        val matchKey1 = entry1.matchKeys[i]
        val matchKey2 = entry2.matchKeys[i]
        val key = keys[i]
        if (isIntersect(matchKey1, matchKey2, key)) {
            intersectCount += 1
        }
    }
    // if two entries intersect on all fields then it's order depended
    if (intersectCount == entry1.matchKeys.size) {
        return true
    }
    return false
}

private fun isIntersect(matchKey1: MatchKey, matchKey2: MatchKey, key: Key): Boolean {
    return when (key.matchType) {
        "exact" -> {
            val key1 = KeyRange.build(matchKey1)
            val key2 = KeyRange.build(matchKey2)
            return KeyRange.intersect(key1, key2)
        }
        "ternary" -> {
            val key1 = KeyRange.build(matchKey1)
            val key2 = KeyRange.build(matchKey2)
            return KeyRange.intersect(key1, key2)
        }
        "lpm" -> {
            throw RuntimeException("lpm Unsupported yet")
        }
        else -> {
            throw RuntimeException("Unrecognized type")
        }
    }
}

data class KeyRange(val key: Int, val mask: Int) {

    companion object {
        private const val INTEGER_MAX_VALUE = "0x7FFFFFFF"

        val getBit = { k: Int, i: Int -> (k shr i) and 1 }
        val setBit = { k: Int, i: Int -> k or (1 shl i) }
        val setValueBit = { k: Int, b: Int, i: Int -> k or (b shl i) }

        /**
         * Check is two key and mask intersect
         * Intersect condition for position i:
         * if both mask on i required and keys on i different then don't intersect
         * if both mask on i optional (0) then may matched
         * otherwise -- may matched
         * not intersect -- at least one first case
         *
         * return: true if intersect false otherwise
         *
         */
        fun intersect(k1: KeyRange, k2: KeyRange): Boolean {
            for (i in 0..31) {
                val v1 = getBit(k1.key, i)
                val m1 = getBit(k1.mask, i)
                val v2 = getBit(k2.key, i)
                val m2 = getBit(k2.mask, i)
                // significant bit
                if (m1 == 1 && m1 == m2) {
                    if (v1 != v2) {
                        return false
                    }
                }
            }
            return true
        }

        fun build(mk: MatchKey): KeyRange {
            return KeyRange(Integer.decode(mk.key), Integer.decode(extendTo(mk.mask ?: INTEGER_MAX_VALUE)))
        }

        private fun extendTo(s: String): String {
            if (s.length == 10) {
                return s
            }
            val res = s.substring(2).padStart(7, 'f').padStart(8, '7')
            return "0x$res"
        }
    }
}