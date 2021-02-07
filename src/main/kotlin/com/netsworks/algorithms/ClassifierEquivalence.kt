package com.netsworks.algorithms

import com.netsworks.pojos.Entry
import com.netsworks.pojos.MatchKey
import com.netsworks.pojos.Table
import java.lang.RuntimeException

private const val NUMBER_SIZE = 32

/**
 * Base function, which check that two classifiers equivalence
 * Note: both classifiers must (should be, in current implementation doesn't matter) order independent
 */
fun isClassifiersEquivalence(t1: Table, t2: Table): Boolean {
    for (e in t1.entries) {
        if (!isCover(e, t2.entries)) {
            return false
        }
    }
    for (e in t2.entries) {
        if (!isCover(e, t1.entries)) {
            return false
        }
    }
    return true
}

/**
 * iterate over k2, cross k1 and each entry from k2, try to merge it back and verify that its entry from k1
 */
fun isCover(k1: Entry, k2: List<Entry>): Boolean {
    var resHolders = crossMatchers(k1.matchKeys, k2[0].matchKeys)

    for (i in 1 until k2.size) {
        val currentHolders = crossMatchers(k1.matchKeys, k2[i].matchKeys)
        val tmpHolders = ArrayList<CrossHolder>()
        for (j in resHolders.indices) {
            tmpHolders.add(mergeHolders(resHolders[j], currentHolders[j]))
        }
        resHolders = tmpHolders
    }

    return isFullyCover(k1.matchKeys, resHolders)
}

/**
 * Cross matchers by name from key
 */
fun crossMatchers(m1: List<MatchKey>, m2: List<MatchKey>): List<CrossHolder> {
    // let assume that both keys equals
    if (m1.size != m2.size) {
        throw RuntimeException("Classifiers bad (unsupported yet)")
    }
    return m1.indices.map { i -> crossMatch(KeyRange.build(m1[i]), KeyRange.build(m2[i])) }
}

fun crossMatch(kr1: KeyRange, kr2: KeyRange): CrossHolder {
    // which bytes are equal

    val result = CrossHolder(NUMBER_SIZE)
    for (i in 0 until NUMBER_SIZE) {
        val k1Mask = KeyRange.getBit(kr1.mask, i)
        val k1Key = KeyRange.getBit(kr1.key, i)

        val k2Mask = KeyRange.getBit(kr2.mask, i)
        val k2Key = KeyRange.getBit(kr2.key, i)

        if (k1Mask == 1 && k1Mask == k2Mask) {
            if (k1Key == k2Key) {
                // genius
                result[i, Bit.fromInt(k1Key)] = Mask.ONE
            }
            // otherwise any zero (by default)
        } else if (k1Mask == 1 && k1Mask != k2Mask) {
            // bit from first key anyway will be accepted by second key
            result[i, Bit.ANY] = Mask.ONE
        } else if (k1Mask == 0 && k1Mask != k2Mask) {
            // non any bit from first will be accepted, will be accepted only passed (i.e. bit from second key)
            result[i, Bit.fromInt(k1Key)] = Mask.HALF
        } else {
            // both bits don't matte, means any bit from first will be accepted dy second
            result[i, Bit.ANY] = Mask.ONE
        }
    }
    return result
}

fun mergeHolders(c1: CrossHolder, c2: CrossHolder): CrossHolder {
    val result = CrossHolder(NUMBER_SIZE)
    for (i in 0 until NUMBER_SIZE) {
        when (Pair(c1.getMask(i), c2.getMask(i))) {
            Pair(Mask.ZERO, Mask.ZERO) -> result[i, Bit.ANY] = Mask.ZERO
            Pair(Mask.ZERO, Mask.ONE) -> result[i, c2.getBit(i)] = Mask.ONE
            Pair(Mask.ZERO, Mask.HALF) -> result[i, c2.getBit(i)] = Mask.HALF

            Pair(Mask.HALF, Mask.ZERO) -> result[i, c1.getBit(i)] = Mask.HALF
            Pair(Mask.HALF, Mask.ONE) -> result[i, c2.getBit(i)] = Mask.ONE
            Pair(Mask.HALF, Mask.HALF) -> {
                if (c2.getBit(i) == c1.getBit(i)) {
                    result[i, c2.getBit(i)] = Mask.HALF
                } else {
                    result[i, Bit.ANY] = Mask.ONE
                }
            }

            Pair(Mask.ONE, Mask.ZERO) -> result[i, c1.getBit(i)] = Mask.ONE
            Pair(Mask.ONE, Mask.ONE) -> result[i, c1.getBit(i)] = Mask.ONE
            Pair(Mask.ONE, Mask.HALF) -> result[i, c1.getBit(i)] = Mask.ONE
            else -> throw RuntimeException("unsupported masks pair")
        }
    }
    return result
}

fun isFullyCover(mk: List<MatchKey>, ch: List<CrossHolder>): Boolean {
    for (i in mk.indices) {
        if (!isFullyCover(KeyRange.build(mk[i]), ch[i])) {
            return false
        }
    }
    return true
}

fun isFullyCover(kr: KeyRange, ch: CrossHolder): Boolean {
    for (i in 0 until NUMBER_SIZE) {
        val m = KeyRange.getBit(kr.mask, i)
        val k = KeyRange.getBit(kr.key, i)
        val mm = ch.getMask(i)
        val mk = ch.getBit(i)

        if (m == 1 && mm == Mask.ONE) {
            // if not any and bits difference then bad
            if (mk != Bit.ANY && (k == 1 && (mk != Bit.ONE)) && (k == 0 && (mk != Bit.ZERO))) {
                return false
            }
            // otherwise skip path bellow, all is ok
            continue
        }
        if (m == 0 && mm == Mask.ZERO) {
            // ok skip it
            continue
        }
        if (mm == Mask.HALF) {
            // cover only some part of cases
            return false
        }
        if (m == 0 && mk == Bit.ANY) {
            // I think it's ok lets allow
            continue
        }
        // otherwise bits are different
        return false
    }
    return true
}


enum class Bit {
    ZERO,
    ONE,
    ANY;

    companion object {
        fun fromInt(i: Int): Bit {
            return when (i) {
                0 -> ZERO
                1 -> ONE
                else -> throw RuntimeException("Wrong value only 1 or 0")
            }
        }
    }
}

enum class Mask {
    ZERO,
    HALF,
    ONE
}

class CrossHolder(range: Int) {
    private val bitSet = Array(range) { Bit.ANY }
    private val maskSet = Array(range) { Mask.ZERO }

    fun getBit(i: Int) = bitSet[i]
    fun getMask(i: Int) = maskSet[i]

    operator fun set(i: Int, b: Bit, m: Mask) {
        bitSet[i] = b
        maskSet[i] = m
    }

}