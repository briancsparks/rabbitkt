import Field.Companion.RABBIT
import Field.Companion.STAY

class Rabbit(
  field: Field,
  id:Int,
  x:Int = 0,
  y:Int = 0,
): Animal(field, RABBIT, x, y, id) {

  override fun decideMove(): Int {
    return STAY;
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun typename(): String {
    return "Rabbit"
  }

}
