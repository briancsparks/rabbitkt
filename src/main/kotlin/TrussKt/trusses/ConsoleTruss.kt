package TrussKt.trusses

import TrussKt.Truss
import TrussKt.kind.LogAttr
import TrussKt.kind.LogItem

class ConsoleTruss(
  private val sourceTruss: Truss
): Truss("ConsoleTruss") {

  init {
    sourceTruss.setDestination(this)
    sourceTruss.setSendDestination(this)
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun onControl(ctrlItem: String, n: Int?, a: String?, b: String?, n2: Int?, c: String?): Boolean {
    return false
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogItem(item: LogItem) {
    if (item == null) {
      return
    }
    System.out.printf("%s: %s\n", item.TAG(), item.toString())
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogItem(item: LogItem, s: String?, s2: String?, s3: String?, s4: String?) {
    if (item == null) {
      return
    }
    System.out.printf("%s: LogItem: (%s|%s|%s|%s) -- %s\n", item.TAG(), s, s2, s3, s4, item.toString())
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogAttr(attr: LogAttr) {
    if (attr == null) {
      return
    }
    System.out.printf("%s: %s\n", attr.TAG(), attr.toString())
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogAttr(attr: LogAttr, s: String?, s2: String?, s3: String?, s4: String?) {
    if (attr == null) {
      return
    }
    System.out.printf("%s: LogItem: (%s|%s|%s|%s) -- %s\n", attr.TAG(), s, s2, s3, s4, attr.toString())
  }

}