

class U {

  companion object {
    fun nag(msg:String) {

    }

    fun not_possible(msg: String) {
      println("(not_possible) $msg")
    }

    fun should_never_happen(cond: Boolean, msg: String): Boolean {
      if (cond) {
        // AARRGGHH! Should never happen

        // BBB: Set BP for should_never_happen
        return true
      }

      return false
    }

    fun check_input_is_bad(cond: Boolean, msg: String): Boolean {
      return should_never_happen(cond, msg)
    }
  }
}

