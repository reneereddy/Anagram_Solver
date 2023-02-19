import java.io.File

private const val NUM_LETTERS_IN_ALPHABET = 26

/**
 * The minimum size of words to include in anagrams.
 */
const val MINIMUM_WORD_SIZE = 3

fun main() {
    runTests()
    provideAnagrams()
}

// Extension functions
/**
 * Converts this alphabetic position to the corresponding
 * lowercase letter in the English alphabet. For example, 0
 * would yield 'a', and 25 would yield 'z'. If this integer
 * is not in the range 0-25 (inclusive), an [IllegalArgumentException]
 * is thrown.
 */
fun Int.toLowerCaseLetter(): Char {
    require(this in 0.until(NUM_LETTERS_IN_ALPHABET))
    return (this + 'a'.code).toChar()
}

/**
 * Converts this character to its 0-based position in the alphabet.
 * For example, 'A' (and 'a') would become 0, and 'Z' (and 'z')
 * would become 25. An [IllegalArgumentException] is thrown
 * if this character is not a letter in the English alphabet.
 */
fun Char.toAlphabeticPosition(): Int {
    require(this.isLetter())
    return this.lowercaseChar() - 'a'
}

/**
 * Returns the number of times [char] appears in this string,
 * using case-insensitive matching.
 */
fun String.countChar(char: Char): Int {
    var counter = 0
    val word = this.lowercase()
    val character = char.lowercaseChar()
    for (c in word) {
        if (c == character)
            counter++
    }

    return counter
}

/**
 * An inventory (count) of the number of times each letter in the English
 * alphabet appears in the provided sourceString. All other characters
 * are ignored.
 */
class LetterInventory(sourceString: String) {
    private val counts: Array<Int> = Array(NUM_LETTERS_IN_ALPHABET) { index ->
        // For each index in the range 0-25, this adds to the array the
        // number of times the corresponding letter appears in string.
        sourceString.countChar(index.toLowerCaseLetter())
    }

    /**
     * Gets the number of times [letter] appears in this inventory.
     * An [IllegalArgumentException] is thrown if [letter] is not a letter.
     */
    operator fun get(letter: Char): Int {
        require(letter.isLetter()) {
            "Non-letter '$letter' was passed to LetterInventory.get()"
        }
        return counts[letter.toAlphabeticPosition()]
    }

    /**
     * Sets the number of times [letter] appears in this inventory to
     * [value]. An [IllegalArgumentException] is thrown if [letter] is not a
     * letter.
     */
    operator fun set(letter: Char, value: Int) {
        assertEquals(true, letter.isLetter())
        val lowerLetter = letter.toAlphabeticPosition()
        counts[lowerLetter] = value
    }

    /**
     * Returns the total number of letters in this inventory. For example,
     * if this inventory contains the letter 'z' once and the letter 'o' twice, the
     * result would be 3.
     */
    fun size(): Int {
        var count = 0
        for (i in counts) {
            count += i
        }
        return count
    }

    /**
     * Returns whether this inventory is empty (contains no letters).
     */
    fun isEmpty(): Boolean {
        // When implementing this, do not duplicate any work that you do in
        // size(), which you may call.
        return this.size() == 0
    }

    // This returns a string of all the letters in alphabetical order,
    // for example, "aaace". Note that we generally do not write KDoc when
    // overriding a method. KDoc can be inherited, just like code.
    override fun toString(): String {
        val list: List<String> = counts.indices.mapNotNull { index ->
            if (counts[index] != 0) {
                index.toLowerCaseLetter().toString().repeat(counts[index])
            } else {
                null
            }
        }
        return list.joinToString(separator = "")
    }

    /**
     * Adds this inventory to [other], producing a new instance of
     * [LetterInventory]. For example, if this inventory has 3 copies of 'a'
     * and [other] has 2, the resulting inventory would have 5 copies of 'a'.
     */
    operator fun plus(other: LetterInventory): LetterInventory {
        return LetterInventory(other.toString() + this.toString())
    }

    /**
     * Subtracts [other] from this inventor, producing a new instance of
     * [LetterInventory], or `null` if subtraction is impossible. Subtraction
     * works on a per-letter basis. For example, if this inventory has 3 copies
     * of the letter 'a' and [other] has 2 copies of the letter 'a', the resulting
     * inventory would have 1 copy of the letter 'a'. If [other] has more copies
     * of any letter than this inventory, the result will be `null`.
     */
    operator fun minus(other: LetterInventory): LetterInventory? {
        val result: LetterInventory = LetterInventory(this.toString())
        for (i in 0 until NUM_LETTERS_IN_ALPHABET) {
            val index = i.toLowerCaseLetter()
            if (other[index] > result[index])
                return null
            result[index] -= other[index]
        }
        return result
    }
}

/**
 * A solver for finding anagrams built from words in [wordList].
 */
class AnagramSolver(val wordList: List<String>, val minLength: Int = MINIMUM_WORD_SIZE) {
    // This map is populated during construction in the init clause.
    private val inventories = mutableMapOf<String, LetterInventory>()

    // This list is cleared in getAnagrams(), populated in findAnagrams(),
    // and returned by getAnagrams(). You will write the code to populate it.
    private val anagrams = mutableListOf<String>()

    init {
        // Precompute the inventories of all the words in our list.
        // That way, each is constructed only once.
        for (word in wordList) {
            if (word.length >= minLength) {
                inventories[word] = LetterInventory(word)
            }
        }
    }

    // Finds all anagrams of s in our wordList and adds them to the
    // property anagrams. There is no KDoc because this method is private.
    // If you prefer, you can change the first argument from String to
    // LetterInventory. You know that a search for anagrams is complete
    // when there are no more unmatched letters. The list named solution
    // contains the words that have been tentatively matched. For example,
    // if the search term was "hello world" and the word "droll" was matched,
    // solution would be set to ["droll"] and unmatchedLetters would be set to
    // "ehlow".
    private fun findAnagrams(unmatchedLetters: LetterInventory, solution: MutableList<String> = mutableListOf()) {
        if (unmatchedLetters.isEmpty()) {
            anagrams.add(solution.joinToString(" "))
            return
        }

        for (word in wordList) {
            val wordInventory = inventories[word]
            if (wordInventory != null) {
                val remainder = unmatchedLetters - wordInventory
                if (remainder != null) {
                    solution.add(word)
                    findAnagrams(remainder, solution)
                    solution.remove(word)
                }
            }
        }

    }

    /**
     * Gets all the [anagrams] of [string] that can be made using
     * the words in [wordList].
     */
    @Synchronized
    fun getAnagrams(string: LetterInventory): List<String> {
        anagrams.clear()
        findAnagrams(string)
        return anagrams
    }
}

fun provideAnagrams() {
    // Use "dictionary-small.txt", "dictionary-medium.txt", or "dictionary-large.txt".
    val wordList = File("src/main/kotlin/homework7/dictionary-medium.txt").readLines()
    val solver = AnagramSolver(wordList)
    while (true) {
        print("Enter text to anagram (or 'exit' to exit): ")
        val input = "Northeastern" //readln()
        if (input == "exit") return // copied from canvas
        val results = solver.getAnagrams(LetterInventory(input))
        if (results.isEmpty()) {
            println("No anagrams were found for '$input'.")
        } else {
            val text = if (results.size == 1)
                "One anagram of '$input' was found:"
            else
                "${results.size} anagrams of '${input}' were found:"
            println(text)
            for (result in results) {
                println(result)
            }
        }
        println()
    }
}
