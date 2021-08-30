package trusskt

import trusskt.kind.LogAttr
import trusskt.kind.LogItem
import trusskt.trusses.MainTruss
import trusskt.utils.Options

open class Truss(
  val moduleName: String,
) {

  companion object {

    // Only do some things on the first run
    private var isFirstRun = true

    // Keep track of the main Truss
    var mainTruss: MainTruss? = null
    var mainTrussCreated = false

    var startMillis: Long = System.currentTimeMillis()
    private val options: Options = Options()

    // App and module names
    var appId: String? = null
  }

  // Other Trusses to forward messages to
  private var destinations: ArrayList<Truss> = arrayListOf()
  private var sendDestinations: ArrayList<Truss> = arrayListOf()
  private var ctlDestination: ArrayList<Truss> = arrayListOf()


  // ==========================================================================================================================================
  // ------------------------------------------------------------------------------------------------------------------
  open fun onControl(ctrlItem: String, n: Int? =null, a: String? =null, b: String? =null, n2: Int? =null, c: String? =null): Boolean {
    var result = true
    for (truss in ctlDestination) {
      val boo = truss.onControl(ctrlItem, n, a, b, n2, c)
      result = result && boo
    }
    return result
  }

  // ==========================================================================================================================================
  // addLogItem() and friends -- Gets called from .end() and .send()
  // ------------------------------------------------------------------------------------------------------------------
  open fun addLogItem(item: LogItem) {
    for (truss in destinations) {
      truss.addLogItem(item)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  open fun sendLogItem(item: LogItem, s: String? =null, s2: String? =null, s3: String? =null, s4: String? =null) {
    addLogItem(item)
    for (truss in sendDestinations) {
      truss.sendLogItem(item, s, s2, s3, s4)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  open fun addLogAttr(attr: LogAttr) {
    for (truss in destinations) {
      truss.addLogAttr(attr)
    }
  }

  // ------------------------------------------------------------------------------------------------------------------
  open fun sendLogAttr(attr: LogAttr, s: String? =null, s2: String? =null, s3: String? =null, s4: String? =null) {
    addLogAttr(attr)
    for (truss in sendDestinations) {
      truss.sendLogAttr(attr, s, s2, s3, s4)
    }
  }


  // ==========================================================================================================================================
  // ------------------------------------------------------------------------------------------------------------------
  // Mechanics
  // ------------------------------------------------------------------------------------------------------------------
  fun setCtlDestination(destination: Truss) {
    ctlDestination.add(destination)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun setDestination(destination: Truss) {
    destinations.add(destination)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun setSendDestination(destination: Truss) {
    sendDestinations.add(destination)
  }


  // ------------------------------------------------------------------------------------------------------------------
  fun setUseTick(useTick: Boolean) {
    options.Set("useTick", useTick)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun useTick(): Boolean {
    return !options.is_false("useTick")
  }


  // ------------------------------------------------------------------------------------------------------------------
  fun option(key: String, value: String) {
    options.Set(key, value)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun is_true(key: String): Boolean {
    return options.is_true(key)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun is_false(key: String): Boolean {
    return options.is_false(key)
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun option(key: String): String {
    return options.string(key)
  }

}