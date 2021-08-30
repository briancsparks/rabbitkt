import Field.Companion.FOX
import Field.Companion.RABBIT
import Field.Companion.STAY
import Field.Companion.directionsCW
import Field.Companion.directionsLost
import U.Companion.should_never_happen

class Fox(
  field: Field,
  x:Int = 0,
  y:Int = 0,
): Animal(field, FOX, x, y) {

//  private var currentDirection: Int = Field.random(8)
  private var distanceToRabbit = 0
  private var directionToRabbit = 0
  private var haveSeenRabbit = false

  override fun decideMove(): Int {

    println(field.fieldString("decide-which-way (${currentDirection})"))

    // Look around for rabbit
    val lookDirs = directionsCW(currentDirection)
    for (direction in lookDirs) {
      if (getObjectInDirection(direction) == RABBIT) {
        directionToRabbit = direction
        haveSeenRabbit = true
        distanceToRabbit = distanceToObject(directionToRabbit) + 1
        break
      }
    }

    // Move toward rabbits last known location, if seen recently
    if (haveSeenRabbit) {
      if (distanceToRabbit > 0) {
        distanceToRabbit -= 1
        return directionToRabbit
      }

      should_never_happen(distanceToRabbit < 0, "Fox::decideMove::distanceToRabbit.NEGATIVE")

      // Ugh. We lost the rabbit
      haveSeenRabbit = false
      currentDirection = Field.random(8)
    }

    // Can we keep going in the current direction?
    val df = directionsLost()
    for (possibleDirection in df) {
      if (canMove(possibleDirection)) {
        return setCurrDirection(possibleDirection)
      }
    }

    return STAY;
  }

}
