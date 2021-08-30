
fun main() {
  var numRabbitWins = 0

  for (n in IntRange(0, 1000)) {

    val field = Field()
    field.runIt()

    if (field.rabbitWins) {
      numRabbitWins += 1
//      println("Rabbit Wins!")
    } else {
//      println("Fox Wins in ${field.stepNum} steps!")
    }

  }

  println("Rabbit wins ${numRabbitWins} / 1000 == ${(numRabbitWins.toDouble() / 1000.0) * 100.0}%")

}

