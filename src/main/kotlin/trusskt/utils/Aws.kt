package trusskt.utils

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class Aws {
  companion object {
    suspend fun s3Put(bodyStr: String) {

      val region: Region = Region.US_EAST_1
      val s3: S3Client = S3Client.builder().region(region).build()
      val bucket = "cdr0-net-ingest"
      val key = "ingest/testthis/a.json"

      println("Putting to S3 ${bucket}/${key} (${bodyStr.length} bytes)")
      val resp = s3.putObject(
        PutObjectRequest.builder().bucket(bucket).key(key).build(),
        RequestBody.fromString(bodyStr)
      )
      println("Done putting to S3 ${bucket}/${key} (${bodyStr.length} bytes) eTag: ${resp.eTag()}")

      val tag = resp.eTag()
    }
  }
}
