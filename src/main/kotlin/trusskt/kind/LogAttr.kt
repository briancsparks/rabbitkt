package trusskt.kind

import trusskt.Truss
import trusskt.utils.Constants.Companion.SEND_ALL
import trusskt.utils.U
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
  fun <T> ent(name: String, value: T): LogAttr {
    val ent = U.jsonObject()
    ent?.let {
      U.put(it, name, value)
      U.put(data, "ent", ent)
    }
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun <T> attr(name: String, value: T): LogAttr {
    return put(name, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun smartAttr(key: String, value: String): LogAttr {
    return smartPut(key, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun meta(key: String, value: String): LogAttr {
    meta_[key] = value
    return this
  }

  // ------------------------------------------------------------------------------------------------------------------
  private fun <T> rtta(value: T, name: String): LogAttr {
    return put(name, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun <T>         x(value: T): LogAttr = rtta(value, "x")
  fun <T>        dx(value: T): LogAttr = rtta(value, "dx")
  fun <T>      xext(value: T): LogAttr = rtta(value, "xext")    // x-extent (width)
  fun <T>         y(value: T): LogAttr = rtta(value, "y")
  fun <T>        dy(value: T): LogAttr = rtta(value, "dy")
  fun <T>      yext(value: T): LogAttr = rtta(value, "yext")    // y-extent (height)
  fun <T>         z(value: T): LogAttr = rtta(value, "z")
  fun <T>        dz(value: T): LogAttr = rtta(value, "dz")
  fun <T>      zext(value: T): LogAttr = rtta(value, "zext")    // z-extent
  fun <T>         w(value: T): LogAttr = rtta(value, "w")
  fun <T>        dw(value: T): LogAttr = rtta(value, "dw")
  fun <T>      wext(value: T): LogAttr = rtta(value, "wext")    // w-extent

  // 2nd version (i.e. before/after)
  fun <T>        x2(value: T): LogAttr = rtta(value, "x2")
  fun <T>       dx2(value: T): LogAttr = rtta(value, "dx2")
  fun <T>        y2(value: T): LogAttr = rtta(value, "y2")
  fun <T>       dy2(value: T): LogAttr = rtta(value, "dy2")
  fun <T>        z2(value: T): LogAttr = rtta(value, "z2")
  fun <T>       dz2(value: T): LogAttr = rtta(value, "dz2")
  fun <T>        w2(value: T): LogAttr = rtta(value, "w2")
  fun <T>       dw2(value: T): LogAttr = rtta(value, "dw2")

  fun <T>       len(value: T): LogAttr = rtta(value, "len")
  fun <T>     width(value: T): LogAttr = rtta(value, "width")
  fun <T>    height(value: T): LogAttr = rtta(value, "height")
  fun <T>      size(value: T): LogAttr = rtta(value, "size")

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
  fun partition() {
    attr("partn", true).end()
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