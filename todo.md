# TO DO List: Modularización del Proyecto

## Objetivo
Refactorizar el proyecto actual para seguir un patrón de diseño modular basado en POO, SOLID y una estructura `src`.

---

## Tareas

### 1. Crear la estructura de directorios
- [ ] Crear la carpeta `src/`.
- [ ] Crear subcarpetas:
  - [ ] `src/config/`
  - [ ] `src/services/`
  - [ ] `src/processors/`
  - [ ] `src/logs/`

### 2. Configuración del Logger
- [ ] Crear el archivo `src/logs/config_logger.py`.
  - **Descripción**: Configurar un logger centralizado que sea accesible desde cualquier módulo.
  - **Tareas**:
    - [ ] Configurar el nivel del logger (INFO, DEBUG, etc.).
    - [ ] Agregar formato para los mensajes de log.
    - [ ] Configurar la salida (consola y/o archivo).

### 3. Modularización de Configuración
- [ ] Crear el archivo `src/config/config_loader.py`.
  - **Descripción**: Refactorizar la clase `ConfigLoader` para que maneje exclusivamente la carga de configuración.
  - **Tareas**:
    - [ ] Mover la lógica de carga de variables desde `.env` a esta clase.
    - [ ] Agregar validaciones para asegurarse de que las variables requeridas estén presentes.

### 4. Modularización de Servicios
#### 4.1 `TokenService`
- [ ] Crear el archivo `src/services/token_service.py`.
  - **Descripción**: Mover la clase `TokenService` para gestionar exclusivamente la autenticación con la API.
  - **Tareas**:
    - [ ] Asegurarse de que la clase pueda ser reutilizada fácilmente.
    - [ ] Manejar excepciones específicas relacionadas con la autenticación.

#### 4.2 `APIClient`
- [ ] Crear el archivo `src/services/api_client.py`.
  - **Descripción**: Refactorizar la clase `APIClient` para manejar solicitudes a la API.
  - **Tareas**:
    - [ ] Implementar métodos genéricos para `GET`, `POST`, etc.
    - [ ] Validar que los headers se configuren correctamente.

### 5. Modularización del Procesador de Datos
- [ ] Crear el archivo `src/processors/client_processor.py`.
  - **Descripción**: Mover la lógica de procesamiento de datos de clientes a esta clase.
  - **Tareas**:
    - [ ] Implementar el formato de salida (JSON o tabla) según la variable `IS_DEVELOPMENT`.
    - [ ] Validar y manejar datos inesperados (listas vacías, claves faltantes, etc.).

### 6. Refactorización del `main.py`
- [ ] Actualizar `main.py` para interactuar con los módulos refactorizados.
  - **Descripción**: Simplificar el script principal, delegando responsabilidades a los módulos correspondientes.
  - **Tareas**:
    - [ ] Importar los módulos desde `src/`.
    - [ ] Manejar errores globales de forma elegante.
    - [ ] Mantener una interfaz de usuario clara y funcional.

### 7. Pruebas Unitarias
- [ ] Crear una carpeta `tests/` para pruebas unitarias.
  - [ ] Probar `ConfigLoader` con casos de éxito y error.
  - [ ] Probar `TokenService` con respuestas simuladas de la API.
  - [ ] Probar `APIClient` con diferentes endpoints y datos.
  - [ ] Probar `ClientDataProcessor` con datos válidos, vacíos y malformados.
