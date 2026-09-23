# Registro de Prompts - Fase 2 (Mejora con IA)

## Proyecto: Clínica Salud+
**Rama:** `mejora-ia`

---

### Prompt 1: Creación de la funcionalidad de cancelar cita y mejoras decorativas en UI
* **Lo que le pedí a la IA:**
  > "Necesito agregar la funcionalidad de cancelar citas en la pantalla `MisCitasScreen` utilizando un `AlertDialog` de confirmación en Jetpack Compose sin usar ViewModel. Además, quiero mejorar la decoración visual de las tarjetas con colores según el estado."

* **Respuesta de la IA:**
  Generó un estado `citaParaCancelar` con `remember` y `mutableStateOf` para controlar la visibilidad del diálogo de alerta, junto con un diseño decorado usando tarjetas con bordes curvos y chips de colores.

* **Lo que tuve que corregir/ajustar:**
    - Ajusté las importaciones de íconos de Material Icons Extended.
    - Modifiqué el callback para que no cree ViewModel y simplemente ejecute una lambda `onCancelarCita`.

---

### Prompt 2: Integración de la función de cancelación en `MainActivity.kt`
* **Lo que le pedí a la IA:**
  > "¿Cómo puedo pasar la función de cancelación desde `MainActivity` hacia `MisCitasScreen` para modificar la lista `mutableStateListOf` directamente?"

* **Respuesta de la IA:**
  Mostró cómo buscar el índice de la cita dentro del estado mutable `citasState` y actualizar su valor con `.copy(estado = "Cancelada")`.

* **Lo que tuve que corregir/ajustar:**
    - Verifiqué que el cambio de estado re-renderizara la vista sin romper la navegación.