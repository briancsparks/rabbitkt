package trusskt.trusses

import trusskt.Truss
import trusskt.kind.LogAttr
import trusskt.kind.LogItem
import trusskt.utils.Constants.Companion.DEBUG
import trusskt.utils.Constants.Companion.DEFAULT_RATE_LIMIT
import trusskt.utils.Constants.Companion.ERROR
import trusskt.utils.Constants.Companion.INFO
import trusskt.utils.Constants.Companion.VERBOSE
import trusskt.utils.Constants.Companion.WARN

class HalfTruss(
  moduleName: String,
): Truss(moduleName) {

  init {
    mainTruss?.setCtlDestination(this)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun control(ctrlItem: String, n: Int) {
    mainTruss?.onControl(ctrlItem, n, null, null, 0, null)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun control(ctrlItem: String, n: Int, a: String, b: String, n2: Int, c: String) {
    mainTruss?.onControl(ctrlItem, n, a, b, n2, c)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun onControl(ctrlItem: String, n: Int?, a: String?, b: String?, n2: Int?, c: String?): Boolean {
    return false
  }






  // ==========================================================================================================================================
  // logX()
  // ------------------------------------------------------------------------------------------------------------------
  fun logv(): LogItem {
    return log_x(VERBOSE)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logd(): LogItem {
    return log_x(DEBUG)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logi(): LogItem {
    return log_x(INFO)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun event(): LogItem {
    return logi()
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logw(message: String): LogItem {
    return log_x(WARN).log("message", message)
  }

  // ------------------------------------------------------------------------------------------------------------------
  // TODO: Add exception object
  fun loge(message: String): LogItem {
    return log_x(ERROR).log("message", message)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun TODO(message: String) {
    logw(message).end()
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun log_x(level: Int): LogItem {
    val item = LogItem(this)
    return fixupLogItem(item)
  }

  // ------------------------------------------------------------------------------------------------------------------
  protected fun fixupLogItem(item: LogItem): LogItem {
    // TODO: fixup (long output, short output, etc.)
    return item
  }


  // ==========================================================================================================================================
  // Rate-limited versions
  // ------------------------------------------------------------------------------------------------------------------
  fun logv(rateLimitId: Int): LogItem {
    return log_xyz(VERBOSE, rateLimitId, -1)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logv(rateLimitId: Int, rateLimit: Int): LogItem {
    return log_xyz(VERBOSE, rateLimitId, rateLimit)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logd(rateLimitId: Int): LogItem {
    return log_xyz(DEBUG, rateLimitId, -1)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun logd(rateLimitId: Int, rateLimit: Int): LogItem {
    return log_xyz(DEBUG, rateLimitId, rateLimit)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun log_xyz(level: Int, rateLimitId: Int, rateLimit: Int): LogItem {
    var rateLimit = rateLimit
    if (rateLimitId > 0) {
      if (rateLimit == -1) {
        rateLimit = DEFAULT_RATE_LIMIT
      }

      // TODO: Implement rate limit
    }
    return log_x(level)
  }


  // ==========================================================================================================================================
  // trace()
  // ------------------------------------------------------------------------------------------------------------------
  fun trace(traceName: String): LogItem {
    val item = LogItem(this)
    return fixupLogItem(item)
  }

  // ==========================================================================================================================================
  // LogAttr
  // ------------------------------------------------------------------------------------------------------------------
  fun <T> attr(entName: String, id: T): LogAttr {
    return attr_x(entName, id)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun <T> ent(entName: String, id: T): LogAttr {
    return attr_x(entName, id)
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun <T> attr_x(entName: String, id: T): LogAttr {
    val attr = LogAttr(this)
    return fixupLogAttr(attr).ent(entName, id)
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun fixupLogAttr(attr: LogAttr): LogAttr {
    // TODO: fixup (long output, short output, etc.)
    return attr
  }

  // ==========================================================================================================================================

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogItem(item: LogItem) {
    mainTruss?.addLogItem(item)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogItem(item: LogItem, s: String?, s2: String?, s3: String?, s4: String?) {
    mainTruss?.sendLogItem(item, s, s2, s3, s4)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogAttr(attr: LogAttr) {
    mainTruss?.addLogAttr(attr)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogAttr(attr: LogAttr, s: String?, s2: String?, s3: String?, s4: String?) {
    mainTruss?.sendLogAttr(attr, s, s2, s3, s4)
  }

}