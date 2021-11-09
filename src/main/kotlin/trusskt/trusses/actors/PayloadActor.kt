package trusskt.trusses.actors

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import trusskt.kind.LogAttr
import trusskt.kind.LogItem

// https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#actors
// https://proandroiddev.com/actor-based-peaceful-state-management-4ea7ca2a1ae9
// https://medium.com/@jagsaund/kotlin-concurrency-with-actors-34bd12531182

data class PayloadState(
  val items: ArrayList<String> = arrayListOf()
)

sealed class PayloadMsg
class AddItemToPayload(val logItem: LogItem): PayloadMsg()
class AddAttrToPayload(val attrItem: LogAttr): PayloadMsg()
class GetPayloadState(val deferred: CompletableDeferred<PayloadState>): PayloadMsg()
class MaybeFlush(val flushedResponse: CompletableDeferred<Boolean>, val state: CompletableDeferred<PayloadState>): PayloadMsg()


fun CoroutineScope.payloadActor(
  initialState: PayloadState,
) = actor<PayloadMsg> {

  var payloadState: PayloadState = initialState

  for (msg in channel) {
    when (msg) {
      is AddItemToPayload ->  payloadState.items.add(msg.logItem.toString())
      is AddAttrToPayload ->  payloadState.items.add(msg.attrItem.toString())

      is GetPayloadState -> msg.deferred.complete(payloadState)

      is MaybeFlush -> {
        var flushed: Boolean = false
        if (payloadState.items.size > 100) {
          val result = payloadState
          payloadState = PayloadState()
          msg.state.complete(result)

          flushed = true
        }

        msg.flushedResponse.complete(flushed)
      }

    }
  }
}

class PayloadStateStore(
  val scope: CoroutineScope,
  initialState: PayloadState = PayloadState(),
) {

  private val state = scope.payloadActor(initialState)

  suspend fun dispatch(msg: PayloadMsg) {
    state.send(msg)
  }

  suspend fun getState(): PayloadState {
    val completableDeferred = CompletableDeferred<PayloadState>()
    dispatch(GetPayloadState(completableDeferred))
    return completableDeferred.await()
  }
}
