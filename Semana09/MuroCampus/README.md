# MuroCampus

App Android que consume el servicio REST JSONPlaceholder: lista publicaciones, muestra el detalle con sus comentarios, filtra por usuario y crea una publicación nueva. Maneja los estados Cargando, Éxito y Error (incluido sin internet).

**Stack:** Kotlin, Jetpack Compose (Material 3), Retrofit + OkHttp, kotlinx.serialization, MVVM + Repository.
**Diseño:** paleta cálida (cognac, ocre y crema), títulos en serif y tarjetas con borde fino.

## Estructura

```
com.ponce.murocampus/
├── MainActivity.kt
├── model/Modelos.kt
├── data/
│   ├── remote/ (PostApi.kt, RetrofitClient.kt)
│   └── PostRepository.kt
├── viewmodel/ (UiState.kt, PostsViewModel.kt, PostsViewModelFactory.kt)
└── ui/ (Componentes.kt, PantallaLista.kt, PantallaDetalle.kt, PantallaCrear.kt, theme/)
```

## Arquitectura

```
View (PantallaLista, PantallaDetalle, PantallaCrear)
   | eventos                         ^ collectAsStateWithLifecycle()
   v                                 |
ViewModel (PostsViewModel) --- StateFlow<UiState> (Cargando / Exito / Error)
   |
   v
PostRepository (interface) -> PostRepositoryRemote (captura excepciones, devuelve Result)
   |
   v
PostApi (Retrofit) --- OkHttp ---> https://jsonplaceholder.typicode.com/
```

## Capturas

(Agrega aquí las 5 capturas: lista, detalle, filtro, crear y error de conexión.)

## Para pensar

(Agrega aquí tus respuestas.)
