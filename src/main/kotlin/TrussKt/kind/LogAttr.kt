package TrussKt.kind

import TrussKt.Truss
import TrussKt.utils.Constants.Companion.SEND_ALL
import TrussKt.utils.U
import org.json.JSONException
import org.json.JSONObject

class LogAttr(
  private var truss: Truss,
) {

  private lateinit var data: JSONObject
  private var meta_: java.util.HashMap<String, String> = hashMapOf()

  var tick: Long = System.currentTimeMillis() - Truss.startMillis

  init {
    try {
      data = JSONObject()
    } catch (ex: Exception) {
      ex.printStackTrace()
    }
    if (truss.useTick()) {
      put("tick", tick)
    }
  }


  // ------------------------------------------------------------------------------------------------------------------
  fun <T> attr(name: String, value: T): LogAttr {
    return put(name, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun smartLog(key: String, value: String): LogAttr {
    return smartPut(key, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun meta(key: String, value: String): LogAttr {
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
      truss.sendLogAttr(this, /*null, null, null, null*/)
      return
    }
    truss.addLogAttr(this)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun send(s: String?) {
    truss.sendLogAttr(this, s, /*null, null, null*/)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun send(s: String?, s2: String?, s3: String?, s4: String?) {
    truss.sendLogAttr(this, s, s2, s3, s4)
  }

  // ------------------------------------------------------------------------------------------------------------------
  // Internal
  private fun <T> put(key: String, value: T): LogAttr {
    U.put(data, key, value)
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun smartPut(key: String, value: String): LogAttr {
    U.smartPut(data, key, value)
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun modName(): String? {
    return truss.moduleName
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun TAG(): String? {
    return modName()
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun toString(): String {
    return data.toString()
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
  fun getData(): JSONObject? {
    return data
  }

}