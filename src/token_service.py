import requests
from requests.auth import HTTPBasicAuth

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
