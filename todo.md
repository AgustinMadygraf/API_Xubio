# TO DO List: Modularización del Proyecto

## Objetivo
Refactorizar el proyecto actual para seguir un patrón de diseño modular basado en POO, SOLID y una estructura `src`.

---

## Tareas

### 1. Crear la estructura de directorios
- [x] Crear la carpeta `src/`.
- [x] Crear subcarpetas:
  - [x] `src/services/`
  - [x] `src/logs/`

### 2. Modularización del Procesador de Datos
- [x] Crear el archivo `src/processors/client_processor.py`.
  - **Descripción**: Mover la lógica de procesamiento de datos de clientes a esta clase.
  - **Tareas**:
    - [x] Implementar el formato de salida (JSON o tabla) según la variable `IS_DEVELOPMENT`.
    - [x] Validar y manejar datos inesperados (listas vacías, claves faltantes, etc.).

### 3. Refactorización del `main.py`
- [x] Actualizar `main.py` para interactuar con los módulos refactorizados.
  - **Descripción**: Simplificar el script principal, delegando responsabilidades a los módulos correspondientes.
  - **Tareas**:
    - [x] Importar los módulos desde `src/`.
    - [x] Manejar errores globales de forma elegante.
    - [x] Mantener una interfaz de usuario clara y funcional.

### 4. Pruebas Unitarias
- [ ] Crear una carpeta `tests/` para pruebas unitarias.
  - [ ] Probar `ClientDataProcessor` con datos válidos, vacíos y malformados.
### **1. Análisis Extenso de SOLID en General y en Particular el Principio O (Abierto/Cerrado)**

#### **General SOLID:**
- **S (Single Responsibility Principle):**
  - `main.py` tiene múltiples responsabilidades: cargar configuraciones, manejar errores globales y ejecutar el menú principal.
  - `menu_service.py` mezcla la lógica de menú con detalles específicos de los servicios.
  - `client_data_processor.py` se enfoca solo en procesar datos de clientes, cumpliendo bien con SRP.

- **O (Open/Closed Principle):**
  - Actualmente, extender las funcionalidades del menú (por ejemplo, agregar nuevas opciones para consultar otros recursos de la API) requiere modificar `menu_service.py` y potencialmente otras partes del código, rompiendo OCP.
  - `ClientDataProcessor` no está preparado para manejar otros formatos de datos sin modificaciones significativas.

- **L (Liskov Substitution Principle):**
  - No se utiliza herencia, lo cual simplifica la evaluación. Las implementaciones existentes cumplen con este principio al ser estáticas y específicas.

- **I (Interface Segregation Principle):**
  - Las clases actuales no están sobrecargadas con interfaces innecesarias. Sin embargo, la separación lógica podría mejorar para evitar dependencia de métodos no utilizados.

- **D (Dependency Inversion Principle):**
  - `main.py` depende de concreciones (`menu_service`, `TokenService`) en lugar de abstracciones. Esto dificulta reemplazar módulos con implementaciones alternativas.

---

#### **Particular O (Open/Closed Principle):**
1. **Problemas Identificados:**
   - El menú (`menu_service.py`) está diseñado con lógica condicional directa (`if-elif`) que no es escalable para añadir nuevas funcionalidades.
   - El procesador de datos (`client_data_processor.py`) está limitado a clientes y formatos JSON/tabla. Para manejar más tipos de datos o formatos, sería necesario modificar esta clase directamente.

2. **Objetivos del Refactor:**
   - Modularizar la lógica del menú para que nuevas opciones puedan añadirse sin modificar `menu_service.py`.
   - Diseñar `ClientDataProcessor` para ser extensible, permitiendo agregar nuevos formatos de salida o tipos de datos sin alterar el código existente.
   - Introducir abstracciones y patrones de diseño (como el patrón Estrategia) para promover la extensibilidad.

---

### **2. Plan de Trabajo**

**Objetivo General:**
Refactorizar `main.py`, `menu_service.py`, y `client_data_processor.py` para cumplir con el Principio de Abierto/Cerrado (OCP) y mejorar la adherencia a SOLID en general.

#### **Tareas:**

### **A. Modificar `menu_service.py`**
1. **Crear un registro dinámico de opciones de menú:**
   - **Archivo:** `src/services/menu_service.py`
   - **Tarea:** Introducir una estructura de registro basada en clases para manejar las opciones del menú.
   - **Descripción:** Cada opción de menú será representada por una clase que implemente una interfaz común. `menu_service.py` delegará la ejecución de opciones a estas clases.

2. **Crear una clase base para opciones de menú:**
   - **Archivo:** `src/services/menu_option_base.py` (nuevo archivo)
   - **Tarea:** Crear una interfaz (clase abstracta) `MenuOption` con métodos como `display` y `execute`.
   - **Descripción:** Todas las opciones del menú deben implementar esta interfaz.

3. **Registrar dinámicamente las opciones del menú:**
   - **Archivo:** `src/services/menu_registry.py` (nuevo archivo)
   - **Tarea:** Implementar un registro de opciones del menú que permita añadir nuevas opciones sin modificar `menu_service.py`.
   - **Descripción:** El menú iterará sobre las opciones registradas para mostrar y ejecutar las selecciones.

---

### **B. Refactorizar `client_data_processor.py`**
1. **Diseñar una estructura extensible para procesadores de datos:**
   - **Archivo:** `src/services/data_processor_base.py` (nuevo archivo)
   - **Tarea:** Crear una clase base `DataProcessor` con un método abstracto `process`.
   - **Descripción:** Las clases concretas extenderán esta base para manejar diferentes tipos de datos.

2. **Crear procesadores específicos:**
   - **Archivo:** `src/services/client_data_processor.py` (actualizar)
   - **Tarea:** Implementar `ClientDataProcessor` como una subclase de `DataProcessor`.
   - **Descripción:** Mover la lógica actual a esta clase y prepararla para coexistir con otros procesadores.

3. **Registrar los procesadores disponibles:**
   - **Archivo:** `src/services/data_processor_registry.py` (nuevo archivo)
   - **Tarea:** Implementar un registro que permita mapear tipos de datos a procesadores específicos.
   - **Descripción:** Esto permitirá extender la funcionalidad agregando nuevos procesadores sin modificar el código existente.

---

### **C. Modificar `main.py`**
1. **Adaptar el flujo principal a la nueva estructura de menú:**
   - **Archivo:** `main.py`
   - **Tarea:** Reemplazar las llamadas directas al menú con la nueva estructura de registro.
   - **Descripción:** Simplificar `main.py` delegando responsabilidades a `menu_registry.py` y las clases de opciones de menú.

2. **Agregar soporte para procesadores dinámicos:**
   - **Archivo:** `main.py`
   - **Tarea:** Reemplazar las referencias directas a `ClientDataProcessor` con una llamada al registro de procesadores.
   - **Descripción:** Permitir que el sistema determine automáticamente qué procesador utilizar.

---

### **D. Crear Nuevas Clases y Archivos**

1. **`menu_option_base.py`:**
   - Clase abstracta para definir la interfaz de las opciones del menú.

2. **`menu_registry.py`:**
   - Registro dinámico de opciones del menú. Permitirá agregar opciones al menú desde cualquier lugar del proyecto.

3. **`data_processor_base.py`:**
   - Clase abstracta para definir la interfaz de los procesadores de datos.

4. **`data_processor_registry.py`:**
   - Registro dinámico de procesadores de datos. Permitirá mapear tipos de datos a procesadores específicos.

---

### **E. Pruebas Unitarias**
1. **Menú:**
   - **Archivo:** `tests/test_menu_service.py`
   - **Tareas:**
     - Verificar que las opciones del menú se registren correctamente.
     - Asegurar que las opciones del menú ejecutan la lógica esperada.

2. **Procesadores de Datos:**
   - **Archivo:** `tests/test_data_processor.py`
   - **Tareas:**
     - Verificar que los procesadores se registren y ejecuten correctamente.
     - Probar casos de datos vacíos, mal formados y válidos.
