package TrussKt.kind

import TrussKt.Truss
import TrussKt.utils.Constants.Companion.DEBUG
import TrussKt.utils.Constants.Companion.SEND_ALL
import TrussKt.utils.U
import org.json.JSONException
import org.json.JSONObject

class LogItem(
  private var truss: Truss
) {

  private var level = DEBUG
  private lateinit var data: JSONObject
  private var meta_: java.util.HashMap<String, String> = hashMapOf()

  var tick: Long = System.currentTimeMillis() - Truss.startMillis

  init {
    try {
      data = JSONObject()
    } catch (ex: Exception) {
      U.showException("TAG", ex, "LogItem c-tor")
    }
    if (truss.useTick()) {
      put("tick", tick)
    }
  }


  // ------------------------------------------------------------------------------------------------------------------
  fun <T> log(key: String, value: T): LogItem {
    return put(key, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun smartLog(key: String, value: String): LogItem {
    return smartPut(key, value)
  }


  // ------------------------------------------------------------------------------------------------------------------
  fun meta(key: String, value: String): LogItem {
    meta_[key] = value
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun getMetaData(key: String): String {
    return meta_.getOrDefault(key, "")
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun hasMetaData(key: String): Boolean {
    return meta_.containsKey(key)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun end() {
    if (SEND_ALL) {
      truss.sendLogItem(this, /*null, null, null, null*/)
      return
    }
    truss.addLogItem(this)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun send() {
    truss.sendLogItem(this, /*null, null, null, null*/)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun send(s: String?) {
    truss.sendLogItem(this, s, /*null, null, null*/)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun send(s: String?, s2: String?, s3: String?, s4: String?) {
    truss.sendLogItem(this, s, s2, s3, s4)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun getData(): JSONObject? {
    return data
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun deepCopy(): JSONObject? {
    try {
      return JSONObject(data.toString())
    } catch (e: JSONException) {
      U.showException(e)
    }
    return null
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun modName(): String {
    return truss.moduleName
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun TAG(): String {
    return modName()
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun toString(): String {
    return data.toString()
  }


  // ==================================================================================================================
  // Internal
  // ------------------------------------------------------------------------------------------------------------------
  private fun <T> put(key: String, value: T): LogItem {
    U.put(data, key, value)
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun smartPut(key: String, value: String): LogItem {
    U.smartPut(data, key, value)
    return this
  }

}