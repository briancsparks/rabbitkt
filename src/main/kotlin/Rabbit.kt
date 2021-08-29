import Field.Companion.RABBIT
import Field.Companion.STAY

class Rabbit(
  field: Field,
  x:Int = 0,
  y:Int = 0,
): Animal(field, RABBIT, x, y) {

  override fun decideMove(): Int {
    return STAY;
  }

}
