"""
Path: src/services/menu_service.py

"""

from src.services.client_data_processor import ClientDataProcessor
from src.logs.config_logger import logger

def run_main_menu(api_client, is_json):
    while True:
        print("\nOpciones:")
        print("0. Salir")
        print("1. Obtener lista de clientes")
        
        opcion = input("Selecciona una opción (0-1): ")

        if opcion == "1":
            try:
                client_data = api_client.get('clienteBean')
                ClientDataProcessor.process_data(client_data, is_json)
            except Exception as e:
                logger.info(f"Error al obtener la lista de clientes: {e}")
        elif opcion == "0":
            logger.info("Saliendo del programa. ¡Hasta luego!")
            break
        else:
            logger.info("Opción inválida. Por favor, selecciona una opción válida.")