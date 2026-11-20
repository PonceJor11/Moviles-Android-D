# Cómo usar este ZIP

1. Opción recomendada: abre tu proyecto base y reemplaza la carpeta
   `app/src/main/java/com/tecsup/centrotareas/` por la de este ZIP.
   Si tu base tiene otras versiones de Gradle o Compose, no toques sus archivos de configuración.
2. Opción alternativa: abre esta carpeta directamente en Android Studio (File > Open) y sincroniza Gradle.
   No incluye `gradlew` ni `gradle-wrapper.jar` (no se pueden generar sin internet);
   Android Studio usa su propio Gradle o te ofrece descargarlo.

## Para el Paso 1 (commit 2)

El código final no puede tener `Thread.sleep`. Para el commit del Paso 1, agrega temporalmente en `TareasViewModel`:

```kotlin
fun tareaLargaMala() {
    _mensaje.value = "Trabajando..."
    Thread.sleep(4000)
    _mensaje.value = "Terminó"
}
```

y un botón "Tarea larga (MAL)" en la pantalla que la llame. Luego bórralos al hacer el commit 3.

## Ajustes

- Si contar hasta 500 000 tarda menos de 1 s o más de 10 s en tu emulador, cambia el valor inicial
  en `TarjetaPrimos` (`mutableStateOf("500000")`) para que tarde entre 2 y 5 s.
- Referencias para validar: 100 → 25, 1000 → 168, 10 000 → 1229, 100 000 → 9592, 500 000 → 41 538.
