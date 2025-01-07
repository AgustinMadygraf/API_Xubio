"""
Path: src/services/client_data_processor.py

"""

from tabulate import tabulate
from src.logs.config_logger import logger

class ClientDataProcessor:
    """Responsable de procesar los datos de clientes."""
    @staticmethod
    def process_data(data, is_json, max_columns=5):
        """
        Procesa y muestra los datos de clientes en formato JSON o tabla.
        
        :param data: Datos a procesar (lista de diccionarios o diccionario).
        :param is_json: Indica si debe mostrarse en formato JSON.
        :param max_columns: Número máximo de columnas permitidas antes de transponer.
        """
        if is_json:
            # Mostrar los datos en formato JSON (modo desarrollo)
            logger.info("Datos en formato JSON:")
            logger.info(data)
        else:
            # Mostrar los datos en formato tabla (modo producción)
            if isinstance(data, list):
                if not data:
                    logger.info("No se encontraron datos de clientes.")
                    return
                print()
                
                # Identificar claves dinámicamente para los encabezados
                headers = list(data[0].keys()) if data and isinstance(data[0], dict) else []
                
                # Determinar si la tabla debe ser transpuesta
                if len(headers) > max_columns:
                    # Transponer datos para formato vertical
                    transposed_data = [
                        [header] + [client.get(header, 'N/A') for client in data] 
                        for header in headers
                    ]
                    transposed_headers = ["Campo"] + [f"Cliente {i+1}" for i in range(len(data))]
                    
                    # Agregar tabla con una línea en blanco antes
                    logger.info("\n" + tabulate(transposed_data, transposed_headers, tablefmt="grid"))
                else:
                    # Crear la tabla normalmente
                    table = [[client.get(header, 'N/A') for header in headers] for client in data]
                    
                    # Agregar tabla con una línea en blanco antes
                    logger.info("\n" + tabulate(table, headers, tablefmt="grid"))
            else:
                logger.info("Formato de datos inesperado:", data)
