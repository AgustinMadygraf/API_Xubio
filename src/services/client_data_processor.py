from tabulate import tabulate
from src.logs.config_logger import logger

class ClientDataProcessor:
    """Responsable de procesar los datos de clientes."""
    @staticmethod
    def process_data(data, is_json):
        """Procesa y muestra los datos de clientes en formato JSON o tabla."""
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
                # Convertir los datos a formato tabular
                table = [[client.get('cliente_id', 'N/A'), client.get('nombre', 'N/A'), client.get('email', 'N/A')] for client in data]
                headers = ["ID", "Nombre", "Email"]

                logger.info(tabulate(table, headers, tablefmt="grid"))
            else:
                logger.info("Formato de datos inesperado:", data)
