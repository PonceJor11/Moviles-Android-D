# Prompts utilizados

## Prompt 1: Gráfico de peso de la mascota
**Texto enviado a Gemini:**
Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface). Agrega en la ficha de la mascota un gráfico de línea con el peso registrado en cada cita ATENDIDA. La consulta va en CitaDao, la función en VetRepository y su implementación, y la lista de puntos se expone como StateFlow en MascotaViewModel. La View solo dibuja el gráfico con Canvas. No uses XML ni librerías externas. Muestra me solo los archivos que cambian.

**Qué generó la IA:**
Generó la consulta SQL en CitaDao, los métodos correspondientes en VetRepository y VetRepositoryRoom, y el componente `GraficoPesosCanvas` con la API de Canvas.

**Qué tuve que corregir yo:**
Ajustar los nombres de los atributos de la entidad (`pesoRegistrado` y `fecha`) y corregir la llamada en el repositorio a través del objeto `db`.

**Respeta MVVM?:** Sí, la vista solo dibuja puntos recibidos desde el StateFlow expuesto por el ViewModel sin invocar directamente la base de datos.

---

## Prompt 2: Reprogramación de citas
**Texto enviado a Gemini:**
Agrega la funcionalidad para reprogramar la fecha y hora de una cita en estado PENDIENTE o CONFIRMADA. Agrega la query en CitaDao, expón la función en VetRepository y su implementación en VetRepositoryRoom respetando la arquitectura MVVM.

**Qué generó la IA:**
La anotación `@Query` con la sentencia `UPDATE` en Room y el método suspendido en las capas de repositorio.

**Qué tuve que corregir yo:**
Adaptar los parámetros para alinearlos con las firmas existentes del repositorio.

**Respeta MVVM?:** Sí, desacopla la ejecución de la consulta a través de la interfaz del repositorio.

---

## Prompt 3: Búsqueda de pacientes
**Texto enviado a Gemini:**
Crea una consulta en MascotaDao para buscar pacientes por nombre de mascota o dueño y filtrar por especie simultáneamente. Expón el flujo en BuscarPacientesViewModel conectándolo a VetRepository.

**Qué generó la IA:**
La consulta SQL combinada con `LIKE` y el operador `combine`/`flatMapLatest` dentro del ViewModel.

**Qué tuve que corregir yo:**
Ajustar los nombres de las relaciones `MascotaConDueno` dentro de `MascotaDao.kt`.

**Respeta MVVM?:** Sí, la interfaz de usuario consume únicamente el `StateFlow<List<MascotaConDueno>>` expuesto por el ViewModel.

---

## Reflexión
- La IA aceleró significativamente la escritura de consultas SQL avanzadas en Room y el trazado de gráficos con Canvas.
- Su principal error fue asumir nombres genéricos de atributos en las entidades en lugar de revisar el esquema real del proyecto.
- Aprendí a mantener la consistencia en la arquitectura MVVM declarando siempre las firmas en la interfaz `VetRepository` antes de escribir su implementación en `VetRepositoryRoom`.