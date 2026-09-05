# Lab 03: Registro de Producto

Estudiante: Jordy Ponce Huarancca
Curso: Desarrollo de Aplicaciones Móviles

## Descripción
Aplicación desarrollada en Android Studio con Jetpack Compose para el registro de productos. Permite ingresar nombre, precio y cantidad, calculando dinámicamente el importe total mediante gestión de estado (`remember` y `mutableStateOf`).

## Capturas de Pantalla

### 1. Pantalla Inicial
![Pantalla Vacía](./captura_vacia.png)

### 2. Producto Registrado
![Producto Registrado](./captura_registrado.png)

---

## Pregunta de Reflexión

¿Qué pasaría si declaras las variables de los campos SIN `remember`?**

Respuesta:
Si se declaran las variables de estado sin utilizar `remember`, el valor de la variable se reiniciará a su estado inicial (`""`) en cada recomposición (cada vez que la pantalla se redibuja al escribir un carácter). Por lo tanto, el usuario no podrá ver lo que escribe en los campos de texto porque el estado se borrará instantáneamente. `remember` es indispensable para preservar el valor de la variable a lo largo del ciclo de vida del composable.

---

## Mejora con IA

### 1. ¿Qué prompt le diste a la IA?
"En PantallaRegistro agrega validación de campos vacíos al presionar AGREGAR mostrando un mensaje de error en rojo en lugar de la Card, y añade un botón Limpiar que vacíe todo el formulario. No modifiques la estructura visual del encabezado."

### 2. ¿Qué generó la IA?
Generó las variables de estado necesarias como `mensajeError`, implementó una fila con los botones **AGREGAR** y **LIMPIAR**, y añadió una lógica básica de verificación comprobando si los campos estaban vacíos usando `.isBlank()`.

### 3. ¿Qué aceptaste o corregiste (y por qué)?
* **Lo que acepté:** El botón **LIMPIAR** y la estructura de la fila con los dos botones.
* **Lo que corregí:** La validación inicial generada por la IA solo revisaba si el texto estaba en blanco, pero permitía ingresar letras en los campos de precio y cantidad.
* **Motivo del cambio:** Agregué validación numérica estricta utilizando `toDoubleOrNull()` y `toIntOrNull()` para evitar fallos en la aplicación, exigir números válidos y prevenir el registro de precios o cantidades menores o iguales a cero.