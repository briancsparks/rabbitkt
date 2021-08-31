package trusskt.trusses

import trusskt.Truss

class MainTruss(
  appId: String,
//  moduleName: String,
  val sessionId: String?,

): Truss("MainTruss") {

  init {
    Truss.mainTruss           = this
    Truss.mainTrussCreated    = true
    Truss.appId               = appId
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun onControl(ctrlItem: String, n: Int?, a: String?, b: String?, n2: Int?, c: String?): Boolean {
    return super.onControl(ctrlItem, n, a, b, n2, c)
  }

}