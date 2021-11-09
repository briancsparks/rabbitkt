import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import net.cdr0.trusskt.trusses.ConsoleTruss
import net.cdr0.trusskt.trusses.HalfTruss
import net.cdr0.trusskt.trusses.MainTruss

//import trusskt.trusses.*

fun main() = runBlocking<Unit> {

//  val payloadJob = Job()
//  val payloadScope = CoroutineScope(Dispatchers.Default + payloadJob)

  val mainTruss = MainTruss("Rabbit::MultiRunner", "")
  val consoleTruss = ConsoleTruss(mainTruss)
//  val kafkaTruss = KafkaTruss("rabbitfox2", mainTruss)
//  val payloadTruss = PayloadTruss(mainTruss, payloadScope)
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

//  payloadJob.join()

  println("Rabbit wins ${numRabbitWins} / ${numRuns} == ${(numRabbitWins.toDouble() / numRuns.toDouble()) * 100.0}%")

}

