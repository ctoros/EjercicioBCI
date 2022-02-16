#  Ejercicio BCI

_Compilar desde terminal de comando en la carpeta del proyecto_

```
$ gradle build
```

_Para iniciar el proyecto deben ir a la carpeta donde se encuentra el jar_

```
$ cd build/libs
$ java -jar EjercicioBCI-0.0.1-SNAPSHOT.jar
```

### Pre-requisitos 

_Programas y versiones_

```
Java 8
Gradle 
Spring Tool Suite 4
H2
Lombok
Postman -> para consumo de microservicios
```

### Consumir endpoints 

_En la carpeta Docs se encuentra la coleccion postman para probar los endpoint_

```
GlobalLogic-User-EndPoint.postman_collection.json
```



## Test Unitarios con Mocks y reporte Jacoco
_En una terminal de comando, ejecutar lo siguiente:_
```
$ gradle clean build jacocoTestReport 
```
_En build\reports\jacoco\test\html abrir archivo index_

