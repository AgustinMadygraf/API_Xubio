# To Do List

## **1. Refactor de Menú y Uso de `menu_config.json`**

### **1.1. Crear una clase o interfaz común para todas las opciones de menú**

- **Archivo Sugerido**: Podría ser `src/services/menu_option_base.py`
- **Objetivo**: 
  - Definir una estructura uniforme que represente a cada opción del menú (por ejemplo, un método para obtener la clave, otro para ejecutar la lógica asociada, etc.).
- **Propuesta de Modificación**:
  1. Crear una clase base (o interfaz) que declare métodos esenciales, por ejemplo:
     - `get_key()`: para identificar la opción del menú (coincidente con `key` en el JSON).
     - `get_description()`: para describir la opción (coincidente con `description` en el JSON).
     - `execute()`: para contener la lógica que se ejecuta al seleccionar la opción.
  2. Asegurarse de que cada opción de `menu_config.json` pueda transformarse en una instancia de esta clase base o de una subclase especializada.
  3. Mantener la consistencia: todas las opciones deben tener un **formato y comportamiento coherente** al ser llamadas.

---

### **1.2. Diseñar un mecanismo dinámico para leer y generar las opciones de menú desde `menu_config.json`**

- **Archivo Sugerido**: Se podría centralizar en `src/services/menu_registry.py`
- **Objetivo**: 
  - Eliminar la dependencia de múltiples `if-elif`.
  - Permitir que, al modificar el archivo JSON, se actualicen las opciones de manera transparente.
- **Propuesta de Modificación**:
  1. Implementar un método que **lea** el contenido de `menu_config.json` solo una vez al inicio o cada vez que se requiera (según la necesidad de recarga en caliente).
  2. Para cada objeto en `"menu_options"`, crear una instancia de la clase base (o de subclases especializadas, si se dieran casos diferentes).
  3. Al presentar el menú, iterar automáticamente por la lista de objetos cargados, mostrando la descripción de cada opción.
  4. Al capturar la selección del usuario, **buscar la opción** correspondiente (por `key`) e **invocar** el método `execute()` de esa instancia.

---

### **1.3. Centralizar la lógica de “llamada a la API + procesamiento de datos” en un método común**

- **Archivo Sugerido**: Podría ubicarse en una clase auxiliar dentro de un nuevo archivo como `menu_actions.py`.
- **Objetivo**:
  - Evitar la repetición de líneas de código para “realizar la petición al endpoint, capturar la respuesta y procesarla con `ClientDataProcessor`”.
  - Estandarizar el manejo de errores y la presentación de resultados.
- **Propuesta de Modificación**:
  1. Crear una función o método compartido que reciba:
     - `api_client`
     - `endpoint`
     - `data_type` (por ejemplo, “list” o “dict”)
     - `is_json` (para saber el formato de salida).
  2. Dentro de esa función, encapsular:
     - La llamada al método `get()` de `api_client`.
     - La verificación del tipo de dato devuelto (si es lista o diccionario).
     - La llamada a `ClientDataProcessor` para la presentación final.
  3. En cada opción del menú, en lugar de repetir el consumo de la API y la validación del tipo de dato, simplemente invocar la función centralizada con los parámetros adecuados.

---

### **1.4. Validar casos de uso de `menu_config.json` estático o actualización futura**

- **Objetivo**:
  - Considerar si el archivo se modificará en caliente (en tiempo de ejecución) o permanecerá estático.
- **Propuesta de Modificación**:
  1. **Caso estático**: Cargar el archivo JSON una sola vez al iniciar el programa y mantener la información en memoria. Bastaría con un simple método de lectura y parseo.
  2. **Caso dinámico**: Implementar un mecanismo para **volver a leer** el archivo cuando se detecten cambios (o a petición explícita). Esto podría requerir:
     - Una marca temporal o un “último hash” del archivo para saber si se actualizó.
     - Un flujo que vuelva a instanciar las clases de menú en caliente.
  3. Definir con claridad si el proyecto **requiere** esta funcionalidad o si basta con suposiciones de inmutabilidad del archivo durante la ejecución.


## **2. Almacenamiento del Token en `config.json`**

### **2.1. Verificar la existencia de `config.json` al inicio**

- [ ] **Crear un método para verificar y crear el archivo si no existe**:
  - Definir la estructura inicial:
    ```json
    {
      "access_token": "",
      "created_at": ""
    }
    ```
  - Implementar este método en un nuevo archivo, por ejemplo, `src/config_manager.py`.

---

### **2.2. Leer el token y su timestamp; decidir si es válido (<1 hora)**

- [ ] **Crear un método para cargar y validar el token**:
  - Leer el contenido de `config.json`.
  - Comparar el campo `"created_at"` con la hora actual para determinar si el token sigue siendo válido.
  - Retornar el token si es válido o `None` si no lo es.

---

### **2.3. Solicitar token nuevo si no existe o está vencido; guardar en `config.json`**

- [ ] **Implementar un flujo para obtener y guardar un nuevo token**:
  - Usar `TokenService` para obtener el token.
  - Guardar el token junto con la marca de tiempo actual en `config.json`.
  - Asegurar que el proceso de escritura sea atómico para evitar inconsistencias.

---

### **2.4. Proteger `config.json`**

- [ ] **Añadir `config.json` al `.gitignore`**:
  - Evitar que el archivo se suba al repositorio.

- [ ] **Evaluar la necesidad de cifrado**:
  - Si es necesario proteger el contenido, implementar cifrado al escribir/leer el archivo.

---

## **3. Escalabilidad e Integración con Django**

- [ ] **Mantener una estructura compatible con la migración a Django**
  - **Propuesta de modificación**: Planificar las carpetas y módulos de manera que puedan convertirse en una o varias apps de Django (p. ej., separar “services” y “api” en aplicaciones de Django).

- [ ] **Centralizar la lógica de negocio en servicios reutilizables**
  - **Propuesta de modificación**: Asegurarse de que el código de consulta a la API y el procesamiento de datos sea fácilmente invocable desde vistas/controladores en Django.

- [ ] **Definir el flujo para la interfaz web y la consola**
  - **Duda/Pendiente**: Precisar cómo se compartirán los servicios/lógica entre la consola y el entorno Django (¿un mismo módulo importado o dos “frontends” distintos?).

- [ ] **Revisar el esquema de usuarios en Django**
  - **Duda/Pendiente**: Confirmar si el token será compartido entre múltiples usuarios o si cada usuario tendrá su propia sesión/autenticación con la API Xubio.

- [ ] **Renombrar o mover “src” para ajustarlo al estándar de Django**
  - **Propuesta de modificación**: Convertir cada parte (servicios, logs, etc.) en aplicaciones Django (por ejemplo, una app “logs”, otra app “xubio_api”, etc.).

- [ ] **Mantener el código de consola en un módulo separado (si sigue coexistiendo)**
  - **Propuesta de modificación**: Tener un archivo principal para la CLI que importe la lógica de `src`, de modo que no interfiera con la estructura Django.

- [ ] **Actualizar documentación interna y scripts de arranque** 
  - **Duda/Pendiente**: Ver cómo se ejecutará la aplicación en entorno local y documentar el proceso (e.g., `python manage.py runserver` para Django y un script separado para la consola).
