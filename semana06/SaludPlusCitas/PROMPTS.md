# Documentación de Prompts y Cuestionario - Fase 2 (Mejora con IA)

## Prompts Utilizados

### Prompt 1: Implementación de Calendario Dinámico con java.time.LocalDate
- **Prompt:** Implementar calendario dinámico en FechaHoraScreen que muestre los 5 días hábiles a partir de hoy (sin fines de semana ni días pasados), navegue por semanas con flechas deshabilitando el pasado, actualice mes/año y recalcular horarios sin romper reservas.
- **Respuesta resumida:** Implementación con TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY), filtrado de días pasados y llamado dinámico a Repositorio.horariosDisponibles().
- **Correcciones realizadas:** Se aisló la lógica para omitir sábados y domingos y se reinició la horaSeleccionada al cambiar de fecha.

### Prompt 2: Rediseño del UI Kit Institucional
- **Prompt:** Aplicar paleta institucional azul (#0066FF, #0D1B2A), tarjetas redondeadas y solucionar el color claro de texto en los campos de entrada de RegistroScreen y ConfirmarCitaScreen.
- **Respuesta resumida:** Definición de TextStyle e indicadores de color explícitos en OutlinedTextField.
- **Correcciones realizadas:** Corrección de visibilidad del texto al escribir el motivo de consulta.

### Prompt 3: Corrección de Navegación y BackStack
- **Prompt:** Resolver los fallos en las flechas superiores de regreso en Perfil, Mis Citas y conectar la ruta del Historial en HomeScreen y BottomNavigationBar.
- **Respuesta resumida:** Vinculación de callbacks onNavigateBack con navController.popBackStack() en AppNavigation.kt y limpieza de ítems duplicados.
- **Correcciones realizadas:** Eliminación de NavigationBarItem repetido en la barra inferior.

---

## Cuestionario de Análisis Téchnico

### 1. ¿Por qué los modelos, Rutas.kt y AppNavigation.kt se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?
Se entregaron completos porque constituyen la arquitectura base del proyecto (la estructura de datos, las constantes de navegación y el mapa global de rutas). Esto garantiza que todos trabajemos bajo los mismos contratos de datos e identificadores. Los archivos que se dejaron como esqueleto tienen en común que son componentes de la interfaz de usuario (Pantallas/UI), los cuales dependían directamente de nuestro trabajo de maquetación, estados (emember) y la implementación lógica requerida en la guía.

### 2. ¿Por qué el Repositorio es un object y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista?
El Repositorio es un object para implementar el patrón Singleton de forma nativa en Kotlin, garantizando una única instancia compartida en toda la aplicación. Si fuera una clase normal y cada pantalla creara su propia instancia de la lista, las reservas hechas en una pantalla no se reflejarían en las demás, provocando pérdida de sincronización de datos y permitiendo que se reserven citas duplicadas en el mismo horario.

### 3. ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos, sin que tú "actualices" nada a mano?
Lo logré utilizando el manejo de estado reactivo de Jetpack Compose mediante emember asignado a variables clave como la consulta de texto o la fecha seleccionada. Al cambiar el valor del estado, Compose detecta el cambio y ejecuta automáticamente una recomposición de la interfaz, reevaluando las consultas de búsqueda y las llamadas al filtro de horarios sin necesidad de refrescar la pantalla manualmente.

### 4. ¿Qué diferencia notaste entre navigate() normal (Especialidades ? Médicos) y el que usa popUpTo (Confirmar cita ? Cita agendada)? ¿Qué pasa al presionar Atrás en cada caso?
Con 
avigate() normal, la pantalla origen se mantiene apilada en el historial (BackStack), por lo que al presionar "Atrás" el usuario regresa a la pantalla anterior (de Médicos vuelve a Especialidades). En cambio, al usar popUpTo se remueven del historial las pantallas del flujo de reserva para que, al confirmar la cita y llegar a la pantalla de Cita Agendada, presionar "Atrás" lleve al usuario directamente al Home en lugar de devolverlo al formulario de confirmación.

### 5. ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?
La IA inicialmente generó una semana fija de 5 días basada únicamente en el lunes actual sin considerar si esos días ya habían pasado, además de no limpiar la hora previamente seleccionada. Tuve que corregir el filtrado para deshabilitar explícitamente los días pasados respecto a la fecha actual (LocalDate.now()), omitir sábados y domingos, y forzar que horaSeleccionada se reinicie a 
ull cada vez que el usuario cambia de fecha.

### 6. Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?
Utilizaría el NavigationBar (barra inferior) para aplicaciones móviles donde existen entre 3 y 5 secciones principales de acceso frecuente que el usuario necesita alternar con una sola mano (como Inicio, Citas, Historial y Perfil). Por otro lado, usaría un NavigationDrawer (menú lateral desplegable) en proyectos con muchas secciones o jerarquías secundarias (configuración, soporte, términos, cerrar sesión) o en interfaces adaptadas a tablets y pantallas grandes.
