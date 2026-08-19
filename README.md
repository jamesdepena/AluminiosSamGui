# Aluminios SamGui

Aplicación móvil para Android destinada a la gestión de trabajos realizados por **Aluminios SamGui**, empresa dedicada a la fabricación, instalación y reparación de puertas, ventanas y artículos en aluminio y cristal.

La aplicación permite registrar trabajos, organizar los huecos o aberturas correspondientes a cada trabajo y calcular las medidas de corte según el tipo de material seleccionado.

## Funcionalidades principales

- Inicio de sesión con una cuenta de Google mediante Firebase Authentication.
- Registro, modificación y eliminación de trabajos.
- Búsqueda de trabajos por nombre.
- Registro de información del cliente.
- Registro de dirección y notas adicionales.
- Registro de múltiples huecos dentro de cada trabajo.
- Clasificación de huecos por tipo:
  - Ventana corredera.
  - Puerta.
  - Cristal fijo.
- Cálculo automático de cortes de aluminio.
- Cálculo de las medidas del cristal fijo.
- Filtrado de huecos.
- Almacenamiento local mediante Room.
- Eliminación automática de los huecos cuando se elimina su trabajo.

## Mini manual de uso

### 1. Iniciar sesión

Al abrir la aplicación se muestra la pantalla de autenticación.

1. Presione **Iniciar sesión con Google**.
2. Seleccione una cuenta de Google.
3. Después de autenticarse, se mostrará la lista de trabajos registrados.

> El inicio de sesión solamente controla el acceso a la aplicación. Actualmente, los trabajos se almacenan localmente en el dispositivo y no están vinculados individualmente con la cuenta seleccionada.

### 2. Registrar un trabajo

1. En la pantalla **Trabajos**, presione el botón flotante con el símbolo `+`.
2. Complete el nombre del trabajo.
3. Opcionalmente, complete:
   - Nombre del cliente.
   - Teléfono del cliente.
   - Dirección.
   - Notas.
4. Presione **Guardar**.

### 3. Buscar un trabajo

En la parte superior de la lista puede escribir el nombre del trabajo en el campo de búsqueda.

La lista y el total mostrado se actualizarán según el filtro introducido.

### 4. Modificar un trabajo

1. Localice el trabajo en la lista.
2. Presione el botón de edición.
3. Modifique los datos necesarios.
4. Presione **Guardar**.

### 5. Eliminar un trabajo

1. Abra la pantalla de edición del trabajo.
2. Presione **Eliminar**.
3. Confirme la operación en el cuadro de diálogo.

> Al eliminar un trabajo también se eliminan automáticamente todos los huecos registrados dentro de este.

### 6. Ver los huecos de un trabajo

1. Localice el trabajo correspondiente.
2. Presione la opción para ver sus huecos.
3. Se mostrará la lista de aberturas registradas para ese trabajo.

Desde esta pantalla puede agregar, buscar o modificar huecos.

### 7. Registrar un hueco

1. Abra la lista de huecos del trabajo.
2. Presione el botón `+`.
3. Introduzca una etiqueta para identificarlo, por ejemplo:
   - H1
   - H2
   - Puerta principal
4. Seleccione el tipo de hueco.
5. Introduzca las medidas solicitadas.
6. Seleccione el material, color y las opciones correspondientes.
7. Presione **Guardar**.

Las medidas generales de los huecos y cristales se registran en **pulgadas**. Las medidas estándar de las puertas se expresan en **centímetros**.

## Tipos de hueco

### Corredera

Para una ventana corredera se puede seleccionar:

- Material tradicional.
- P65.
- P92.
- Dos o tres vías.
- Color del material.

Los colores disponibles son:

- Negro.
- Blanco.
- Caoba.
- Gris.

La aplicación calcula las siguientes piezas:

- Cabezal y riel fijo.
- Laterales.
- Alféizar.
- Llavín y enganche.
- Tres vías ALF, cuando corresponda.

El vidrio utilizado para las correderas es de **3/16 de pulgada**.

### Puerta

Las puertas utilizan medidas estándar.

Anchos disponibles:

- 60 cm
- 65 cm
- 70 cm
- 75 cm
- 80 cm
- 85 cm
- 90 cm
- 95 cm
- 100 cm
- 105 cm

El alto estándar es de **210 cm**.

Acabados disponibles:

- Lisa.
- Diseño.

Colores disponibles:

- Caoba.
- Blanco.
- Otro color personalizado.

Las puertas no utilizan la tabla de descuentos de corte de las ventanas correderas.

### Cristal fijo

El cristal fijo utiliza material **P40**.

Colores disponibles:

- Negro.
- Blanco.
- Gris.
- Caoba.

Reglas de cálculo:

- El marco P40 utiliza las medidas exactas del hueco.
- El vidrio utiliza un descuento de `1/4"` en el ancho y en el largo.
- El grosor del vidrio es de `1/4"`.

Ejemplo:

```text
Medida del hueco: 50" × 50"
Marco P40:        50" × 50"
Vidrio:           49.75" × 49.75"
