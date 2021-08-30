package TrussKt.utils

import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern

class U {
  companion object {

    var isDebug = true

    // ------------------------------------------------------------------------------------------------------------------
    fun setIsDebug(isDebuggable: Boolean) {
      isDebug = isDebuggable
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun isEmpty(s: String?): Boolean {
      return s == null || s.isEmpty()
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun isNotEmpty(s: String?): Boolean {
      return !isEmpty(s)
    }

    // ------------------------------------------------------------------------------------------------------------------
    private fun isBuildConfigEmpty(s: String?): Boolean {
      return s == null || s.isEmpty() || s.lowercase(Locale.getDefault()) == "null"
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun buildConfigString(s: String, def: String): String {
      return if (!isBuildConfigEmpty(s)) {
        s
      } else def
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun buildConfigString(s: String): String? {
      return if (!isBuildConfigEmpty(s)) {
        s
      } else null
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun assert_(isGood: Boolean, message: String, isDie: Boolean): Boolean {
      if (isDebug) {
        if (!isGood) {
          // BBB: Set BP here
          if (isDie) {
            assert(isGood) { message!! }
          } else {
//          Log.e("utils", message);
          }
        }
      }
      return !isGood
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun assert_(value: Boolean, message: String): Boolean {
      return assert_(value, message, true)
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Returns true if bad condition
    //
    //   if (!assert__(...)) {
    //      // We are good
    //   }
    //
    fun assert__(value: Boolean, message: String): Boolean {
      return !assert_(value, message)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun <E : Exception> showException(TAG: String, e: E) {
      showException(TAG, e, "")
    }

    @Throws(Exception::class)
    fun <E : Exception> showException(TAG: String, e: E, throwit: Boolean) {
      showException(TAG, e, "", throwit)
    }

    @Throws(Exception::class)
    fun <E : Exception> showException(TAG: String, e: E, where: String, throwit: Boolean) {
      showException(TAG, e, where)
      if (throwit) {
        throw e
      }
    }

    fun <E : Exception> showException(TAG: String, e: E, where: String) {
      /// BBB: Set BP here for exceptions
      //Log.e(TAG, "Exception at " + where, e);
      e.printStackTrace()
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun showException(e: Exception) {
      // BBB BP for exceptions
      e.printStackTrace()
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun <T> put(data: JSONObject?, key: String, value: T?) {
      //if (ignoreKey(key)) { return this; }
      if (data == null) {
        return
      }
      try {
        if (value != null) {
          data.put(key, value)
        } else {
          data.put(key, "null")
        }
      } catch (ex: JSONException) {
        showException(ex)
      }
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun smartPut(data: JSONObject?, key: String, value: String) {
      //if (ignoreKey(key)) { return this; }
      if (data == null) {
        return
      }

      // Is it an integerish type
      val reInteger = Pattern.compile("^[0-9]+$")
      val mInteger = reInteger.matcher(value)
      if (mInteger.find()) {
        val n = value.toInt().toLong()
        put(data, key, n)
        return
      }

      // Is it a realish type
      val reNumber = Pattern.compile("^[0-9.]+$")
      val mNumber = reNumber.matcher(value)
      if (mNumber.find()) {
        val x = value.toDouble()
        put(data, key, x)
        return
      }
      if (value == "true") {
        put(data, key, true)
        return
      }
      if (value == "false") {
        put(data, key, false)
        return
      }
      put(data, key, value)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun <T> getOrDefault(map: HashMap<String, T>, key: String, def: T): T? {
      return if (!map.containsKey(key)) {
        def
      } else map[key]
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Uninteresting
    // ------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------
    fun allEmpty(s: String, s2: String): Boolean {
      return isEmpty(s) && isEmpty(s2)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun allEmpty(s: String, s2: String, s3: String): Boolean {
      return allEmpty(s, s2) && isEmpty(s3)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun allEmpty(s: String, s2: String, s3: String, s4: String): Boolean {
      return allEmpty(s, s2) && allEmpty(s3, s4)
    }


    // ------------------------------------------------------------------------------------------------------------------
    fun anyEmpty(s: String, s2: String): Boolean {
      return isEmpty(s) || isEmpty(s2)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun anyEmpty(s: String, s2: String, s3: String): Boolean {
      return anyEmpty(s, s2) || isEmpty(s3)
    }

    // ------------------------------------------------------------------------------------------------------------------
    fun anyEmpty(s: String, s2: String, s3: String, s4: String): Boolean {
      return anyEmpty(s, s2) || anyEmpty(s3, s4)
    }


    // ------------------------------------------------------------------------------------------------------------------
    // I need all of these strings to be non-empty
    fun allNotEmpty(s: String, s2: String): Boolean {
      return !isEmpty(s) && !isEmpty(s2)
    }

    // ------------------------------------------------------------------------------------------------------------------
    // I need all of these strings to be non-empty
    fun allNotEmpty(s: String, s2: String, s3: String): Boolean {
      return allNotEmpty(s, s2) && !isEmpty(s3)
    }

    // ------------------------------------------------------------------------------------------------------------------
    // I need all of these strings to be non-empty
    fun allNotEmpty(s: String, s2: String, s3: String, s4: String): Boolean {
      return allNotEmpty(s, s2) && allNotEmpty(s3, s4)
    }


    // ------------------------------------------------------------------------------------------------------------------
    // Let me know if any of these are non-empty
    fun anyNotEmpty(s: String, s2: String): Boolean {
      return !isEmpty(s) || !isEmpty(s2)
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Let me know if any of these are non-empty
    fun anyNotEmpty(s: String, s2: String, s3: String): Boolean {
      return anyNotEmpty(s, s2) || !isEmpty(s3)
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Let me know if any of these are non-empty
    fun anyNotEmpty(s: String, s2: String, s3: String, s4: String): Boolean {
      return anyNotEmpty(s, s2) || anyNotEmpty(s3, s4)
    }
  }

}