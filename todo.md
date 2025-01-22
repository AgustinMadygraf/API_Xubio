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

## **3. Escalabilidad e Integración con Flask** 
facu estoy cansado, luego lo reviso bien este apartado.

### **3.1. Estructura compatible con Flask**

- [ ] **Organizar carpetas para Flask**  
  - **Propuesta**: Mantener un directorio principal (p. ej., `app_flask/`) con los siguientes subdirectorios:  
    - `routes/` para las rutas o *blueprints*,  
    - `services/` para la lógica de negocio (compartida con la consola),  
    - `static/` y `templates/` (si se utilizará front-end HTML).  
  - **Objetivo**: Facilitar la integración con Flask, conservando la modularidad.

- [ ] **Mantener “app_console” separado**  
  - **Propuesta**: Colocar la lógica y el script principal de la consola en una carpeta como `app_console/`, importando los servicios desde la misma ubicación que “app_flask”.

---

### **3.2. Centralizar la lógica de negocio en servicios reutilizables**

- [ ] **Compartir clases y funciones entre consola y Flask**  
  - **Propuesta**: Crear (o mantener) un directorio `services/` que contenga:  
    - `token_service.py`  
    - `client_data_processor.py`  
    - Cualquier otra clase encargada de la lógica de negocio.  
  - **Objetivo**: Evitar duplicación de código y garantizar que ambos puntos de entrada (consola y Flask) accedan a la misma funcionalidad.

- [ ] **Mantener un solo token compartido**  
  - **Propuesta**: Desde `services/token_service.py` o un “config manager”, manejar la lectura/escritura de `config.json` para todos los usuarios de la aplicación (CLI y Flask).  
  - **Objetivo**: Asegurar que siempre se use el mismo token, sin requerir múltiples validaciones para cada usuario.

---

### **3.3. Definir el flujo para la interfaz web y la consola**

- [ ] **Diseñar endpoints de Flask**  
  - **Propuesta**: Crear un archivo de rutas (por ejemplo, `routes/xubio_routes.py`) que, al recibir una solicitud HTTP, invoque los métodos de los servicios para obtener o procesar datos.  
  - **Objetivo**: Ofrecer la misma funcionalidad disponible en la consola (listar clientes, obtener datos de empresa, etc.) a través de endpoints HTTP.

- [ ] **Mantener un script de consola**  
  - **Propuesta**: En `app_console/main.py` o similar, conservar la interfaz de línea de comandos que use la misma lógica de `services/`.  
  - **Objetivo**: Permitir que la solución sea ejecutable tanto desde la terminal como desde un navegador.

---

### **3.4. Ejecución y despliegue en servidor local**

- [ ] **Configuración del servidor Flask**  
  - **Propuesta**: En `app_flask/__init__.py` o `run.py`, inicializar la app Flask con la configuración base.  
  - **Objetivo**: Permitir el arranque local con el comando `flask run` o un script personalizado.

- [ ] **Documentar el arranque simultáneo**  
  - **Propuesta**: Explicar en el README los pasos para:  
    1. Ejecutar la consola (`python app_console/main.py`).  
    2. Ejecutar la app Flask (`python run.py` o `flask run`).  
  - **Objetivo**: Que otros desarrolladores o usuarios puedan elegir la interfaz que mejor se adapte a sus necesidades.

---

### **3.5. Actualizar la documentación interna**

- [ ] **Instrucciones de uso para consola y Flask**  
  - **Propuesta**: Indicar en el README o en la documentación:  
    - Cómo funciona la CLI (comandos disponibles, opciones).  
    - Cómo consumir los endpoints Flask (URLs, parámetros, ejemplos de uso con Postman/cURL).

- [ ] **Explicar uso de `config.json`**  
  - **Propuesta**: Destacar que el token se comparte entre ambos entornos, detallando dónde se ubica el archivo y cómo se evita su inclusión en el repositorio (`.gitignore`).
