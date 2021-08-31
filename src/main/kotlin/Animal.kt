
import Field.Companion.BUSH
import Field.Companion.EDGE
import Field.Companion.STAY

open class Animal(
  field: Field,
  type:Int,
  x:Int = 0,
  y:Int = 0,
  id:Int,
): Fieldable(field,type,x,y,id) {

  protected var currentDirection: Int = 0

  // ------------------------------------------------------------------------------------------------------------------
  open fun decideMove(): Int {
    return STAY;
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun setCurrDirection(direction: Int): Int {
    currentDirection = direction
    return direction
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirection(dx: Int): Int {
    return super.nextDirection(currentDirection, dx)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirectionCW(): Int {
    return super.nextDirectionCW(currentDirection)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun nextDirectionCCW(): Int {
    return super.nextDirectionCCW(currentDirection)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun directionsForward(): Array<Int> {
    return Field.directionsForward(currentDirection)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun directionsLost(): Array<Int> {
    return Field.directionsLost(currentDirection)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun canMove(direction: Int): Boolean {
    if (direction == STAY) {
      return true
    }

    val dist = distanceToObject(direction)
    if (dist < 0 || dist > 1) {
      return true
    }

    val type = getObjectInDirection(direction)

    // TODO: should FOX and RABBIT be false?
    return when(type) {
      EDGE, BUSH -> false
      else -> true
    }
  }


  // ------------------------------------------------------------------------------------------------------------------
  override fun typename(): String {
    return "Animal"
  }
}
