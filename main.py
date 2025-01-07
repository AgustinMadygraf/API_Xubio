import os
from dotenv import load_dotenv
from src.logs.config_logger import logger
from src.config_loader import ConfigLoader
from src.token_service import TokenService
from src.api_client import APIClient
from src.services.client_data_processor import ClientDataProcessor

# Punto de entrada del script
def main():
    os.system('cls' if os.name == 'nt' else 'clear')
    print("\nBienvenido al programa de consulta de clientes de Xubio.\n")
    try:
        # Cargar configuración
        client_id, secret_id, is_json = ConfigLoader.load_env_variables()

        # Obtener token de acceso
        token_service = TokenService(client_id, secret_id)
        access_token = token_service.get_access_token()
        logger.info('Access Token obtenido: %s', access_token)
        # Consumir API
        api_client = APIClient(base_url='https://xubio.com/API/1.1', access_token=access_token)

        while True:
            print("\nOpciones:")
            print("1. Obtener lista de clientes")
            print("2. Salir")
            opcion = input("Selecciona una opción (1-2): ")

            if opcion == "1":
                try:
                    client_data = api_client.get('clienteBean')
                    ClientDataProcessor.process_data(client_data, is_json)
                except Exception as e:
                    logger.info(f"Error al obtener la lista de clientes: {e}")
            elif opcion == "2":
                logger.info("Saliendo del programa. ¡Hasta luego!")
                break
            else:
                logger.info("Opción inválida. Por favor, selecciona una opción válida.")
    
    except Exception as e:
        logger.info(f"Error: {e}")


if __name__ == '__main__':
    main()
