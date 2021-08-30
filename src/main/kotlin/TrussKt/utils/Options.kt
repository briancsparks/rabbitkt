package TrussKt.utils

class Options {

  private val boolOpts:   MutableMap<String, Boolean>     = HashMap()
  private val strOpts:    MutableMap<String, String>      = HashMap()
  private val numberOpts: MutableMap<String, Long>        = HashMap()

  fun Set(key: String, value: Boolean) {
    boolOpts[key] = value
  }

  fun Set(key: String, value: String) {
    strOpts[key] = value
  }

  fun Set(key: String, value: Long) {
    numberOpts[key] = value
  }

  fun is_true(key: String): Boolean {
    return boolOpts.getOrDefault(key, false)
  }

  fun is_false(key: String): Boolean {
    if (!boolOpts.containsKey(key)) {
      return false
    }
    val b = boolOpts.getOrDefault(key, false)
    return b == false
  }

  fun string(key: String): String {
    return strOpts.getOrDefault(key, "")
  }

}