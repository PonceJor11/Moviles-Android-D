# CampusGPS

Aplicación Android que obtiene la ubicación del dispositivo, la muestra en un mapa y indica cuál de los cinco lugares del campus está más cerca, con su distancia en metros y ordenados de menor a mayor.

**Autor:** Ponce · **Docente:** Juan León S. · **Modalidad:** individual

## Descripción

- Solicita el permiso de ubicación en tiempo de ejecución.
- Obtiene la posición con `FusedLocationProviderClient`.
- Dibuja el mapa con Maps Compose: marcador propio (rojo), lugares (azul) y lugar más cercano (verde).
- Calcula y ordena las distancias en el ViewModel; la interfaz solo dibuja el estado.
- Maneja los casos de error: permiso denegado, GPS apagado y dispositivo sin Google Play Services.
- Conserva ubicación, lista y selección al rotar la pantalla.

Los cinco lugares son datos ficticios cercanos al punto base `-12.0464, -77.0428` (Lima).

## Tecnologías

Kotlin · Jetpack Compose · Material 3 (sin XML) · Google Play Services Location · Maps Compose · Coroutines · StateFlow · MVVM + Repository.

## Configuración

1. Abrir el proyecto en Android Studio y esperar la sincronización de Gradle.
2. Crear una clave de **Maps SDK for Android** en Google Cloud, restringida al paquete `com.ponce.campusgps` y a la huella SHA-1 (`./gradlew signingReport`).
3. Guardarla en `local.properties` (archivo ignorado por Git):
   ```properties
   MAPS_API_KEY=tu_clave
   ```
   Hay una plantilla en `local.properties.example`.
4. Usar un emulador con **Google Play**. Para ver los lugares cerca, configurar la ubicación en *Extended controls → Location* con `-12.0464`, `-77.0428`.

## Estructura de paquetes

```
com.ponce.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                  Posicion, Lugar, LugarConDistancia, TipoError, UbicacionUi
├── data/
│   ├── UbicacionRepository.kt      interface (+ UbicacionNoDisponibleException)
│   ├── UbicacionRepositoryGoogle.kt  implementación con FusedLocationProviderClient
│   ├── LugaresRepository.kt        interface
│   └── LugaresRepositoryLocal.kt   los 5 lugares fijos
├── util/
│   ├── Distancia.kt                distanciaMetros(...)
│   └── Formato.kt                  formatearDistancia(...)
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt
    ├── MapaUbicacion.kt
    ├── ListaLugares.kt
    ├── MensajeError.kt
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaMapa)
   | evento: pulsar el botón            ^ collectAsState()
   v                                    |
ViewModel (MapaViewModel) --- StateFlow --->
   | llama a las interfaces
   v
UbicacionRepository ---> UbicacionRepositoryGoogle (FusedLocationProviderClient)
LugaresRepository   ---> LugaresRepositoryLocal    (los 5 lugares fijos)
```

| Capa | Conoce a | No debe |
|---|---|---|
| View (`ui/`) | Solo a su ViewModel | Usar Repository, `FusedLocationProviderClient` ni calcular distancias |
| ViewModel | Solo a las interfaces de Repository | Conocer Composables ni recibir un `Context` |
| Repository | Google Play Services (en la implementación) | Exponer clases de UI |
| `util/` | Funciones puras (distancia, formato) | Guardar estado |

## Capturas

| Permiso | Mapa con mi ubicación | Marcadores |
|---|---|---|
| ![Permiso](docs/capturas/01-permiso.png) | ![Mapa](docs/capturas/02-mapa-ubicacion.png) | ![Marcadores](docs/capturas/03-marcadores.png) |

| Lista ordenada | Error |
|---|---|
| ![Lista](docs/capturas/04-lista-ordenada.png) | ![Error](docs/capturas/05-error.png) |

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Abrir la app y pulsar el botón | Aparece el cuadro de permiso | ☐ |
| Aceptar el permiso | Latitud, longitud y marcador *Estás aquí* | ☐ |
| Negar el permiso | Mensaje con *Abrir ajustes*; la app no se cierra | ☐ |
| Ubicación en el punto base | Los 5 marcadores azules cerca de la posición | ☐ |
| Tocar un marcador | Nombre y categoría del lugar | ☐ |
| Lista de lugares | Ordenada por distancia, con m o km | ☐ |
| Tocar una tarjeta | La cámara viaja al lugar y la tarjeta se resalta | ☐ |
| Ir al más cercano | La cámara va al lugar verde; botón deshabilitado antes de buscar | ☐ |
| Cambiar la ubicación y buscar otra vez | Distancias, orden y lugar verde se actualizan | ☐ |
| Ubicación apagada | Mensaje de GPS con *Reintentar* | ☐ |
| Rotar la pantalla | No se pierden la ubicación ni la lista | ☐ |
| Revisar el repositorio | `local.properties` no está subido y la clave no aparece en Git | ☐ |

## Para pensar

**1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?**
Porque obtener la ubicación es un detalle de acceso a datos. El ViewModel solo conoce la interfaz `UbicacionRepository`, así que no depende de Google Play Services ni necesita un `Context` (que podría filtrar la Activity al sobrevivir a la rotación). Además, se puede sustituir la implementación por una falsa en pruebas o cambiar de proveedor sin tocar el ViewModel ni la UI.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo concede el usuario y autoriza a la app a leer su posición. La clave de API identifica a la app ante Google Cloud para usar el Maps SDK y la configura el desarrollador. Sin permiso falla la ubicación (`SecurityException`), pero el mapa se dibuja. Sin clave válida el mapa sale gris, pero la ubicación sigue funcionando.

**3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?**
`local.properties` está en `.gitignore`, de modo que la clave no entra al repositorio; llega al Manifest como un placeholder en tiempo de compilación. Una clave pública puede ser usada por terceros y generar costos. Si se subiera, la eliminaría y crearía otra en Google Cloud, la restringiría por paquete y SHA-1, y limpiaría el historial de Git (por ejemplo con `git filter-repo`), porque el commit anterior sigue conteniéndola.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
En `MapaViewModel.actualizarDistancias`, apoyado en la función pura `distanciaMetros` de `util/`. Un Composable puede recomponerse muchas veces y repetiría el cálculo, además de mezclar lógica con dibujo y volverla difícil de probar. En el ViewModel se calcula una vez por búsqueda, sobrevive a la rotación y la pantalla solo dibuja el estado.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?**
Android deja de mostrar el cuadro y la solicitud se resuelve como denegada de inmediato. La app no se cierra: el ViewModel pasa a `UbicacionUi.Error` con tipo `SIN_PERMISO` y la pantalla muestra el mensaje con el botón *Abrir ajustes*, que lleva a la configuración de la app para activar el permiso a mano. Al volver y pulsar *Obtener mi ubicación*, se revisa el permiso de nuevo.
