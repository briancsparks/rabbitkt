import C.Companion.FIELD_HEIGHT
import C.Companion.FIELD_WIDTH
import Field.Companion.BUSH
import Field.Companion.EDGE
import Field.Companion.EMPTY
import Field.Companion.FOX
import Field.Companion.RABBIT
import U.Companion.check_input_is_bad
import U.Companion.not_possible
import java.util.*
import kotlin.math.abs
import kotlin.math.max

// ====================================================================================================================
class Field(
  private val width: Int = FIELD_WIDTH,
  private val height: Int = FIELD_HEIGHT,
) {

  var rabbit: Rabbit = Rabbit(this)
  var fox: Fox = Fox(this)

  var rabbitIsAlive: Boolean = true
  var rabbitWins: Boolean = false

  val maxDim = max(width, height)

  var stepNum: Int = 0
  var seed: Long = randomNumberGenerator.nextLong()

  private val field = Array(height) { row ->
    Array(width) { col ->
      Fieldable(this, -1, col, row)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  init {

    if (System.getenv("RANDOM_SEED") != null) {
      seed = System.getenv("RANDOM_SEED").toLong()
    }

    if (System.getenv("REPORT_SEED") != null) {
      // seed = System.getenv("REPORT_SEED") != "0"
      println("Seed: $seed")
    }

    randomNumberGenerator.setSeed(seed)
    populate()
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun populate(): Unit {

    // initialize
    for (x in 0 until width) {
      for (y in 0 until height) {
        putAt(x, y, Empty(this, x, y))
      }
    }

    // rabbit
    putAt(random(width), random(height), rabbit)

    // fox
    var distance: Int
    var foxX: Int
    var foxY: Int
    do {
      foxX = random(width)
      foxY = random(height)
      distance = abs(foxY - rabbit.location.x).coerceAtLeast(abs(foxX - rabbit.location.y))
    } while (distance >= ((height + width) / 4))
    putAt(foxX, foxY, fox)

    // bushes
    var numBushesToPlace = (width * height) / 20
    while (numBushesToPlace > 0) {
      val bushX = random(width)
      val bushY = random(height)
      if (at(bushX, bushY)!!.type == EMPTY) {
        putAt(bushX, bushY, Bush(this, bushX, bushY))
        numBushesToPlace -= 1
      }
    }

//    println(fieldString("populate"))
  }

  // ------------------------------------------------------------------------------------------------------------------
  public fun runIt(
    numSteps: Int = C.NUM_STEPS
  ) {

    for (n in IntRange(0, numSteps)) {
      stepNum = n
      if (!stepRabbit()) {
        break;
      }

      if (!stepEnemies()) {
        break;
      }
    }

    // Who won?
    if (rabbitIsAlive) {
      rabbitWins = true
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun stepRabbit(): Boolean {
    // TODO: Force rabbit to have location that is stored in Field, not their own Location - no cheating

    val dir = rabbit.decideMove()

    // TODO: See if rabbit committed suicide

    return true;  // true === should continue
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun stepEnemies(): Boolean {
    // TODO: Force fox to have location that is stored in Field, not their own Location - no cheating

    val dir = fox.decideMove()
    val newX = fox.location.x + dx_(dir)
    val newY = fox.location.y + dy_(dir)

    // Is this the rabbits location? (Did the enemy win?)
    if (newX == rabbit.location.x && newY == rabbit.location.y) {
      rabbitIsAlive = false
      return false
    }

    moveTo(newX, newY, fox)


    // TODO: See if game is over

    return true;  // true === should continue
  }



  // ------------------------------------------------------------------------------------------------------------------
  fun getObjectInDirection(location: Location, n: Int):Int {
    val dx = dx_(n)
    val dy = dy_(n)

    var x = location.x
    var y = location.y

    for (distance in 1..maxDim) {
      x += dx
      y += dy

      if (!isLegal(x, y)) {
        return EDGE
      }

      val fieldable = at(x,y)
      if (fieldable != null) {
        if (isSomething(fieldable)) {
          return fieldable.type
        }
      }
    }

    not_possible("getObjectInDirection::NONE_FOUND")
    return EDGE
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun distanceToObject(location: Location, n: Int):Int {
    val dx = dx_(n)
    val dy = dy_(n)

    var x = location.x
    var y = location.y

    for (distance in 1..maxDim) {
      x += dx
      y += dy

      if (isSomething(x,y)) {
        return distance
      }
    }

    return maxDim
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun isLegal(x:Int, y:Int): Boolean {
    return (x in 0 until width) && (y in 0 until height)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun isLegal(location: Location): Boolean {
    return (location.x in 0 until width) && (location.y in 0 until height)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun isSomething(x:Int, y:Int): Boolean {
    val fieldable = at(x,y) ?: return true

    return isSomething(fieldable)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun isSomething(fieldable: Fieldable): Boolean {

    return when (fieldable.type) {
      RABBIT -> true
      ENEMY -> true
      BUSH -> true
      EDGE -> true

      else -> false
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun at(x:Int, y:Int): Fieldable? {
    if (x < 0 || y < 0) {
      return null
    }
    if (x >= width || y >= height) {
      return null
    }

    return field[y][x]
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun putAt(x: Int, y: Int, f:Fieldable): Fieldable {
    f.location = Location(x, y)
    field[y][x] = f
    return at(x, y)!!
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun moveTo(x: Int, y: Int, f:Fieldable): Fieldable {
    val origLocation = f.location
    f.location = Location(x, y)
    field[y][x] = f
    field[origLocation.y][origLocation.x] = Empty(this, origLocation.x, origLocation.y)
    return at(x, y)!!
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun fieldString(msg:String =""): String {
    var result: String = ""

    for (x in 0 until width) {
      result += "-"
    }
    result += "  ${msg}\n"

    for (y in 0 until height) {
      for (x in 0 until width) {
        result += field[y][x]
      }
      result += "| ${y}\n"
    }

    for (x in 0 until width) {
      result += "-"
    }
    result += "\n"

    return result
  }

  // ------------------------------------------------------------------------------------------------------------------
  companion object {
    const val N     = 0
    const val NE    = 1
    const val E     = 2
    const val SE    = 3
    const val S     = 4
    const val SW    = 5
    const val W     = 6
    const val NW    = 7
    const val STAY  = 8

    const val EMPTY   = -1
    const val EDGE    = 0
    const val RABBIT  = 1
    const val ENEMY   = 2
    const val FOX     = ENEMY
    const val BUSH    = 3

    private val randomNumberGenerator = Random()

    // ----------------------------------------------------------------------------------------------------------------
    fun random(min: Int, max: Int): Int {
      return randomNumberGenerator.nextInt(max - min + 1) + min
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun random(end: Int): Int {
      return random(0, end-1)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun dx_(dir: Int): Int = when(dir) {
      N  ->  0
      NE ->  1
      E  ->  1
      SE ->  1
      S  ->  0
      SW -> -1
      W  -> -1
      NW -> -1
      else -> 0
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun dy_(dir: Int): Int = when(dir) {
      N  -> -1
      NE -> -1
      E  ->  0
      SE ->  1
      S  ->  1
      SW ->  1
      W  ->  0
      NW -> -1
      else -> 0
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun directionsCW(currDir: Int): Array<Int> {
      val clockwise = arrayOf(0,1,2,3,4,5,6,7)
      return directions(currDir, clockwise)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun directionsForward(currDir: Int): Array<Int> {
      val clockwise = arrayOf(0,1,7,2,6,3,5,4)
      return directions(currDir, clockwise)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun directionsLost(currDir: Int): Array<Int> {
      val clockwise = arrayOf(0,2,4,6,1,3,5,7)
      return directions(currDir, clockwise)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun directions(currDir: Int, deltas: Array<Int>): Array<Int> {
      return arrayOf(
        (currDir + deltas[0]) % 8,
        (currDir + deltas[1]) % 8,
        (currDir + deltas[2]) % 8,
        (currDir + deltas[3]) % 8,
        (currDir + deltas[4]) % 8,
        (currDir + deltas[5]) % 8,
        (currDir + deltas[6]) % 8,
        (currDir + deltas[7]) % 8,
      )
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun nextDirection(x: Int): Int {
      return nextDirectionCW(x)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun nextDirectionCW(x: Int): Int {
      return normalize(x, 1)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun nextDirectionCCW(x: Int): Int {
      return normalize(x, -1)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun nextDirection(x: Int, dx: Int): Int {
      return normalize(x, dx)
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun normalize(x: Int, dx: Int): Int {
      check_input_is_bad(dx < -7, "dx < -7")
      return (x + dx + 16) % 8
    }

  }
}

// ====================================================================================================================
open class Fieldable(
  val field: Field,
  val type: Int,
  var x:Int,
  var y:Int,
) {
  var location: Location = Location(x,y)

  // ------------------------------------------------------------------------------------------------------------------
  fun getObjectInDirection(n: Int): Int {
    return field.getObjectInDirection(location, n)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun distanceToObject(n: Int): Int {
    return field.distanceToObject(location, n)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirection(x: Int, dx: Int): Int {
    return Field.nextDirection(x, dx)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirectionCW(x: Int): Int {
    return Field.nextDirectionCW(x)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirectionCCW(x: Int): Int {
    return Field.nextDirectionCCW(x)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun toString(): String {
    return when(type) {
      EMPTY -> " "
      EDGE -> " "
      RABBIT -> "*"
      FOX -> "^"
      BUSH -> "#"

      else -> "-"
    }
  }
}

// ====================================================================================================================
class Empty(
  field: Field,
  x:Int = 0,
  y:Int = 0,
): Fieldable(field, EMPTY, x,y) {
}

// ====================================================================================================================
class Bush(
  field: Field,
  x:Int = 0,
  y:Int = 0,
): Fieldable(field, BUSH, x,y) {
}

