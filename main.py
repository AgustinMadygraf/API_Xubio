import os
import requests
from requests.auth import HTTPBasicAuth
from dotenv import load_dotenv
from tabulate import tabulate
from src.logs.config_logger import logger

class ConfigLoader:
    """Responsable de cargar la configuración desde un archivo .env."""
    @staticmethod
    def load_env_variables():
        load_dotenv()
        client_id = os.getenv('CLIENT_ID')
        secret_id = os.getenv('SECRET_ID')
        is_development = os.getenv('IS_DEVELOPMENT', 'false').lower() == 'true'

        if not client_id or not secret_id:
            raise ValueError("CLIENT_ID o SECRET_ID no encontrados en el archivo .env")
        
        return client_id, secret_id, is_development


class TokenService:
    """Responsable de manejar la autenticación y la obtención del token."""
    def __init__(self, client_id: str, secret_id: str):
        self.client_id = client_id
        self.secret_id = secret_id
        self.token_url = 'https://xubio.com/API/1.1/TokenEndpoint'

    def get_access_token(self) -> str:
        """Solicita un token de acceso."""
        data = {'grant_type': 'client_credentials'}
        response = requests.post(self.token_url, data=data, auth=HTTPBasicAuth(self.client_id, self.secret_id))

        if response.status_code != 200:
            raise ConnectionError(f"Error al obtener el token: {response.status_code}, {response.text}")
        
        return response.json().get('access_token')


class APIClient:
    """Responsable de interactuar con los endpoints de la API."""
    def __init__(self, base_url: str, access_token: str):
        self.base_url = base_url
        self.headers = {
            'Authorization': f'Bearer {access_token}',
            'Accept': 'application/json'
        }

    def get(self, endpoint: str) -> dict:
        """Realiza una solicitud GET a un endpoint específico."""
        url = f"{self.base_url}/{endpoint}"
        response = requests.get(url, headers=self.headers)

        if response.status_code != 200:
            raise ConnectionError(f"Error en GET {endpoint}: {response.status_code}, {response.text}")
        
        return response.json()


class ClientDataProcessor:
    """Responsable de procesar los datos de clientes."""
    @staticmethod
    def process_data(data, is_development):
        """Procesa y muestra los datos de clientes en formato JSON o tabla."""
        if is_development:
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


# Punto de entrada del script
def main():
    os.system('cls' if os.name == 'nt' else 'clear')
    print("\nBienvenido al programa de consulta de clientes de Xubio.\n")
    try:
        # Cargar configuración
        client_id, secret_id, is_development = ConfigLoader.load_env_variables()

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
                    ClientDataProcessor.process_data(client_data, is_development)
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
