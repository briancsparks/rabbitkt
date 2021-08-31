package trusskt.trusses

import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.serialization.StringSerializer
import trusskt.Truss
import trusskt.kind.LogAttr
import trusskt.kind.LogItem
import trusskt.utils.Constants.Companion.VERBOSE
import trusskt.utils.Constants.Companion.VERBOSE_KAFKA
import trusskt.utils.U
import trusskt.utils.U.Companion.getProps
import java.util.*
import java.util.concurrent.ExecutionException

class KafkaTruss(
  private val topicName: String,
  private val sourceTruss: Truss,
): Truss("KafkaTruss") {

  lateinit var producer: KafkaProducer<String, String>
  val kafkaHost = U.getTrussProperty<String>("kafkahost")

  init {
    sourceTruss.setDestination(this)
    sourceTruss.setSendDestination(this)
    sourceTruss.setCtlDestination(this)

    val props = getProps()

    // Add additional properties.
    props[ProducerConfig.ACKS_CONFIG] = "all"
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaJsonSerializer::class.qualifiedName

    upsertTopic(topicName, props)

    producer = KafkaProducer<String, String>(props)

  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun onControl(ctrlItem: String, n: Int?, a: String?, b: String?, n2: Int?, c: String?): Boolean {
    return false
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogItem(item: LogItem) {
    // System.out.printf("%s: %s\n", item.TAG(), item.toString())

    producer.send(ProducerRecord(topicName, item::class.java.simpleName, item.toString())) { m: RecordMetadata, e: Exception? ->
      when(e) {
        null ->
          if (VERBOSE_KAFKA) {
            println("Produced record to topic ${m.topic()} partition [${m.partition()}] @ offset ${m.offset()}")
          }

        else -> e.printStackTrace()
      }
    }

    // TODO: Probably do not need to do this every time, right?
    producer.flush()
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogItem(item: LogItem, s: String?, s2: String?, s3: String?, s4: String?) {
    // System.out.printf("%s: LogItem: (%s|%s|%s|%s) -- %s\n", item.TAG(), s, s2, s3, s4, item.toString())
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun addLogAttr(attr: LogAttr) {
    // System.out.printf("%s: %s\n", attr.TAG(), attr.toString())

    producer.send(ProducerRecord(topicName, attr::class.java.simpleName, attr.toString())) { m: RecordMetadata, e: Exception? ->
      when(e) {
        null ->
          if (VERBOSE_KAFKA) {
            println("Produced record to topic ${m.topic()} partition [${m.partition()}] @ offset ${m.offset()}")
          }

        else -> e.printStackTrace()
      }
    }

    // TODO: Probably do not need to do this every time, right?
    producer.flush()
  }

  // ------------------------------------------------------------------------------------------------------------------
  override fun sendLogAttr(attr: LogAttr, s: String?, s2: String?, s3: String?, s4: String?) {
    // System.out.printf("%s: LogItem: (%s|%s|%s|%s) -- %s\n", attr.TAG(), s, s2, s3, s4, attr.toString())
  }

  // ------------------------------------------------------------------------------------------------------------------
  fun upsertTopic(topic: String, adminConfig: Properties, partitions: Int = 1, replication: Short = 1) {
    val newTopic = NewTopic(topic, partitions, replication)

    try {
      with(AdminClient.create(adminConfig)) {
        createTopics(listOf(newTopic)).all().get()
      }
    } catch (e: ExecutionException) {
      if (e.cause !is TopicExistsException) throw e
    }
  }

}
