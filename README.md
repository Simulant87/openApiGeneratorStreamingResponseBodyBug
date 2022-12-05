# About

This is an example project to reproduce a Bug in the `org.openapi.generator`.
In version 6.0.0 of the OpenAPI Generator I can use the `StreamingResponseBody` from Spring Framework.
After updating to 6.0.1 this no longer works. The class `com.example.filestorage.generated.model.StreamingResponseBody` is no longer generated.

the compile error is:

```
> Task :compileKotlin FAILED
e: /src/main/kotlin/com/example/filestorage/rest/FilesController.kt: (18, 34): 
    Return type of 'downloadFile' is not a subtype of the return type of the overridden member 
    'public open fun downloadFile(): ResponseEntity<JsonNode!>! defined in com.example.filestorage.generated.api.FilesApi'
```

## Reproduce the bug

try to start the service, which starts the code generation and the compilation, which fails with the error mentioned above:
```
./gradlew bootRun
```

change the version of `id("org.openapi.generator")` to  `"6.0.0"` and the compilation and download of the file via Swagger works just fine.

## Swagger UI API Documentation

The swagger UI can then be accessed on [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) once the service is started.
