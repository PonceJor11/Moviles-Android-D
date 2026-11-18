# CentroTareas

App Android (Kotlin + Jetpack Compose + Material 3) que demuestra cómo mantener la pantalla fluida con corrutinas:
una tarea con barra de progreso, un cálculo pesado (contar números primos) y dos cargas simuladas, una por una y a la vez.

## Arquitectura

```
View (PantallaTareas)
   | eventos: botones          ^ collectAsState()
   v                           |
ViewModel (TareasViewModel) --- StateFlow --->
   | viewModelScope.launch { ... }
   v
TareasRepository (interface) ---> TareasRepositoryLocal
                                  (cargas simuladas y contar primos)
```

## Paquetes

```
com.tecsup.centrotareas/
├── MainActivity.kt
├── model/Estados.kt
├── data/TareasRepository.kt
├── data/TareasRepositoryLocal.kt
├── viewmodel/TareasViewModel.kt
├── viewmodel/TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt
    └── theme/ (Color, Type, Theme)
```

## Observación del Paso 1

_(Escribe aquí con tus palabras: qué pasa con la pantalla, por qué no se ve "Trabajando..." y qué mostraría Android si la espera fuera de 10 segundos.)_

## Capturas

1. Tarea con progreso
2. Tarea cancelada
3. Primos
4. Una por una contra a la vez
5. Error con Reintentar

## Para pensar

_(Tus respuestas a las 5 preguntas.)_
