import os
from dotenv import load_dotenv

class ConfigLoader:
    """Responsable de cargar la configuración desde un archivo .env."""
    @staticmethod
    def load_env_variables():
        load_dotenv()
        client_id = os.getenv('CLIENT_ID')
        secret_id = os.getenv('SECRET_ID')
        is_json = os.getenv('IS_JSON', 'false').lower() == 'true'

        if not client_id or not secret_id:
            raise ValueError("CLIENT_ID o SECRET_ID no encontrados en el archivo .env")
        
        return client_id, secret_id, is_json
