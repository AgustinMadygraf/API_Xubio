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
