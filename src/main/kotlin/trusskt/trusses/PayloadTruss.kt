package trusskt.trusses

import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import trusskt.Truss
import trusskt.kind.LogAttr
import trusskt.kind.LogItem
import trusskt.trusses.actors.*
import trusskt.utils.Aws

class PayloadTruss(
  private val sourceTruss: Truss,
  private val payloadScope: CoroutineScope,
  private val sendsOnly: Boolean = false,
): Truss("PayloadTruss") {

  val payloadStateStore = PayloadStateStore(payloadScope)

  val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
  var client = OkHttpClient()

//  private lateinit var payload: SendChannel<PayloadMsg>
//
//  // TODO: should payloadJob.cancel()
//  private val payloadJob = Job()
//  private val payloadScope = CoroutineScope(Dispatchers.Default + payloadJob)

  init {
    sourceTruss.setDestination(this)
    sourceTruss.setSendDestination(this)
    sourceTruss.setCtlDestination(this)

//    payloadScope.launch {
//      payload = payloadActor()
//    }
//    val x = CoroutineScope(Dispatchers.Default).launch {
//      payloadJob.join()
//    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun onControl(ctrlItem: String, n: Int?, a: String?, b: String?, n2: Int?, c: String?): Boolean {
    return false
  }

//  // ------------------------------------------------------------------------------------------------------------------
//  fun finishUp() {
//    CoroutineScope(Dispatchers.Default).launch {
//      payloadJob.join()
//      val i = 10
//    }
//  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogItem(item: LogItem) {
    if (!sendsOnly) {
      payloadLogItem(item)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogItem(item: LogItem, s: String?, s2: String?, s3: String?, s4: String?) {
    if (sendsOnly) {
      payloadLogItem(item)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogAttr(attr: LogAttr) {
    if (!sendsOnly) {
      payloadLogAttr(attr)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogAttr(attr: LogAttr, s: String?, s2: String?, s3: String?, s4: String?) {
    if (sendsOnly) {
      payloadLogAttr(attr)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun payloadLogItem(item: LogItem) {
    payloadScope.launch {
      payloadStateStore.dispatch(AddItemToPayload(item))
      handleFlush()
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun payloadLogAttr(attr: LogAttr) {
    payloadScope.launch {
      payloadStateStore.dispatch(AddAttrToPayload(attr))
      handleFlush()
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private suspend fun handleFlush() {
    val flushedResponse = CompletableDeferred<Boolean>()
    val stateResponse = CompletableDeferred<PayloadState>()
    payloadStateStore.dispatch(MaybeFlush(flushedResponse, stateResponse))

    var state: PayloadState? = null
    val flushed = flushedResponse.await()
    if (flushed) {
      state = stateResponse.await()
      val arrStr = state.items.joinToString()

      val i = arrStr.length

      // TODO: Save and/or upload
      s3Put(state.items)
    }

  }

  // ------------------------------------------------------------------------------------------------------------------
  private suspend fun upload(list: ArrayList<String>) {
    val bodyStr = buildJsonPayload(list)
    val body: RequestBody = bodyStr.toRequestBody(JSON)
    val request: Request = Request.Builder()
      .url("")
      .post(body)
      .build()

    client.newCall(request).execute().use { response ->
      //return response.body()!!.string()
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  private suspend fun s3Put(list: ArrayList<String>) {
    val bodyStr = buildJsonPayload(list)
    Aws.s3Put(bodyStr)

  }

  // ------------------------------------------------------------------------------------------------------------------
  private suspend fun buildJsonPayload(list: ArrayList<String>): String {
    val payloadStr = list.joinToString()

    return "{\"payload\":[${payloadStr}]}"
  }
}

