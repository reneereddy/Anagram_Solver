// Tests for this assignment
fun runTests() {
    testAlphabeticPosition()
    testCountChar()
    testGetSet()
    testLetterInventory()
    testLetterInventoryAdd()
    testLetterInventorySubtract()
    testAnagramSolver()
    println("All tests pass")
}

fun testAlphabeticPosition() {
    assertEquals(0, 'a'.toAlphabeticPosition())
    assertEquals(0, 'A'.toAlphabeticPosition())
    assertEquals(12, 'm'.toAlphabeticPosition())
    assertEquals(25, 'Z'.toAlphabeticPosition())
}

fun testCountChar() {
    assertEquals(0, "Hello!".countChar('x'))
    assertEquals(1, "Hello!".countChar('h'))
    assertEquals(2, "Hello!".countChar('l'))
    assertEquals(1, "Hello!".countChar('O'))
    assertEquals(1, "Hello!".countChar('!'))
}

fun testGetSet() {
    val tester = LetterInventory("mississippi")
    assertEquals(4, tester['i'])
    tester['i'] = 5
    assertEquals(5, tester['i'])
    assertEquals(0, tester['l'])
    tester['l'] = 1
    assertEquals(1, tester['l'])
    assertEquals(1, tester['m'])
    tester['m'] = 2
    assertEquals(2, tester['m'])
    assertEquals(2, tester['p'])
    tester['p'] = 3
    assertEquals(3, tester['p'])
}

fun testLetterInventory() {
    val northInventory = LetterInventory("NoRTh")
    assertEquals(0, northInventory['a']) // letter does not appear
    assertEquals(1, northInventory['h']) // letter appears once
    assertEquals(1, northInventory['N']) // letter was capital in constructor
    assertEquals(5, northInventory.size())
    assertEquals("hnort", northInventory.toString())
    assertFalse(northInventory.isEmpty())

    val easternInventory = LetterInventory("eaSTErn")
    assertEquals(2, easternInventory['e']) // letter appears twice
    assertEquals("aeenrst", easternInventory.toString())
    assertEquals(7, easternInventory.size())
    assertFalse(easternInventory.isEmpty())

    val emptyInventory = LetterInventory("")
    assertEquals(0, emptyInventory.size())
    assertTrue(emptyInventory.isEmpty())
}

fun testLetterInventoryAdd() {
    val northInventory = LetterInventory("NoRTh")
    val easternInventory = LetterInventory("eaSTErn")

    val northeasternInventory = northInventory + easternInventory
    assertEquals(2, northeasternInventory['n'])
    assertEquals("aeehnnorrstt", northeasternInventory.toString())
    assertFalse(easternInventory.isEmpty())
}

fun testLetterInventorySubtract() {
    val northInventory = LetterInventory("NoRTh")
    val easternInventory = LetterInventory("eaSTErn")
    val notInventory = LetterInventory("not")

    // These subtractions cannot be done.
    assertNull(northInventory - easternInventory)
    assertNull(easternInventory - northInventory)

    // This subtraction can be done.
    val diff = northInventory - notInventory
    assertNotNull(diff)
    assertEquals("hr", diff?.toString())
}


fun testAnagramSolver() {
    val solver = AnagramSolver(listOf("dirt", "dirty", "room", "roomy"))
    val results = solver.getAnagrams(LetterInventory("Dormitory"))
    assertEquals(4, results.size)
    assertTrue(results.contains("dirty room"))
    assertTrue(results.contains("room dirty"))
    assertTrue(results.contains("dirt roomy"))
    assertTrue(results.contains("roomy dirt"))
}

// General test functions

/**
 * Asserts that the [expected] value is equal to the [actual] value.
 */
fun assertEquals(expected: Any?, actual: Any?) {
    if (expected != actual) {
        throw IllegalArgumentException("Expected <$expected>, actual <$actual>")
    }
}

/**
 * Asserts that the [actual] value is `true`.
 */
fun assertTrue(actual: Boolean) {
    if (!actual) {
        throw java.lang.IllegalArgumentException("Expected <true>, actual <false>")
    }
}

/**
 * Asserts that the [actual] value is `false`.
 */
fun assertFalse(actual: Boolean) {
    if (actual) {
        throw java.lang.IllegalArgumentException("Expected <false>, actual <true>")
    }
}

/**
 * Asserts that the [actual] value is not `null`.
 */
fun assertNotNull(actual: Any?) {
    if (actual == null) {
        throw java.lang.IllegalArgumentException("Unexpected null value")
    }
}

/**
 * Asserts that the [actual] value is `null`.
 */
fun assertNull(actual: Any?) {
    if (actual != null) {
        throw java.lang.IllegalArgumentException("Unexpected non-null value")
    }
}