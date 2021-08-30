
fun main() {
  val field = Field()
  field.runIt()

  if (field.rabbitWins) {
    println("Rabbit Wins!")
  } else {
    println("Fox Wins in ${field.stepNum} steps!")
  }
}


