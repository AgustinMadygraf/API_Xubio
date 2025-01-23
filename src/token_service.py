"""
Path: src/token_service.py

"""

import requests
from requests.auth import HTTPBasicAuth
from datetime import datetime
from src.config_manager import (
    ensure_config_file,
    load_token_info,
    save_token_info,
    is_token_valid
)

class TokenService:
    """Responsable de manejar la autenticación y la obtención del token."""
    def __init__(self, client_id: str, secret_id: str):
        self.client_id = client_id
        self.secret_id = secret_id
        self.token_url = 'https://xubio.com/API/1.1/TokenEndpoint'

    def get_access_token(self) -> str:
        """Obtiene un token válido desde config.json o solicita uno nuevo al servidor."""
        ensure_config_file()
        token, created_at = load_token_info()

        if token and is_token_valid(created_at):
            return token
        else:
            new_token = self.request_new_token()
            # Guardamos junto con la marca de tiempo actual
            now_str = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            save_token_info(new_token, now_str)
            return new_token

    def request_new_token(self) -> str:
        """Solicita un nuevo token de acceso al servidor."""
        data = {'grant_type': 'client_credentials'}
        response = requests.post(
            self.token_url,
            data=data,
            auth=HTTPBasicAuth(self.client_id, self.secret_id)
        )
        if response.status_code != 200:
            raise ConnectionError(f"Error al obtener el token: {response.status_code}, {response.text}")
        return response.json().get('access_token')
