package trusskt.utils

class Constants {

  companion object {
    const val TRUSS_SEND_TELEMETRY = 2001

    const val IPC_SEND_LOGITEM = "IPC_SEND_LOGITEM"
    const val IPC_SEND_LOGATTR = "IPC_SEND_LOGATTR"

    const val IPC_PARAM_SESSIONID = "IPC_PARAM_SESSIONID"
    const val IPC_PARAM_PAYLOAD = "IPC_PARAM_PAYLOAD"

    const val IPC_PARAM_S1 = "IPC_PARAM_S1"
    const val IPC_PARAM_S2 = "IPC_PARAM_S2"
    const val IPC_PARAM_S3 = "IPC_PARAM_S3"
    const val IPC_PARAM_S4 = "IPC_PARAM_S4"

    const val HTTP_CONNECT_TIMEOUT = 12 * 1000
    const val HTTP_TIMEOUT = 18 * 1000

    const val SEND_ALL = false
    const val LOG_ALL_SENDS = false

    // Level
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6
    const val ASSERT = 7

    // Rate Limit
    const val DEFAULT_RATE_LIMIT = 250 // 4 per second

  }

}