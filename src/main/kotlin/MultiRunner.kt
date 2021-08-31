import trusskt.trusses.ConsoleTruss
import trusskt.trusses.HalfTruss
import trusskt.trusses.KafkaTruss
import trusskt.trusses.MainTruss

fun main() {
  val mainTruss = MainTruss("Rabbit::MultiRunner", "")
  val consoleTruss = ConsoleTruss(mainTruss)
  val kafkaTruss = KafkaTruss("rabbitfox2", mainTruss)
  val tr = HalfTruss("MultiRunner::main")

  var numRabbitWins = 0

  val numRuns = 10
  for (n in IntRange(0, numRuns)) {
    tr.ent("game", n).partition()

    val field = Field(n)
    field.runIt()

    if (field.rabbitWins) {
      numRabbitWins += 1
    }

  }

  println("Rabbit wins ${numRabbitWins} / ${numRuns} == ${(numRabbitWins.toDouble() / numRuns.toDouble()) * 100.0}%")

}

