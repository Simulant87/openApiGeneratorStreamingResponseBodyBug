package com.example.filestorage.rest

import com.example.filestorage.generated.api.FilesApi
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM
import org.springframework.http.ResponseEntity
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream


@RestController
class FilesController : FilesApi {

    override fun downloadFile(): ResponseEntity<StreamingResponseBody> {
        val targetStream = ByteArrayInputStream("testContent".toByteArray())
        val streamingResponseBody = getStreamingResponseBody(targetStream)

        return ResponseEntity
            .ok()
            .header(CONTENT_DISPOSITION, """attachment; filename="testFileName.txt"""")
            .contentType(APPLICATION_OCTET_STREAM)
            .body(streamingResponseBody)
    }

    private fun getStreamingResponseBody(contentStreamValue: InputStream): StreamingResponseBody {
        val streamingResponseBody = StreamingResponseBody { outputStream: OutputStream ->
            FileCopyUtils.copy(contentStreamValue, outputStream)
        }
        return streamingResponseBody
    }
}
